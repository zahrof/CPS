package baduren;

import baduren.components.Broker.Broker;
import baduren.components.publishers.PublisherTeacher;
import baduren.components.subscribers.SubscriberStudent;
import baduren.replicator.combinators.FixedCombinator;
import baduren.replicator.combinators.LoneCombinator;
import baduren.replicator.combinators.MajorityVoteCombinator;
import baduren.replicator.combinators.RandomCombinator;
import baduren.replicator.components.ReplicationManager;
import baduren.replicator.components.ReplicationManagerNonBlocking;
import baduren.replicator.connectors.ReplicableConnector;
import baduren.replicator.interfaces.PortFactoryI;
import baduren.replicator.ports.ReplicableInboundPort;
import baduren.replicator.ports.ReplicableOutboundPort;
import baduren.replicator.selectors.RandomDispatcherSelector;
import baduren.replicator.selectors.RoundRobinDispatcherSelector;
import baduren.replicator.selectors.WholeSelector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import fr.sorbonne_u.components.cvm.CVMState;
import fr.sorbonne_u.components.examples.basic_cs.components.URIConsumer;
import fr.sorbonne_u.components.examples.basic_cs.components.URIProvider;
import fr.sorbonne_u.components.ports.InboundPortI;
import fr.sorbonne_u.components.ports.OutboundPortI;
import jdk.nashorn.internal.runtime.ECMAException;

public class DistributedCVM extends AbstractDistributedCVM{

	public static final String RECEPTION_INBOUND_PORT_URI = "receptionIport";
	private static final String[] SERVER_INBOUND_PORT_URIS =
			new String[]{
					"server-service-1",
					"server-service-2"
			} ;

	public static final String			MANAGER_INBOUND_PORT_URI = "manager" ;
	/***************  COMPONENTS URI ATTRIBUTES   *****************/

	/** PUBLISHERS URI**/
	protected String uri_Publisher_Teacher1;
	protected String uri_Publisher_Teacher2;
	private String uri_Publisher_Teacher3;

	/** BROKER URI**/
	protected String uriBrokerURI;

	/** SUBSCRIBER URI**/
	protected String uri_Subscriber_Student1;
	protected String uri_Subscriber_Student2;
	private String uriBrokerURI2;
	private String uri_Publisher_Teacher4;
	private String uri_Subscriber_Student3;
	private String uri_Subscriber_Student4;


	protected static final String JVM1_COMPONENT_URI ="jvm1";
	protected static final String JVM2_COMPONENT_URI ="jvm2";

	public static final PortFactoryI PC =
			new PortFactoryI() {
				@Override
				public InboundPortI createInboundPort(ComponentI c)
						throws Exception
				{
					return new ReplicableInboundPort<String>(c) ;
				}

				@Override
				public InboundPortI createInboundPort(String uri, ComponentI c)
						throws Exception
				{
					return new ReplicableInboundPort<String>(uri, c) ;
				}

				@Override
				public OutboundPortI createOutboundPort(ComponentI c)
						throws Exception
				{
					return new ReplicableOutboundPort<String>(c) ;
				}

				@Override
				public OutboundPortI createOutboundPort(String uri, ComponentI c)
						throws Exception
				{
					return new ReplicableOutboundPort<String>(uri, c) ;
				}

				@Override
				public String getConnectorClassName() {
					return ReplicableConnector.class.getCanonicalName() ;
				}
			} ;
	protected final ReplicationManagerNonBlocking.CallMode currentCallMode = ReplicationManagerNonBlocking.CallMode.ALL ;


	public DistributedCVM(String[] args, int xLayout, int yLayout) throws Exception{
		super(args, xLayout, yLayout);
	}


	@Override
	public  void initialise() throws Exception{
		super.initialise();
	}

	@Override
	public void instantiateAndPublish() throws Exception{
		if (thisJVMURI.equals(JVM1_COMPONENT_URI)) {
			this.uriBrokerURI =
					AbstractComponent.createComponent(
							Broker.class.getCanonicalName(),
							new Object[]{ SERVER_INBOUND_PORT_URIS[0], MANAGER_INBOUND_PORT_URI,
									20, 0, CVM.BROKER_COMPONENT_URI});
			assert this.isDeployedComponent(this.uriBrokerURI);

			this.toggleTracing(this.uriBrokerURI);

			// arguments : { nbThreads , nbSchedulableThreads , number_teacher }
			this.uri_Publisher_Teacher1 =
					AbstractComponent.createComponent(
							PublisherTeacher.class.getCanonicalName(),
							new Object[]{1, 0, 1});
			assert this.isDeployedComponent(uri_Publisher_Teacher1);
			this.toggleTracing(this.uri_Publisher_Teacher1);

			this.uri_Publisher_Teacher2 =
					AbstractComponent.createComponent(
							PublisherTeacher.class.getCanonicalName(),
							new Object[]{1, 0, 2});
			assert this.isDeployedComponent(uri_Publisher_Teacher2);
			this.toggleTracing(this.uri_Publisher_Teacher2);

			this.uri_Subscriber_Student1 =
					AbstractComponent.createComponent(
							SubscriberStudent.class.getCanonicalName(),
							new Object[]{RECEPTION_INBOUND_PORT_URI,1, 0, 1});
			assert this.isDeployedComponent(this.uri_Subscriber_Student1);

			this.toggleTracing(this.uri_Subscriber_Student1);

			this.uri_Subscriber_Student2 =
					AbstractComponent.createComponent(
							SubscriberStudent.class.getCanonicalName(),
							new Object[]{RECEPTION_INBOUND_PORT_URI, 1, 0, 2});
			assert this.isDeployedComponent(this.uri_Subscriber_Student2);

			this.toggleTracing(this.uri_Subscriber_Student2);


		} else if (thisJVMURI.equals(JVM2_COMPONENT_URI)) {

			this.uriBrokerURI2 =
					AbstractComponent.createComponent(
							Broker.class.getCanonicalName(),
							new Object[]{SERVER_INBOUND_PORT_URIS[1], MANAGER_INBOUND_PORT_URI,
									20, 0, CVM.BROKER_COMPONENT_URI2});
			assert this.isDeployedComponent(this.uriBrokerURI2);

			this.toggleTracing(this.uriBrokerURI2);

			this.uri_Publisher_Teacher3 =
					AbstractComponent.createComponent(
							PublisherTeacher.class.getCanonicalName(),
							new Object[]{1, 0, 3});
			assert this.isDeployedComponent(uri_Publisher_Teacher3);
			this.toggleTracing(this.uri_Publisher_Teacher3);

			this.uri_Publisher_Teacher4 =
					AbstractComponent.createComponent(
							PublisherTeacher.class.getCanonicalName(),
							new Object[]{1, 0, 4});
			assert this.isDeployedComponent(uri_Publisher_Teacher4);
			this.toggleTracing(this.uri_Publisher_Teacher4);

			this.uri_Subscriber_Student3 =
					AbstractComponent.createComponent(
							SubscriberStudent.class.getCanonicalName(),
							new Object[]{RECEPTION_INBOUND_PORT_URI, 1, 0, 3});
			assert this.isDeployedComponent(this.uri_Subscriber_Student3);

			this.toggleTracing(this.uri_Subscriber_Student3);

			this.uri_Subscriber_Student4 =
					AbstractComponent.createComponent(
							SubscriberStudent.class.getCanonicalName(),
							new Object[]{RECEPTION_INBOUND_PORT_URI, 1, 0, 4});
			assert this.isDeployedComponent(this.uri_Subscriber_Student4);

			this.toggleTracing(this.uri_Subscriber_Student4);

		} else if (thisJVMURI.equals("manager")) {
			AbstractComponent.createComponent(
					ReplicationManagerNonBlocking.class.getCanonicalName(),
					new Object[]{
							SERVER_INBOUND_PORT_URIS.length,
							MANAGER_INBOUND_PORT_URI,
							new WholeSelector(),
							this.currentCallMode,
							new RandomCombinator<String>(),
							PC,
							SERVER_INBOUND_PORT_URIS
					});

		} else {

			System.out.println("Unknown JVM URI... " + thisJVMURI) ;
		}



		super.instantiateAndPublish();

	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractDistributedCVM#finalise()
	 */
	@Override
	public void			finalise() throws Exception
	{
		// Port disconnections can be done here for static architectures
		// otherwise, they can be done in the finalise methods of components.

		if (thisJVMURI.equals(JVM1_COMPONENT_URI)) {

		/*	assert	this.uriConsumerURI == null && this.uriProviderURI != null ;*/
			// nothing to be done on the provider side

		} else if (thisJVMURI.equals(JVM1_COMPONENT_URI)) {

			/*assert	this.uriConsumerURI != null && this.uriProviderURI == null ;
			this.doPortDisconnection(this.uriConsumerURI, URIGetterOutboundPortURI) ;*/

		} else {

			System.out.println("Unknown JVM URI333... " + thisJVMURI) ;

		}

		super.finalise() ;
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractDistributedCVM#shutdown()
	 */
	@Override
	public void			shutdown() throws Exception
	{
		if (thisJVMURI.equals(JVM1_COMPONENT_URI)) {

			/*assert	this.uriConsumerURI == null && this.uriProviderURI != null ;
			// any disconnection not done yet can be performed here
*/
		} else if (thisJVMURI.equals(JVM2_COMPONENT_URI)) {

			/*assert	this.uriConsumerURI != null && this.uriProviderURI == null ;
			// any disconnection not done yet can be performed here*/

		} else {

			System.out.println("Unknown JVM URI666... " + thisJVMURI) ;

		}

		super.shutdown();
	}

	public static void	main(String[] args)
	{
		try {
			DistributedCVM da  = new DistributedCVM(args, 2, 5) ;
			da.startStandardLifeCycle(1000000L) ;
			Thread.sleep(500000L) ;
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}