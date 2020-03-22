package baduren.message;
import java.io.Serializable;

import baduren.interfaces.MessageI;

/**
 * The type Message.
 */
public class Message implements MessageI, Serializable {

	/**
	 * The Uri.
	 */
	protected String uri;
	/**
	 * The Time stamp.
	 */
	protected TimeStamp timeStamp;
	/**
	 * The Properties.
	 */
	protected Properties properties;
	/**
	 * The Serializable object.
	 */
	protected Serializable serializableObject;

	/**
	 * Instantiates a new Message.
	 *
	 * @param uri the uri
	 * @throws Exception the exception
	 */
	public Message(String uri) throws Exception {
		this.uri=uri;
		this.timeStamp = new TimeStamp();
		this.properties = new Properties();
	}

	/**
	 * Instantiates a new Message with uri, timeStamp and properties
	 * Used for the unit tests
	 *
	 * @param uri the uri
	 * @throws Exception the exception
	 */
	public Message(String uri, TimeStamp timeStamp, Properties properties) throws Exception {
		this.uri=uri;
		this.timeStamp = timeStamp;
		this.properties = properties;
		this.serializableObject = this;
	}

	@Override
	public String toString() {
		return uri;
	}

	@Override
	public String getURI() {
		return this.uri; 
	}

	@Override
	public TimeStamp getTimeStamp() {
		return this.timeStamp; 
	}

	@Override
	public Properties getProperties() {
		return this.properties; 
	}

	@Override
	public Serializable getPayload() {
		return this.serializableObject; 
	}

}
