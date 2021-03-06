package baduren;

import baduren.components.Broker.Broker;
import baduren.components.publishers.PublisherTeacher;
import baduren.components.subscribers.SubscriberStudent;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.helpers.CVMDebugModes;

//-----------------------------------------------------------------------------

/**
 * The type Cvm.
 */
public class CVM  extends AbstractCVM {

	/****************** COMPONENTS URI CONSTANTS ********************/
	/** PUBLISHERS URI**/
	//public static final String PUBLISHER_COMPONENT_URI = "my-URI-publisher";
	public static final String TEACHER1_PUBLISHER_COMPONENT_URI = "my-URI-publisher-teacher1";

	/** BROKER URI**/
	public static final String BROKER_COMPONENT_URI = "my-URI-broker";
	public static final String BROKER_COMPONENT_URI2 = "my-URI-broker2";

	/** SUBSCRIBER URI**/
	public static final String SUBSCRIBER_COMPONENT_URI = "my-URI-subscriber";
	public static final String SUBSCRIBER_STUDENT1_COMPONENT_URI = "my-URI-subscriber-student1";
	public static final String SUBSCRIBER_STUDENT2_COMPONENT_URI = "my-URI-subscriber-student2";


	/******************   PORTS URI CONSTANTS   ********************/
	/** OUTBOUND PORTS URI**/
	public static final String MANAGEMENT_OUTBOUND_PORT_URI = "managementOport";
	public static final String PUBLICATION_OUTBOUND_PORT_URI = "publicationOport";
	public static final String RECEPTION_OUTBOUND_PORT_URI = "receptionOport";

	/** INBOUND PORTS URI**/
	public static final String MANAGEMENT_INBOUND_PORT_URI = "managementIport";
	public static final String PUBLICATION_INBOUND_PORT_URI = "publicationIport";
	public static final String RECEPTION_INBOUND_PORT_URI = "receptionIport";


	/***************  COMPONENTS URI ATTRIBUTES   *****************/

	/** PUBLISHERS URI**/
	//protected String uriPublisherURI;
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


	/*********************   CONSTRUCTOR   **********************/

	public	CVM() throws Exception{
		super() ;
	}

	/*********************   LIFE CYCLE   **********************/
	@Override
	public void	 deploy() throws Exception
	{
		assert	!this.deploymentDone() ;
		//AbstractCVM.DEBUG_MODE.add(CVMDebugModes.PUBLIHSING) ;
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CONNECTING) ;
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.COMPONENT_DEPLOYMENT) ;

		// --------------------------------------------------------------------
		// Component creation phase
		// --------------------------------------------------------------------
		/******* create the broker component ********/
		this.uriBrokerURI =
				AbstractComponent.createComponent(
						Broker.class.getCanonicalName(),
						new Object[]{"broker"+1 +"-",
								10,0,CVM.BROKER_COMPONENT_URI}) ;
		assert	this.isDeployedComponent(this.uriBrokerURI) ;

		this.toggleTracing(this.uriBrokerURI) ;


		this.uriBrokerURI2 =
				AbstractComponent.createComponent(
						Broker.class.getCanonicalName(),
						new Object[]{"broker"+2 +"-",
								10,0,CVM.BROKER_COMPONENT_URI2}) ;
		assert	this.isDeployedComponent(this.uriBrokerURI2) ;

		this.toggleTracing(this.uriBrokerURI2) ;

		/******* create the publishers components ********/

		/*this.uriPublisherURI =
			AbstractComponent.createComponent(
					Publisher.class.getCanonicalName(),
					new Object[]{PUBLISHER_COMPONENT_URI,
							ManagementOutboundPortUri,
							PublicationOutboundPortUri,1,0});*/

		/*assert	this.isDeployedComponent(this.uriPublisherURI) ;

		this.toggleTracing(this.uriPublisherURI) ;*/

		// arguments : { nbThreads , nbSchedulableThreads , number_teacher }
		this.uri_Publisher_Teacher1 =
				AbstractComponent.createComponent(
						PublisherTeacher.class.getCanonicalName(),
						new Object[]{1, 0, 1});
		assert	this.isDeployedComponent(uri_Publisher_Teacher1) ;
		this.toggleTracing(this.uri_Publisher_Teacher1) ;

		this.uri_Publisher_Teacher2 =
				AbstractComponent.createComponent(
						PublisherTeacher.class.getCanonicalName(),
						new Object[]{1, 0, 2});
		assert	this.isDeployedComponent(uri_Publisher_Teacher2) ;
		this.toggleTracing(this.uri_Publisher_Teacher2) ;

		try {
			this.uri_Publisher_Teacher3 =
					AbstractComponent.createComponent(
							PublisherTeacher.class.getCanonicalName(),
							new Object[]{1, 0, 3});
			assert this.isDeployedComponent(uri_Publisher_Teacher3);
			this.toggleTracing(this.uri_Publisher_Teacher3);
		}catch (Exception e){
			System.out.println(" test");
		}
		this.uri_Publisher_Teacher4 =
				AbstractComponent.createComponent(
						PublisherTeacher.class.getCanonicalName(),
						new Object[]{1, 0, 4});
		assert	this.isDeployedComponent(uri_Publisher_Teacher4) ;
		this.toggleTracing(this.uri_Publisher_Teacher4) ;

		/******* create the subscriber component ********/
		this.uri_Subscriber_Student1 =
				AbstractComponent.createComponent(
						SubscriberStudent.class.getCanonicalName(),
						new Object[]{RECEPTION_INBOUND_PORT_URI, 2 ,0, 1}) ;
		assert	this.isDeployedComponent(this.uri_Subscriber_Student1) ;

		this.toggleTracing(this.uri_Subscriber_Student1) ;

		this.uri_Subscriber_Student2 =
				AbstractComponent.createComponent(
						SubscriberStudent.class.getCanonicalName(),
						new Object[]{RECEPTION_INBOUND_PORT_URI, 1, 0, 2}) ;
		assert	this.isDeployedComponent(this.uri_Subscriber_Student2) ;

		this.toggleTracing(this.uri_Subscriber_Student2) ;

		this.uri_Subscriber_Student3 =
				AbstractComponent.createComponent(
						SubscriberStudent.class.getCanonicalName(),
						new Object[]{RECEPTION_INBOUND_PORT_URI, 1, 0, 3}) ;
		assert	this.isDeployedComponent(this.uri_Subscriber_Student3) ;

		this.toggleTracing(this.uri_Subscriber_Student3) ;

		this.uri_Subscriber_Student4 =
				AbstractComponent.createComponent(
						SubscriberStudent.class.getCanonicalName(),
						new Object[]{RECEPTION_INBOUND_PORT_URI, 1, 0, 4}) ;
		assert	this.isDeployedComponent(this.uri_Subscriber_Student4) ;

		this.toggleTracing(this.uri_Subscriber_Student4) ;

		// --------------------------------------------------------------------
		// Connection phase if we are not using plugins
		// --------------------------------------------------------------------

		/*this.doPortConnection(
				this.uriPublisherURI,
				PublicationOutboundPortUri,
				PublicationInboundPortUri,
				PublicationConnector.class.getCanonicalName()) ;*/

/*		this.doPortConnection(
				this.uriPublisherURI,
				ManagementOutboundPortUri,
				ManagementInboundPortUri,
				ManagementConnector.class.getCanonicalName()) ;*/

/*		this.doPortConnection(
				this.uriSubscriberURI,
				ManagementOutboundPortUri,
				ManagementInboundPortUri,
				ManagementConnector.class.getCanonicalName()) ;*/
		// do the connection
		//logMessage(this.uriSubscriberURI+ReceptionOutboundPortUri+ ReceptionInboundPortUri) ;
		
	/*	this.doPortConnection(
				this.uriBrokerURI,
				ReceptionOutboundPortUri,
				ReceptionInboundPortUri,
				ReceptionConnector.class.getCanonicalName()) ;*/



		// --------------------------------------------------------------------
		// Deployment
		// --------------------------------------------------------------------

		super.deploy();
		assert	this.deploymentDone() ;
	}

	// --------------------------------------------------------------------
	// Finalise if we are not using plugins
	// --------------------------------------------------------------------
	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#finalise()
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
			CVM a = new CVM() ;
			// Execute the application.
			a.startStandardLifeCycle(5000L) ;
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
			Thread.sleep(1000L) ;
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
