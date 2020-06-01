package baduren.plugins;

import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import baduren.interfaces.PublicationImplementationI;
import baduren.ports.inboundPortsForPlugin.PublicationInboundPortForPlugin;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class BrokerPublicationPlugin extends AbstractPlugin implements PublicationImplementationI {
    // -------------------------------------------------------------------------
    // Plug-in variables and constants
    // -------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    public PublicationInboundPortForPlugin getPip() {
        return pip;
    }

    /** the inbound port which calls will be on this plug-in.				*/
    protected PublicationInboundPortForPlugin pip ;

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
        this.addOfferedInterface(PublicationCI.class) ;
        this.pip = new PublicationInboundPortForPlugin(this.getPluginURI(), this.owner) ;
        this.pip.publishPort() ;
    }

    /**
     * @see fr.sorbonne_u.components.AbstractPlugin#uninstall()
     */
    @Override
    public void			uninstall() throws Exception
    {
        this.pip.unpublishPort();
        this.pip.destroyPort() ;
        this.removeOfferedInterface(PublicationCI.class) ;
    }
    // -------------------------------------------------------------------------
    // Plug-in services implementation
    // -------------------------------------------------------------------------
    private PublicationCI	getOwner()
    {
        return ((PublicationCI)this.owner) ;
    }

    @Override
    public void publish(MessageI m, String topic) throws Exception {
            this.getOwner().publish(m, topic);
    }

    @Override
    public void publish(MessageI m, String[] topics) throws Exception {
        this.getOwner().publish(m, topics);
    }

    @Override
    public void publish(MessageI[] ms, String topics) throws Exception {
        this.getOwner().publish(ms, topics);
    }

    @Override
    public void publish(MessageI[] ms, String[] topics) throws Exception {
        this.getOwner().publish(ms, topics);
    }
}
