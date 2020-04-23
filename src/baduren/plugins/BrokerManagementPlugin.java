package baduren.plugins;

import baduren.interfaces.*;
import baduren.ports.inboundPortsForPlugin.ManagementInboundPortForPlugin;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class BrokerManagementPlugin extends AbstractPlugin implements SubscriptionImplementationI,ManagementImplementationI {

    // -------------------------------------------------------------------------
    // Plug-in variables and constants
    // -------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;
    /** the inbound port which calls will be on this plug-in.				*/
    protected ManagementInboundPortForPlugin mip ;

    // -------------------------------------------------------------------------
    // Life cycle
    // -------------------------------------------------------------------------

    /**
     * @see fr.sorbonne_u.components.AbstractPlugin#installOn(fr.sorbonne_u.components.ComponentI)
     */
    @Override
    public void			installOn(ComponentI owner) throws Exception
    {
        super.installOn(owner) ;

        //  assert	owner instanceof PublicationCI ;

        // Add interfaces and create ports

        this.addOfferedInterface(SubscriptionImplementationI.class) ;
        this.addOfferedInterface(ManagementImplementationI.class) ;
        this.mip = new ManagementInboundPortForPlugin(
                this.getPluginURI(), this.owner) ;
        this.mip.publishPort() ;
    }

    /**
     * @see fr.sorbonne_u.components.AbstractPlugin#uninstall()
     */
    @Override
    public void			uninstall() throws Exception
    {
        this.mip.unpublishPort() ;
        this.mip.destroyPort() ;
        this.removeOfferedInterface(ManagementCI.class) ;

    }

    // -------------------------------------------------------------------------
    // Plug-in services implementation
    // -------------------------------------------------------------------------
    private ManagementImplementationI	getOwnerManagement()
    {
        return ((ManagementImplementationI)this.owner) ;
    }

    private SubscriptionImplementationI	getOwnerSubscription()
    {
        return ((SubscriptionImplementationI)this.owner) ;
    }

    @Override
    public void createTopic(String topic) throws Exception {
        this.getOwnerManagement().createTopic(topic);
    }

    @Override
    public void createTopics(String[] topics) throws Exception {
        this.getOwnerManagement().createTopics(topics);
    }

    @Override
    public void destroyTopic(String topic) throws Exception {
        this.getOwnerManagement().destroyTopic(topic);
    }

    @Override
    public boolean isTopic(String topic) throws Exception {
        return   this.getOwnerManagement().isTopic(topic);
    }

    @Override
    public String[] getTopics() throws Exception {
        return   this.getOwnerManagement().getTopics();
    }

    @Override
    public String getPublicationPortURI() throws Exception {
        return   this.getOwnerManagement().getPublicationPortURI();
    }

    @Override
    public void subscribe(String topic, String inboundPortURI) throws Exception {
        this.getOwnerSubscription().subscribe(topic, inboundPortURI);
    }

    @Override
    public void subscribe(String[] topics, String inboundPortURI) throws Exception {
        this.getOwnerSubscription().subscribe(topics, inboundPortURI);
    }

    @Override
    public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception {
        this.getOwnerSubscription().subscribe(topic, filter, inboundPortURI);
    }

    @Override
    public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception {
        this.getOwnerSubscription().modifyFilter(topic, newFilter, inboundPortURI);
    }

    @Override
    public void unsubscribe(String topic, String inboundPortUri) throws Exception {
        this.getOwnerSubscription().unsubscribe(topic ,inboundPortUri);
    }
}
