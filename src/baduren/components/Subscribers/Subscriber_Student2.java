package baduren.components.Subscribers;

import baduren.CVM;
import baduren.interfaces.MessageFilterI;
import baduren.interfaces.MessageI;
import baduren.interfaces.ReceptionCI;
import baduren.plugins.PublisherSubscriberManagementPlugin;
import baduren.plugins.SubscriberReceptionPlugin;
import baduren.ports.inboundPorts.ReceptionInboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

public class Subscriber_Student2 extends	AbstractComponent implements ReceptionCI {
    protected final static String MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI = "management-subscriber2-client-plugin-uri";
    protected final static String MY_RECEPTION_STUDENT1_SUBSCRIBER_PLUGIN_URI = "reception-subscriber2-client-plugin-uri";
    protected final static String RECEPTION_INBOUND_PORT_URI = "student2" ;
    protected String uri;
        /**	the outbound port used to call the service.							*/
        protected ReceptionInboundPort receptionInboundPort;

    protected Subscriber_Student2(String receptionInboundPortName) throws Exception {
        super(CVM.SUBSCRIBER_STUDENT2_COMPONENT_URI,1, 0);
        this.uri = CVM.SUBSCRIBER_STUDENT1_COMPONENT_URI;
        this.receptionInboundPort = new ReceptionInboundPort(RECEPTION_INBOUND_PORT_URI,this);

        // Install the plug-in.
        PublisherSubscriberManagementPlugin pluginManagement = new PublisherSubscriberManagementPlugin() ;
        pluginManagement.setPluginURI(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI) ;
        this.installPlugin(pluginManagement) ;

        // Install the plug-in.
        SubscriberReceptionPlugin pluginReception = new SubscriberReceptionPlugin(RECEPTION_INBOUND_PORT_URI,
                MY_RECEPTION_STUDENT1_SUBSCRIBER_PLUGIN_URI) ;
        pluginReception.setPluginURI(MY_RECEPTION_STUDENT1_SUBSCRIBER_PLUGIN_URI) ;
        this.installPlugin(pluginReception) ;


        this.tracer.setTitle("Student 2") ;
        this.tracer.setRelativePosition(1, 4) ;
    }

        protected Subscriber_Student2(String receptionInboundPortName, int nbThreads, int nbSchedulableThreads)
                throws Exception {
            super(CVM.SUBSCRIBER_STUDENT1_COMPONENT_URI,nbThreads, nbSchedulableThreads);
            this.uri = CVM.SUBSCRIBER_STUDENT1_COMPONENT_URI;
            this.receptionInboundPort = new ReceptionInboundPort(RECEPTION_INBOUND_PORT_URI,this);

            // Install the plug-in.
            PublisherSubscriberManagementPlugin pluginManagement = new PublisherSubscriberManagementPlugin() ;
            pluginManagement.setPluginURI(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI) ;
            this.installPlugin(pluginManagement) ;

            // Install the plug-in.
            SubscriberReceptionPlugin pluginReception = new SubscriberReceptionPlugin(RECEPTION_INBOUND_PORT_URI,
                    MY_RECEPTION_STUDENT1_SUBSCRIBER_PLUGIN_URI) ;
            pluginReception.setPluginURI(MY_RECEPTION_STUDENT1_SUBSCRIBER_PLUGIN_URI) ;
            this.installPlugin(pluginReception) ;


            this.tracer.setTitle("Student 2") ;
            this.tracer.setRelativePosition(1, 3) ;
        }

        @Override
        public void			start() throws ComponentStartException
        {
            super.start() ;
            this.logMessage("starting subscriber component.") ;
        }

        public class VehiculeAerien implements MessageFilterI {

            @Override
            public boolean filter(MessageI m) throws Exception {
                return m.getProperties().getBooleanProp("can_fly");
            }

        }

        @Override
        public void			execute() throws Exception
        {
           // super.execute() ;
            subscribe("APS",this.receptionInboundPort.getPortURI());
        }



        @Override
        public void			finalise() throws Exception
        {
            super.finalise();
        }

        // TOUTES LES METHODES DE MANAGEMENTCI


        public void subscribe(String topic, String inboundPortURI) throws Exception{
            //	logMessage("Ask a subscription at port: "+inboundPortURI+" to topic " + topic);
            ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).subscribe(topic, inboundPortURI);

        }

        public void subscribe(String[] topics, String inboundPortURI)throws Exception {
            ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).subscribe(topics, inboundPortURI);


        }

        public void subscribe(String topic, MessageFilterI filter, String inboundPortURI)throws Exception {
            ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).subscribe(topic,filter,inboundPortURI);
            System.out.println("let's filterU");


        }

        public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI)throws Exception {
            ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).modifyFilter(topic,newFilter,inboundPortURI);


        }

        public void unsubscribe(String topic, String inboundPortUri)throws Exception {
            ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).unsubscribe(topic,inboundPortUri);


        }

        public void createTopic(String topic) throws Exception{
            ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).createTopic(topic);


        }

        public void createTopics(String[] topics) throws Exception{
            ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).createTopics(topics);


        }

        public void destroyTopic(String topic) throws Exception{
            ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).destroyTopic(topic);


        }

        public boolean isTopic(String topic) throws Exception {
            return ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).isTopic(topic);

        }

        public String[] getTopics() throws Exception{
            return ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).getTopics();
        }

        public String getPublicationPortURI() throws Exception {
            return ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).getPublicationPortURI();
        }

        // TOUTES LES METHODES DE RECEPTIONCI

        public void acceptMessage(MessageI m) {
            this.logMessage("Receiving/accepting the message "+m.getURI()+ " send by : "+ m.getTimeStamp().getTimeStamper() +
                    " a la date de "+ m.getTimeStamp().getTime());
        }
        public void acceptMessages(MessageI[] ms) {
            for (MessageI m : ms) {
                acceptMessage(m);
            }
        }
    }


