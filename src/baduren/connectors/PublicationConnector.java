package baduren.connectors;

import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import baduren.interfaces.PublicationImplementationI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.examples.basic_cs.interfaces.URIProviderI;
import baduren.interfaces.ManagementCI;

public class PublicationConnector extends AbstractConnector implements PublicationImplementationI {

	@Override
	public void publish(MessageI m, String topic)throws Exception {
		 ((PublicationImplementationI)this.offering).publish(m, topic);
	}

	@Override
	public void publish(MessageI m, String[] topics)throws Exception {
		 ((PublicationImplementationI)this.offering).publish(m, topics);
	}

	@Override
	public void publish(MessageI[] ms, String topics)throws Exception {
		 ((PublicationImplementationI)this.offering).publish(ms, topics);
	}

	@Override
	public void publish(MessageI[] ms, String[] topics)throws Exception {
		((PublicationImplementationI)this.offering).publish(ms, topics);
	}

}
