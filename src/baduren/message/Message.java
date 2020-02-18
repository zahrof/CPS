package baduren.message;
import java.io.Serializable;

import baduren.interfaces.MessageI;

@FunctionalInterface
interface MessageFilterI 
{ 
    boolean filter(MessageI m); 
} 
//@FunctionalInterface
//interface Square 
//{ 
//    int calculate(int x); 
//} 
//  

public class Message implements MessageI {
	
	protected String uri; 
	protected TimeStamp timeStamp; 
	protected Properties properties; 
	protected Serializable serializableObject; 
	
	public Message(String uri) throws Exception {
		this.uri=uri; 
		this.timeStamp = new TimeStamp();
		// lambda expression to define the calculate method 
       // Square s = (int x)->x*x; 
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
