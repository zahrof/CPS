package baduren.ports.inboundPortsForPlugin;

import baduren.components.Broker.Broker;
import baduren.interfaces.ManagementCI;
import baduren.interfaces.MessageFilterI;
import baduren.interfaces.ReceptionImplementationI;
import baduren.plugins.BrokerManagementPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.forplugins.AbstractInboundPortForPlugin;

public class ManagementInboundPortForPlugin extends AbstractInboundPortForPlugin implements ManagementCI {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Publication inbound port.
     *
     * @param pluginUri   the uri
     * @param owner the owner
     * @throws Exception the exception
     */
    public	ManagementInboundPortForPlugin(String pluginUri, ComponentI owner)
            throws Exception
    {
        super(ManagementCI.class, pluginUri, owner) ;
    }

    public	ManagementInboundPortForPlugin(
            String uri,
            String pluginURI,
            ComponentI owner
    ) throws Exception
    {
        super(uri, ManagementCI.class, pluginURI, owner);

        assert	owner instanceof ManagementCI ;
    }

    @Override
    public void subscribe(String topic, String inboundPortURI) throws Exception {
/*        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerManagementPlugin)this.getServiceProviderReference()).subscribe(topic,inboundPortURI);
                        return null;
                    }
                }) ;*/
       // ((BrokerManagementPlugin)this.getServiceProviderReference()).subscribe(topic,inboundPortURI);

        this.owner.runTask(
                new AbstractComponent.AbstractTask(this.pluginURI) {
                    @Override
                    public void run() {
                        try {
                            ((BrokerManagementPlugin) this.getTaskProviderReference()).subscribe(topic,inboundPortURI);
                        } catch (Exception e) {
                            e.printStackTrace() ;
                        }
                    }
                }) ;
    }

    @Override
    public void subscribe(String[] topics, String inboundPortURI) throws Exception {
/*        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerManagementPlugin)this.getServiceProviderReference()).subscribe(topics, inboundPortURI);
                        return null;
                    }
                }) ;*/

        this.owner.runTask(
                new AbstractComponent.AbstractTask(this.pluginURI) {
                    @Override
                    public void run() {
                        try {
                            ((BrokerManagementPlugin) this.getTaskProviderReference()).subscribe(topics, inboundPortURI);
                        } catch (Exception e) {
                            e.printStackTrace() ;
                        }
                    }
                }) ;
    }

    @Override
    public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception {
/*        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerManagementPlugin)this.getServiceProviderReference()).subscribe(topic, filter, inboundPortURI);
                        return null;
                    }
                });*/

        this.owner.runTask(
                new AbstractComponent.AbstractTask(this.pluginURI) {
                    @Override
                    public void run() {
                        try {
                            ((BrokerManagementPlugin) this.getTaskProviderReference()).subscribe(topic, filter, inboundPortURI);
                        } catch (Exception e) {
                            e.printStackTrace() ;
                        }
                    }
                }) ;
    }

    @Override
    public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception {
/*        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerManagementPlugin)this.getServiceProviderReference()).modifyFilter(topic,newFilter, inboundPortURI);
                        return null;
                    }
                }) ;*/

        this.owner.runTask(
                new AbstractComponent.AbstractTask(this.pluginURI) {
                    @Override
                    public void run() {
                        try {
                            ((BrokerManagementPlugin) this.getTaskProviderReference()).modifyFilter(topic,newFilter, inboundPortURI);
                        } catch (Exception e) {
                            e.printStackTrace() ;
                        }
                    }
                }) ;
    }

    @Override
    public void unsubscribe(String topic, String inboundPortUri) throws Exception {
/*        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerManagementPlugin)this.getServiceProviderReference()).unsubscribe(topic, inboundPortUri);
                        return null;
                    }
                }) ;*/

        this.owner.runTask(
                new AbstractComponent.AbstractTask(this.pluginURI) {
                    @Override
                    public void run() {
                        try {
                            ((BrokerManagementPlugin) this.getTaskProviderReference()).unsubscribe(topic, inboundPortUri);
                        } catch (Exception e) {
                            e.printStackTrace() ;
                        }
                    }
                }) ;
    }


    @Override
    public void createTopic(String topic) throws Exception {
        this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerManagementPlugin)this.getServiceProviderReference()).createTopic(topic);
                        return null;
                    }
                }) ;
    }

    @Override
    public void createTopics(String[] topics) throws Exception {
        this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerManagementPlugin)this.getServiceProviderReference()).createTopics(topics);
                        return null;
                    }
                }) ;
    }

    @Override
    public void destroyTopic(String topic) throws Exception {
/*        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerManagementPlugin)this.getServiceProviderReference()).destroyTopic(topic);
                        return null;
                    }
                }) ;*/

        this.owner.runTask(
                new AbstractComponent.AbstractTask(this.pluginURI) {
                    @Override
                    public void run() {
                        try {
                            ((BrokerManagementPlugin) this.getTaskProviderReference()).destroyTopic(topic);
                        } catch (Exception e) {
                            e.printStackTrace() ;
                        }
                    }
                }) ;
    }

    @Override
    public boolean isTopic(String topic) throws Exception {
        return this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<Boolean>(this.pluginURI) {
                    @Override
                    public Boolean call() throws Exception {
                        return( (BrokerManagementPlugin)this.getServiceProviderReference()).isTopic(topic);
                    }
                }) ;
    }

    @Override
    public String[] getTopics() throws Exception {
        return this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<String[]>(this.pluginURI) {
                    @Override
                    public String[] call() throws Exception {
                        return ((BrokerManagementPlugin)this.getServiceProviderReference()).getTopics();
                    }
                }) ;
    }

    @Override
    public String getPublicationPortURI() throws Exception {
        return this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<String>(this.pluginURI) {
                    @Override
                    public String call() throws Exception {
                        return ((BrokerManagementPlugin)this.getServiceProviderReference()).getPublicationPortURI();
                    }
                }) ;

    }





}
