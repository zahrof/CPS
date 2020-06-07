package baduren.components.publishers;

import baduren.interfaces.ManagementCI;
import baduren.interfaces.MessageFilterI;
import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import baduren.message.Message;
import baduren.message.Properties;
import baduren.ports.outboundPorts.ManagementOutboundPort;
import baduren.ports.outboundPorts.PublicationOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

/**
 * The type Publisher.
 */
public class PublisherWithoutPlugin extends AbstractComponent {


	// ------------------------------------------------------------------------
	// Constructors and instance variables
	// ------------------------------------------------------------------------

	/**
	 * The Uri prefix.
	 */
	protected String uriPrefix ;

	/**
	 * The Management outbound port.
	 */
	protected ManagementOutboundPort managementOutboundPort;
	/**
	 * The Publication outbound port.
	 */
	protected PublicationOutboundPort publicationOutboundPort;

	/**
	 * Instantiates a new Publisher.
	 *
	 * @param uri                         the uri
	 * @param managementOutboundPortName  the management outbound port name
	 * @param publicationOutboundPortName the publication outbound port name
	 * @throws Exception the exception
	 */
	protected PublisherWithoutPlugin(String uri, String managementOutboundPortName, String publicationOutboundPortName,
									 int nbThreads, int nbSchedulableThreads) throws Exception {
		super(uri, nbThreads, nbSchedulableThreads);
		this.uriPrefix = uri;
		this.addRequiredInterface(PublicationCI.class);
		this.addRequiredInterface(ManagementCI.class);
		this.managementOutboundPort = new ManagementOutboundPort(managementOutboundPortName, this); 
		this.publicationOutboundPort = new PublicationOutboundPort(publicationOutboundPortName,this); 
		managementOutboundPort.localPublishPort();
		publicationOutboundPort.localPublishPort();

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		}else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		this.tracer.setTitle("publisher") ;
		this.tracer.setRelativePosition(0, 2) ;

	}


	@Override
	public void			start() throws ComponentStartException
	{
		super.start() ;
		this.logMessage("starting publisher component.") ;
	}

	@Override
	public void			execute() throws Exception
	{
		super.execute() ;
		
		try {
			Thread.sleep(100);
			for (int i=0; i <10; i++) {
				publish(new Message("Banane"+i), "fruits");
			}
			String[] topics = {"voiture", "avions"};
			Message m = new Message("voiture-volante"); 
			Properties p = m.getProperties();
			p.putProp("can_fly", true);
			
			publish(m,topics);
			System.out.print(" ");

			Thread.sleep(1000);
			for (int i=0; i <10; i++) {
				publish(new Message("Banane2"+i), "fruits");
			}
			String topics2[]= {"voiture", "avions"};
			Message m2 = new Message("voiture-volante2");
			Properties p2 = m2.getProperties();
			p2.putProp("can_fly", true);

			publish(m2,topics2);


		} catch(Throwable t) {
			t.printStackTrace();
		}
		
	}

	@Override
	public void			finalise() throws Exception
	{
		this.logMessage("stopping publisher component.") ;
		this.printExecutionLogOnFile("publisher");

		this.publicationOutboundPort.unpublishPort();
		this.managementOutboundPort.unpublishPort();

		super.finalise();
	}

	
	// TOUTES LES METHODES DE PUBLICATIONSCI

	/**
	 * Publish.
	 *
	 * @param m     the m
	 * @param topic the topic
	 * @throws Exception the exception
	 */
	public void publish(MessageI m, String topic) throws Exception {
		logMessage("Publishing message " + m.getURI()+ " to the topic : "+ topic );
		this.publicationOutboundPort.publish(m, topic);
	}


	/**
	 * Publish.
	 *
	 * @param m      the m
	 * @param topics the topics
	 * @throws Exception the exception
	 */
	public void publish(MessageI m, String[] topics) throws Exception {
		String str= " "; 
		for (String s : topics) {
			str += s+ " ";
		}
		logMessage("Publishing message " + m.getURI()+ " to the topic : "+str);
		this.publicationOutboundPort.publish(m, topics);

	}


	/**
	 * Publish.
	 *
	 * @param ms     the ms
	 * @param topics the topics
	 * @throws Exception the exception
	 */
	public void publish(MessageI[] ms, String topics) throws Exception {
		String str= " "; 
		for (MessageI s : ms) {
			str += s.getURI()+ " ";
		}
		logMessage("Publishing message " + str+ " to the topic : "+topics);
		this.publicationOutboundPort.publish(ms, topics);

	}


	/**
	 * Publish.
	 *
	 * @param ms     the ms
	 * @param topics the topics
	 * @throws Exception the exception
	 */
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		String str= " "; 
		for (MessageI s : ms) {
			str += s.getURI()+ " ";
		}
		String str2= " "; 
		for (String s : topics) {
			str2 += s+ " ";
		}
		logMessage("Publishing message " + str+ " to the topic : "+str2);
		this.publicationOutboundPort.publish(ms, topics);

	}
	
	// TOUTES LES METHODES DE MANAGEMENTCI


	/**
	 * Subscribe.
	 *
	 * @param topic          the topic
	 * @param inboundPortURI the inbound port uri
	 * @throws Exception the exception
	 */
	public void subscribe(String topic, String inboundPortURI)throws Exception {
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
	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception{
		this.managementOutboundPort.subscribe(topic,filter, inboundPortURI);

	}

	/**
	 * Modify filter.
	 *
	 * @param topic          the topic
	 * @param newFilter      the new filter
	 * @param inboundPortURI the inbound port uri
	 * @throws Exception the exception
	 */
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception{
		this.managementOutboundPort.subscribe(topic, newFilter, inboundPortURI);

	}

	/**
	 * Unsubscribe.
	 *
	 * @param topic          the topic
	 * @param inboundPortUri the inbound port uri
	 * @throws Exception the exception
	 */
	public void unsubscribe(String topic, String inboundPortUri) throws Exception {
		this.managementOutboundPort.unsubscribe(topic, inboundPortUri);
	}

	/**
	 * Create topic.
	 *
	 * @param topic the topic
	 * @throws Exception the exception
	 */
	public void createTopic(String topic)throws Exception {
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
	public void destroyTopic(String topic)throws Exception {
		this.managementOutboundPort.destroyTopic(topic);
	}

	/**
	 * Is topic boolean.
	 *
	 * @param topic the topic
	 * @return the boolean
	 * @throws Exception the exception
	 */
	public boolean isTopic(String topic) throws Exception{
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
	 * @throws Exception the exception
	 */
	public String getPublicationPortURI() throws Exception{
		return this.managementOutboundPort.getPublicationPortURI();
	}






}
