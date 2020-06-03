package baduren.plugins;

import baduren.CVM;
import baduren.components.Broker.Broker;
import baduren.connectors.PublicationConnector;
import baduren.interfaces.MessageI;
import baduren.interfaces.PublicationCI;
import baduren.interfaces.PublicationImplementationI;
import baduren.ports.outboundPorts.PublicationOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;


public class PublisherPublicationPlugin extends AbstractPlugin{

    private static final long serialVersionUID = 1L;


    protected PublicationOutboundPort publicationOutboundPort;

    public void installOn(ComponentI owner) throws Exception
    {
        super.installOn(owner);
        // Add interfaces and create ports
        // Plugin du côté client donc on fait appel ) addRequiredInterface
        this.addRequiredInterface(PublicationImplementationI.class);
        this.publicationOutboundPort = new PublicationOutboundPort(this.getPluginURI(), this.owner);
        this.publicationOutboundPort.publishPort();
    }

    public void initialise() throws Exception
    {
        // Use the reflection approach to get the URI of the inbound port
        // of the Publication component.
        this.addRequiredInterface(ReflectionI.class) ;
        ReflectionOutboundPort ropPublisher = new ReflectionOutboundPort(this.owner) ;
        ropPublisher.publishPort() ;
        this.owner.doPortConnection(
                ropPublisher.getPortURI(),
                CVM.BROKER_COMPONENT_URI,
                ReflectionConnector.class.getCanonicalName()) ;

        String[] urisPublisher = ropPublisher.findPortURIsFromInterface(PublicationImplementationI.class) ;
        System.out.println("uriPublisher "+urisPublisher.toString());
        assert	urisPublisher != null && urisPublisher.length == 1 ;

        this.owner.doPortDisconnection(ropPublisher.getPortURI()) ;
        ropPublisher.unpublishPort() ;
        ropPublisher.destroyPort() ;
        this.removeRequiredInterface(ReflectionI.class) ;
        // connect the outbound port.
        this.owner.doPortConnection(
                this.publicationOutboundPort.getPortURI(),
                urisPublisher[0],
                PublicationConnector.class.getCanonicalName()) ;

        super.initialise();

    }
    public void finalise() throws Exception
    {
        this.owner.doPortDisconnection(this.publicationOutboundPort.getPortURI()) ;
    }
    public void uninstall() throws Exception
    {
        this.publicationOutboundPort.unpublishPort() ;
        this.publicationOutboundPort.destroyPort() ;
        this.removeRequiredInterface(PublicationImplementationI.class) ;
    }

    /**
     * Publish.
     *
     * @param m     the m
     * @param topic the topic
     * @throws Exception the exception
     */
    public void publish(MessageI m, String topic) throws Exception {
        try{
        this.publicationOutboundPort.publish(m, topic);
        }catch (Exception e){
            System.out.println("test");
        }
    }


    /**
     * Publish.
     *
     * @param m      the m
     * @param topics the topics
     * @throws Exception the exception
     */
    public void publish(MessageI m, String[] topics) throws Exception {
        this.publicationOutboundPort.publish(m, topics);

    }


    /**
     * Publish.
     *
     * @param ms     the ms
     * @param topics the topics
     * @throws Exception the exception
     */
    public void publish(MessageI[] ms, String topics) throws Exception {
        this.publicationOutboundPort.publish(ms, topics);

    }


    /**
     * Publish.
     *
     * @param ms     the ms
     * @param topics the topics
     * @throws Exception the exception
     */
    public void publish(MessageI[] ms, String[] topics) throws Exception {

        this.publicationOutboundPort.publish(ms, topics);

    }


}
