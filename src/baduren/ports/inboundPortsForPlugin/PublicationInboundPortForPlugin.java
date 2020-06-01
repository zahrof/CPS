package baduren.ports.inboundPortsForPlugin;

import baduren.components.Broker.Broker;
import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import baduren.interfaces.PublicationImplementationI;
import baduren.interfaces.SubscriptionImplementationI;
import baduren.plugins.BrokerManagementPlugin;
import baduren.plugins.BrokerPublicationPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.forplugins.AbstractInboundPortForPlugin;

public class PublicationInboundPortForPlugin extends AbstractInboundPortForPlugin implements PublicationImplementationI {

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
            super(PublicationImplementationI.class, pluginUri, owner) ;
        }

        public	PublicationInboundPortForPlugin(
            String uri,
            String pluginURI,
            ComponentI owner
    ) throws Exception
    {
        super(uri, PublicationImplementationI.class, pluginURI, owner);

        assert	owner instanceof PublicationCI ;
    }

        @Override
        public void publish(MessageI m, String topic) throws Exception {
/*		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>(this.pluginURI) {
					@Override
					public Void call() throws Exception {
						((BrokerPublicationPlugin)this.getServiceProviderReference()).publish(m, topic);
						return null;
					}
				}); ;*/
           // ((Broker)this.owner).publish(m, topic);
			//((BrokerPublicationPlugin)this.getSer).publish(m, topic);

			///???????
			this.owner.runTask(
					new AbstractComponent.AbstractTask() {

						@Override
						public void run() {
							try {
								((Broker) this.getTaskOwner()).publish(m, topic);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
        }



        @Override
        public void publish(MessageI m, String[] topics) throws Exception {
/*		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>(this.pluginURI) {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(m, topics);
						return null;
					}
				}) ;*/

			this.owner.runTask(
					new AbstractComponent.AbstractTask() {

						@Override
						public void run() {
							try {
								((Broker) this.getTaskOwner()).publish(m, topics);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
        }

        @Override
        public void publish(MessageI[] ms, String topics) throws Exception {
/*		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>(this.pluginURI) {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(ms, topics);
						return null;
					}
				}) ;*/

			this.owner.runTask(
					new AbstractComponent.AbstractTask() {

						@Override
						public void run() {
							try {
								((Broker) this.getTaskOwner()).publish(ms, topics);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
        }

        @Override
        public void publish(MessageI[] ms, String[] topics) throws Exception {
/*		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>(this.pluginURI) {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(ms, topics);
						return null;
					}
				}) ;*/

			this.owner.runTask(
					new AbstractComponent.AbstractTask() {

						@Override
						public void run() {
							try {
								((Broker) this.getTaskOwner()).publish(ms, topics);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
        }
    }




