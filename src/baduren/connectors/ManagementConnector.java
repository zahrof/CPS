package baduren.connectors;

import baduren.interfaces.ManagementCI;
import baduren.interfaces.MessageFilterI;
import baduren.interfaces.PublicationCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.examples.basic_cs.interfaces.URIProviderI;

public class ManagementConnector extends AbstractConnector implements ManagementCI{

	@Override
	public void subscribe(String topic, String inboundPortURI)throws Exception {
		((ManagementCI)this.offering).subscribe(topic, inboundPortURI); 
	}

	@Override
	public void subscribe(String[] topics, String inboundPortURI)throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI)throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI)throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsubscribe(String topic, String inboundPortUri) throws Exception{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createTopic(String topic)throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createTopics(String[] topics)throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroyTopic(String topic) throws Exception{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTopic(String topic)throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] getTopics() throws Exception{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPublicationPortURI() throws Exception{
		// TODO Auto-generated method stub
		return null;
	}

}
