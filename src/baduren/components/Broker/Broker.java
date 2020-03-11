package baduren.components.Broker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import baduren.CVM;
import baduren.connectors.ReceptionConnector;
import baduren.interfaces.ManagementCI;
import baduren.interfaces.MessageFilterI;
import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import baduren.message.Message;
import baduren.ports.inboundPorts.ManagementInboundPort;
import baduren.ports.inboundPorts.PublicationInboundPort;
import baduren.ports.outboundPorts.ReceptionOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import fr.sorbonne_u.components.ports.PortI;
import baduren.plugins.*;


public class Broker extends AbstractComponent implements PublicationCI, ManagementCI {

	// -------------------------------------------------------------------------
	// Broker variables and constants
	// -------------------------------------------------------------------------
	protected final static String MY_RECEPTION_BROKER_PLUGIN_URI = "reception-broker-client-plugin-uri" ;
	private static final String PUBLICATION_ACCESS_HANDLER_URI = "pah";
	private static final String ACCEPT_ACCESS_HANDLER_URI = "aah";
	private static final String SUBSCRIBE_ACCESS_HANDLER_URI = "sah";

	/**
	 * The Broker's uri.
	 */
	protected String uri;
	private HashMap<String, List<MessageI>> messages_ready; // Map between inbound port and messages
	protected final ReentrantReadWriteLock messages_ready_locker;
	private int compteur; // increments for each new subscriber
	private HashMap<String, List<MessageI>> messages; // Map between topic and messages (each topic has several messages)
	private HashMap<String, Subscriber> subscribers; // Map between the receptionInboundport and the subscribe

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
	protected Broker (int nbThreads, int nbSchedulableThreads) throws Exception {
		super(CVM.BROKER_COMPONENT_URI, nbThreads, nbSchedulableThreads) ;
		this.uri=CVM.BROKER_COMPONENT_URI;
		this.compteur = 0;
		this.messages = new HashMap<>();
		this.subscribers = new HashMap<>();
		this.messages_ready = new HashMap<>();
		this.messages_ready_locker = new ReentrantReadWriteLock();

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
		this.tracer.setRelativePosition(1, 1) ;
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
		Thread.sleep(500);
		search_messages_to_send();
		acceptMessage();
		//acceptMessage();
	/*	this.subscribers_without_filters.get("fruits").get(0).acceptMessage(this.messages.get("fruits").get(0));
		this.subscribers_without_filters.get("fruits").get(0).acceptMessage(this.messages.get("fruits").get(1));*/
		//HashMap<ReceptionOutboundPort,MessageFilterI>hm =new HashMap<>();
/*		hm = this.subscribers.get("voiture");
		//hm.ge
		for(ReceptionOutboundPort up: hm.keySet()) {
			up.acceptMessage(this.messages.get("voiture").get(0));
		}*/

		//this.subscribers.get("voiture").get(0).acceptMessage(this.messages.get("voiture").get(0));
		//this.subscribers.get("voiture").get(0).acceptMessage(this.messages.get("voiture").get(1));

		//this.Map_URIPort.get("fruits").acceptMessage(this.messages.get("fruits").get(0));



		// Thread à part entiere qui s'occupe d'envoyer les messages
		/*this.runTask(
			new AbstractComponent.AbstractTask() {
				@Override
				public void run() {
					while(true){
						try {Thread.sleep(100);} catch (Throwable e) {}
						synchronized (this) {
							if (messages_ready.isEmpty()){
								System.out.println("avant lock");
								messages_ready_locker.unlock();
								System.out.println("après lock");
							}
							if (!messages_ready.isEmpty()) {
								for (String inboundPortI : messages_ready.keySet()) {
									for (MessageI m : messages_ready.get(inboundPortI)) {
										try {
											subscribers.get(inboundPortI).receptionOutboundPort.acceptMessage(m);
											messages_ready.remove(inboundPortI);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
					}
				}
			}
		);*/


		/*new Thread(() -> {
			while(true){
				try {Thread.sleep(100);} catch (Throwable e) {}
				synchronized (this) {
					if (messages_ready.isEmpty()) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (!messages_ready.isEmpty()) {
						for (String inboundPortI : messages_ready.keySet()) {
							for (MessageI m : messages_ready.get(inboundPortI)) {
								try {
									subscribers.get(inboundPortI).receptionOutboundPort.acceptMessage(m);
									messages_ready.remove(inboundPortI);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}).start();*/



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

		Thread.sleep(500);
		if (messages_ready.isEmpty()) {
	/*		try {
				//this..wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
		if (!messages_ready.isEmpty()) {
			for (String inboundPortI : messages_ready.keySet()) {
				for (MessageI m : messages_ready.get(inboundPortI)) {
					try {
						subscribers.get(inboundPortI).receptionOutboundPort.acceptMessage(m);
						messages_ready.remove(inboundPortI);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}else System.out.println("Vide");
	}


	private synchronized void search_messages_to_send() throws Exception {
		HashSet<MessageI> sent_messages;

		for (String inboundPortURI : subscribers.keySet()) {
			Subscriber subscriber = subscribers.get(inboundPortURI);

			sent_messages = new HashSet<>();

			for(String topic: this.messages.keySet()){
				for(MessageI m : this.messages.get(topic)){


					if (subscriber.topics.containsKey(topic)) {
						boolean transfer_message = false;

						if (subscriber.topics.get(topic) == null) {
							transfer_message = true;
						} else if (subscriber.topics.get(topic).filter(m)) {
							transfer_message = true;
						}

						if (transfer_message) {
							// Méthode classique
							//subscriber.receptionOutboundPort.acceptMessage(m);

							if(!sent_messages.contains(m)){
								if(!messages_ready.containsKey(inboundPortURI)){
									messages_ready.put(inboundPortURI, new ArrayList<>());
								}
								messages_ready.get(inboundPortURI).add(m);
								if(messages_ready.size() == 1){ // Si la liste etait vide avant d'ajouter un element, on notify
									//System.out.println("avant unlock");
									//this.messages_ready_locker.lock();
									//System.out.println("après unlock");
								}
								sent_messages.add(m);
							}
						}
					}
				}
			}
		}

		for(String topic: this.messages.keySet()){
			this.messages.put(topic, new ArrayList<>());
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
		if (!isTopic(topic)) createTopic(topic); // Si le topic n'existait pas déjà on le crée
		this.messages.get(topic).add(m); // On ajoute le message
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
		System.out.println("HOLAAAAA");
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
		this.logMessage("Subscribing " + inboundPortURI + " to topic " + topic + " with filter");

		if(!subscribers.containsKey(inboundPortURI)) {
			subscribers.put(inboundPortURI, new Subscriber(this));
			subscribers.get(inboundPortURI).receptionOutboundPort.publishPort();

			this.doPortConnection(
					subscribers.get(inboundPortURI).uri,
					inboundPortURI,
					ReceptionConnector.class.getCanonicalName()
			);
		}

		createTopic(topic);

		if(!subscribers.get(inboundPortURI).topics.containsKey(topic)) {
			subscribers.get(inboundPortURI).topics.put(topic, filter);
		}

		this.compteur++;


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
