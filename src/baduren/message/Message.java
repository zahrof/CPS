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
	protected Serializable messagePayload;



	/**
	 * Instantiates a new Message with uri, timeStamp and properties
	 * Used for the unit tests
	 *
	 * @param uri the uri
	 * @throws Exception the exception
	 */
	public Message(String uri, TimeStamp timeStamp, Properties properties, String messagePayload){
		this.messagePayload = messagePayload;
		this.uri = uri;
		this.timeStamp = timeStamp;
		this.properties = properties;
		//this.serializableObject = this; --> car la charge utile c'est le message et pas l'instance de l'objet
	}
	// Constructor with only the message content as parameter
	public Message(String messagePayload) throws Exception {
		this(""+(new TimeStamp(System.currentTimeMillis(), "TimeStamper")).getTimeStamper()
						+ (new TimeStamp(System.currentTimeMillis(), "TimeStamper")).hashCode(),
				new TimeStamp(),
				new Properties(),
				messagePayload);
	}


	@Override
	public String toString() {
		return "[" + timeStamp + "] [" + uri + "] " + messagePayload;
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
		return this.messagePayload;
	}

}
