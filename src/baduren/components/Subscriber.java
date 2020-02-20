package baduren.components;

import baduren.interfaces.MessageFilterI;
import baduren.interfaces.MessageI;
import baduren.interfaces.ReceptionCI;
import baduren.ports.inboundPorts.ReceptionInboundPort;
import baduren.ports.outboundPorts.ManagementOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.AddPlugin;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.plugins.dconnection.DynamicConnectionServerSidePlugin;
import fr.sorbonne_u.components.ports.InboundPortI;
import fr.sorbonne_u.components.ports.PortI;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;

/**
 * The type Subscriber.
 */
@OfferedInterfaces(offered = {ReceptionCI.class})
@AddPlugin(pluginClass = Subscriber.SubscriberSidePlugin.class,
		   pluginURI = Subscriber.DYNAMIC_CONNECTION_PLUGIN_URI)

public class Subscriber extends	AbstractComponent{
	/**
	 * The constant DYNAMIC_CONNECTION_PLUGIN_URI.
	 */
	public final static String	DYNAMIC_CONNECTION_PLUGIN_URI =
			"serverSidePLuginURI" ;

	/**
	 * The type Subscriber side plugin.
	 */
	public static class	SubscriberSidePlugin
	extends		DynamicConnectionServerSidePlugin
	{
		private static final long serialVersionUID = 1L;

		/**
		 * @see fr.sorbonne_u.components.plugins.dconnection.DynamicConnectionServerSidePlugin#createServerSideDynamicPort(java.lang.Class)
		 */
		@Override
		protected InboundPortI createServerSideDynamicPort(
			Class<?> offeredInterface
			) throws Exception
		{
			return new ReceptionInboundPort(this.owner) ;
		}
	}


	/**
	 * the outbound port used to call the service.
	 */
	protected ManagementOutboundPort managementOutboundPort;
	/**
	 * The Uri.
	 */
	protected String uri;
	/**
	 * The Reception inbound port.
	 */
	protected ReceptionInboundPort receptionInboundPort;


	/**
	 * Instantiates a new Subscriber.
	 *
	 * @param uri                        the uri
	 * @param receptionInboundPortName   the reception inbound port name
	 * @param managementOutboundPortName the management outbound port name
	 * @throws Exception the exception
	 */
	protected Subscriber(String uri, String receptionInboundPortName, String managementOutboundPortName)
			throws Exception {
		super(uri, 0, 1); 
		this.uri = uri; 
		this.receptionInboundPort  = new ReceptionInboundPort(receptionInboundPortName,this); 
		this.managementOutboundPort = new ManagementOutboundPort(managementOutboundPortName, this);
		receptionInboundPort.publishPort();
		managementOutboundPort.publishPort();
		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		}else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		this.tracer.setTitle("subscriber") ;
		this.tracer.setRelativePosition(1, 3) ;
	}
	
	@Override
	public void			start() throws ComponentStartException
	{
		super.start() ;
		this.logMessage("starting subscriber component.") ;
	}

	/**
	 * The type Vehicule aerien.
	 */
	public class VehiculeAerien implements MessageFilterI{

		@Override
		public boolean filter(MessageI m) throws Exception {
			//System.out.println()
			return m.getProperties().getBooleanProp("can_fly"); 
		}
		
	}
	
	@Override
	public void			execute() throws Exception
	{
		super.execute() ;
		subscribe("fruits", this.receptionInboundPort.getPortURI());
//		Thread.sleep(1000);
//		System.out.println("let's filterU"); 
		subscribe("voiture",new VehiculeAerien(),this.receptionInboundPort.getPortURI());
		
		subscribe("voiture", new VehiculeAerien(), this.receptionInboundPort.getPortURI());
		while(true) {}
	}
	

	
	@Override
	public void			finalise() throws Exception
	{
		super.finalise();
	}
	
	// TOUTES LES METHODES DE MANAGEMENTCI


	/**
	 * Subscribe.
	 *
	 * @param topic          the topic
	 * @param inboundPortURI the inbound port uri
	 * @throws Exception the exception
	 */
	public void subscribe(String topic, String inboundPortURI) throws Exception{
	//	logMessage("Ask a subscription at port: "+inboundPortURI+" to topic " + topic);
		this.managementOutboundPort.subscribe(topic, inboundPortURI);

	}

	/**
	 * Subscribe.
	 *
	 * @param topics         the topics
	 * @param inboundPortURI the inbound port uri
	 * @throws Exception the exception
	 */
	public void subscribe(String[] topics, String inboundPortURI)throws Exception {
		this.managementOutboundPort.subscribe(topics, inboundPortURI);

	}

	/**
	 * Subscribe.
	 *
	 * @param topic          the topic
	 * @param filter         the filter
	 * @param inboundPortURI the inbound port uri
	 * @throws Exception the exception
	 */
	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI)throws Exception {
		System.out.println("let's filterU"); 
		this.managementOutboundPort.subscribe(topic,filter,inboundPortURI);

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
		this.managementOutboundPort.modifyFilter(topic,newFilter,inboundPortURI);

	}

	/**
	 * Unsubscribe.
	 *
	 * @param topic          the topic
	 * @param inboundPortUri the inbound port uri
	 * @throws Exception the exception
	 */
	public void unsubscribe(String topic, String inboundPortUri)throws Exception {
		this.managementOutboundPort.unsubscribe(topic,inboundPortUri);

	}

	/**
	 * Create topic.
	 *
	 * @param topic the topic
	 * @throws Exception the exception
	 */
	public void createTopic(String topic) throws Exception{
		this.managementOutboundPort.createTopic(topic);

	}

	/**
	 * Create topics.
	 *
	 * @param topics the topics
	 * @throws Exception the exception
	 */
	public void createTopics(String[] topics) throws Exception{
		this.managementOutboundPort.createTopics(topics);

	}

	/**
	 * Destroy topic.
	 *
	 * @param topic the topic
	 * @throws Exception the exception
	 */
	public void destroyTopic(String topic) throws Exception{
		this.managementOutboundPort.destroyTopic(topic);

	}

	/**
	 * Is topic boolean.
	 *
	 * @param topic the topic
	 * @return the boolean
	 */
	public boolean isTopic(String topic) {
		return this.managementOutboundPort.isTopic(topic);
	}

	/**
	 * Get topics string [ ].
	 *
	 * @return the string [ ]
	 * @throws Exception the exception
	 */
	public String[] getTopics() throws Exception{
		return this.managementOutboundPort.getTopics();
	}

	/**
	 * Gets publication port uri.
	 *
	 * @return the publication port uri
	 */
	public String getPublicationPortURI() {
		return this.managementOutboundPort.getPublicationPortURI();
	}
	
	// TOUTES LES METHODES DE RECEPTIONCI

	/**
	 * Accept message.
	 *
	 * @param m the m
	 */
	public void acceptMessage(MessageI m) {
		this.logMessage("Receiving/accepting the message "+m.getURI()+ " send by : "+ m.getTimeStamp().getTimeStamper() +
				" a la date de "+ m.getTimeStamp().getTime());
	}

	/**
	 * Accept messages.
	 *
	 * @param ms the ms
	 */
	public void acceptMessages(MessageI[] ms) {
		for (MessageI m : ms) {
			acceptMessage(m); 
		}
	}
}
