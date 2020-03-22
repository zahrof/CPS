package baduren.ports.outboundPorts;

import baduren.interfaces.MessageI;
import baduren.interfaces.ReceptionCI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.examples.pingpong.components.Ball;
import fr.sorbonne_u.components.examples.pingpong.components.PingPongPlayer;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * The type ReceptionOutboundPort.
 */
public class ReceptionOutboundPort extends AbstractOutboundPort implements ReceptionCI {

	private static final long serialVersionUID = 1L;
	
	// Constructeurs

	/**
	 * Instantiates a new Reception outbound port.
	 *
	 * @param uri   the uri
	 * @param owner the owner
	 * @throws Exception the exception
	 */
	public ReceptionOutboundPort(String uri, ComponentI owner) throws Exception{
		super(uri, ReceptionCI.class, owner) ;

		assert	uri != null && owner != null ;
	}

	/**
	 * Instantiates a new Reception outbound port.
	 *
	 * @param owner the owner
	 * @throws Exception the exception
	 */
	public ReceptionOutboundPort(ComponentI owner) throws Exception{
		super(ReceptionCI.class, owner) ;
		assert	uri != null && owner != null ;
	}
	
	// Methodes

	@Override
	public void acceptMessage(MessageI m)throws Exception {
		((ReceptionCI)this.connector).acceptMessage(m);
		
	}

	@Override
	public void acceptMessages(MessageI[] ms)throws Exception {
		/*this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((PingPongPlayer)this.getServiceOwner()).
								playOnDataReception((Ball) d) ;
						return null;
					}
				}) ;*/
		((ReceptionCI)this.connector).acceptMessages(ms);
	}




}
