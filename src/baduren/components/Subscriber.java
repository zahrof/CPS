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
	
	public class VehiculeAerien implements MessageFilterI{

		@Override
		public boolean filter(MessageI m) throws Exception {
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
//		subscribe("voiture",new VehiculeAerien(),this.receptionInboundPort.getPortURI());
		
		subscribe("voiture", new VehiculeAerien(), this.receptionInboundPort.getPortURI());
		while(true) {}
	}
	

	
	@Override
	public void			finalise() throws Exception
	{
		super.finalise();
	}
	
	// TOUTES LES METHODES DE MANAGEMENTCI


	public void subscribe(String topic, String inboundPortURI) throws Exception{
	//	logMessage("Ask a subscription at port: "+inboundPortURI+" to topic " + topic);
		this.managementOutboundPort.subscribe(topic, inboundPortURI);

	}

	public void subscribe(String[] topics, String inboundPortURI)throws Exception {
		this.managementOutboundPort.subscribe(topics, inboundPortURI);

	}

	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI)throws Exception {
		System.out.println("let's filterU"); 
		this.managementOutboundPort.subscribe(topic,filter,inboundPortURI);

	}

	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI)throws Exception {
		this.managementOutboundPort.modifyFilter(topic,newFilter,inboundPortURI);

	}

	public void unsubscribe(String topic, String inboundPortUri)throws Exception {
		this.managementOutboundPort.unsubscribe(topic,inboundPortUri);

	}

	public void createTopic(String topic) throws Exception{
		this.managementOutboundPort.createTopic(topic);

	}

	public void createTopics(String[] topics) throws Exception{
		this.managementOutboundPort.createTopics(topics);

	}

	public void destroyTopic(String topic) throws Exception{
		this.managementOutboundPort.destroyTopic(topic);

	}

	public boolean isTopic(String topic) {
		return this.managementOutboundPort.isTopic(topic);
	}

	public String[] getTopics() throws Exception{
		return this.managementOutboundPort.getTopics();
	}

	public String getPublicationPortURI() {
		return this.managementOutboundPort.getPublicationPortURI();
	}
	
	// TOUTES LES METHODES DE RECEPTIONCI
	
	public void acceptMessage(MessageI m) {
		this.logMessage("Receiving/accepting the message "+m.getURI()+ "send by : "+ m.getTimeStamp()+
				" a la date de "+ m.getTimeStamp().getTime());
	}
	public void acceptMessages(MessageI[] ms) {
		for (MessageI m : ms) {
			this.logMessage("Receiving/accepting the message "+m.getURI());
		}
	}
}
