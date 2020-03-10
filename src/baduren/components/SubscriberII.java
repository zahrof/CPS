package baduren.components;

import baduren.interfaces.MessageFilterI;
import baduren.interfaces.MessageI;
import baduren.interfaces.ReceptionCI;
import baduren.ports.inboundPorts.ReceptionInboundPort;
import baduren.ports.outboundPorts.ManagementOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.AddPlugin;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.plugins.dconnection.DynamicConnectionServerSidePlugin;
import fr.sorbonne_u.components.ports.InboundPortI;
import plugins.PublisherManagementPlugin;
import plugins.PublisherPublicationPlugin;

public class SubscriberII extends	AbstractComponent{
    protected final static String MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI = "management-subscriber-client-plugin-uri" ;

        /**	the outbound port used to call the service.							*/
        protected ReceptionInboundPort receptionInboundPort;

    protected SubscriberII(String receptionInboundPortName,
                           String reflectionInboundPortURI) throws Exception {
        super(reflectionInboundPortURI,1, 0);
       // this.uri = uri;
        this.receptionInboundPort  = new ReceptionInboundPort(receptionInboundPortName,this);
        receptionInboundPort.publishPort();
        // Install the plug-in.
        PublisherManagementPlugin pluginManagement = new PublisherManagementPlugin() ;
        pluginManagement.setPluginURI(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI) ;
        this.installPlugin(pluginManagement) ;

        this.tracer.setTitle("subscriber2") ;
        this.tracer.setRelativePosition(1, 3) ;
    }

        protected SubscriberII(String uri, String receptionInboundPortName, String managementOutboundPortName,
                             int nbThreads, int nbSchedulableThreads) throws Exception {
            super(uri, nbThreads, nbSchedulableThreads);
            //this.uri = uri;
            this.receptionInboundPort  = new ReceptionInboundPort(receptionInboundPortName,this);
          //  this.managementOutboundPort = new ManagementOutboundPort(managementOutboundPortName, this);
            receptionInboundPort.publishPort();
            //managementOutboundPort.publishPort();

            this.tracer.setTitle("subscriber") ;
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
                //System.out.println()
                return m.getProperties().getBooleanProp("can_fly");
            }

        }

        @Override
        public void			execute() throws Exception
        {
            super.execute() ;
            subscribe("fruits", this.receptionInboundPort.getPortURI());
//		Thread.sleep(1000);
//		System.out.println("let's filterU");
            subscribe("voiture",new VehiculeAerien(),this.receptionInboundPort.getPortURI());

            subscribe("voiture", new VehiculeAerien(), this.receptionInboundPort.getPortURI());
            while(true) {}
        }



        @Override
        public void			finalise() throws Exception
        {
            super.finalise();
        }

        // TOUTES LES METHODES DE MANAGEMENTCI


        public void subscribe(String topic, String inboundPortURI) throws Exception{
            //	logMessage("Ask a subscription at port: "+inboundPortURI+" to topic " + topic);
            ((PublisherManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).subscribe(topic, inboundPortURI);

        }

        public void subscribe(String[] topics, String inboundPortURI)throws Exception {
            ((PublisherManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).subscribe(topics, inboundPortURI);


        }

        public void subscribe(String topic, MessageFilterI filter, String inboundPortURI)throws Exception {
            ((PublisherManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).subscribe(topic,filter,inboundPortURI);
            System.out.println("let's filterU");


        }

        public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI)throws Exception {
            ((PublisherManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).modifyFilter(topic,newFilter,inboundPortURI);


        }

        public void unsubscribe(String topic, String inboundPortUri)throws Exception {
            ((PublisherManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).unsubscribe(topic,inboundPortUri);


        }

        public void createTopic(String topic) throws Exception{
            ((PublisherManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).createTopic(topic);


        }

        public void createTopics(String[] topics) throws Exception{
            ((PublisherManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).createTopics(topics);


        }

        public void destroyTopic(String topic) throws Exception{
            ((PublisherManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).destroyTopic(topic);


        }

        public boolean isTopic(String topic) throws Exception {
            return ((PublisherManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).isTopic(topic);

        }

        public String[] getTopics() throws Exception{
            return ((PublisherManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).getTopics();
        }

        public String getPublicationPortURI() throws Exception {
            return ((PublisherManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).getPublicationPortURI();
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


