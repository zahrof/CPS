package baduren.components.subscribers;

import baduren.CVM;
import baduren.interfaces.MessageFilterI;
import baduren.interfaces.MessageI;
import baduren.interfaces.ReceptionCI;
import baduren.ports.inboundPorts.ReceptionInboundPort;
import baduren.ports.inboundPortsForPlugin.ReceptionInboundPortForPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import baduren.plugins.PublisherSubscriberManagementPlugin;
import baduren.plugins.SubscriberReceptionPlugin;

public class SubscriberStudent extends	AbstractComponent implements ReceptionCI {

    // -------------------------------------------------------------------------
    // Component variables and constants
    // -------------------------------------------------------------------------

    protected String MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI = "management-subscriber-client-plugin-uri";
    protected String MY_RECEPTION_STUDENT1_SUBSCRIBER_PLUGIN_URI = "reception-subscriber-client-plugin-uri";
    protected String RECEPTION_INBOUND_PORT_URI = "student" ;
    protected String uri;
    /**	the outbound port used to call the service.							*/
    protected ReceptionInboundPortForPlugin receptionInboundPort;

    // To know what senario each student should follow
    private int number_student;

    // To differentiate each students created with the empty constructor
    private static int number_of_students = 0;


    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------


    protected SubscriberStudent(String receptionInboundPortName) throws Exception {
        this(receptionInboundPortName,1, 0, number_of_students++);
    }

    protected SubscriberStudent(String receptionInboundPortName, int nbThreads, int nbSchedulableThreads, int number_student)
            throws Exception {
        super(CVM.SUBSCRIBER_STUDENT1_COMPONENT_URI + number_student, nbThreads, nbSchedulableThreads);

        this.number_student = number_student;
        System.out.println("number_student (constructor)" + number_student);


        this.MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI = MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI + number_student;
        this.MY_RECEPTION_STUDENT1_SUBSCRIBER_PLUGIN_URI = MY_RECEPTION_STUDENT1_SUBSCRIBER_PLUGIN_URI + number_student;
        this.RECEPTION_INBOUND_PORT_URI = RECEPTION_INBOUND_PORT_URI + number_student;


        this.uri = CVM.SUBSCRIBER_STUDENT1_COMPONENT_URI + number_student;
     //   this.receptionInboundPort = new ReceptionInboundPortForPlugin(RECEPTION_INBOUND_PORT_URI,this);




        this.tracer.setTitle("Student " + this.number_student) ;
        this.tracer.setRelativePosition(this.number_student, 2) ;
    }




    // -------------------------------------------------------------------------
    // Messages filters
    // -------------------------------------------------------------------------


    public class SeraEvalueeAER1 implements MessageFilterI {

        @Override
        public boolean filter(MessageI m) throws Exception {
            // Nous devons le programmer ainsi car la condition re
            if(m.getProperties().getBooleanProp("Sera évaluée à l'examen réparti 1 ")) return  true;
            else return false;
        }

    }
    public class EnseigneParMalenfant implements MessageFilterI {

        @Override
        public boolean filter(MessageI m) throws Exception {
            boolean filtreVerifie=true;
            if(!m.getProperties().getStringProp("professeur").equals("Malenfant"))filtreVerifie=false;
           return filtreVerifie;
        }

    }

    public class TestTousLesFiltres implements MessageFilterI {

        @Override
        public boolean filter(MessageI m) throws Exception {
            boolean filtreVerifie=true;
            if( m.getProperties().getBooleanProp("UE obligatoire")!=true) filtreVerifie=false;
            if(! m.getProperties().getStringProp("Random String").equals("random")) filtreVerifie=false;
            if(m.getProperties().getCharProp("Première lettre de l'UE") != 'c') filtreVerifie=false;
            if( Double.compare(m.getProperties().getDoubleProp("Random Double"),2.00)!=0) filtreVerifie=false;
            if( Float.compare(m.getProperties().getFloatProp("Random Float"),(float) 2.50)!=0) filtreVerifie=false;
            if(m.getProperties().getIntProp("Random Integer") != 3) filtreVerifie=false;
            if(m.getProperties().getLongProp("Random Long") != (long) 3) filtreVerifie=false;
            if(m.getProperties().getShortProp("Random Short") != (short) 3) filtreVerifie=false;
            return filtreVerifie;
        }

    }


    // -------------------------------------------------------------------------
    // Life cycle
    // -------------------------------------------------------------------------

    @Override
    public void			start() throws ComponentStartException
    {
        super.start() ;
        this.logMessage("starting subscriber component.") ;
    }



    @Override
    public void			execute() throws Exception
    {
        // Install the plug-in.
        PublisherSubscriberManagementPlugin pluginManagement = new PublisherSubscriberManagementPlugin() ;
        pluginManagement.setPluginURI(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI) ;
        this.installPlugin(pluginManagement) ;

        // Install the plug-in.
        SubscriberReceptionPlugin pluginReception = new SubscriberReceptionPlugin(RECEPTION_INBOUND_PORT_URI,
                MY_RECEPTION_STUDENT1_SUBSCRIBER_PLUGIN_URI) ;
        pluginReception.setPluginURI(MY_RECEPTION_STUDENT1_SUBSCRIBER_PLUGIN_URI) ;
        this.installPlugin(pluginReception) ;

        System.out.println("number_student (execute)" + number_student);


        switch(this.number_student) {
            case 1:
                subscribe("CPS", new TestTousLesFiltres(), pluginReception.receptionInboundPortUri);
                modifyFilter("CPS", new EnseigneParMalenfant(), pluginReception.receptionInboundPortUri);
                break;
            case 2:
                subscribe("PAF",  pluginReception.receptionInboundPortUri);
                subscribe("PC3R",  pluginReception.receptionInboundPortUri);
                subscribe("CPS",  pluginReception.receptionInboundPortUri);
                break;
        }


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


