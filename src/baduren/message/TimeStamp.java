package baduren.message;

import java.net.InetAddress;
import java.time.Instant;

/**
 * The class Timestamp.
 */
public class TimeStamp {
	/**
	 * The Time.
	 */
	protected long time;
	/**
	 * The Time stamper.
	 */
	protected String timeStamper; // nom de l'hôte ou @ IP.

	/**
	 * Instantiates a new TimeStamp.
	 *
	 * @throws Exception the exception
	 */
	protected TimeStamp() throws Exception {
		this.time=Instant.now().getEpochSecond();
		this.timeStamper = InetAddress.getLocalHost().getHostAddress();
	}


	/**
	 * Instantiates a new TimeStamp.
	 *
	 * @throws Exception the exception
	 */
	public TimeStamp(long time, String timeStamper) throws Exception {
		this.time=time;
		this.timeStamper = timeStamper;
	}

	/**
	 * Is initialised boolean.
	 *
	 * @return the boolean
	 */
	boolean isInitialised() {
		return (this.time==0)&& (this.timeStamper==null);  
	}

	/**
	 * Get time long.
	 *
	 * @return the long
	 */
	public long getTime(){
		return this.time; 
	}

	/**
	 * Sets time.
	 *
	 * @param time the time
	 */
	void setTime(long time) {
		this.time=time; 
	}

	/**
	 * Gets time stamper.
	 *
	 * @return the time stamper
	 */
	public String getTimeStamper() {
		return this.timeStamper; 
	}

	/**
	 * Sets time stamper.
	 *
	 * @param timeStamper the time stamper
	 */
	void setTimeStamper(String timeStamper) {
		this.timeStamper = timeStamper; 
	}
}
