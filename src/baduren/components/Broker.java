package baduren.components;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import baduren.interfaces.MessageFilterI;
import baduren.interfaces.MessageI;
import baduren.message.Message;
import baduren.ports.inboundPorts.ManagementInboundPort;
import baduren.ports.inboundPorts.PublicationInboundPort;
import baduren.ports.outboundPorts.ReceptionOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.PortI;

public class Broker extends AbstractComponent {

	protected ReceptionOutboundPort receptionOutboundPort;
	protected String uri; 
	
	private Map<String, List<Message>> messages; //Map between topic and messages ( each topic has several messages)
	private Map<String, List<String>> subscribers; //Map between topics and subscribers URI ( each topic has several URI followers) 
	
//	private String[] topics= new String[0]; 
//	private Message[] messages = new Message[0]; 

	protected Broker (String uri, 
			String managementInboundPortName, String publicationInboundPortName, String receptionOutboundPortName) throws Exception {
		super(uri, 1, 0) ;
		this.uri=uri; 
		PortI managementInboundPort = new ManagementInboundPort(managementInboundPortName, this); 
		PortI publicationInboundPort = new PublicationInboundPort(publicationInboundPortName, this); 
		this.receptionOutboundPort = new ReceptionOutboundPort(receptionOutboundPortName,this); 
		managementInboundPort.publishPort();
		publicationInboundPort.publishPort();
		receptionOutboundPort.localPublishPort();
		
		List<Message> aux = new Vector<Message>(); 
		this.messages = new HashMap<String,List<Message>>(); 

		// Pour les logs
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		this.tracer.setTitle("broker") ;
		this.tracer.setRelativePosition(1, 1) ;
	}


	@Override
	public void			start() throws ComponentStartException
	{
		super.start() ;
		this.logMessage("starting broker component.") ;
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
	
	public void publish(MessageI m, String topic)throws Exception {
		logMessage("Publishing message "+m.getURI()+" to topic " + topic);
		if(!isTopic(topic)) createTopic(topic); // Si le topic n'existait pas déjà on le crée
		this.messages.get(topic).add((Message) m); 
		
		this.receptionOutboundPort.acceptMessage(m);
		
	}
	
	
	public void publish(MessageI m, String[] topics) throws Exception{
		for(String topic : topics)
			publish(m,topic);
	}

	
	public void publish(MessageI[] ms, String topics) throws Exception{
		for(MessageI msg : ms)
			publish(msg, topics); 
		
	}

	
	public void publish(MessageI[] ms, String[] topics) throws Exception{
		for(MessageI msg : ms) {
			for(String topic: topics) {
				publish(msg, topic); 
			}
		}
		
	}
	
	public void subscribe(String topic, String inboundPortURI) throws Exception{
		//  
		// 
		
		
	}

	public void subscribe(String[] topics, String inboundPortURI) throws Exception{
		// TODO Auto-generated method stub
		
	}

	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception{
		// TODO Auto-generated method stub
		
	}

	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI)throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void unsubscribe(String topic, String inboundPortUri) throws Exception{
		// TODO Auto-generated method stub
		
	}

	public void createTopic(String topic) throws Exception {
		this.messages.putIfAbsent(topic, new Vector<Message>());
//		this.topics = addX(this.topics, topic); 

	}

	
	public void createTopics(String[] topics) throws Exception {
		for (int i=0; i< topics.length; i++) 
			this.messages.putIfAbsent(topics[i], new Vector<Message>());

	}

	
	public void destroyTopic(String topic) throws Exception {
		this.messages.remove(topic);

	}

	
	public boolean isTopic(String topic) throws Exception {
		return this.messages.containsKey(topic); 
	}

	
	public String[] getTopics() throws Exception {
		return messages.keySet().toArray(new String[messages.keySet().size()]); 
	}

	
	public String getPublicationPortURI() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
