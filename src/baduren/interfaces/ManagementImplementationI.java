package baduren.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

/**
 * The interface ManagementImplementationI.
 */
public interface ManagementImplementationI extends OfferedI, RequiredI {
	/**
	 * Create topic.
	 *
	 * @param topic the topic
	 * @throws Exception the exception
	 */
	public void createTopic(String topic)throws Exception;

	/**
	 * Create topics.
	 *
	 * @param topics the topics
	 * @throws Exception the exception
	 */
	public void createTopics(String[] topics)throws Exception;

	/**
	 * Destroy topic.
	 *
	 * @param topic the topic
	 * @throws Exception the exception
	 */
	public void destroyTopic(String topic)throws Exception;

	/**
	 * Is topic boolean.
	 *
	 * @param topic the topic
	 * @return the boolean
	 * @throws Exception the exception
	 */
	public boolean isTopic(String topic)throws Exception;

	/**
	 * Get topics string [ ].
	 *
	 * @return the string [ ]
	 * @throws Exception the exception
	 */
	public String[] getTopics()throws Exception;

	/**
	 * Gets publication port uri.
	 *
	 * @return the publication port uri
	 * @throws Exception the exception
	 */
	public String getPublicationPortURI()throws Exception;
}
