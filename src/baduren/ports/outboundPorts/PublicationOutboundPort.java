package baduren.ports.outboundPorts;

import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class PublicationOutboundPort extends	AbstractOutboundPort implements PublicationCI {

	private static final long serialVersionUID = 1L;
	
	// Constructeurs
	
	public PublicationOutboundPort(String uri, ComponentI owner) throws Exception{
		super(uri, PublicationCI.class, owner) ;
	}
	
	public PublicationOutboundPort(ComponentI owner) throws Exception{
		super(PublicationCI.class, owner) ;
	}
	
	// Methodes
	
	
	@Override
	public void publish(MessageI m, String topic) throws Exception{
		((PublicationCI)this.connector).publish(m, topic);
	}

	@Override
	public void publish(MessageI m, String[] topics)throws Exception {
		((PublicationCI)this.connector).publish(m, topics);
		
	}

	@Override
	public void publish(MessageI[] ms, String topics) throws Exception{
		((PublicationCI)this.connector).publish(ms, topics);
		
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception{
		((PublicationCI)this.connector).publish(ms, topics);
		
	}
	
}
