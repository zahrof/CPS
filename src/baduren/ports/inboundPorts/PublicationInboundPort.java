package baduren.ports.inboundPorts;

import baduren.components.Broker;
import baduren.components.Publisher;
import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.examples.basic_cs.components.URIProvider;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class PublicationInboundPort extends	AbstractInboundPort implements PublicationCI {

	private static final long serialVersionUID = 1L;
	
	public	PublicationInboundPort(String uri, ComponentI owner) 
			throws Exception
		{
			super(uri, PublicationCI.class, owner) ;

		}
	
	public				PublicationInboundPort(ComponentI owner)
	throws Exception
	{
		super(PublicationCI.class, owner) ;
	}

	@Override
	public void publish(MessageI m, String topic) throws Exception {
		this.owner.handleRequestAsync(new AbstractComponent.AbstractService<Void>() {

			@Override
			public Void call() throws Exception {
				((Broker)this.getServiceOwner()).publish(m, topic);
				return null;
				
			}
			
		});

	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		this.owner.handleRequestAsync(new AbstractComponent.AbstractService<Void>() {

			@Override
			public Void call() throws Exception {
				((Broker)this.getServiceOwner()).publish(m, topics);
				return null;
				
			}
			
		});
		
	}

	@Override
	public void publish(MessageI[] ms, String topics) throws Exception {
		this.owner.handleRequestAsync(new AbstractComponent.AbstractService<Void>() {

			@Override
			public Void call() throws Exception {
				((Broker)this.getServiceOwner()).publish(ms, topics);
				return null;
				
			}
			
		});
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		this.owner.handleRequestAsync(new AbstractComponent.AbstractService<Void>() {

			@Override
			public Void call() throws Exception {
				((Broker)this.getServiceOwner()).publish(ms, topics);
				return null;
				
			}
			
		});
		
	}
}


