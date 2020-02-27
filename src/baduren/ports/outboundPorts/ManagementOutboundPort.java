package baduren.ports.outboundPorts;

import baduren.interfaces.ManagementCI;
import baduren.interfaces.MessageFilterI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.examples.basic_cs.interfaces.URIConsumerI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * The type ManagementOutboundPort.
 */
public class ManagementOutboundPort extends		AbstractOutboundPort implements ManagementCI {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new Management outbound port.
	 *
	 * @param uri   the uri
	 * @param owner the owner
	 * @throws Exception the exception
	 */
	public ManagementOutboundPort(String uri, ComponentI owner) throws Exception{
		super(uri, ManagementCI.class, owner) ;
		assert	uri != null && owner != null ;
	}

	/**
	 * Instantiates a new Management outbound port.
	 *
	 * @param owner the owner
	 * @throws Exception the exception
	 */
	public ManagementOutboundPort(ComponentI owner) throws Exception{
		super(ManagementCI.class, owner) ;
		assert	uri != null && owner != null ;
	}
	

	@Override
	public void subscribe(String topic, String inboundPortURI) throws Exception {
		((ManagementCI)this.connector).subscribe(topic,inboundPortURI);
		assert	topic != null && inboundPortURI != null ;
		
	}

	@Override
	public void subscribe(String[] topics, String inboundPortURI) throws Exception {
		((ManagementCI)this.connector).subscribe(topics,inboundPortURI);
		assert	topics != null && inboundPortURI != null ;
		
	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception {
		System.out.println("let's filter"); 
		((ManagementCI)this.connector).subscribe(topic,filter,inboundPortURI);
		
	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception {
		((ManagementCI)this.connector).modifyFilter(topic,newFilter,inboundPortURI);
		assert	topic != null && inboundPortURI != null ;
		
	}

	@Override
	public void unsubscribe(String topic, String inboundPortUri) throws Exception {
		((ManagementCI)this.connector).unsubscribe(topic,inboundPortUri);
		//assert	topic != null && inboundPortURI != null ;
		
	}

	@Override
	public void createTopic(String topic) throws Exception {
		((ManagementCI)this.connector).createTopic(topic);
		//assert	topic != null && inboundPortURI != null ;
		
	}

	@Override
	public void createTopics(String[] topics) throws Exception {
		((ManagementCI)this.connector).createTopics(topics);
		//assert	topic != null && inboundPortURI != null ;
		
	}

	@Override
	public void destroyTopic(String topic) throws Exception {
		((ManagementCI)this.connector).destroyTopic(topic);
		//assert	topic != null && inboundPortURI != null ;
		
	}

	@Override
	public boolean isTopic(String topic) throws Exception {
		return ((ManagementCI)this.connector).isTopic(topic);
		//assert	topic != null && inboundPortURI != null ;
	}

	@Override
	public String[] getTopics() throws Exception {
		return ((ManagementCI)this.connector).getTopics();
		//assert	topic != null && inboundPortURI != null ;
	}

	@Override
	public String getPublicationPortURI() throws Exception {
		return ((ManagementCI)this.connector).getPublicationPortURI();
	}


}
