package baduren.components.Broker;

import java.io.File;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import baduren.CVM;
import baduren.CVM2;
import baduren.TestsIntegration;
import baduren.connectors.ReceptionConnector;
import baduren.interfaces.*;
import baduren.message.Message;
import baduren.ports.outboundPorts.ReceptionOutboundPort;
import baduren.replicator.connectors.ReplicableConnector;
import baduren.replicator.interfaces.*;
import baduren.replicator.ports.ReplicableInboundPort;
import baduren.replicator.ports.ReplicableOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import baduren.plugins.*;
import fr.sorbonne_u.components.helpers.Logger;
import fr.sorbonne_u.components.ports.InboundPortI;

import static baduren.Utils.filterToString;


public class Broker extends AbstractComponent implements ManagementImplementationI,
		SubscriptionImplementationI, PublicationImplementationI, ReplicationI<String> {

	// -------------------------------------------------------------------------
	// Broker variables and constants
	// -------------------------------------------------------------------------
	private static final String ACCEPT_ACCESS_HANDLER_URI = "aah";
	private static final String SELECT_MESSAGES_HANDLER_URI = "smh";
	private static final int SIZE_MSG_AUX = 500 ;
	public static int messagesSupprimes=0;
	public static int messagesFiltres=0;
	public static String changementFiltres="";
	public static String desabonnements="";
	public static String historiqueAbonnements="";
	public static String historiqueCreationTopics="\n";
	public static String suppressionSujets="\n";
	public static int messagesAcceptDeBroker;
	/** URI of the inbound port used to offer the service.					*/
	private ReplicableInboundPort<String> rip = null;
	/** URI of the inbound port proposed by the replication service.		*/
	private String replicableInboundPortURI=null;
	/** outbound port used to call the service.								*/
	protected ReplicableOutboundPort<String> 	rop ;

	/**
	 * The Broker's uri.
	 */

	protected String uri;


	private int compteur =0; // increments for each new subscriber
	private HashMap<String, List<MessageI>> messages = new HashMap<>(); // Map between topic and messages (each topic has several messages)
	private HashMap<String, Subscriber> subscribers = new HashMap<>(); // Map between the receptionInboundport and the subscribe
	private HashMap<String, List<MessageI>> messagesTriees = new HashMap<>(); // Map between inbound port and messages
	protected final Lock messagesLock = new ReentrantLock();
	protected final Lock subscribersLock = new ReentrantLock();
	protected final Lock messagesTrieesLock = new ReentrantLock();
	final private Condition hasSubscribers = subscribersLock.newCondition();
	final private Condition newSubscribers = subscribersLock.newCondition();
	private BrokerPublicationPlugin pluginPublication;



	@Override
	public String call(Object... parameters) throws Exception {
		String ret;
		if(!parameters[0].equals(this.uri)) {
			this.messagesLock.lock();
			try {
			if (!this.messages.containsKey(parameters[2]))
				messages.put((String) parameters[2], new ArrayList<>()); // Si le topic n'existait pas déjà on le crée
			this.messages.get(parameters[2]).add((MessageI) parameters[1]);
			this.logMessage("stockage du message "+ parameters[1]);
			ret = "Le mesage a bien été stocke! ";

			}finally {
				this.messagesLock.unlock();
			}
				return ret;
		}
		ret = "Votre message:  a pas été enregistré car c'est le même broker";
		return ret;
	}


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
		*//*
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

	/*** BROKER'S CONSTRUCTOR WITH PLUGINS AND CHOSING THE NUMBER OF THREADS ***/

	protected Broker(String inboundPortURI,String replicableInboundPortURI,  int nbThreads, int nbSchedulableThreads, String nbBroker) throws Exception {
		super(nbBroker, nbThreads, nbSchedulableThreads) ;
		addRequiredInterface(ReceptionCI.class);
		addRequiredInterface(ReplicableCI.class);
		addOfferedInterface(ReplicableCI.class);

		if(replicableInboundPortURI!=null) {
			this.replicableInboundPortURI = replicableInboundPortURI;
			this.rop = new ReplicableOutboundPort<String>(this);
			this.rop.publishPort();

			this.rip = new ReplicableInboundPort<String>(inboundPortURI, this);
			this.rip.publishPort();
		}

		uri=nbBroker;

		/** INSTALLING PLUGINS **/
		BrokerManagementPlugin pluginManagement = new BrokerManagementPlugin();
		pluginManagement.setPluginURI("management-broker-plugin-"+uri);
		this.installPlugin(pluginManagement);

		this.pluginPublication = new BrokerPublicationPlugin();
		pluginPublication.setPluginURI("publication-broker-plugin-"+uri);
		this.installPlugin(pluginPublication);


		/** SETTING TRACER **/
		this.tracer.setTitle(nbBroker) ;
		this.tracer.setRelativePosition(CVM2.brokerCounter++, 0) ;
		if(! new File(TestsIntegration.LOG_FOLDER).exists()) new File(TestsIntegration.LOG_FOLDER).mkdir();
		Logger logger = new Logger(TestsIntegration.LOG_FOLDER);
		logger.toggleLogging();
		this.setLogger(logger);
	}

	// -------------------------------------------------------------------------
	// Broker life cycle
	// -------------------------------------------------------------------------

	/**
	 * @see AbstractComponent#start()
	 * @throws ComponentStartException
	 */
	@Override
	public void	start() throws ComponentStartException
	{
		this.logMessage("starting broker component.") ;
		super.start() ;
		try{
			this.doPortConnection(
					this.rop.getPortURI(),
					this.replicableInboundPortURI,
					ReplicableConnector.class.getCanonicalName());
		}catch(Exception e){
			throw new ComponentStartException(e);
		}

	}

	/**
	 * @see AbstractComponent#execute()
	 * @throws Exception
	 */
	@Override
	public void	execute() throws Exception
	{
		super.execute() ;
		this.createNewExecutorService(SELECT_MESSAGES_HANDLER_URI, 5, false) ;
		this.createNewExecutorService(ACCEPT_ACCESS_HANDLER_URI, 5, false) ;
		// pas besoin de faire plusieurs threads pour le publieur car elle éxécuterai la méthode publish
		// qui est locké du début à la fin du coup pas trop de parallelisation

	/*	handleRequestAsync(SELECT_MESSAGES_HANDLER_URI,new AbstractComponent.AbstractService<Void>() {
			@Override
			public Void call() throws Exception {
				((Broker)this.getServiceOwner()).searchMessagesToSend();
				return null;
			}
		});*/

		runTask(SELECT_MESSAGES_HANDLER_URI,
				new AbstractComponent.AbstractTask() {

					@Override
					public void run() {
						try {
							((Broker) this.getTaskOwner()).searchMessagesToSend();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

/*		handleRequestAsync(ACCEPT_ACCESS_HANDLER_URI,new AbstractComponent.AbstractService<Void>() {
			@Override
			public Void call() throws Exception {
				((Broker)this.getServiceOwner()).acceptMessage();
				return null;
			}
		});*/

		this.runTask(ACCEPT_ACCESS_HANDLER_URI,
				new AbstractComponent.AbstractTask() {

					@Override
					public void run() {
						try {
							((Broker) this.getTaskOwner()).acceptMessage();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});



	}

	/**
	 * @see AbstractComponent#finalise()
	 * @throws Exception
	 */
	@Override
	public void	finalise() throws Exception
	{
		this.logMessage("stopping broker component.") ;

		for(String subscriber : this.subscribers.keySet()){
			try {
				subscribers.get(subscriber).receptionOutboundPort.unpublishPort();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		rop.unpublishPort();
		rip.unpublishPort();

		this.printExecutionLogOnFile(TestsIntegration.LOG_FOLDER + TestsIntegration.BROKER_LOG_FILE);


		super.finalise();
	}

	// -------------------------------------------------------------------------
	// Broker services implementation
	// -------------------------------------------------------------------------

	/*
		RECEPTION METHODS
	 */


	public void acceptMessage() throws Exception {

		MessageI msg[]= new MessageI[SIZE_MSG_AUX];
		int current =0;
		while(true) {

			this.subscribersLock.lock();
			try {
				while (subscribers.isEmpty()) { //check if there's data, don't modify shared variables
					hasSubscribers.await();
				}
				for (String inboundPortI : subscribers.keySet()) {

					for (String inboundPortII : messagesTriees.keySet()) {
						if (inboundPortI.equals(inboundPortII)) {
							messagesTrieesLock.lock();
							try {
								if (messagesTriees.get(inboundPortI).size() == 1) {
									this.logMessage("Envoi du message " + messagesTriees.get(inboundPortI).get(0).toString() + "au port " + inboundPortI);
									subscribers.get(inboundPortI).receptionOutboundPort.acceptMessage(messagesTriees.get(inboundPortI).get(0));
									messagesAcceptDeBroker++;
								} else {

									for (MessageI m : messagesTriees.get(inboundPortI)) {
										this.logMessage("Envoi du message " + m.toString() + "au port " + inboundPortI);
										msg[current] = m;
										current++;
										messagesAcceptDeBroker++;
									}
									subscribers.get(inboundPortI).receptionOutboundPort.acceptMessages(msg);
									current = 0;
									msg = new MessageI[SIZE_MSG_AUX];
								}
							} finally {
								messagesTrieesLock.unlock();
							}
						}
					}
					messagesTrieesLock.lock();
					try {
						messagesTriees.remove(inboundPortI);
					}finally {
						messagesTrieesLock.unlock();
					}


				}
			}finally {
				this.subscribersLock.unlock();
			}

		}
	}



	private void searchMessagesToSend() throws Exception {
		while(true) {

			this.subscribersLock.lock();
			try{
			this.messagesLock.lock();
			try{
			this.messagesTrieesLock.lock();
			try{
			if(!messages.isEmpty()) {
				for (String topic : messages.keySet()) {

					if(messages.get(topic).size()==0)continue;
					for (String inboundPortURI : subscribers.keySet()) {
						Subscriber subscriber = subscribers.get(inboundPortURI);
						if (!subscriber.topics.keySet().contains(topic)) continue;
						for (MessageI m : this.messages.get(topic)) {

							if (subscriber.topics.get(topic) == null) {
								if (!this.messagesTriees.containsKey(inboundPortURI))
									messagesTriees.put(inboundPortURI, new ArrayList<>());
								messagesTriees.get(inboundPortURI).add(m);
							} else {
								if (subscriber.topics.get(topic).filter(m)) {
									messagesFiltres++;
									if (!this.messagesTriees.containsKey(inboundPortURI))
										messagesTriees.put(inboundPortURI, new ArrayList<>());
									messagesTriees.get(inboundPortURI).add(m);
								}
							}
						}

					}
					for (MessageI m : this.messages.get(topic)) {
						messagesSupprimes++;
						this.logMessage("Suppression des messages "+m.getPayload()+" du topic " + topic);
					}
					this.messages.put(topic, new ArrayList<>());
				}
			}
			}finally {
				this.messagesTrieesLock.unlock();
			}
			}finally {
				this.messagesLock.unlock();
			}
			}finally {
				this.subscribersLock.unlock();
			}
		}

	}

	/*
		PUBLICATION METHODS
	 */

	/**
	 * Publish 1 message with 1 topic
	 * {@link PublicationImplementationI#publish(MessageI, String)}
	 *
	 * @param m     It's the message to transmit
	 * @param topic It's the topic where we want to publish the message m
	 * @throws Exception
	 */
	@Override
	public void publish(MessageI m, String topic)throws Exception {


		try {
			this.messagesLock.lock();
			if (!this.messages.containsKey(topic))
				messages.put(topic, new ArrayList<>()); // Si le topic n'existait pas déjà on le crée
			this.messages.get(topic).add(m); // On ajoute le message
		}finally {
			this.messagesLock.unlock();

		}
		this.logMessage("avant le call");
		this.rop.call(this.uri, m, topic);
		this.logMessage("apres le call");
		this.logMessage("Message " + m.getPayload() + " stocked to topic " + topic + " at the moment "
					+ m.getTimeStamp().getTime());



	}

	/**
	 * Publish 1 message with multiple topics
	 * {@link PublicationImplementationI#publish(MessageI, String[])}
	 *
	 * @param m      the message
	 * @param topics the topics
	 * @throws Exception
	 */
	@Override
	public void publish(MessageI m, String[] topics) throws Exception{
		for(String topic : topics)
			publish(m,topic);

	}


	/**
	 * Publish multiple messages with 1 topic
	 * {@link PublicationImplementationI#publish(MessageI[], String)}
	 *
	 * @param ms     the messages
	 * @param topics the topics
	 * @throws Exception
	 */
	@Override
	public void publish(MessageI[] ms, String topics) throws Exception{
		for(MessageI msg : ms)
			publish(msg, topics);
	}


	/**
	 * Publish multiple messages with multiple topics
	 * {@link PublicationImplementationI#publish(MessageI[], String[])}
	 *
	 * @param ms     the messages
	 * @param topics the topics
	 * @throws Exception
	 */
	@Override
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
	 * Subscribe to one topic
	 * {@link SubscriptionImplementationI#subscribe(String, String)}
	 *
	 * @param topic          the topic
	 * @throws Exception
	 */
	@Override
	public void subscribe(String topic, String inboundPortURIaux) throws Exception{
		subscribe(topic, (MessageFilterI) null, inboundPortURIaux);


	}

	/**
	 * Subscribe to multiple topics
	 * {@link SubscriptionImplementationI#subscribe(String[], String)}
	 *
	 * @param topics         the topics
	 * @param inboundPortURI the inbound port uri
	 * @throws Exception
	 */
	@Override
	public void subscribe(String[] topics, String inboundPortURI) throws Exception{
		for(String s: topics)
			subscribe(s, inboundPortURI);

	}

	/**
	 * Subscribe to one topic with a filter
	 * {@link SubscriptionImplementationI#subscribe(String, MessageFilterI, String)}
	 *
	 * @param topic          the topic
	 * @param filter         the filter
	 * @param inboundPortURI the inbound port uri
	 * @throws Exception
	 */
	@Override
	public  void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception{
		// Si le subscriber était pas présent encore on le crée et connecte
		this.subscribersLock.lock();
		try {
			if (!subscribers.containsKey(inboundPortURI)) {
				subscribers.put(inboundPortURI, new Subscriber(this));
				subscribers.get(inboundPortURI).receptionOutboundPort.publishPort();

				this.doPortConnection(
						subscribers.get(inboundPortURI).uri,
						inboundPortURI,
						ReceptionConnector.class.getCanonicalName()
				);
				this.newSubscribers.signal();
				this.hasSubscribers.signal();
			}
			this.messagesLock.lock();
			try {
				if (!messages.containsKey(topic)) {
					if (!messages.containsKey(topic)) messages.put(topic, new ArrayList<>());
				}
			}finally {
				this.messagesLock.unlock();
			}

			//Si le messager avait pas ce topic on l'ajoute
			if (!subscribers.get(inboundPortURI).topics.containsKey(topic)) {
				subscribers.get(inboundPortURI).topics.put(topic, filter);
				// mettre variable pour compter les subscribers
			}
			this.hasSubscribers.signal();
			this.compteur++;
			if (filter == null) {
				subscribers.get(inboundPortURI).receptionOutboundPort.acceptMessage(new Message("Bravo tu viens de " +
						"te souscrire au topic " + topic + " sans filtres broker "+uri));
				messagesAcceptDeBroker++;
			} else {
				subscribers.get(inboundPortURI).receptionOutboundPort.acceptMessage(new Message("Bravo tu viens de " +
						"te souscrire au topic " + topic + "avec un filtre " + filterToString(filter) + " broker " +uri));
				messagesAcceptDeBroker++;
			}
		}finally {
			this.subscribersLock.unlock();
		}

		if (filter == null) {
			this.logMessage("Subscribed " + inboundPortURI + " to topic " + topic + " with no filter");
			this.historiqueAbonnements += "\n		On abonne " + inboundPortURI + " au sujet " + topic + " sans filtres";
		}
		else {
			this.logMessage("Subscribed " + inboundPortURI + " to topic " + topic + " with filter"+filter);
			this.historiqueAbonnements += "\n		On abonne " + inboundPortURI + " au sujet " + topic + " avec le filtre " + filterToString(filter);
		}


	}

	/**
	 * Modify filter.
	 * {@link SubscriptionImplementationI#modifyFilter(String, MessageFilterI, String)}
	 *
	 * @param topic          the topic
	 * @param newFilter      the new filter
	 * @param inboundPortURI the inbound port uri
	 * @throws Exception
	 */
	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI)throws Exception {
		this.subscribersLock.lock();
		try {
			while (!subscribers.containsKey(inboundPortURI))
				newSubscribers.await();
			if (isTopic(topic)) {
				this.changementFiltres += " \n		On change un filtre par un autre filtre ";
				this.logMessage("je remplace" + topic + "par " + filterToString(newFilter));
				subscribers.get(inboundPortURI).topics.replace(topic, newFilter);


			}
		}finally {
			this.subscribersLock.unlock();
		}

	}

	/**
	 * Unsubscribe to a topic
	 * {@link SubscriptionImplementationI#unsubscribe(String, String)}
	 *
	 * @param topic          the topic
	 * @param inboundPortUri the inbound port uri
	 * @throws Exception
	 */
	@Override
	public void unsubscribe(String topic, String inboundPortUri) throws Exception {
		this.subscribersLock.lock();
		try {
			if (this.subscribers.get(inboundPortUri).topics.containsKey(topic)) {
				this.logMessage("On enlève l'abonnement de " + inboundPortUri + " au topic " + topic);
				subscribers.get(inboundPortUri).receptionOutboundPort.acceptMessage(new Message("Tu viens de " +
						"te desabonner au topic " + topic));
				messagesAcceptDeBroker++;
				this.desabonnements += "\n		On desabonne " + inboundPortUri + " du topic " + topic;
				this.subscribers.get(inboundPortUri).topics.remove(topic);
			} else {
				this.desabonnements += "\n		On a pas pu desabonner " + inboundPortUri + " du topic " + topic + " car " +
						" il était pas abonné\n";
			}
		}finally {
			this.subscribersLock.unlock();
		}
	}

	/**
	 * Create a topic.
	 * {@link ManagementImplementationI#createTopic(String)}
	 *
	 * @param topic the topic
	 * @throws Exception
	 */
	@Override
	public void createTopic(String topic){
		logMessage("Creation of topic " + topic);
		this.messagesLock.lock();
		try{
			if(!messages.containsKey(topic)){
				messages.put(topic,new ArrayList<>());
				historiqueCreationTopics += "		On vient de créer dans le broker le topic "+topic +"\n";
			}else {
				historiqueCreationTopics += "		Le topic "+topic+" était déjà présent. \n";
			}
		}finally {
			this.messagesLock.unlock();
		}


	}


	/**
	 * Create multiple topics.
	 * {@link ManagementImplementationI#createTopics(String[])}
	 *
	 * @param topics the topics
	 * @throws Exception
	 */
	@Override
	public void createTopics(String[] topics){
		for (int i=0; i< topics.length; i++)
			createTopic(topics[i]);
	}


	/**
	 * Destroy a topic.
	 * {@link ManagementImplementationI#destroyTopic(String)}
	 *
	 * @param topic the topic
	 * @throws Exception
	 */
	@Override
	public void destroyTopic(String topic) {
		logMessage("Destruction of the topic " + topic);
		this.messagesLock.lock();
		try {
			if (messages.containsKey(topic)) {
				messages.remove(topic);
				suppressionSujets += "		Broker vient de supprimer le topic " + topic + "\n";
			} else {
				suppressionSujets += "		Broker a pas supprimé le topic  " + topic + " car il était pas présent. \n";
			}
			this.messages.remove(topic);
		}finally {
			this.messagesLock.unlock();
		}
	}


	/**
	 * Test if the topic exists.
	 * {@link ManagementImplementationI#isTopic(String)}
	 *
	 * @param topic the topic
	 * @return the boolean
	 * @throws Exception
	 */
	@Override
	public boolean isTopic(String topic) {
		boolean res;
		this.messagesLock.lock();
		try{
			res=  this.messages.containsKey(topic);
		}

		finally {
			this.messagesLock.unlock();
		}
		return res;
	}


	/**
	 * Get all the topics.
	 * {@link ManagementImplementationI#getTopics()}
	 *
	 * @return a tab containing the topics
	 * @throws Exception
	 */
	@Override
	public String[] getTopics()  {
		String[]res;
		this.messagesLock.lock();
		try {
			res = messages.keySet().toArray(new String[messages.keySet().size()]);
		}finally {
			this.messagesLock.unlock();
		}
		return res;
	}

	/**
	 * Gets publication port uri.
	 * {@link ManagementImplementationI#getPublicationPortURI()}
	 *
	 * @return the publication port uri
	 * @throws Exception
	 */
	@Override
	public String getPublicationPortURI() throws Exception {
		return this.pluginPublication.getPip().getPortURI();
	}


}
