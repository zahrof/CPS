package baduren.components;

import java.lang.reflect.Array;
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
	
	//private List<ReceptionOutboundPort> rop; 
	
	private HashMap<String, List<MessageI>> messages; //Map between topic and messages (each topic has several messages)
	//private List<String> topics;
	/*
	private HashMap<String, HashMap<ReceptionOutboundPort,MessageFilterI>> subscribers; //Map between topics and subscribers URI ( each topic has several URI followers) 
	private HashMap<String, List<ReceptionOutboundPort>> subscribers_without_filters; 
	private HashMap<String, ReceptionOutboundPort> Map_URIPort;
	*/
	
	private class Subscriber {
		
		public Subscriber(Broker b_instance, HashMap<String, MessageFilterI> topics) {
			this.uri = b_instance.uri + compteur;
			this.topics = topics;
			try {
				this.receptionOutboundPort = new ReceptionOutboundPort(b_instance.uri+compteur, b_instance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public Subscriber(Broker b_instance) {
			this(b_instance, new HashMap<String, MessageFilterI>());
		}
		
		public String uri;
		public HashMap<String, MessageFilterI> topics;
		public ReceptionOutboundPort receptionOutboundPort;
	}
	private HashMap<String, Subscriber> subscribers;
	
	
	

	protected Broker (String uri, String managementInboundPortName,String publicationInboundPortName, String receptionOutboundPortName) throws Exception {
		super(uri, 1, 0) ;
		this.uri=uri; 
		this.compteur = 0; 
		
		this.messages = new HashMap<>(); 
		//this.topics = new ArrayList<>();
		/*
		this.subscribers = new HashMap<>(); 
		this.rop = new ArrayList<>(); 
		this.Map_URIPort = new HashMap<>(); 
		this.subscribers_without_filters = new HashMap<>(); 
		*/
		
		this.subscribers = new HashMap<>();
		
		PortI managementInboundPort = new ManagementInboundPort(managementInboundPortName, this); 
		PortI publicationInboundPort = new PublicationInboundPort(publicationInboundPortName, this); 
		//this.rop.add( new ReceptionOutboundPort(receptionOutboundPortName,this)); 
		
		
		//this.rop.get(0).localPublishPort();
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
		
		/*
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
		
		this.subscribers_without_filters.get("fruits").get(0).acceptMessage(this.messages.get("fruits").get(0));
		this.subscribers_without_filters.get("fruits").get(0).acceptMessage(this.messages.get("fruits").get(1));
		HashMap<ReceptionOutboundPort,MessageFilterI>hm =new HashMap<>();
		hm = this.subscribers.get("voiture"); 
		//hm.ge
		for(ReceptionOutboundPort up: hm.keySet()) {
			up.acceptMessage(this.messages.get("voiture").get(0));
		}
		
		//this.subscribers.get("voiture").get(0).acceptMessage(this.messages.get("voiture").get(0));
		//this.subscribers.get("voiture").get(0).acceptMessage(this.messages.get("voiture").get(1));
	
		//this.Map_URIPort.get("fruits").acceptMessage(this.messages.get("fruits").get(0));
		 * */
		 


	}

	@Override
	public void	finalise() throws Exception
	{
		this.logMessage("stopping broker component.") ;
		super.finalise();

	}
	

	
	public void publish(MessageI m, String topic)throws Exception {
		
		
		/* Non car on ne peut pas publier sur un topic qui a été supp
		if(!isTopic(topic)) {
			createTopic(topic); // Si le topic n'existait pas déjà on le crée
			logMessage("Creation of topic " + topic);
			this.messages.get(topic).add((Message) m);
			
		}
		else this.messages.get(topic).add((Message) m); // On ajoute le message
		*/
		this.messages.get(topic).add((Message) m); // On ajoute le message
		
		
		
		if(!isTopic(topic)) {
			logMessage("The following message hasn't been published because it's topic (" + topic + ") doesn't exist : " + m.getURI());
		} else {
			logMessage("Publishing message "+m.getURI()+" to topic " + topic);
			
			for(String inboundPortURI: subscribers.keySet()) {
				Subscriber subscriber = subscribers.get(inboundPortURI);
				
				if(subscriber.topics.containsKey(topic)) {
					if(subscriber.topics.get(topic) == null) {
						subscriber.receptionOutboundPort.acceptMessage(m);
					} else if (subscribers.get(inboundPortURI).topics.get(topic).filter(m)) {
						subscriber.receptionOutboundPort.acceptMessage(m);
					}
				}
			}
		}
		
		
		
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
		subscribe(topic, (MessageFilterI) null, inboundPortURI);
	}
	
	public void subscribe(String[] topics, String inboundPortURI) throws Exception{
		for(String s: topics)
			subscribe(s, inboundPortURI); 
		
	}

	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception{
		this.logMessage("Subscribing " + inboundPortURI + " to topic " + topic + " with filter");
		
		if(!subscribers.containsKey(inboundPortURI)) {
			subscribers.put(inboundPortURI, new Subscriber(this));
			subscribers.get(inboundPortURI).receptionOutboundPort.publishPort();
			
			this.doPortConnection(
					subscribers.get(inboundPortURI).uri, 
					inboundPortURI, 
					ReceptionConnector.class.getCanonicalName()
			);
		}
		
		createTopic(topic);
		
		if(!subscribers.get(inboundPortURI).topics.containsKey(topic)) {
			subscribers.get(inboundPortURI).topics.put(topic, filter);
		}
		
		this.compteur++;
		 
		
	}

	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI)throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void unsubscribe(String topic, String inboundPortUri) throws Exception{
		// TODO Auto-generated method stub
		
	}

	public void createTopic(String topic) throws Exception {
		if(!messages.containsKey(topic)) messages.put(topic,new ArrayList<>()); 
		//if(!topics.contains(topic)) topics.add(topic);
	}

	
	public void createTopics(String[] topics) throws Exception {
		for (int i=0; i< topics.length; i++) 
			createTopic(topics[i]); 

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
