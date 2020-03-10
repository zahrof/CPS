package baduren.ports.inboundPortsForPlugin;

import baduren.components.Subscribers.Subscriber_Student1;
import baduren.interfaces.MessageI;
import baduren.interfaces.ReceptionCI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.forplugins.AbstractInboundPortForPlugin;
import baduren.plugins.SubscriberReceptionPlugin;

public class ReceptionInboundPortForPlugin extends AbstractInboundPortForPlugin implements ReceptionCI {

    private static final long serialVersionUID = 1L;

    public ReceptionInboundPortForPlugin(String pluginUri, ComponentI owner)
            throws Exception {
        super(ReceptionCI.class, pluginUri, owner);
    }

    public ReceptionInboundPortForPlugin(String uri, String pluginURI, ComponentI owner) throws Exception {
        super(uri, ReceptionCI.class, pluginURI, owner);

        assert owner instanceof ReceptionCI;
    }

    public ReceptionInboundPortForPlugin(String pluginURI, ComponentI owner, String uri) throws Exception {
        super(uri, ReceptionCI.class,pluginURI,owner );
    }

    @Override
    public void acceptMessage(MessageI m) throws Exception {
      /*  this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((SubscriberReceptionPlugin) this.getServiceProviderReference()).acceptMessage(m);
                        return null;
                    }
                });*/
        ((Subscriber_Student1)this.owner).acceptMessage(m);
    }

    @Override
    public void acceptMessages(MessageI[] ms) throws Exception {
        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((SubscriberReceptionPlugin) this.getServiceProviderReference()).acceptMessages(ms);
                        return null;
                    }
                });

    }
}
