package baduren;

import baduren.components.*;
import baduren.connectors.ManagementConnector;
import baduren.connectors.PublicationConnector;
import baduren.connectors.ReceptionConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.helpers.CVMDebugModes;

//-----------------------------------------------------------------------------

/**
 * The type Cvm.
 */
public class			CVM

extends		AbstractCVM
{
	/**
	 * The constant PUBLISHER_COMPONENT_URI.
	 */
	//public static final String PUBLISHER_COMPONENT_URI = "my-URI-publisher";
	public static final String PUBLISHER_STUDENT1_COMPONENT_URI = "my-URI-publisher-student1";
	/**
	 * The constant BROKER_COMPONENT_URI.
	 */
	public static final String BROKER_COMPONENT_URI = "my-URI-broker";
	/**
	 * The constant SUBSCRIBER_COMPONENT_URI.
	 */
	public static final String SUBSCRIBER_COMPONENT_URI = "my-URI-subscriber";

	/**
	 * The constant ManagementOutboundPortUri.
	 */
	public static final String ManagementOutboundPortUri = "managementOport";
	/**
	 * The constant PublicationOutboundPortUri.
	 */
	//public static final String PublicationOutboundPortUri = "publicationOport";
	/**
	 * The constant ReceptionOutboundPortUri.
	 */
	public static final String ReceptionOutboundPortUri = "receptionOport";

	/**
	 * The constant ManagementInboundPortUri.
	 */
	public static final String ManagementInboundPortUri = "managementIport";

	/**
	 * The constant PublicationInboundPortUri.
	 */
	//public static final String PublicationInboundPortUri = "publicationIport";
	/**
	 * The constant ReceptionInboundPortUri.
	 */
	public static final String ReceptionInboundPortUri = "receptionIport";

	/**
	 * Instantiates a new Cvm.
	 *
	 * @throws Exception the exception
	 */
	public	CVM() throws Exception{
		super() ;
	}


	/**
	 * The Uri publisher uri.
	 */
	//protected String uriPublisherURI;
	protected String uriPublisherIIURI;
	/**
	 * The Uri broker uri.
	 */
	protected String uriBrokerURI;
	/**
	 * The Uri subscriber uri.
	 */
	protected String uriSubscriberURI;

	@Override
	public void			deploy() throws Exception
	{
		assert	!this.deploymentDone() ;
		//AbstractCVM.DEBUG_MODE.add(CVMDebugModes.PUBLIHSING) ;
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CONNECTING) ;
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.COMPONENT_DEPLOYMENT) ;

		/******* create the broker component ********/
		//create the broker component
		this.uriBrokerURI =
				AbstractComponent.createComponent(
						Broker.class.getCanonicalName(),
						new Object[]{
								ReceptionOutboundPortUri,1,0}) ;
		assert	this.isDeployedComponent(this.uriBrokerURI) ;

		this.toggleTracing(this.uriBrokerURI) ;

		/******* create the publisher component ********/
		
		/*this.uriPublisherURI =
			AbstractComponent.createComponent(
					Publisher.class.getCanonicalName(),
					new Object[]{PUBLISHER_COMPONENT_URI,
							ManagementOutboundPortUri,
							PublicationOutboundPortUri,1,0});*/

		/*assert	this.isDeployedComponent(this.uriPublisherURI) ;

		this.toggleTracing(this.uriPublisherURI) ;*/

		this.uriPublisherIIURI= AbstractComponent.createComponent(
				PublisherII.class.getCanonicalName(), new  Object[]{PUBLISHER_STUDENT1_COMPONENT_URI});
		assert	this.isDeployedComponent(uriPublisherIIURI) ;
		this.toggleTracing(this.uriPublisherIIURI) ;
		


		
		/******* create the subscriber component ********/
		// create the broker component
		this.uriSubscriberURI =
			AbstractComponent.createComponent(
					SubscriberII.class.getCanonicalName(),
					new Object[]{ReceptionInboundPortUri,SUBSCRIBER_COMPONENT_URI,
							}) ;
		assert	this.isDeployedComponent(this.uriSubscriberURI) ;

		this.toggleTracing(this.uriSubscriberURI) ;
		
		// --------------------------------------------------------------------
		// Connection phase
		// --------------------------------------------------------------------

		// do the connection
		/*this.doPortConnection(
				this.uriPublisherURI,
				PublicationOutboundPortUri,
				PublicationInboundPortUri,
				PublicationConnector.class.getCanonicalName()) ;*/
//		// do the connection
/*		this.doPortConnection(
				this.uriPublisherURI,
				ManagementOutboundPortUri,
				ManagementInboundPortUri,
				ManagementConnector.class.getCanonicalName()) ;*/
		// do the connection
/*		this.doPortConnection(
				this.uriSubscriberURI,
				ManagementOutboundPortUri,
				ManagementInboundPortUri,
				ManagementConnector.class.getCanonicalName()) ;*/
		// do the connection
		//logMessage(this.uriSubscriberURI+ReceptionOutboundPortUri+ ReceptionInboundPortUri) ;
		
		this.doPortConnection(
				this.uriBrokerURI,
				ReceptionOutboundPortUri,
				ReceptionInboundPortUri,
				ReceptionConnector.class.getCanonicalName()) ;
				
		

		// --------------------------------------------------------------------
		// Deployment done
		// --------------------------------------------------------------------

		super.deploy();
		assert	this.deploymentDone() ;
	}

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
		this.doPortDisconnection(
				this.uriSubscriberURI,
				ReceptionOutboundPortUri) ;
		super.finalise();
	}


	@Override
	public void				shutdown() throws Exception
	{
		assert	this.allFinalised() ;
		super.shutdown();
	}

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
			a.startStandardLifeCycle(2000000000000000L) ;
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
