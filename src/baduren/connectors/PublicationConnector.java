package baduren.connectors;

import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.examples.basic_cs.interfaces.URIProviderI;
import baduren.interfaces.ManagementCI;

public class PublicationConnector extends AbstractConnector implements PublicationCI {

	@Override
	public void publish(MessageI m, String topic)throws Exception {
		 ((PublicationCI)this.offering).publish(m, topic); 
	}

	@Override
	public void publish(MessageI m, String[] topics)throws Exception {
		 ((PublicationCI)this.offering).publish(m, topics); 
	}

	@Override
	public void publish(MessageI[] ms, String topics)throws Exception {
		 ((PublicationCI)this.offering).publish(ms, topics); 
	}

	@Override
	public void publish(MessageI[] ms, String[] topics)throws Exception {
		((PublicationCI)this.offering).publish(ms, topics);
	}

}
