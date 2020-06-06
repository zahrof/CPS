package baduren;

import baduren.components.Broker.Broker;
import baduren.components.publishers.PublisherTeacher;
import baduren.components.subscribers.SubscriberStudent;
import baduren.replicator.combinators.RandomCombinator;
import baduren.replicator.components.ReplicationManagerNonBlocking;
import baduren.replicator.connectors.ReplicableConnector;
import baduren.replicator.interfaces.PortFactoryI;
import baduren.replicator.ports.ReplicableInboundPort;
import baduren.replicator.ports.ReplicableOutboundPort;
import baduren.replicator.selectors.WholeSelector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.helpers.CVMDebugModes;
import fr.sorbonne_u.components.ports.InboundPortI;
import fr.sorbonne_u.components.ports.OutboundPortI;

//-----------------------------------------------------------------------------

/**
 * The type Cvm.
 */
public class CVM2 extends AbstractCVM {



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



	public static enum SelectorType {
		ROUND_ROBIN,
		RANDOM,
		WHOLE,
		MANY_ALL
	}
	protected final DistributedCVM.SelectorType currentSelector = DistributedCVM.SelectorType.MANY_ALL ;

	public static enum CombinatorType {
		FIXED,
		LONE,
		MAJORITY_VOTE,
		RANDOM
	}
	protected final DistributedCVM.CombinatorType currentCombinator = DistributedCVM.CombinatorType.MAJORITY_VOTE ;
	/*********************   CONSTRUCTOR   **********************/

	public CVM2() throws Exception{
		super() ;
	}

	/*********************   LIFE CYCLE   **********************/
	@Override
	public void	 deploy() throws Exception {
		int deploiments = 0;
		this.uriBrokerURI =
				AbstractComponent.createComponent(
						Broker.class.getCanonicalName(),
						new Object[]{"server" + 1 + "-", SERVER_INBOUND_PORT_URIS[0], MANAGER_INBOUND_PORT_URI,
								10, 0, CVM.BROKER_COMPONENT_URI});
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
						new Object[]{RECEPTION_INBOUND_PORT_URI, 2, 0, 1});
		assert this.isDeployedComponent(this.uri_Subscriber_Student1);

		this.toggleTracing(this.uri_Subscriber_Student1);

		this.uri_Subscriber_Student2 =
				AbstractComponent.createComponent(
						SubscriberStudent.class.getCanonicalName(),
						new Object[]{RECEPTION_INBOUND_PORT_URI, 1, 0, 2});
		assert this.isDeployedComponent(this.uri_Subscriber_Student2);

		this.toggleTracing(this.uri_Subscriber_Student2);
		deploiments++;
		System.out.println("deploiments jvm1" + deploiments);



		this.uriBrokerURI2 =
				AbstractComponent.createComponent(
						Broker.class.getCanonicalName(),
						new Object[]{"server" + 2 + "-", SERVER_INBOUND_PORT_URIS[1], MANAGER_INBOUND_PORT_URI,
								10, 0, CVM.BROKER_COMPONENT_URI2});
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
		deploiments++;
		System.out.println("deploiments jvm1" + deploiments);


		System.out.println("totooooooooooooooooooooooooooooo");

/*			AbstractComponent.createComponent(
			ReplicationManager.class.getCanonicalName(),
					new Object[]{
							currentSelector == SelectorType.WHOLE ?
									1
									:	SERVER_INBOUND_PORT_URIS.length,
							MANAGER_INBOUND_PORT_URI,
							(currentSelector == SelectorType.ROUND_ROBIN ?
									new RoundRobinDispatcherSelector(
											SERVER_INBOUND_PORT_URIS.length)
									:	currentSelector == SelectorType.RANDOM ?
									new RandomDispatcherSelector()
									:	new WholeSelector()
							),
							(currentCombinator == CombinatorType.FIXED) ?
									new FixedCombinator<String>(1)
									:	currentCombinator == CombinatorType.LONE ?
									new LoneCombinator<String>()
									:	currentCombinator == CombinatorType.MAJORITY_VOTE ?
									new MajorityVoteCombinator<String>(
											(o1,o2) -> o1.equals(o2),
											RuntimeException.class
									)
									:	new RandomCombinator<String>()
							,
							PC,
							SERVER_INBOUND_PORT_URIS
					}) ;*/

		AbstractComponent.createComponent(
				ReplicationManagerNonBlocking.class.getCanonicalName(),
				new Object[]{
						SERVER_INBOUND_PORT_URIS.length,
						MANAGER_INBOUND_PORT_URI,
						new WholeSelector()
						,
						this.currentCallMode,

						new RandomCombinator<String>(),
						PC,
						SERVER_INBOUND_PORT_URIS
				});
		deploiments++;
		System.out.println("deploiments jvm1" + deploiments);

		super.deploy();
		assert this.deploymentDone();

	}

	// --------------------------------------------------------------------
	// Finalise if we are not using plugins
	// --------------------------------------------------------------------
	/**
	 * @see AbstractCVM#finalise()
	 */
	@Override
	public void				finalise() throws Exception
	{
		// Port disconnections can be done here for static architectures
		// otherwise, they can be done in the finalise methods of components.
/*		this.doPortDisconnection(
				this.uriPublisherURI,
				PublicationOutboundPortUri) ;*/
//		this.doPortDisconnection(
//				this.uriBrokerURI,
//				ManagementOutboundPortUri) ;
	/*	this.doPortDisconnection(
				this.uriSubscriberURI,
				ReceptionOutboundPortUri) ;
		*/
		super.finalise();

	}


	@Override
	public void				shutdown() throws Exception
	{
		assert	this.allFinalised() ;
		super.shutdown();
	}


	/*********************   MAIN FUNCTION   **********************/
	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void		main(String[] args)
	{
		try {
			// Create an instance of the defined component virtual machine.
			CVM2 a = new CVM2() ;
			// Execute the application.
			a.startStandardLifeCycle(10000000L) ;
			System.out.println("--------------------------------------------------------------- RESULTATS --------------------------------------------------------------");
			System.out.println("Messages publiées à partir de Publisher: "+ PublisherTeacher.publications);
			System.out.println("Messages sauvegardés dans broker : " + Broker.messagesSupprimes);
			System.out.println("Nombre de messages envoyés depuis Broker: "+ Broker.messagesAcceptDeBroker);
			System.out.println("Nombre de messages réçu de Broker: "+ SubscriberStudent.messagesAcceptDeSubscriber);
			System.out.println("Historique des abonnements dans broker : "+ Broker.historiqueAbonnements);
			System.out.println("Historique des changement de filtres  dans broker : "+Broker.changementFiltres);
			System.out.println("Historique des desabonnements dans broker : "+ Broker.desabonnements);
			System.out.println("Historique des création de topic dans broker: "+ Broker.historiqueCreationTopics);
			System.out.println("Historique des destruction de topics dans broker: "+ Broker.suppressionSujets);
			System.out.println("Réponse de la demande 'isTopic(ALASCA)' dans publisher (doit retourner vrai)" +
					": "+ PublisherTeacher.reponseIsTopic);
			System.out.println("Tous les sujets avant avoir supprimé le topic SRCS : ");
			System.out.print("		   ");
			for (String s :  PublisherTeacher.allTopicsAtTheEnd)
				System.out.print(s+"   ");
			System.out.println("");
			System.out.println("Tous les sujets après avoir supprimé le topic SRCS : ");
			if(SubscriberStudent.allTopicsAtTheEnd!=null) {
				System.out.print("		   ");
				for (String s : SubscriberStudent.allTopicsAtTheEnd)
					System.out.print(s + "   ");
				System.out.println("\nPublication Port Uri : " + SubscriberStudent.publicationPortUri);
				System.out.println("\n-------------------------------------------------------------------------------------------------------");

			}else {
				System.out.println("ALL topics at the end is null! " );// Give some time to see the traces (convenience).
			}
			Thread.sleep(1000000L) ;
			// Simplifies the termination (termination has yet to be treated
			// properly in BCM).

			// Mise en commentaire de cette commande pour pouvoir utiliser la CVM depuis le main de la classe Tests
			//System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
//-----------------------------------------------------------------------------