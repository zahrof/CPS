package baduren.ports.inboundPorts;

import baduren.components.Broker;
import baduren.components.Publisher;
import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.examples.basic_cs.components.URIProvider;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * The type PublicationInboundPort.
 */
public class PublicationInboundPort extends	AbstractInboundPort implements PublicationCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new Publication inbound port.
	 *
	 * @param uri   the uri
	 * @param owner the owner
	 * @throws Exception the exception
	 */
	public	PublicationInboundPort(String uri, ComponentI owner)
			throws Exception
		{
			super(uri, PublicationCI.class, owner) ;
		}

	/**
	 * Instantiates a new Publication inbound port.
	 *
	 * @param owner the owner
	 * @throws Exception the exception
	 */
	public				PublicationInboundPort(ComponentI owner)
	throws Exception
	{
		super(PublicationCI.class, owner) ;
	}

	@Override
	public void publish(MessageI m, String topic) throws Exception {
	/*	this.getOwner().handleRequestSync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(m, topic);
						return null;
					}
				}) ;*/
		((Broker)this.owner).publish(m, topic);
	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
/*		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).destroyTopic(topic);
						return null;
					}
				}) ;*/
		((Broker)this.owner).publish(m, topics);
	}

	@Override
	public void publish(MessageI[] ms, String topics) throws Exception {
/*		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).destroyTopic(topic);
						return null;
					}
				}) ;*/
		((Broker)this.owner).publish(ms, topics);
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
/*		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).destroyTopic(topic);
						return null;
					}
				}) ;*/
		((Broker)this.owner).publish(ms, topics);
	}
}


