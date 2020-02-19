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
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.PortI;

public class Broker extends AbstractComponent {

	protected String uri; 
	int compteur; 
	
	private List<ReceptionOutboundPort> rop; 
	
	private HashMap<String, List<MessageI>> messages; //Map between topic and messages (each topic has several messages)
	private HashMap<String, HashMap<ReceptionOutboundPort,MessageFilterI>> subscribers; //Map between topics and subscribers URI ( each topic has several URI followers) 
	private HashMap<String, List<ReceptionOutboundPort>> aux; 
	private HashMap<String, ReceptionOutboundPort> clients;
	

	protected Broker (String uri, String managementInboundPortName,String publicationInboundPortName, String receptionOutboundPortName) throws Exception {
		super(uri, 1, 0) ;
		this.uri=uri; 
		this.compteur =0; 
		
		this.messages = new HashMap<>(); 
		this.subscribers = new HashMap<>(); 
		this.rop = new ArrayList<>(); 
		this.clients = new HashMap<>(); 
		this.aux = new HashMap<>(); 
		
		PortI managementInboundPort = new ManagementInboundPort(managementInboundPortName, this); 
		PortI publicationInboundPort = new PublicationInboundPort(publicationInboundPortName, this); 
		this.rop.add( new ReceptionOutboundPort(receptionOutboundPortName,this)); 
		
		
		this.rop.get(0).localPublishPort();
		managementInboundPort.publishPort();
		publicationInboundPort.publishPort();
		
		this.tracer.setTitle("broker") ;
		this.tracer.setRelativePosition(1, 1) ;
	}


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
		
		System.out.println("taille ici :" + this.messages.size()); 
		System.out.println("ici on a bizarrement : " +  this.subscribers.get("fruits").size());
				while(this.messages.size()<1 || this.subscribers.get("fruits").size() != 1) {
					
					try {
						//System.out.println(mapMessage.size());
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("hola");
				this.logMessage("le broker previent les publisher ");
				//System.out.println("ici c'est bon la taille est bien de " ); //+ mapSubscription.get("banane").size());
				
//				for(ReceptionOutboundPort sub :this.subscribers.get("fruits").keySet()) {
//					
//					this.logMessage("kokokokko");
//					sub.acceptMessage(this.messages.get("fruits").get(0));
//					this.logMessage("halleluiah "+ this.messages.get("fruits").get(0).getURI());
//						
//				}
				this.aux.get("fruits").get(0).acceptMessage(this.messages.get("fruits").get(0));
			
				this.clients.get("fruits").acceptMessage(this.messages.get("fruits").get(0));


	}

	@Override
	public void	finalise() throws Exception
	{
		this.logMessage("stopping broker component.") ;
		super.finalise();

	}
	

	
	public void publish(MessageI m, String topic)throws Exception {
		
		logMessage("Publishing message "+m.getURI()+" to topic " + topic);
		if(!isTopic(topic)) {
			createTopic(topic); // Si le topic n'existait pas déjà on le crée
			logMessage("I've create the topic "+ topic);
			this.messages.get(topic).add((Message) m);
			
		}
		else this.messages.get(topic).add((Message) m); // On ajoute le message
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
		logMessage("Subscribing "+inboundPortURI+" to topic " + topic);
	
		ReceptionOutboundPort rp; 
		
		if(clients.containsKey(inboundPortURI)) {
			
			rp = this.clients.get(inboundPortURI); 
		}else {
			System.out.println("patate"); 
			rp = new ReceptionOutboundPort(this.uri+compteur, this); 
			this.clients.put(inboundPortURI, rp); 
			rp.publishPort();
			this.rop.add(rp); 
			this.doPortConnection(this.uri+compteur, inboundPortURI, ReceptionConnector.class.getCanonicalName());
		}
		
		if(this.aux.containsKey(topic)) {
			this.aux.get(topic).add(rp); 
		}else {
			this.createTopic(topic);
			this.subscribers.get(topic).put(rp, null); 
			this.aux.get(topic).add(rp); 
		}
		System.out.println("subs: "+ this.subscribers.get("fruits").keySet().size()); 
		System.out.println("aux: "+ this.aux.get("fruits").size()); 
		
		
//		if(this.subscribers.containsKey(topic)) {
//			this.subscribers.get(topic).put(rp, null); 
//		}else {
//			this.createTopic(topic);
//			this.subscribers.get(topic).put(rp, null); 
//		}
//		System.out.println("subs: "+ this.subscribers.get("fruits").keySet().size()); 
		this.compteur++; 
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
		this.subscribers.put(topic, new HashMap<>());
		this.aux.put(topic, new ArrayList<>());
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
