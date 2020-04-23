package baduren.interfaces;

/**
 * The interface MessagefilterI.
 */
@FunctionalInterface
public interface MessageFilterI {
	/**
	 * Filter boolean.
	 *
	 * @param m the message
	 * @return the boolean
	 * @throws Exception the exception
	 */
	boolean filter(MessageI m)throws Exception;
}
