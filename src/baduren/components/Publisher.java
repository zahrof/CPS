package baduren.components;

import baduren.interfaces.MessageFilterI;
import baduren.interfaces.MessageI;
import baduren.message.Message;
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
		for (int i=0; i <10; i++) {
			publish(new Message("Banane"+i), "fruits");
		}
		String topics[]= {"voiture", "avions"}; 
		publish(new Message("voiture-volante"), topics); 
		
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
		this.publicationOutboundPort.publish(m, topics);

	}


	public void publish(MessageI[] ms, String topics) throws Exception {
		this.publicationOutboundPort.publish(ms, topics);

	}


	public void publish(MessageI[] ms, String[] topics) throws Exception {
		this.publicationOutboundPort.publish(ms, topics);

	}
	
	// TOUTES LES METHODES DE MANAGEMENTCI


	public void subscribe(String topic, String inboundPortURI) {
		// TODO Auto-generated method stub

	}

	public void subscribe(String[] topics, String inboundPortURI) {
		// TODO Auto-generated method stub

	}

	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) {
		// TODO Auto-generated method stub

	}

	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) {
		// TODO Auto-generated method stub

	}

	public void unsubscribe(String topic, String inboundPortUri) {
		// TODO Auto-generated method stub

	}

	public void createTopic(String topic) {
		// TODO Auto-generated method stub

	}

	public void createTopics(String[] topics) {
		// TODO Auto-generated method stub

	}

	public void destroyTopic(String topic) {
		// TODO Auto-generated method stub

	}

	public boolean isTopic(String topic) {
		// TODO Auto-generated method stub
		return false;
	}

	public String[] getTopics() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPublicationPortURI() {
		// TODO Auto-generated method stub
		return null;
	}






}
