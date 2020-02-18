package baduren.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import baduren.connectors.ReceptionConnector;
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

	//protected ReceptionOutboundPort receptionOutboundPort;
	protected String uri; 
	
	int compteur =0; 
	
	private List<ReceptionOutboundPort> rop; 
	
	private HashMap<String, List<MessageI>> messages; //Map between topic and messages ( each topic has several messages)
	private HashMap<String, List<String>> subscribers; //Map between topics and subscribers URI ( each topic has several URI followers) 
	
//	private String[] topics= new String[0]; 
//	private Message[] messages = new Message[0]; 

	protected Broker (String uri, String managementInboundPortName,String publicationInboundPortName, String receptionOutboundPortName) throws Exception {
		super(uri, 1, 0) ;
		this.uri=uri; 
		
		PortI managementInboundPort = new ManagementInboundPort(managementInboundPortName, this); 
		PortI publicationInboundPort = new PublicationInboundPort(publicationInboundPortName, this); 
		
		
		
		managementInboundPort.publishPort();
		
		publicationInboundPort.publishPort();
		
		
		
		this.rop = new ArrayList<ReceptionOutboundPort>(); 
		this.rop.add( new ReceptionOutboundPort(receptionOutboundPortName,this)); 
		this.rop.get(0).localPublishPort();
		this.messages = new HashMap<String,List<MessageI>>(); 
		
		this.messages = new HashMap<String,List<MessageI>>(); 
		
		

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
		
		while(this.messages.size()==0) {
			try {
				this.wait();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("on est la");
		System.out.println("messages: "+ this.messages+ this.messages.size()); 
		System.out.println("messgfgfages: "+ this.messages.get("fruits")+ this.messages.size()); 
		this.logMessage("envoie message depuis Broker ");
		System.out.println("taille: "+this.subscribers.get("fruits").size());
		this.logMessage("patate ");
		for(String sub : this.subscribers.get("fruits") ) {
			for(ReceptionOutboundPort r : this.rop) {
		
				if( r.getPortURI()==sub) {
			
					r.acceptMessage(this.messages.get("fruits").get(0));
				}
			}
		}
		System.out.println("on est la encore");
		
		//this.mapMessage.get("banane");

	}

	@Override
	public void			finalise() throws Exception
	{
		super.finalise();
	}
	
	public void publish(MessageI m, String topic)throws Exception {
		
		logMessage("Publishing message "+m.getURI()+" to topic " + topic);
		if(!isTopic(topic)) {
			logMessage("holzzz" );
			createTopic(topic); // Si le topic n'existait pas déjà on le crée
			logMessage("I've create the topic "+ topic);
			this.messages.get(topic).add((Message) m);
			
		}
		else this.messages.get(topic).add((Message) m); // On ajoute le message
		System.out.println("messagesfruite: "+this.messages.get("fruits").indexOf(0));
		//this.receptionOutboundPort.acceptMessage(m);
		
		
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
		System.out.println("je bloque là ");
		if(this.subscribers.containsKey(topic)) {
			
			this.subscribers.get(topic).add(inboundPortURI); 
			
			this.rop.add(new ReceptionOutboundPort(uri+compteur, this));
			this.doPortConnection(this.uri+compteur, inboundPortURI, ReceptionConnector.class.getCanonicalName());
			this.compteur++; 
		}else {
			
			this.createTopic(topic);
			this.subscribers.get(topic).add(inboundPortURI);
		}
		System.out.println("subscribers: "+ this.subscribers);
		
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
		this.messages.put(topic,new ArrayList<>()); 
		this.subscribers.put(topic, new ArrayList<>());
	}

	
	public void createTopics(String[] topics) throws Exception {
		for (int i=0; i< topics.length; i++) 
			createTopic(topics[i]); 

	}

	
	public void destroyTopic(String topic) throws Exception {
		this.messages.remove(topic);
		this.subscribers.remove(topic); 

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
