package baduren.ports.inboundPorts;

import baduren.components.Broker.Broker;
import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import baduren.interfaces.PublicationImplementationI;
import baduren.interfaces.SubscriptionImplementationI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
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
/*		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((PublicationImplementationI)this.getServiceOwner()).publish(m, topic);
						return null;
					}
				}) ;*/

		this.owner.runTask(
				new AbstractComponent.AbstractTask() {

					@Override
					public void run() {
						try {
							((PublicationImplementationI) this.getTaskOwner()).publish(m, topic);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
/*		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((PublicationImplementationI)this.getServiceOwner()).publish(m, topics);
						return null;
					}
				}) ;*/
		this.owner.runTask(
				new AbstractComponent.AbstractTask() {

					@Override
					public void run() {
						try {
							((PublicationImplementationI) this.getTaskOwner()).publish(m, topics);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	public void publish(MessageI[] ms, String topics) throws Exception {
/*		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((PublicationImplementationI)this.getServiceOwner()).publish(ms, topics);
						return null;
					}
				}) ;*/
		this.owner.runTask(
				new AbstractComponent.AbstractTask() {

					@Override
					public void run() {
						try {
							((PublicationImplementationI) this.getTaskOwner()).publish(ms, topics);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
	/*	this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((PublicationImplementationI)this.getServiceOwner()).publish(ms, topics);
						return null;
					}
				}) ;*/
		this.owner.runTask(
				new AbstractComponent.AbstractTask() {

					@Override
					public void run() {
						try {
							((PublicationImplementationI) this.getTaskOwner()).publish(ms, topics);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}
}


