package baduren.ports.inboundPortsForPlugin;

import baduren.components.Broker.Broker;
import baduren.interfaces.ManagementCI;
import baduren.interfaces.MessageFilterI;
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
      /*  this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((Broker)this.getServiceOwner()).subscribe(topic,inboundPortURI);
                        return null;
                    }
                }) ;*/
        ((Broker)this.owner).subscribe(topic,inboundPortURI);
    }

    @Override
    public void subscribe(String[] topics, String inboundPortURI) throws Exception {
        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((Broker)this.getServiceOwner()).subscribe(topics, inboundPortURI);
                        return null;
                    }
                }) ;
    }

    @Override
    public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception {
        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((Broker)this.getServiceOwner()).subscribe(topic, filter, inboundPortURI);
                        return null;
                    }
                });
    }

    @Override
    public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception {
        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((Broker)this.getServiceOwner()).subscribe(topic,newFilter, inboundPortURI);
                        return null;
                    }
                }) ;
    }

    @Override
    public void unsubscribe(String topic, String inboundPortUri) throws Exception {
        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((Broker)this.getServiceOwner()).unsubscribe(topic, inboundPortUri);
                        return null;
                    }
                }) ;
    }


    @Override
    public void createTopic(String topic) throws Exception {
        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((Broker)this.getServiceOwner()).createTopic(topic);
                        return null;
                    }
                }) ;
    }

    @Override
    public void createTopics(String[] topics) throws Exception {
        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((Broker)this.getServiceOwner()).createTopics(topics);
                        return null;
                    }
                }) ;
    }

    @Override
    public void destroyTopic(String topic) throws Exception {
        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((Broker)this.getServiceOwner()).destroyTopic(topic);
                        return null;
                    }
                }) ;
    }

    @Override
    public boolean isTopic(String topic) throws Exception {
        return this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return((Broker)this.getServiceOwner()).isTopic(topic);
                    }
                }) ;
    }

    @Override
    public String[] getTopics() throws Exception {
        return this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<String[]>() {
                    @Override
                    public String[] call() throws Exception {
                        return ((Broker)this.getServiceOwner()).getTopics();
                    }
                }) ;
    }

    @Override
    public String getPublicationPortURI() throws Exception {
        return this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<String>() {
                    @Override
                    public String call() throws Exception {
                        return ((Broker)this.getServiceOwner()).getPublicationPortURI();
                    }
                }) ;

    }





}
