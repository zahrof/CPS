package baduren.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

/**
 * The interface Subscription implementationI.
 */
public interface SubscriptionImplementationI extends OfferedI, RequiredI {
	/**
	 * Subscribe.
	 *
	 * @param topic          the topic
	 * @param inboundPortURI the inbound port uri
	 * @throws Exception the exception
	 */
	public void subscribe(String topic, String inboundPortURI)throws Exception;

	/**
	 * Subscribe.
	 *
	 * @param topics         the topics
	 * @param inboundPortURI the inbound port uri
	 * @throws Exception the exception
	 */
	public void subscribe(String[] topics, String inboundPortURI)throws Exception;

	/**
	 * Subscribe.
	 *
	 * @param topic          the topic
	 * @param filter         the filter
	 * @param inboundPortURI the inbound port uri
	 * @throws Exception the exception
	 */
	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI)throws Exception;

	/**
	 * Modify filter.
	 *
	 * @param topic          the topic
	 * @param newFilter      the new filter
	 * @param inboundPortURI the inbound port uri
	 * @throws Exception the exception
	 */
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI)throws Exception;

	/**
	 * Unsubscribe.
	 *
	 * @param topic          the topic
	 * @param inboundPortUri the inbound port uri
	 * @throws Exception the exception
	 */
	public void unsubscribe(String topic, String inboundPortUri)throws Exception;
}
