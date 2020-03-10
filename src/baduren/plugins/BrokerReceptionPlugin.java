package baduren.plugins;

import baduren.connectors.PublicationConnector;
import baduren.interfaces.MessageI;
import baduren.interfaces.ReceptionCI;
import baduren.ports.outboundPorts.ReceptionOutboundPort;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;

public class BrokerReceptionPlugin extends AbstractPlugin {
    private static final long serialVersionUID = 1L;


    protected ReceptionOutboundPort receptionOutboundPort;

    public void installOn(ComponentI owner) throws Exception
    {
        super.installOn(owner);
        // Add interfaces and create ports
        // Plugin du côté client donc on fait appel ) addRequiredInterface
        this.addRequiredInterface(ReceptionOutboundPort.class);
        this.receptionOutboundPort = new ReceptionOutboundPort(this.owner);
        this.receptionOutboundPort.publishPort();
    }

    public void initialise() throws Exception
    {
        // Use the reflection approach to get the URI of the inbound port
        // of the Publication component.
        this.addRequiredInterface(ReflectionI.class) ;
        ReflectionOutboundPort ropPublisher = new ReflectionOutboundPort(this.owner) ;
        ropPublisher.publishPort() ;
        String[] urisPublisher = ropPublisher.findPortURIsFromInterface(ReceptionCI.class) ;
        assert	urisPublisher != null && urisPublisher.length == 1 ;

        this.owner.doPortDisconnection(ropPublisher.getPortURI()) ;
        ropPublisher.unpublishPort() ;
        ropPublisher.destroyPort() ;
        this.removeRequiredInterface(ReflectionI.class) ;

        // connect the outbound port.
        this.owner.doPortConnection(
                this.receptionOutboundPort.getPortURI(),
                urisPublisher[0],
                PublicationConnector.class.getCanonicalName()) ;

        super.initialise();

    }
    public void finalise() throws Exception
    {
        this.owner.doPortDisconnection(this.receptionOutboundPort.getPortURI()) ;
    }
    public void unistall() throws Exception
    {
        this.receptionOutboundPort.unpublishPort() ;
        this.receptionOutboundPort.destroyPort() ;
        this.removeRequiredInterface(ReceptionCI.class) ;
    }

    // -------------------------------------------------------------------------
    // Plug-in services implementation
    // -------------------------------------------------------------------------

    public void acceptMessage(MessageI m) throws Exception {
        this.receptionOutboundPort.acceptMessage(m);
    }

    public void acceptMessages(MessageI[] ms) throws Exception {
        this.receptionOutboundPort.acceptMessages(ms);
    }
}
