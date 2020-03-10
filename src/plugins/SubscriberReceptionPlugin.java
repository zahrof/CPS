package plugins;

import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import baduren.interfaces.ReceptionCI;
import baduren.ports.inboundPortsForPlugin.ReceptionInboundPortForPlugin;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class SubscriberReceptionPlugin extends AbstractPlugin implements ReceptionCI {
    // -------------------------------------------------------------------------
    // Plug-in variables and constants
    // -------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;
    /** the inbound port which calls will be on this plug-in.				*/
    protected ReceptionInboundPortForPlugin rip;
    protected String receptionInboundPortUri;
    protected String myReceptionStudent1SubscriberPluginUri;

    public SubscriberReceptionPlugin(String receptionInboundPortUri, String myReceptionStudent1SubscriberPluginUri) {
        this.myReceptionStudent1SubscriberPluginUri= myReceptionStudent1SubscriberPluginUri;
         this.receptionInboundPortUri=receptionInboundPortUri;
    }

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

        assert	owner instanceof ReceptionCI ;

        // Add interfaces and create ports
        this.addOfferedInterface(ReceptionCI.class) ;
        this.rip = new ReceptionInboundPortForPlugin(this.getPluginURI(), this.owner,receptionInboundPortUri) ;
        this.rip.publishPort() ;
    }

    /**
     * @see fr.sorbonne_u.components.AbstractPlugin#uninstall()
     */
    @Override
    public void			uninstall() throws Exception
    {
        this.rip.unpublishPort();
        this.rip.destroyPort() ;
        this.removeOfferedInterface(ReceptionCI.class) ;
    }
    // -------------------------------------------------------------------------
    // Plug-in services implementation
    // -------------------------------------------------------------------------
    private ReceptionCI	getOwner()
    {
        return ((ReceptionCI)this.owner) ;
    }
    @Override
    public void acceptMessage(MessageI m) throws Exception {
        this.getOwner().acceptMessage(m);
    }

    @Override
    public void acceptMessages(MessageI[] ms) throws Exception {
        this.getOwner().acceptMessages(ms);
    }
}
