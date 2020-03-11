package baduren.components.Broker;

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

		/** CREATING THREADS GROUPS**/
	/*	this.createNewExecutorService(PUBLICATION_ACCESS_HANDLER_URI, 1, false) ;
		this.createNewExecutorService(ACCEPT_ACCESS_HANDLER_URI, 1, false) ;
		this.createNewExecutorService(SUBSCRIBE_ACCESS_HANDLER_URI, 1, false) ;*/
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
		this.createNewExecutorService(SELECT_MESSAGES_HANDLER_URI, 1, false) ;
		this.createNewExecutorService(ACCEPT_ACCESS_HANDLER_URI, 1, false) ;

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
		//this.messages_ready_locker.readLock().lock();
		while(true) {
			System.out.println("avant lock dans accept message pour subscribers");
			this.subscribersLock.lock(); //blocks writers only
			System.out.println("après lock dans accept message pour subscribers");
			while(subscribers.isEmpty()) { //check if there's data, don't modify shared variables
				System.out.println("waiting for messages ready");
				hasSubscribers.await();
			}

			//there should be data now
			System.out.println("Il y a des subscribers pour accept");

			for (String inboundPortI : subscribers.keySet()) {
				messagesReadyLock.lock();
				for (String inboundPortII : messagesReady.keySet()) {
					if(inboundPortI.equals(inboundPortII)){
						for(MessageI m: messagesReady.get(inboundPortI)){
							subscribers.get(inboundPortI).receptionOutboundPort.acceptMessage(m);
						}
					}
				}
				messagesReady.remove(inboundPortI);
				messagesReadyLock.unlock();
			}
			this.subscribersLock.unlock();
		}
	}



	private void search_messages_to_send() throws Exception {
		HashSet<MessageI> sent_messages;
		while(true) {
			System.out.println("avant lock dans select message pour subscribers");
			this.subscribersLock.lock(); //blocks writers only
			System.out.println("après lock dans select message pour subscribers");


			while(subscribers.isEmpty()){
				System.out.println("waiting for subscribers");
				hasSubscribers.await(); //will unlock and re-lock after writer has signalled and unlocked.
			}

			//there should be data now
			System.out.println("Il y a des subscribers");

			for (String inboundPortURI : subscribers.keySet()) {
				Subscriber subscriber = subscribers.get(inboundPortURI);
				//sent_messages = new HashSet<>();

				for(String topic : subscriber.topics.keySet()){
					System.out.println("Select avant de prendre le messageLock");
					this.messagesLock.lock();
					for(MessageI m : this.messages.get(topic)){
						System.out.println("Select avant de prendre le messageReady");
						this.messagesReadyLock.lock();
						System.out.println("Select après de prendre le messageReady");
						if (!this.messagesReady.containsKey(inboundPortURI)) {
							messagesReady.put(inboundPortURI, new ArrayList<>());
						}
						messagesReady.get(inboundPortURI).add(m);
						this.messagesReadyLock.unlock();

						/*	if (messages_ready.size() == 1) { // Si la liste etait vide avant d'ajouter un element, on notify
								this.messagesReadyLock.notify();
								System.out.println("PATATE notifié");
								//System.out.println("après unlock");
							}*/
						//sent_messages.add(m);
					}
					this.messagesLock.unlock();
					System.out.println("Select après de rendre le messageLock");
				}
			}
			this.messagesLock.lock();
			for (String topic : this.messages.keySet())
				this.messages.put(topic, new ArrayList<>());
			this.messagesLock.unlock();
			System.out.println("Select va rendre subscriberLoc");
			this.subscribersLock.unlock();

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
		System.out.println("publish avant prends le lock pour messages");
		this.messagesLock.lock();
		System.out.println("publish après prends le lock pour messages");
		if (!isTopic(topic)) createTopic(topic); // Si le topic n'existait pas déjà on le crée
		this.messages.get(topic).add(m); // On ajoute le message
		this.messagesLock.unlock();
		System.out.println("publish rend le lock pour messages");

		this.logMessage("Message " + m.getURI() + " stocked to topic " + topic);
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
		//subscribers.get(inboundPortURI).a;
		/*f (subscribers.get(inboundPortURI).topics.get(topic).filter(m)) {
			subscriber.receptionOutboundPort.acceptMessage(m);*/
		/*((BrokerReceptionPlugin)this.getPlugin(MY_RECEPTION_BROKER_PLUGIN_URI)).acceptMessage((new Message("Bravo tu viens de " +
				"te souscrire au topic "+topic)));*/
		subscribers.get(inboundPortURIaux).receptionOutboundPort.acceptMessage(new Message("Bravo tu viens de " +
				"te souscrire au topic "+topic));
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
		System.out.println("subscribe avant de prendre le lock subscribersLock");
		this.subscribersLock.lock();
		System.out.println("subscribe après de prendre le lock subscribersLock");

		if(!subscribers.containsKey(inboundPortURI)) {
			subscribers.put(inboundPortURI, new Subscriber(this));
			subscribers.get(inboundPortURI).receptionOutboundPort.publishPort();

			this.doPortConnection(
					subscribers.get(inboundPortURI).uri,
					inboundPortURI,
					ReceptionConnector.class.getCanonicalName()
			);

			this.hasSubscribers.signal();
			System.out.println("signal que maintenant il y a des subscribers");
		}
		System.out.println("subscribe avant de prendre le lock messagesLock");
		this.messagesLock.lock();
		System.out.println("subscribe après de prendre le lock messagesLock");
		if(!messages.containsKey(topic)) createTopic(topic);
		this.messagesLock.unlock();
		System.out.println("subscribe après de renre le lock messagesLock");

		//Si le messager avait pas ce topic on l'ajoute
		if(!subscribers.get(inboundPortURI).topics.containsKey(topic)) {
			subscribers.get(inboundPortURI).topics.put(topic, filter);
		}
		System.out.println("subscribe avant de rendre le lock subscribedlock");
		this.subscribersLock.unlock();
		System.out.println("subscribe après de rendre le lock subscribedlock");

		this.compteur++;
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
