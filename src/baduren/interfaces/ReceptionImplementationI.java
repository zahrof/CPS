package baduren.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

/**
 * The interface Reception implementationI.
 */
public interface ReceptionImplementationI  extends OfferedI, RequiredI {
	/**
	 * Accept message.
	 *
	 * @param m the message
	 * @throws Exception the exception
	 */
	public void acceptMessage(MessageI m) throws Exception;;

	/**
	 * Accept multiple messages.
	 *
	 * @param ms the messages
	 * @throws Exception the exception
	 */
	public void acceptMessages(MessageI[] ms) throws Exception;;
}
