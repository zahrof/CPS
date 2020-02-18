package baduren;

import baduren.components.Broker;
import baduren.components.Publisher;
import baduren.components.Subscriber;
import baduren.connectors.ManagementConnector;
import baduren.connectors.PublicationConnector;
import baduren.connectors.ReceptionConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.helpers.CVMDebugModes;

//-----------------------------------------------------------------------------

public class			CVM
extends		AbstractCVM
{
	protected static final String PUBLISHER_COMPONENT_URI = "my-URI-publisher"; 
	protected static final String BROKER_COMPONENT_URI = "my-URI-broker"; 
	protected static final String SUBSCRIBER_COMPONENT_URI = "my-URI-subscriber"; 
	
	protected static final String ManagementOutboundPortUri = "managementOport";
	protected static final String PublicationOutboundPortUri = "publicationOport";
	protected static final String ReceptionOutboundPortUri = "receptionOport";
	
	protected static final String ManagementInboundPortUri = "managementIport";
	protected static final String PublicationInboundPortUri = "publicationIport";
	protected static final String ReceptionInboundPortUri = "receptionIport";

	public	CVM() throws Exception{
		super() ;
	}

	
	protected String uriPublisherURI; 
	protected String uriBrokerURI; 
	protected String uriSubscriberURI; 

	@Override
	public void			deploy() throws Exception
	{
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.PUBLIHSING) ;
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CONNECTING) ;
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.COMPONENT_DEPLOYMENT) ;
		
		/******* create the publisher component ********/
		
		this.uriPublisherURI =
			AbstractComponent.createComponent(
					Publisher.class.getCanonicalName(),
					new Object[]{PUBLISHER_COMPONENT_URI,
							ManagementOutboundPortUri,
							PublicationOutboundPortUri }) ;
		assert	this.isDeployedComponent(this.uriPublisherURI) ;
		// make it trace its operations; comment and uncomment the line to see
		// the difference
		this.toggleTracing(this.uriPublisherURI) ;
		//this.toggleLogging(this.uriPublisherURI) ;
		

		/******* create the broker component ********/
		 //create the broker component
		this.uriBrokerURI =
			AbstractComponent.createComponent(
					Broker.class.getCanonicalName(),
					new Object[]{BROKER_COMPONENT_URI,
							ManagementInboundPortUri,
							PublicationInboundPortUri,
							ReceptionOutboundPortUri}) ;
		assert	this.isDeployedComponent(this.uriBrokerURI) ;
		// make it trace its operations; comment and uncomment the line to see
		// the difference
		this.toggleTracing(this.uriBrokerURI) ;
		//this.toggleLogging(this.uriBrokerURI) ;
		
		/******* create the subscriber component ********/
		// create the broker component
		this.uriSubscriberURI =
			AbstractComponent.createComponent(
					Subscriber.class.getCanonicalName(),
					new Object[]{SUBSCRIBER_COMPONENT_URI,
							ReceptionInboundPortUri,
							ManagementOutboundPortUri }) ;
		assert	this.isDeployedComponent(this.uriSubscriberURI) ;
		// make it trace its operations; comment and uncomment the line to see
		// the difference
		this.toggleTracing(this.uriSubscriberURI) ;
		
		// --------------------------------------------------------------------
		// Connection phase
		// --------------------------------------------------------------------

		// do the connection
		this.doPortConnection(
				this.uriPublisherURI,
				PublicationOutboundPortUri,
				PublicationInboundPortUri,
				PublicationConnector.class.getCanonicalName()) ;
//		// do the connection
		this.doPortConnection(
				this.uriPublisherURI,
				ManagementOutboundPortUri,
				ManagementInboundPortUri,
				ManagementConnector.class.getCanonicalName()) ;
		// do the connection
		this.doPortConnection(
				this.uriSubscriberURI,
				ManagementOutboundPortUri,
				ManagementInboundPortUri,
				ManagementConnector.class.getCanonicalName()) ;
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
		this.doPortDisconnection(
				this.uriPublisherURI,
				PublicationOutboundPortUri) ;
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
		super.shutdown();
	}

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
