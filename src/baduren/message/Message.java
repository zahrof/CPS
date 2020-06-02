package baduren.message;
import java.io.Serializable;

import baduren.interfaces.MessageI;

/**
 * The type Message.
 */
public class Message implements MessageI {

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

	public String getMessage() {
		return message;
	}

	protected String message;

	public Message(String serializableObject) throws Exception {
		TimeStamp ts = new TimeStamp(System.currentTimeMillis(), "TimeStamper");
		this.serializableObject=serializableObject;
		this.uri=""+ts.getTimeStamper()+ ts.hashCode();
		this.timeStamp = new TimeStamp();
		this.properties = new Properties();
		this.message=serializableObject;
		}


	/**
	 * Instantiates a new Message with uri, timeStamp and properties
	 * Used for the unit tests
	 *
	 * @param uri the uri
	 * @throws Exception the exception
	 */
	public Message(String uri, TimeStamp timeStamp, Properties properties, String content){
		this.serializableObject=content;
		this.uri=uri;
		this.timeStamp = timeStamp;
		this.properties = properties;
		this.serializableObject = this;
		this.message= content;
	}

	@Override
	public String toString() {
		return message;
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
