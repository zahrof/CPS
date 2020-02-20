package baduren.ports.inboundPorts;

import baduren.components.Broker;
import baduren.interfaces.ManagementCI;
import baduren.interfaces.MessageFilterI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * The type ManagementInboundPort.
 */
public class ManagementInboundPort extends	AbstractInboundPort implements ManagementCI {

	/**
	 * Instantiates a new Management inbound port.
	 *
	 * @param uri   the uri
	 * @param owner the owner
	 * @throws Exception the exception
	 */
	public ManagementInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ManagementCI.class, owner);
	}

	/**
	 * Instantiates a new Management inbound port.
	 *
	 * @param owner the owner
	 * @throws Exception the exception
	 */
	public ManagementInboundPort(ComponentI owner) throws Exception{
		super(ManagementCI.class, owner); 
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void subscribe(String topic, String inboundPortURI) throws Exception {
		((Broker)this.owner).subscribe(topic, inboundPortURI);
		
	}

	@Override
	public void subscribe(String[] topics, String inboundPortURI) throws Exception {
		((Broker)this.owner).subscribe(topics, inboundPortURI);
		
	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception {
		((Broker)this.owner).subscribe(topic, filter, inboundPortURI);
		
	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception {
		((Broker)this.owner).subscribe(topic,newFilter, inboundPortURI);
		
	}

	@Override
	public void unsubscribe(String topic, String inboundPortUri) throws Exception {
		((Broker)this.owner).subscribe(topic, inboundPortUri);
		
	}


	@Override
	public void createTopic(String topic) throws Exception {
		((Broker)this.owner).createTopic(topic);
		
	}

	@Override
	public void createTopics(String[] topics) throws Exception {
		((Broker)this.owner).createTopics(topics);
		
	}

	@Override
	public void destroyTopic(String topic) throws Exception {
		((Broker)this.owner).destroyTopic(topic);
		
	}

	@Override
	public boolean isTopic(String topic) throws Exception {
		((Broker)this.owner).isTopic(topic);
		return false;
	}

	@Override
	public String[] getTopics() throws Exception {
		return ((Broker)this.owner).getTopics();
	}

	@Override
	public String getPublicationPortURI() throws Exception {
		return ((Broker)this.owner).getPublicationPortURI();
	}



	
	
	
	
}


