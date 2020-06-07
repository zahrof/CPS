package baduren.ports.inboundPorts;

import baduren.components.Broker.Broker;
import baduren.interfaces.ManagementCI;
import baduren.interfaces.ManagementImplementationI;
import baduren.interfaces.MessageFilterI;
import baduren.interfaces.SubscriptionImplementationI;
import fr.sorbonne_u.components.AbstractComponent;
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

	private static final long serialVersionUID = 1L;

	@Override
	public void subscribe(String topic, String inboundPortURI) throws Exception {
/*		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((SubscriptionImplementationI)this.getServiceOwner()).subscribe(topic,inboundPortURI);
						return null;
					}
				}) ;*/

		this.owner.runTask(
				new AbstractComponent.AbstractTask() {

					@Override
					public void run() {
						try {
							((SubscriptionImplementationI) this.getTaskOwner()).subscribe(topic,inboundPortURI);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	public void subscribe(String[] topics, String inboundPortURI) throws Exception {
/*		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((SubscriptionImplementationI)this.getServiceOwner()).subscribe(topics, inboundPortURI);
						return null;
					}
				}) ;*/

		this.owner.runTask(
				new AbstractComponent.AbstractTask() {

					@Override
					public void run() {
						try {
							((SubscriptionImplementationI) this.getTaskOwner()).subscribe(topics,inboundPortURI);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception {
	/*	this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((SubscriptionImplementationI)this.getServiceOwner()).subscribe(topic, filter, inboundPortURI);
						return null;
					}
				});*/

		this.owner.runTask(
				new AbstractComponent.AbstractTask() {

					@Override
					public void run() {
						try {
							((SubscriptionImplementationI) this.getTaskOwner()).subscribe(topic, filter, inboundPortURI);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception {


		this.getOwner().handleRequestSync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((SubscriptionImplementationI)this.getServiceOwner()).modifyFilter(topic,newFilter ,inboundPortURI);
						return null;
					}
				}) ;
	}

	@Override
	public void unsubscribe(String topic, String inboundPortUri) throws Exception {
	/*	this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((SubscriptionImplementationI)this.getServiceOwner()).unsubscribe(topic, inboundPortUri);
						return null;
					}
				}) ;*/

		this.owner.runTask(
				new AbstractComponent.AbstractTask() {

					@Override
					public void run() {
						try {
							((SubscriptionImplementationI) this.getTaskOwner()).unsubscribe(topic, inboundPortUri);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}


	@Override
	public void createTopic(String topic) throws Exception {
		this.getOwner().handleRequestSync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((ManagementImplementationI)this.getServiceOwner()).createTopic(topic);
						return null;
					}
				}) ;
	}

	@Override
	public void createTopics(String[] topics) throws Exception {
		this.getOwner().handleRequestSync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((ManagementImplementationI)this.getServiceOwner()).createTopics(topics);
						return null;
					}
				}) ;
	}

	@Override
	public void destroyTopic(String topic) throws Exception {

		this.getOwner().handleRequestSync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((ManagementImplementationI)this.getServiceOwner()).destroyTopic(topic);
						return null;
					}
				}) ;
	}

	@Override
	public boolean isTopic(String topic) throws Exception {
		return this.getOwner().handleRequestSync(
				new AbstractComponent.AbstractService<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return((ManagementImplementationI)this.getServiceOwner()).isTopic(topic);
					}
				}) ;
	}

	@Override
	public String[] getTopics() throws Exception {
		return this.getOwner().handleRequestSync(
				new AbstractComponent.AbstractService<String[]>() {
					@Override
					public String[] call() throws Exception {
						return ((ManagementImplementationI)this.getServiceOwner()).getTopics();
					}
				}) ;
	}

	@Override
	public String getPublicationPortURI() throws Exception {
		return this.getOwner().handleRequestSync(
				new AbstractComponent.AbstractService<String>() {
					@Override
					public String call() throws Exception {
						return ((ManagementImplementationI)this.getServiceOwner()).getPublicationPortURI();
					}
				}) ;

	}



	
	
	
	
}


