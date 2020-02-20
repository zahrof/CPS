package baduren.interfaces;

/**
 * The interface Reception implementationI.
 */
public interface ReceptionImplementationI {
	/**
	 * Accept message.
	 *
	 * @param m the message
	 * @throws Exception the exception
	 */
	public void acceptMessage(MessageI m) throws Exception;;

	/**
	 * Accept messages.
	 *
	 * @param ms the messages
	 * @throws Exception the exception
	 */
	public void acceptMessages(MessageI[] ms) throws Exception;;
}
