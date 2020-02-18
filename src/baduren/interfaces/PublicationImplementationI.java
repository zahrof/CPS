package baduren.interfaces;

public interface PublicationImplementationI {
	public void publish(MessageI m, String topic)throws Exception;
	public void publish(MessageI m, String[] topics)throws Exception; 
	public void publish(MessageI[] ms, String topics)throws Exception; 
	public void publish(MessageI[] ms, String[] topics)throws Exception; 

}
