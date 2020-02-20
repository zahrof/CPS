package baduren.ports.inboundPorts;

import baduren.components.Subscriber;
import baduren.interfaces.MessageI;
import baduren.interfaces.ReceptionCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * The type ReceptionInboundPort.
 */
public class ReceptionInboundPort extends		AbstractInboundPort implements ReceptionCI {

	private static final long serialVersionUID = 1L;


	/**
	 * Constructeur
	 *
	 * @param uri   the uri
	 * @param owner the owner
	 * @throws Exception the exception
	 */
	public	ReceptionInboundPort(String uri, ComponentI owner) throws Exception{
		// the implemented interface is statically known
		super(uri, ReceptionCI.class, owner) ;
	}

	/**
	 * Instantiates a new Reception inbound port.
	 *
	 * @param owner the owner
	 * @throws Exception the exception
	 */
	public	ReceptionInboundPort(ComponentI owner) throws Exception{
		// the implemented interface is statically known
		super(ReceptionCI.class, owner) ;
	}

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		((Subscriber)this.owner).acceptMessage(m);
	}

	@Override
	public void acceptMessages(MessageI[] ms) {
		((Subscriber)this.owner).acceptMessages(ms);
	}

	
}
