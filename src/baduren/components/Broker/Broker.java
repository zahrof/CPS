package baduren.components.Broker;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import baduren.CVM;
import baduren.connectors.ReceptionConnector;
import baduren.interfaces.ManagementCI;
import baduren.interfaces.MessageFilterI;
import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import baduren.message.Message;
import baduren.ports.outboundPorts.ReceptionOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import baduren.plugins.*;


public class Broker extends AbstractComponent implements PublicationCI, ManagementCI {

	// -------------------------------------------------------------------------
	// Broker variables and constants
	// -------------------------------------------------------------------------
	protected final static String MY_RECEPTION_BROKER_PLUGIN_URI = "reception-broker-client-plugin-uri" ;
	private static final String PUBLICATION_ACCESS_HANDLER_URI = "pah";
	private static final String ACCEPT_ACCESS_HANDLER_URI = "aah";
	private static final String SUBSCRIBE_ACCESS_HANDLER_URI = "sah";
	private static final String SELECT_MESSAGES_HANDLER_URI = "smh";
	public static int messagesSupprimes;
	public static int messagesFiltres;

	/**
	 * The Broker's uri.
	 */

	protected String uri= CVM.BROKER_COMPONENT_URI;


	private int compteur =0; // increments for each new subscriber
	private HashMap<String, List<MessageI>> messages = new HashMap<>(); // Map between topic and messages (each topic has several messages)
	private HashMap<String, Subscriber> subscribers = new HashMap<>(); // Map between the receptionInboundport and the subscribe
	private HashMap<String, List<MessageI>> messagesReady = new HashMap<>(); // Map between inbound port and messages
	protected final Lock messagesLock = new ReentrantLock();
	protected final Lock subscribersLock = new ReentrantLock();
	protected final Lock messagesReadyLock = new ReentrantLock();
	final private Condition hasSubscribers = subscribersLock.newCondition();
	final private Condition hasMessagesReady = messagesReadyLock.newCondition();

	private class Subscriber {
		/**
		 * Instantiates a new Subscriber.
		 *
		 * @param b_instance the Broker instance
		 */
		public Subscriber(Broker b_instance) {
			this.uri = b_instance.uri + compteur;
			this.topics = new HashMap<String, MessageFilterI>();
			try {
				this.receptionOutboundPort = new ReceptionOutboundPort(b_instance.uri+compteur, b_instance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * The subscriber's Uri.
		 */
		public String uri; // uri of outbound port
		/**
		 * The Topics which the subscriber is subscribed with the filter (null if no filter).
		 * Key :	 topic
		 * Value :	 MessageFilterI
		 */
		public HashMap<String, MessageFilterI> topics;
		/**
		 * The subscriber's reception outbound port.
		 */
		public ReceptionOutboundPort receptionOutboundPort;

	}

	// -------------------------------------------------------------------------
	// Broker constructors
	// -------------------------------------------------------------------------
	/*** BROKER'S CONSTRUCTOR WITHOUT PLUGINS AND WITHOUT CHOOSING THE NUMBER OF THREADS ***/
	/*protected Broker(String managementInboundPortName, String publicationInboundPortName,
					 String receptionOutboundPortName)
			throws Exception {
		*//** INITIALIZE VARIABLES **//*
		super(CVM.BROKER_COMPONENT_URI, 1, 0) ;
		this.uri=CVM.BROKER_COMPONENT_URI;
		this.compteur = 0;
		this.messages = new HashMap<>();
		this.subscribers = new HashMap<>();
		this.messages_ready = new HashMap<>();
		this.messages_ready_locker = new ReentrantReadWriteLock();
		Thread.current_
		*//** TESTING VARIABLES **//*
		assert managementInboundPortName != null ||  managementInboundPortName != "":
				new PreconditionException("managementInboundPortName is wrong");
		assert publicationInboundPortName != null ||  publicationInboundPortName != "":
				new PreconditionException("publicationInboundPortName is wrong");
		assert receptionOutboundPortName != null ||  receptionOutboundPortName != "":
				new PreconditionException("receptionOutboundPortName is wrong");

		*//** CREATING PORTS **//*
		PortI managementInboundPort = new ManagementInboundPort(managementInboundPortName, this);
		PortI publicationInboundPort = new PublicationInboundPort(publicationInboundPortName, this);
		PortI receptionOutboundPort = new ReceptionOutboundPort(receptionOutboundPortName,this);

		*//** PUBLISHING PORTS **//*
		receptionOutboundPort.localPublishPort();
		managementInboundPort.publishPort();
		publicationInboundPort.publishPort();

		*//** SETTING TRACER **//*
		this.tracer.setTitle("broker") ;
		this.tracer.setRelativePosition(1, 1) ;
	}

	*//*** BROKER'S CONSTRUCTOR WITH PLUGINS AND WITHOUT CHOOSING THE NUMBER OF THREADS ***//*
	protected Broker () throws Exception{
		*//** INITIALIZE VARIABLES **//*
		super(CVM.BROKER_COMPONENT_URI, 1,0 );
		this.uri=CVM.BROKER_COMPONENT_URI;
		this.compteur = 0;
		this.messages = new HashMap<>();
		this.subscribers = new HashMap<>();
		this.messages_ready = new HashMap<>();
		this.messages_ready_locker = new ReentrantReadWriteLock();

		*//** TESTING VARIABLES **//*
		// TODO
		*//** INSTALLING PLUGINS **//*
		BrokerManagementPlugin pluginManagement = new BrokerManagementPlugin();
		pluginManagement.setPluginURI("management-broker-plugin-uri");
		this.installPlugin(pluginManagement);

		BrokerPublicationPlugin pluginPublication = new BrokerPublicationPlugin();
		pluginPublication.setPluginURI("publication-broker-plugin-uri");
		this.installPlugin(pluginPublication);

		*//** SETTING TRACER **//*
		this.tracer.setTitle("BROKER") ;
		this.tracer.setRelativePosition(1, 1) ;
	}

	*//*** BROKER'S CONSTRUCTOR WITHOUT PLUGINS AND CHOOSING THE NUMBER OF THREADS ***//*
	protected Broker(String managementInboundPortName, String publicationInboundPortName,
					 String receptionOutboundPortName, int nbThreads, int nbSchedulableThreads)
			throws Exception {
		*//** INITIALIZE VARIABLES **//*
		super(CVM.BROKER_COMPONENT_URI, nbThreads, nbSchedulableThreads) ;
		this.uri=CVM.BROKER_COMPONENT_URI;
		this.compteur = 0;
		this.messages = new HashMap<>();
		this.subscribers = new HashMap<>();
		this.messages_ready = new HashMap<>();
		this.messages_ready_locker = new ReentrantReadWriteLock();

		*//** TESTING VARIABLES **//*
		assert managementInboundPortName != null ||  managementInboundPortName != "":
				new PreconditionException("managementInboundPortName is wrong");
		assert publicationInboundPortName != null ||  publicationInboundPortName != "":
				new PreconditionException("publicationInboundPortName is wrong");
		assert receptionOutboundPortName != null ||  receptionOutboundPortName != "":
				new PreconditionException("receptionOutboundPortName is wrong");

		*//** CREATING PORTS **//*
		PortI managementInboundPort = new ManagementInboundPort(managementInboundPortName, this);
		PortI publicationInboundPort = new PublicationInboundPort(publicationInboundPortName, this);
		PortI receptionOutboundPort = new ReceptionOutboundPort(receptionOutboundPortName,this);

		*//** PUBLISHING PORTS **//*
		receptionOutboundPort.localPublishPort();
		managementInboundPort.publishPort();
		publicationInboundPort.publishPort();

		*//** SETTING TRACER **//*
		this.tracer.setTitle("broker") ;
		this.tracer.setRelativePosition(1, 1) ;
	}*/

	/*** BROKER'S CONSTRUCTOR WITH PLUGINS AND CHOSING THE NUMBER OF THREADS ***/
	protected Broker(int nbThreads, int nbSchedulableThreads) throws Exception {
		super(CVM.BROKER_COMPONENT_URI, nbThreads, nbSchedulableThreads) ;

		/** TESTING VARIABLES **/
		// TODO

		/** INSTALLING PLUGINS **/
		BrokerManagementPlugin pluginManagement = new BrokerManagementPlugin();
		pluginManagement.setPluginURI("management-broker-plugin-uri");
		this.installPlugin(pluginManagement);

		BrokerPublicationPlugin pluginPublication = new BrokerPublicationPlugin();
		pluginPublication.setPluginURI("publication-broker-plugin-uri");
		this.installPlugin(pluginPublication);

		/** SETTING TRACER **/
		this.tracer.setTitle("broker") ;
		this.tracer.setRelativePosition(1, 0) ;
	}

	// -------------------------------------------------------------------------
	// Broker life cycle
	// -------------------------------------------------------------------------


	@Override
	public void	start() throws ComponentStartException
	{
		this.logMessage("starting broker component.") ;
		super.start() ;
	}

	@Override
	public void	execute() throws Exception
	{
		super.execute() ;
		this.createNewExecutorService(SELECT_MESSAGES_HANDLER_URI, 4, false) ;
		this.createNewExecutorService(ACCEPT_ACCESS_HANDLER_URI, 4, false) ;
		// pas besoin de faire plusieurs threads pour le publieur car elle éxécuterai la méthode publish
		// qui est locké du début à la fin du coup pas trop de parallelisation

		handleRequestAsync(SELECT_MESSAGES_HANDLER_URI,new AbstractComponent.AbstractService<Void>() {
			@Override
			public Void call() throws Exception {
				((Broker)this.getServiceOwner()).search_messages_to_send();
				return null;
			}
		});

		handleRequestAsync(ACCEPT_ACCESS_HANDLER_URI,new AbstractComponent.AbstractService<Void>() {
			@Override
			public Void call() throws Exception {
				((Broker)this.getServiceOwner()).acceptMessage();
				return null;
			}
		});


	}

	@Override
	public void	finalise() throws Exception
	{

		for(String subscriber : this.subscribers.keySet()){
			subscribers.get(subscriber).receptionOutboundPort.unpublishPort();
		}
		//this.publicationInboundPort.unpublishPort() ;

		this.logMessage("stopping broker component.") ;
		super.finalise();
	}

	// -------------------------------------------------------------------------
	// Broker services implementation
	// -------------------------------------------------------------------------

	/*
		RECEPTION METHODS
	 */
	public void acceptMessage() throws Exception {


		while(true) {
			//System.out.println("avant lock dans accept message pour subscribers");
			this.subscribersLock.lock(); //blocks writers only
			//System.out.println("après lock dans accept message pour subscribers");
			while(subscribers.isEmpty()) { //check if there's data, don't modify shared variables
				//System.out.println("waiting for messages ready");
				hasSubscribers.await();
			}
			//System.out.println("avant prendre message Ready dans ACCEPT Message");

			//System.out.println("après prendre message Ready dans ACCEPT Message");
			for (String inboundPortI : subscribers.keySet()) {

				//if(messagesReady.isEmpty()) hasMessagesReady.await();
				for (String inboundPortII : messagesReady.keySet()) {
					//System.out.println("pour tous les subscriber dans messageReady");
					if(inboundPortI.equals(inboundPortII)){
						//System.out.println("Equals");
						messagesReadyLock.lock();
						if(messagesReady.get(inboundPortI).size()==1){
							this.logMessage("Envoi du message " + messagesReady.get(inboundPortI).get(0).toString() + "au port " + inboundPortI);
							subscribers.get(inboundPortI).receptionOutboundPort.acceptMessage(messagesReady.get(inboundPortI).get(0));
						}

						else {
							for (MessageI m : messagesReady.get(inboundPortI)) {
								this.logMessage("Envoi du message " + m.toString() + "au port " + inboundPortI);
								subscribers.get(inboundPortI).receptionOutboundPort.acceptMessage(m);
								//System.out.println("je viens de l'envoyer à accept message le message " + m.toString());
							}
						}messagesReadyLock.unlock();
					}

				}
				messagesReadyLock.lock();
				//System.out.println("je vais supprimer inbound Port ACCEPT");
				messagesReady.remove(inboundPortI);
				messagesReadyLock.unlock();

			}
			//System.out.println("avant rendre message Ready dans ACCEPT Message");

			//System.out.println("avant rendre subscriber Lock dans ACCEPT Message");
			this.subscribersLock.unlock();
		}
	}



	private void search_messages_to_send() throws Exception {

		HashSet<MessageI> sent_messages;
		while(true) {
			/*
			Thread.sleep(5000);
			for (String topic : messages.keySet()) {
				for (MessageI m : this.messages.get(topic)) {
					System.out.println("topic : " + topic + "    message : " + m + "");
				}
			}
			Thread.sleep(5000);
			*/

			//System.out.println("avant lock dans SELECT message pour subscribers");
			this.subscribersLock.lock();
			//System.out.println("avant lock dans SELECT message pour messagelock");
			this.messagesLock.lock();
			this.messagesReadyLock.lock();
			//System.out.println("après lock dans SELECT message pour messagelock");
			//System.out.println("messages "+messages.toString());
			if(!messages.isEmpty()) {
				for (String topic : messages.keySet()) {
					for (String inboundPortURI : subscribers.keySet()) {
						Subscriber subscriber = subscribers.get(inboundPortURI);
						if(!subscriber.topics.keySet().contains(topic)) continue;
						for (MessageI m : this.messages.get(topic)) {

							//System.out.println("avant prendre message_ready Lock dans SELECT Message TOPIC "+topic);

							//System.out.println("j'ai pris message_ready Lock dans SELECT Message TOPIC "+topic);
							if (subscriber.topics.get(topic)==null) {
								//System.out.println("il y a des trucs à ajouter pour le topic "+topic);
								if (!this.messagesReady.containsKey(inboundPortURI)) {
									//System.out.println("pUTTING SOME CONTENT");
									messagesReady.put(inboundPortURI, new ArrayList<>());
								}

								messagesReady.get(inboundPortURI).add(m);
								//System.out.println("MESSAGE READY"+ messagesReady.toString());
							}
							else{
								if(subscriber.topics.get(topic).filter(m)) {
									messagesFiltres++;
									//System.out.println("il y a des trucs à ajouter pour le topic " + topic);
									if (!this.messagesReady.containsKey(inboundPortURI)) {
										//System.out.println("pUTTING SOME CONTENT");
										messagesReady.put(inboundPortURI, new ArrayList<>());
									}

									messagesReady.get(inboundPortURI).add(m);
									//System.out.println("MESSAGE READY" + messagesReady.toString());
								}
							}

							//System.out.println("avant rendre message_ready Lock dans SELECT Message TOPIC "+topic);


						}

					}
					for (MessageI m : this.messages.get(topic)) {
						messagesSupprimes++;
						this.logMessage("Suppression des messages "+m.toString()+" du topic " + topic);
					}
					this.messages.put(topic, new ArrayList<>());
				}
				//Thread.sleep(1000);
			}
			this.messagesReadyLock.unlock();
			//System.out.println("avant rendre message_lock Lock dans SELECT Message");
			this.messagesLock.unlock();
			//System.out.println("avant rendre subscriber Lock dans SELECT Message");
			this.subscribersLock.unlock();
			//System.out.println("BYE");

		}

	}

	/*
		PUBLICATION METHODS
	 */

	/**
	 * This method
	 *
	 * @param m     It's the message to transmit
	 * @param topic It's the topic where we want to publish the message m
	 * @throws Exception the exception
	 */
	public void publish(MessageI m, String topic)throws Exception {
		//System.out.println("publish avant prends le lock pour messages");
		this.messagesLock.lock();
		//System.out.println("publish après prends le lock pour messages");
		if (!isTopic(topic)) createTopic(topic); // Si le topic n'existait pas déjà on le crée

		this.messages.get(topic).add(m); // On ajoute le message
		this.logMessage("Message " + m.getURI() + " stocked to topic " + topic+ " at the moment "+m.getTimeStamp().getTime() );
		this.messagesLock.unlock();
		//System.out.println("publish rend le lock pour messages "+m.toString());


		//Thread.sleep(1000);
	}

	/**
	 * Publish.
	 *
	 * @param m      the m
	 * @param topics the topics
	 * @throws Exception the exception
	 */
	public void publish(MessageI m, String[] topics) throws Exception{
		for(String topic : topics)
			publish(m,topic);

	}


	/**
	 * Publish.
	 *
	 * @param ms     the ms
	 * @param topics the topics
	 * @throws Exception the exception
	 */
	public void publish(MessageI[] ms, String topics) throws Exception{
		for(MessageI msg : ms)
			publish(msg, topics);
	}


	/**
	 * Publish.
	 *
	 * @param ms     the ms
	 * @param topics the topics
	 * @throws Exception the exception
	 */
	public void publish(MessageI[] ms, String[] topics) throws Exception{
		for(MessageI msg : ms) {
			for(String topic: topics) {
				publish(msg, topic);
			}
		}
	}

	/*
		MANAGEMENT METHODS
	 */

	/**
	 * Subscribe.
	 *
	 * @param topic          the topic
	 * @throws Exception the exception
	 */
	public void subscribe(String topic, String inboundPortURIaux) throws Exception{
		subscribe(topic, (MessageFilterI) null, inboundPortURIaux);
	}

	/**
	 * Subscribe.
	 *
	 * @param topics         the topics
	 * @param inboundPortURI the inbound port uri
	 * @throws Exception the exception
	 */
	public void subscribe(String[] topics, String inboundPortURI) throws Exception{
		for(String s: topics)
			subscribe(s, inboundPortURI);

	}

	/**
	 * Subscribe.
	 *
	 * @param topic          the topic
	 * @param filter         the filter
	 * @param inboundPortURI the inbound port uri
	 * @throws Exception the exception
	 */
	public synchronized void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception{


		// Si le subscriber était pas présent encore on le crée et connecte
		System.out.println("subscribe avant de prendre le lock subscribersLock - " + inboundPortURI);
		this.subscribersLock.lock();
		System.out.println("subscribe après de prendre le lock subscribersLock - " + inboundPortURI);

		if(!subscribers.containsKey(inboundPortURI)) {
			subscribers.put(inboundPortURI, new Subscriber(this));
			subscribers.get(inboundPortURI).receptionOutboundPort.publishPort();

			this.doPortConnection(
					subscribers.get(inboundPortURI).uri,
					inboundPortURI,
					ReceptionConnector.class.getCanonicalName()
			);

			this.hasSubscribers.signal();
			System.out.println("signal que maintenant il y a des subscribers - " + inboundPortURI);
		}
		System.out.println("subscribe avant de prendre le lock messagesLock - " + inboundPortURI);
		this.messagesLock.lock();
		System.out.println("subscribe après de prendre le lock messagesLock - " + inboundPortURI);
		if(!messages.containsKey(topic)) createTopic(topic);
		this.messagesLock.unlock();
		System.out.println("subscribe après de renre le lock messagesLock - " + inboundPortURI);

		//Si le messager avait pas ce topic on l'ajoute
		if(!subscribers.get(inboundPortURI).topics.containsKey(topic)) {
			subscribers.get(inboundPortURI).topics.put(topic, filter);
		}
		System.out.println("subscribe avant de rendre le lock subscribedlock - " + inboundPortURI);

		System.out.println("subscribe après de rendre le lock subscribedlock - " + inboundPortURI);

		this.compteur++;
		subscribers.get(inboundPortURI).receptionOutboundPort.acceptMessage(new Message("Bravo tu viens de " +
				"te souscrire au topic "+topic));
		this.subscribersLock.unlock();
		if (filter == null) {
			this.logMessage("Subscribed " + inboundPortURI + " to topic " + topic + " with no filter");
		}
		else this.logMessage("Subscribed " + inboundPortURI + " to topic " + topic + " with filter"+filter);


	}

	/**
	 * Modify filter.
	 *
	 * @param topic          the topic
	 * @param newFilter      the new filter
	 * @param inboundPortURI the inbound port uri
	 * @throws Exception the exception
	 */
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI)throws Exception {
		if (isTopic(topic)) {
			if(!subscribers.get(inboundPortURI).topics.containsKey(topic)) {
				subscribers.get(inboundPortURI).topics.put(topic, newFilter);
			}
		}

	}

	/**
	 * Unsubscribe.
	 *
	 * @param topic          the topic
	 * @param inboundPortUri the inbound port uri
	 * @throws Exception the exception
	 */
	public void unsubscribe(String topic, String inboundPortUri) throws Exception{
		this.subscribers.get(inboundPortUri).topics.remove(topic);
	}

	/**
	 * Create topic.
	 *
	 * @param topic the topic
	 * @throws Exception the exception
	 */
	public void createTopic(String topic) throws Exception {
		logMessage("Creation of topic " + topic);
		if(!messages.containsKey(topic)) messages.put(topic,new ArrayList<>());
	}


	/**
	 * Create topics.
	 *
	 * @param topics the topics
	 * @throws Exception the exception
	 */
	public void createTopics(String[] topics) throws Exception {
		for (int i=0; i< topics.length; i++)
			createTopic(topics[i]);

	}


	/**
	 * Destroy topic.
	 *
	 * @param topic the topic
	 * @throws Exception the exception
	 */
	public void destroyTopic(String topic) throws Exception {
		this.messages.remove(topic);
	}


	/**
	 * Is topic boolean.
	 *
	 * @param topic the topic
	 * @return the boolean
	 * @throws Exception the exception
	 */
	public boolean isTopic(String topic) throws Exception {
		return this.messages.containsKey(topic);
	}


	/**
	 * Get topics string [ ].
	 *
	 * @return the string [ ]
	 * @throws Exception the exception
	 */
	public String[] getTopics() throws Exception {
		return messages.keySet().toArray(new String[messages.keySet().size()]);
	}


	/**
	 * Gets publication port uri.
	 *
	 * @return the publication port uri
	 * @throws Exception the exception
	 */
	public String getPublicationPortURI() throws Exception {
		//return this.publicationInboundPort.getPortURI();
		return "";
	}

}
