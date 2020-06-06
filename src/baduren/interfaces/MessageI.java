package baduren.interfaces;

import java.io.Serializable;

import baduren.message.Properties;
import baduren.message.TimeStamp;

/**
 * The interface MessageI.
 */
public interface MessageI extends java.io.Serializable{
	/**
	 * Gets uri.
	 *
	 * @return the uri
	 */
	public String getURI();

	/**
	 * Gets time stamp.
	 *
	 * @return the time stamp
	 */
	public TimeStamp getTimeStamp();

	/**
	 * Gets properties.
	 *
	 * @return the properties
	 */
	public Properties getProperties();

	/**
	 * Gets payload.
	 *
	 * @return the payload
	 */
	public Serializable getPayload();
}
