package baduren.components;

import baduren.interfaces.MessageFilterI;
import baduren.interfaces.MessageI;
import baduren.ports.inboundPorts.ReceptionInboundPort;
import baduren.ports.outboundPorts.ManagementOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.PortI;

public class Subscriber extends	AbstractComponent{


	/**	the outbound port used to call the service.							*/
	protected ManagementOutboundPort managementOutboundPort;
	protected String uri; 
	protected ReceptionInboundPort receptionInboundPort; 
	
	
	protected Subscriber(String uri, String receptionInboundPortName, String managementOutboundPortName) 
			throws Exception {
		super(uri, 0, 1); 
		this.uri = uri; 
		this.receptionInboundPort  = new ReceptionInboundPort(receptionInboundPortName,this); 
		this.managementOutboundPort = new ManagementOutboundPort(managementOutboundPortName, this);
		receptionInboundPort.publishPort();
		managementOutboundPort.localPublishPort();
		
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
		this.logMessage("starting publisher component.") ;
	}
	
	@Override
	public void			execute() throws Exception
	{
		super.execute() ;
		
		

		
	}
	
	@Override
	public void			finalise() throws Exception
	{
		super.finalise();
	}
	
	// TOUTES LES METHODES DE MANAGEMENTCI


	public void subscribe(String topic, String inboundPortURI) {
		logMessage("Subscribing to topic " + topic+ " with the inbound PortURI : "+ inboundPortURI);
		this.managementOutboundPort.subscribe(topic, inboundPortURI);

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
	
	// TOUTES LES METHODES DE RECEPTIONCI
	
	public void acceptMessage(MessageI m) {
		this.logMessage("Receiving/accepting the message "+m.getURI());
	}
	public void acceptMessages(MessageI[] ms) {
		
	}
}
