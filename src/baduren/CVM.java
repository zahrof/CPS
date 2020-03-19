package baduren;

import baduren.components.Broker.Broker;
import baduren.components.Publishers.Publisher_Teacher;
import baduren.components.Publishers.Publisher_Teacher1;
import baduren.components.Publishers.Publisher_Teacher2;
import baduren.components.Subscribers.Subscriber_Student1;
import baduren.components.Subscribers.Subscriber_Student2;
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

	/** SUBSCRIBER URI**/
	public static final String SUBSCRIBER_COMPONENT_URI = "my-URI-subscriber";
	public static final String SUBSCRIBER_STUDENT1_COMPONENT_URI = "my-URI-subscriber-student1";
	public static final String SUBSCRIBER_STUDENT2_COMPONENT_URI = "my-URI-subscriber-student2";


	/******************   PORTS URI CONSTANTS   ********************/
	/** OUTBOUND PORTS URI**/
	public static final String ManagementOutboundPortUri = "managementOport";
	public static final String PublicationOutboundPortUri = "publicationOport";
	public static final String ReceptionOutboundPortUri = "receptionOport";

	/** INBOUND PORTS URI**/
	public static final String ManagementInboundPortUri = "managementIport";
	public static final String PublicationInboundPortUri = "publicationIport";
	public static final String ReceptionInboundPortUri = "receptionIport";


	/***************  COMPONENTS URI ATTRIBUTES   *****************/

	/** PUBLISHERS URI**/
	//protected String uriPublisherURI;
	protected String uri_Publisher_Teacher1;
	protected String uri_Publisher_Teacher2;

	/** BROKER URI**/
	protected String uriBrokerURI;

	/** SUBSCRIBER URI**/
	protected String uri_Subscriber_Student1;
	private String uri_Subscriber_Student2;


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
						new Object[]{
								5,0}) ;
		assert	this.isDeployedComponent(this.uriBrokerURI) ;

		this.toggleTracing(this.uriBrokerURI) ;

		/******* create the publishers components ********/
		
		/*this.uriPublisherURI =
			AbstractComponent.createComponent(
					Publisher.class.getCanonicalName(),
					new Object[]{PUBLISHER_COMPONENT_URI,
							ManagementOutboundPortUri,
							PublicationOutboundPortUri,1,0});*/

		/*assert	this.isDeployedComponent(this.uriPublisherURI) ;

		this.toggleTracing(this.uriPublisherURI) ;*/
		Object args1[] = {1, 0, 1};
		Object args2[] = {1, 0, 2};

		this.uri_Publisher_Teacher1 = AbstractComponent.createComponent(
				Publisher_Teacher.class.getCanonicalName(), args1);
		assert	this.isDeployedComponent(uri_Publisher_Teacher1) ;
		this.toggleTracing(this.uri_Publisher_Teacher1) ;

		this.uri_Publisher_Teacher2 = AbstractComponent.createComponent(
				Publisher_Teacher.class.getCanonicalName(), args2);
		assert	this.isDeployedComponent(uri_Publisher_Teacher2) ;
		this.toggleTracing(this.uri_Publisher_Teacher2) ;

		/******* create the subscriber component ********/
		this.uri_Subscriber_Student1 =
				AbstractComponent.createComponent(
						Subscriber_Student1.class.getCanonicalName(),
						new Object[]{ReceptionInboundPortUri,2,0}) ;
		assert	this.isDeployedComponent(this.uri_Subscriber_Student1) ;

		this.toggleTracing(this.uri_Subscriber_Student1) ;

		this.uri_Subscriber_Student2 =
				AbstractComponent.createComponent(
						Subscriber_Student2.class.getCanonicalName(),
						new Object[]{ReceptionInboundPortUri}) ;
		assert	this.isDeployedComponent(this.uri_Subscriber_Student2) ;

		this.toggleTracing(this.uri_Subscriber_Student2) ;

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
		super.finalise();*/
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
			a.startStandardLifeCycle(2000000L) ;
			// Give some time to see the traces (convenience).
			Thread.sleep(5000L) ;
			// Simplifies the termination (termination has yet to be treated
			// properly in BCM).
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
//-----------------------------------------------------------------------------
