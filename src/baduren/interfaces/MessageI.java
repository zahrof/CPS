package baduren.interfaces;

import java.io.Serializable;

import baduren.message.Properties;
import baduren.message.TimeStamp;

public interface MessageI extends java.io.Serializable{
	public String getURI(); 
	public TimeStamp getTimeStamp(); 
	public Properties getProperties(); 
	public Serializable getPayload(); 
}
