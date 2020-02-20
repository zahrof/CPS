package baduren.components;

import baduren.interfaces.MessageFilterI;
import baduren.interfaces.MessageI;
import baduren.message.Message;
import baduren.message.Properties;
import baduren.ports.outboundPorts.ManagementOutboundPort;
import baduren.ports.outboundPorts.PublicationOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

public class Publisher extends AbstractComponent {


	// ------------------------------------------------------------------------
	// Constructors and instance variables
	// ------------------------------------------------------------------------

	protected String uriPrefix ;

	protected ManagementOutboundPort managementOutboundPort;
	protected PublicationOutboundPort publicationOutboundPort;

	protected Publisher(String uri, String managementOutboundPortName, String publicationOutboundPortName) 
			throws Exception {
		super(uri, 0, 1); 
		this.uriPrefix = uri;

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
		this.tracer.setRelativePosition(1, 2) ;

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
			Thread.sleep(1000);
			for (int i=0; i <10; i++) {
				publish(new Message("Banane"+i), "fruits");
			}
			String topics[]= {"voiture", "avions"}; 
			Message m = new Message("voiture-volante"); 
			Properties p = m.getProperties();
			p.putProp("can_fly", true);
			
			publish(m,topics); 
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

	public void publish(MessageI m, String topic) throws Exception {
		logMessage("Publishing message " + m.getURI()+ " to the topic : "+ topic);
		this.publicationOutboundPort.publish(m, topic);
	}


	public void publish(MessageI m, String[] topics) throws Exception {
		String str= " "; 
		for (String s : topics) {
			str += s+ " ";
		}
		logMessage("Publishing message " + m.getURI()+ " to the topic : "+str);
		this.publicationOutboundPort.publish(m, topics);

	}


	public void publish(MessageI[] ms, String topics) throws Exception {
		String str= " "; 
		for (MessageI s : ms) {
			str += s.getURI()+ " ";
		}
		logMessage("Publishing message " + str+ " to the topic : "+topics);
		this.publicationOutboundPort.publish(ms, topics);

	}


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


	public void subscribe(String topic, String inboundPortURI)throws Exception {
		this.managementOutboundPort.subscribe(topic, inboundPortURI);

	}

	public void subscribe(String[] topics, String inboundPortURI)throws Exception {
		this.managementOutboundPort.subscribe(topics, inboundPortURI);

	}

	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception{
		this.managementOutboundPort.subscribe(topic,filter, inboundPortURI);

	}

	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception{
		this.managementOutboundPort.subscribe(topic, newFilter, inboundPortURI);

	}

	public void unsubscribe(String topic, String inboundPortUri) throws Exception {
		this.managementOutboundPort.unsubscribe(topic, inboundPortUri);
	}

	public void createTopic(String topic)throws Exception {
		this.managementOutboundPort.createTopic(topic);
	}

	public void createTopics(String[] topics) throws Exception{
		this.managementOutboundPort.createTopics(topics);
	}

	public void destroyTopic(String topic)throws Exception {
		this.managementOutboundPort.destroyTopic(topic);
	}

	public boolean isTopic(String topic) throws Exception{
		return this.managementOutboundPort.isTopic(topic);
	}

	public String[] getTopics() throws Exception{
		return this.managementOutboundPort.getTopics();
	}

	public String getPublicationPortURI() throws Exception{
		return this.managementOutboundPort.getPublicationPortURI();
	}






}
