package plugins;

import baduren.interfaces.ManagementCI;
import baduren.interfaces.MessageFilterI;
import baduren.interfaces.PublicationCI;
import baduren.ports.inboundPorts.PublicationInboundPort;
import baduren.ports.inboundPortsForPlugin.ManagementInboundPortForPlugin;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class BrokerManagementPlugin extends AbstractPlugin implements ManagementCI {

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
        this.addOfferedInterface(ManagementCI.class) ;
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
    private PublicationCI	getOwner()
    {
        return ((PublicationCI)this.owner) ;
    }

    @Override
    public void createTopic(String topic) throws Exception {

    }

    @Override
    public void createTopics(String[] topics) throws Exception {

    }

    @Override
    public void destroyTopic(String topic) throws Exception {

    }

    @Override
    public boolean isTopic(String topic) throws Exception {
        return false;
    }

    @Override
    public String[] getTopics() throws Exception {
        return new String[0];
    }

    @Override
    public String getPublicationPortURI() throws Exception {
        return null;
    }

    @Override
    public void subscribe(String topic, String inboundPortURI) throws Exception {

    }

    @Override
    public void subscribe(String[] topics, String inboundPortURI) throws Exception {

    }

    @Override
    public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception {

    }

    @Override
    public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception {

    }

    @Override
    public void unsubscribe(String topic, String inboundPortUri) throws Exception {

    }
}
