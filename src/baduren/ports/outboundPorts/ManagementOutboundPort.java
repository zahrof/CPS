package baduren.ports.outboundPorts;

import baduren.interfaces.ManagementCI;
import baduren.interfaces.MessageFilterI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.examples.basic_cs.interfaces.URIConsumerI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class ManagementOutboundPort extends		AbstractOutboundPort implements ManagementCI {
	
	private static final long serialVersionUID = 1L;

	// Constructeurs
	
	public ManagementOutboundPort(String uri, ComponentI owner) throws Exception{
		super(uri, ManagementCI.class, owner) ;
		assert	uri != null && owner != null ;
	}
	
	public ManagementOutboundPort(ComponentI owner) throws Exception{
		super(ManagementCI.class, owner) ;
		assert	uri != null && owner != null ;
	}
	
	// Methodes

	@Override
	public void subscribe(String topic, String inboundPortURI) {
//		((ManagementCI)this.connector).subscribe(topic,inboundPortURI);
		
	}

	@Override
	public void subscribe(String[] topics, String inboundPortURI) {
//		((ManagementCI)this.connector).subscribe(topics,inboundPortURI);
		
	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) {
//		((ManagementCI)this.connector).subscribe(topic,filter,inboundPortURI);
		
	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) {
//		((ManagementCI)this.connector).modifyFilter(topic,newFilter,inboundPortURI);
		
	}

	@Override
	public void unsubscribe(String topic, String inboundPortUri) {
//		((ManagementCI)this.connector).unsubscribe(topic,inboundPortUri);
		
	}

	@Override
	public void createTopic(String topic) {
//		((ManagementCI)this.connector).createTopic(topic);
		
	}

	@Override
	public void createTopics(String[] topics) {
//		((ManagementCI)this.connector).createTopics(topics);
		
	}

	@Override
	public void destroyTopic(String topic) {
//		((ManagementCI)this.connector).destroyTopic(topic);
		
	}

	@Override
	public boolean isTopic(String topic) {
		return isRemotelyConnected;
		//return ((ManagementCI)this.connector).isTopic(topic);
	}

	@Override
	public String[] getTopics() {
		return null;
		//return ((ManagementCI)this.connector).getTopics();
	}

	@Override
	public String getPublicationPortURI() {
		return serverPortURI;
		//return ((ManagementCI)this.connector).getPublicationPortURI();
	}


}
