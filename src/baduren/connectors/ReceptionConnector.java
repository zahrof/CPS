package baduren.connectors;

import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import baduren.interfaces.ReceptionCI;
import baduren.interfaces.ReceptionImplementationI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ReceptionConnector extends AbstractConnector implements ReceptionImplementationI {

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		 ((ReceptionImplementationI)this.offering).acceptMessage(m);
		
	}

	@Override
	public void acceptMessages(MessageI[] ms) throws Exception {
		((ReceptionImplementationI)this.offering).acceptMessages(ms);
		
	}

}
