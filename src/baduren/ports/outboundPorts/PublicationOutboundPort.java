package baduren.ports.outboundPorts;

import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import baduren.interfaces.PublicationImplementationI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * The type PublicationOutboundPort.
 */
public class PublicationOutboundPort extends	AbstractOutboundPort implements PublicationImplementationI {

	private static final long serialVersionUID = 1L;
	
	// Constructeurs

	/**
	 * Instantiates a new Publication outbound port.
	 *
	 * @param uri   the uri
	 * @param owner the owner
	 * @throws Exception the exception
	 */
	public PublicationOutboundPort(String uri, ComponentI owner) throws Exception{
		super(uri, PublicationImplementationI.class, owner) ;
	}

	/**
	 * Instantiates a new Publication outbound port.
	 *
	 * @param owner the owner
	 * @throws Exception the exception
	 */
	public PublicationOutboundPort(ComponentI owner) throws Exception{
		super(PublicationImplementationI.class, owner) ;
	}
	
	// Methodes
	
	
	@Override
	public void publish(MessageI m, String topic) throws Exception{
		((PublicationImplementationI)this.connector).publish(m, topic);
	}

	@Override
	public void publish(MessageI m, String[] topics)throws Exception {
		((PublicationImplementationI)this.connector).publish(m, topics);
		
	}

	@Override
	public void publish(MessageI[] ms, String topics) throws Exception{
		((PublicationImplementationI)this.connector).publish(ms, topics);
		
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception{
		((PublicationImplementationI)this.connector).publish(ms, topics);
		
	}
	
}
