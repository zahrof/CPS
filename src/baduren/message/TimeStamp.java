package baduren.message;

import java.net.InetAddress;
import java.time.Instant;

public class TimeStamp {
	protected long time; 
	protected String timeStamper; // nom de l'hôte ou @ IP. 

	protected TimeStamp() throws Exception {
		this.time=Instant.now().getEpochSecond();
		this.timeStamper = InetAddress.getLocalHost().getHostAddress();
	}

	boolean isInitialised() {
		return (this.time==0)&& (this.timeStamper==null);  
	}

	long getTime(){
		return this.time; 
	}

	void setTime(long time) {
		this.time=time; 
	}

	String getTimeStamper(String timeStamper) {
		return this.timeStamper; 
	}

	void setTimeStamper(String timeStamper) {
		this.timeStamper = timeStamper; 
	}
}
