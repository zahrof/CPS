package baduren.plugins;

import baduren.interfaces.MessageI;
import baduren.interfaces.ReceptionImplementationI;
import baduren.ports.inboundPortsForPlugin.ReceptionInboundPortForPlugin;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class SubscriberReceptionPlugin extends AbstractPlugin implements ReceptionImplementationI {
    // -------------------------------------------------------------------------
    // Plug-in variables and constants
    // -------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;
    /** the inbound port which calls will be on this plug-in.				*/
    public ReceptionInboundPortForPlugin rip;
    public String receptionInboundPortUri;
    protected String myReceptionStudent1SubscriberPluginUri;

    public SubscriberReceptionPlugin(String receptionInboundPortUri, String myReceptionStudent1SubscriberPluginUri) {
        this.myReceptionStudent1SubscriberPluginUri= myReceptionStudent1SubscriberPluginUri;
         this.receptionInboundPortUri=receptionInboundPortUri;
    }

    @Override
    public void initialise() throws Exception {
        super.initialise();
        this.rip = new ReceptionInboundPortForPlugin(this.getPluginURI(), this.owner,receptionInboundPortUri) ;
        this.rip.publishPort() ;
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

        assert	owner instanceof ReceptionImplementationI ;

        // Add interfaces and create ports
        this.addOfferedInterface(ReceptionImplementationI.class) ;

    }

    /**
     * @see fr.sorbonne_u.components.AbstractPlugin#uninstall()
     */
    @Override
    public void			uninstall() throws Exception
    {
        this.rip.unpublishPort();
        this.rip.destroyPort() ;
        this.removeOfferedInterface(ReceptionImplementationI.class) ;
    }
    // -------------------------------------------------------------------------
    // Plug-in services implementation
    // -------------------------------------------------------------------------
    private ReceptionImplementationI	getOwner()
    {
        return ((ReceptionImplementationI)this.owner) ;
    }
    @Override
    public void acceptMessage(MessageI m) throws Exception {
        this.getOwner().acceptMessage(m);
    }

    @Override
    public void acceptMessages(MessageI[] ms) throws Exception {
        try {
            this.getOwner().acceptMessages(ms);
        }catch (Exception e ){
            System.out.println("toto");
        }
    }
}
