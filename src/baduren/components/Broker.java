package baduren.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import baduren.connectors.ReceptionConnector;
import baduren.interfaces.MessageFilterI;
import baduren.interfaces.MessageI;
import baduren.interfaces.ReceptionCI;
import baduren.message.Message;
import baduren.ports.inboundPorts.ManagementInboundPort;
import baduren.ports.inboundPorts.PublicationInboundPort;
import baduren.ports.outboundPorts.ReceptionOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.AddPlugin;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.plugins.dconnection.DynamicConnectionClientSidePlugin;
import fr.sorbonne_u.components.ports.PortI;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;

@RequiredInterfaces(required = {ReflectionI.class, ReceptionCI.class})
@AddPlugin(pluginClass = DynamicConnectionClientSidePlugin.class, pluginURI = Broker.DYNAMIC_CONNECTION_PLUGIN_URI)

public class Broker extends AbstractComponent {
	
	private class Subscriber {
		public Subscriber(Broker b_instance) {
			this.uri = b_instance.uri + compteur;
			this.topics = new HashMap<String, MessageFilterI>();
			try {
				this.receptionOutboundPort = new ReceptionOutboundPort(b_instance.uri+compteur, b_instance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public String uri; // uri of outbound port
		public HashMap<String, MessageFilterI> topics; // topics aquel il est abonée
		public ReceptionOutboundPort receptionOutboundPort;
	}
	
	

	public final static String	DYNAMIC_CONNECTION_PLUGIN_URI ="clientSidePLuginURI" ;
	protected String uri; 
	int compteur; 
	private HashMap<String, List<MessageI>> messages; //Map between topic and messages (each topic has several messages)
	private HashMap<String, Subscriber> subscribers; // Map between the receptionInboundport and the subscriber
	private PublicationInboundPort publicationInboundPort; 
	

	protected Broker (String uri, String managementInboundPortName,String publicationInboundPortName, String receptionOutboundPortName) throws Exception {
		super(uri, 1, 0) ;
		this.uri=uri; 
		this.compteur = 0; 
		
		this.messages = new HashMap<>(); 
		this.subscribers = new HashMap<>();
		
		PortI managementInboundPort = new ManagementInboundPort(managementInboundPortName, this); 
		this.publicationInboundPort = new PublicationInboundPort(publicationInboundPortName, this); 
		PortI rop = new ReceptionOutboundPort(receptionOutboundPortName,this); 
		
		
		rop.localPublishPort();
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
		
		if(!isTopic(topic)) createTopic(topic); // Si le topic n'existait pas déjà on le crée
		this.messages.get(topic).add((Message) m); // On ajoute le message
		
		
		
		if(!isTopic(topic)) {
			logMessage("The following message hasn't been published because it's topic (" + topic + ") doesn't exist : " + m.getURI());
		} else {
			logMessage("Publishing message "+m.getURI()+" to topic " + topic);
			
			for(String inboundPortURI: subscribers.keySet()) {
				Subscriber subscriber = subscribers.get(inboundPortURI);
				
				if(subscriber.topics.containsKey(topic)) {
					boolean transfer_message = false;
					
					if(subscriber.topics.get(topic) == null) {
						transfer_message = true;
					} else if (subscriber.topics.get(topic).filter(m)) {
						transfer_message = true;
					}
					
					if(transfer_message) {
						// Méthode classique
						subscriber.receptionOutboundPort.acceptMessage(m);
						
						// Méthode greffon
						
						
						
						/*
						//System.out.println(this.installedPlugins.keySet().toString());
						//DynamicConnectionClientSidePlugin dconnectionPlugIn = null;
						
						//for(String plugin : installedPlugins.keySet()) {
						//	dconnectionPlugIn = (DynamicConnectionClientSidePlugin) installedPlugins.get(plugin);
						//	System.out.println(installedPlugins.get(plugin).toString());
						//}
						
						
						try {
							// Connecting the dynamic connection plug-ins
							
							DynamicConnectionClientSidePlugin dconnectionPlugIn =
									(DynamicConnectionClientSidePlugin)
											this.getPlugin(DYNAMIC_CONNECTION_PLUGIN_URI) ; // Peut etre faux
							System.out.println(inboundPortURI);
							
							
							//DynamicConnectionClientSidePlugin dconnectionPlugIn =
							//		(DynamicConnectionClientSidePlugin) installedPlugins.get(this.installedPlugins.keySet().toArray()[0]);
							
							dconnectionPlugIn.connectWithServerSide(inboundPortURI) ;

							// Use the dynamic connection facilities to connect the example
							// ports.
							ReceptionOutboundPort top =
								(ReceptionOutboundPort)
									dconnectionPlugIn.doDynamicConnection(
										ReceptionCI.class, // peut etre ReceptionCI
										inboundPortURI,
										ReceptionCI.class,
										new DynamicConnectionDescriptorI() {
											@Override
											public OutboundPortI	 createClientSideDynamicPort(
													Class<?> requiredInterface,
													ComponentI owner) {
												try {
													assert	requiredInterface.equals(ReceptionCI.class) ;
													return new ReceptionOutboundPort(owner) ;
												} catch (Exception e) {
													throw new RuntimeException(e) ;
												}
											}

											@Override
											public String dynamicConnectorClassName(
												Class<?> requiredInterface
												)
											{
												assert	requiredInterface.equals(ReceptionCI.class) ;
												return ReceptionConnector.class.getCanonicalName() ;
											}
										}) ;

							top.acceptMessage(m);
						} catch(Throwable t) {
							t.printStackTrace();
						}
						*/
						
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
		if (isTopic(topic)) {
			if(!subscribers.get(inboundPortURI).topics.containsKey(topic)) {
				subscribers.get(inboundPortURI).topics.put(topic, newFilter);
			}
		}
		
	}

	public void unsubscribe(String topic, String inboundPortUri) throws Exception{
		this.subscribers.remove(inboundPortUri); 
	}

	public void createTopic(String topic) throws Exception {
		logMessage("Creation of topic " + topic);
		if(!messages.containsKey(topic)) messages.put(topic,new ArrayList<>()); 
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
		return this.publicationInboundPort.getPortURI();
	}

}
