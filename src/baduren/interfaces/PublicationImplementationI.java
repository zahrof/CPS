package baduren.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

/**
 * The interface Publication implementationI.
 */
public interface PublicationImplementationI extends OfferedI, RequiredI {
	/**
	 * Publish.
	 *
	 * @param m     the message
	 * @param topic the topic
	 * @throws Exception the exception
	 */
	public void publish(MessageI m, String topic)throws Exception;

	/**
	 * Publish.
	 *
	 * @param m      the message
	 * @param topics the topics
	 * @throws Exception the exception
	 */
	public void publish(MessageI m, String[] topics)throws Exception;

	/**
	 * Publish.
	 *
	 * @param ms     the messages
	 * @param topics the topics
	 * @throws Exception the exception
	 */
	public void publish(MessageI[] ms, String topics)throws Exception;

	/**
	 * Publish.
	 *
	 * @param ms     the messages
	 * @param topics the topics
	 * @throws Exception the exception
	 */
	public void publish(MessageI[] ms, String[] topics)throws Exception;

}
