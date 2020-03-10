package plugins;

import baduren.CVM;
import baduren.connectors.ManagementConnector;
import baduren.connectors.PublicationConnector;
import baduren.interfaces.ManagementCI;
import baduren.interfaces.MessageFilterI;
import baduren.interfaces.PublicationCI;
import baduren.ports.outboundPorts.ManagementOutboundPort;
import baduren.ports.outboundPorts.PublicationOutboundPort;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.PluginI;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;

public class PublisherSubscriberManagementPlugin extends AbstractPlugin implements PluginI {


    private static final long serialVersionUID = 1L;

/** the inbound port which calls will be on this plug-in.*/
    protected ManagementOutboundPort managementOutboundPort;

    public void installOn(ComponentI owner) throws Exception
    {
        super.installOn(owner);
       // assert	owner instanceof MapImplementationI ;
        // Plugin du côté client donc on fait appel ) addRequiredInterface
        this.addRequiredInterface(ManagementCI.class);
        this.managementOutboundPort = new ManagementOutboundPort(this.owner);
        this.managementOutboundPort.publishPort();
    }

    @Override
    public void			uninstall() throws Exception
    {
        this.managementOutboundPort.unpublishPort() ;
        this.managementOutboundPort.destroyPort() ;
        this.removeOfferedInterface(PublicationCI.class) ;
    }


    public void initialise() throws Exception
    {
        // Use the reflection approach to get the URI of the inbound port
        // of the Publication component.
        this.addRequiredInterface(ReflectionI.class) ;
        ReflectionOutboundPort rop = new ReflectionOutboundPort(this.owner) ;
        rop.publishPort() ;

        this.owner.doPortConnection(
                rop.getPortURI(),
                CVM.BROKER_COMPONENT_URI,
                ReflectionConnector.class.getCanonicalName()) ;

        String[] uris = rop.findPortURIsFromInterface(ManagementCI.class) ;
        assert	uris != null && uris.length == 1 ;

        this.owner.doPortDisconnection(rop.getPortURI()) ;
        rop.unpublishPort() ;
        rop.destroyPort() ;
        this.removeRequiredInterface(ReflectionI.class) ;

        // connect the outbound port.
        this.owner.doPortConnection(
                this.managementOutboundPort.getPortURI(),
                uris[0],
                ManagementConnector.class.getCanonicalName()) ;

        super.initialise();

    }

    public void finalise() throws Exception
    {
        this.owner.doPortDisconnection(this.managementOutboundPort.getPortURI()) ;
    }

    // TOUTES LES METHODES DE MANAGEMENTCI


    /**
     * Subscribe.
     *
     * @param topic          the topic
     * @param inboundPortURI the inbound port uri
     * @throws Exception the exception
     */
    public void subscribe(String topic, String inboundPortURI)throws Exception {
        this.managementOutboundPort.subscribe(topic, inboundPortURI);

    }

    /**
     * Subscribe.
     *
     * @param topics         the topics
     * @param inboundPortURI the inbound port uri
     * @throws Exception the exception
     */
    public void subscribe(String[] topics, String inboundPortURI)throws Exception {
        this.managementOutboundPort.subscribe(topics, inboundPortURI);

    }

    /**
     * Subscribe.
     *
     * @param topic          the topic
     * @param filter         the filter
     * @param inboundPortURI the inbound port uri
     * @throws Exception the exception
     */
    public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception{
        this.managementOutboundPort.subscribe(topic,filter, inboundPortURI);

    }

    /**
     * Modify filter.
     *
     * @param topic          the topic
     * @param newFilter      the new filter
     * @param inboundPortURI the inbound port uri
     * @throws Exception the exception
     */
    public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception{
        this.managementOutboundPort.subscribe(topic, newFilter, inboundPortURI);

    }

    /**
     * Unsubscribe.
     *
     * @param topic          the topic
     * @param inboundPortUri the inbound port uri
     * @throws Exception the exception
     */
    public void unsubscribe(String topic, String inboundPortUri) throws Exception {
        this.managementOutboundPort.unsubscribe(topic, inboundPortUri);
    }

    /**
     * Create topic.
     *
     * @param topic the topic
     * @throws Exception the exception
     */
    public void createTopic(String topic)throws Exception {
        this.managementOutboundPort.createTopic(topic);
    }

    /**
     * Create topics.
     *
     * @param topics the topics
     * @throws Exception the exception
     */
    public void createTopics(String[] topics) throws Exception{
        this.managementOutboundPort.createTopics(topics);
    }

    /**
     * Destroy topic.
     *
     * @param topic the topic
     * @throws Exception the exception
     */
    public void destroyTopic(String topic)throws Exception {
        this.managementOutboundPort.destroyTopic(topic);
    }

    /**
     * Is topic boolean.
     *
     * @param topic the topic
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean isTopic(String topic) throws Exception{
        return this.managementOutboundPort.isTopic(topic);
    }

    /**
     * Get topics string [ ].
     *
     * @return the string [ ]
     * @throws Exception the exception
     */
    public String[] getTopics() throws Exception{
        return this.managementOutboundPort.getTopics();
    }

    /**
     * Gets publication port uri.
     *
     * @return the publication port uri
     * @throws Exception the exception
     */
    public String getPublicationPortURI() throws Exception{
        return this.managementOutboundPort.getPublicationPortURI();
    }


}
