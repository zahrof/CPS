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

/**
 * The type Subscriber student.
 */
public class SubscriberStudent extends	AbstractComponent implements ReceptionCI {

    // -------------------------------------------------------------------------
    // Component variables and constants
    // -------------------------------------------------------------------------

    /**
     * The My management subscriber plugin uri.
     */
    protected String MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI = "management-subscriber-client-plugin-uri";
    /**
     * The My reception student 1 subscriber plugin uri.
     */
    protected String MY_RECEPTION_STUDENT1_SUBSCRIBER_PLUGIN_URI = "reception-subscriber-client-plugin-uri";
    /**
     * The Reception inbound port uri.
     */
    protected String RECEPTION_INBOUND_PORT_URI = "student" ;
    /**
     * The Uri.
     */
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


    /**
     * Instantiates a new Subscriber student.
     *
     * @param receptionInboundPortName the reception inbound port name
     * @throws Exception the exception
     */
    protected SubscriberStudent(String receptionInboundPortName) throws Exception {
        this(receptionInboundPortName,1, 0, number_of_students++);
    }

    /**
     * Instantiates a new Subscriber student.
     *
     * @param receptionInboundPortName the reception inbound port name
     * @param nbThreads                the nb threads
     * @param nbSchedulableThreads     the nb schedulable threads
     * @param number_student           the number student
     * @throws Exception the exception
     */
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



        // Display to logs in to right position
        this.tracer.setTitle("Student " + this.number_student) ;
        this.tracer.setRelativePosition(this.number_student, 2) ;
    }




    // -------------------------------------------------------------------------
    // Messages filters
    // -------------------------------------------------------------------------


    /**
     * The type Sera evaluee aer 1.
     */
    public class SeraEvalueeAER1 implements MessageFilterI {

        @Override
        public boolean filter(MessageI m) throws Exception {
            // Nous devons le programmer ainsi car la condition re
            if(m.getProperties().getBooleanProp("Sera évaluée à l'examen réparti 1 ")) return  true;
            else return false;
        }

    }

    /**
     * The type Enseigne par malenfant.
     */
    public class EnseigneParMalenfant implements MessageFilterI {

        @Override
        public boolean filter(MessageI m) throws Exception {
            boolean filtreVerifie=true;
            if(!m.getProperties().getStringProp("professeur").equals("Malenfant"))filtreVerifie=false;
           return filtreVerifie;
        }

    }

    /**
     * The type Test tous les filtres.
     */
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
                /*                          TEST SCENARIO:

                 */
                subscribe("CPS", new TestTousLesFiltres(), pluginReception.receptionInboundPortUri);
                modifyFilter("CPS", new EnseigneParMalenfant(), pluginReception.receptionInboundPortUri);
                break;
            case 2:
                /*                          TEST SCENARIO:

                 */
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


    /**
     * Subscribe.
     *
     * @param topic          the topic
     * @param inboundPortURI the inbound port uri
     * @throws Exception the exception
     */
    public void subscribe(String topic, String inboundPortURI) throws Exception{
        logMessage("Ask a subscription at port: "+inboundPortURI+" to topic " + topic);
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).subscribe(topic, inboundPortURI);

    }

    /**
     * Subscribe.
     *
     * @param topics         the topics
     * @param inboundPortURI the inbound port uri
     * @throws Exception the exception
     */
    public void subscribe(String[] topics, String inboundPortURI)throws Exception {
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).subscribe(topics, inboundPortURI);
    }

    /**
     * Subscribe.
     *
     * @param topic          the topic
     * @param filter         the filter
     * @param inboundPortURI the inbound port uri
     * @throws Exception the exception
     */
    public void subscribe(String topic, MessageFilterI filter, String inboundPortURI)throws Exception {
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).subscribe(topic,filter,inboundPortURI);
    }

    /**
     * Method to modify a filter.
     *
     * @param topic          the topic
     * @param newFilter      the new filter
     * @param inboundPortURI the inbound port uri
     * @throws Exception the exception
     */
    public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI)throws Exception {
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).modifyFilter(topic,newFilter,inboundPortURI);


    }

    /**
     * Unsubscribe.
     *
     * @param topic          the topic
     * @param inboundPortUri the inbound port uri
     * @throws Exception the exception
     */
    public void unsubscribe(String topic, String inboundPortUri)throws Exception {
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).unsubscribe(topic,inboundPortUri);


    }

    /**
     * Create topic.
     *
     * @param topic the topic
     * @throws Exception the exception
     */
    public void createTopic(String topic) throws Exception{
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).createTopic(topic);


    }

    /**
     * Create topics.
     *
     * @param topics the topics
     * @throws Exception the exception
     */
    public void createTopics(String[] topics) throws Exception{
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).createTopics(topics);
    }

    /**
     * Destroy topic.
     *
     * @param topic the topic
     * @throws Exception the exception
     */
    public void destroyTopic(String topic) throws Exception{
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).destroyTopic(topic);


    }

    /**
     * Is topic boolean.
     *
     * @param topic the topic
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean isTopic(String topic) throws Exception {
        return ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).isTopic(topic);

    }

    /**
     * Get topics string [ ].
     *
     * @return the string [ ]
     * @throws Exception the exception
     */
    public String[] getTopics() throws Exception{
        return ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_SUBSCRIBER_PLUGIN_URI)).getTopics();
    }

    /**
     * Gets publication port uri.
     *
     * @return the publication port uri
     * @throws Exception the exception
     */
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


