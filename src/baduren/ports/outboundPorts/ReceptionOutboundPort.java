package baduren.ports.outboundPorts;

import baduren.interfaces.MessageI;
import baduren.interfaces.ReceptionCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class ReceptionOutboundPort extends AbstractOutboundPort implements ReceptionCI {

	private static final long serialVersionUID = 1L;
	
	// Constructeurs
	
	public ReceptionOutboundPort(String uri, ComponentI owner) throws Exception{
		super(uri, ReceptionCI.class, owner) ;
		assert	uri != null && owner != null ;
	}
	
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
		((ReceptionCI)this.connector).acceptMessages(ms);
	}




}
