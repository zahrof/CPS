package baduren.ports.inboundPortsForPlugin;

import baduren.components.Broker.Broker;
import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.forplugins.AbstractInboundPortForPlugin;

public class PublicationInboundPortForPlugin extends AbstractInboundPortForPlugin implements PublicationCI{

        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new Publication inbound port.
         *
         * @param pluginUri   the uri
         * @param owner the owner
         * @throws Exception the exception
         */
        public	PublicationInboundPortForPlugin(String pluginUri, ComponentI owner)
                throws Exception
        {
            super(PublicationCI.class, pluginUri, owner) ;
        }

        public	PublicationInboundPortForPlugin(
            String uri,
            String pluginURI,
            ComponentI owner
    ) throws Exception
    {
        super(uri, PublicationCI.class, pluginURI, owner);

        assert	owner instanceof PublicationCI ;
    }

        @Override
        public void publish(MessageI m, String topic) throws Exception {
	/*	this.getOwner().handleRequestSync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(m, topic);
						return null;
					}
				}) ;*/
            ((Broker)this.owner).publish(m, topic);
        }

        @Override
        public void publish(MessageI m, String[] topics) throws Exception {
/*		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).destroyTopic(topic);
						return null;
					}
				}) ;*/
            ((Broker)this.owner).publish(m, topics);
        }

        @Override
        public void publish(MessageI[] ms, String topics) throws Exception {
/*		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).destroyTopic(topic);
						return null;
					}
				}) ;*/
            ((Broker)this.owner).publish(ms, topics);
        }

        @Override
        public void publish(MessageI[] ms, String[] topics) throws Exception {
/*		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).destroyTopic(topic);
						return null;
					}
				}) ;*/
            ((Broker)this.owner).publish(ms, topics);
        }
    }




