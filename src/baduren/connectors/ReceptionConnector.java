package baduren.connectors;

import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import baduren.interfaces.ReceptionCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ReceptionConnector extends AbstractConnector implements ReceptionCI {

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		 ((ReceptionCI)this.offering).acceptMessage(m); 
		
	}

	@Override
	public void acceptMessages(MessageI[] ms) {
		// TODO Auto-generated method stub
		
	}

}
