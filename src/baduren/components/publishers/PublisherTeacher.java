package baduren.components.publishers;

import baduren.CVM;
import baduren.TestsIntegration;
import baduren.interfaces.*;
import baduren.message.Message;
import baduren.message.Properties;
import baduren.message.TimeStamp;
import fr.sorbonne_u.components.AbstractComponent;
import baduren.plugins.PublisherPublicationPlugin;
import baduren.plugins.PublisherSubscriberManagementPlugin;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.helpers.Logger;

import java.io.File;

public class PublisherTeacher extends AbstractComponent implements ManagementImplementationI,
        SubscriptionImplementationI, PublicationImplementationI{


    // -------------------------------------------------------------------------
    // Component variables and constants
    // -------------------------------------------------------------------------

    /** the URI that will be used for the plug-in (assumes a singleton).	*/
    protected String MY_PUBLISHER_PLUGIN_URI = "publisher-client-plugin-uri" ;
    protected String MY_MANAGEMENT_PLUGIN_URI = "management-client-plugin-uri" ;
    private PublisherSubscriberManagementPlugin pluginManagement;
    private PublisherPublicationPlugin plugin;

    // To know what senario each teacher should follow
    private int number_teacher;

    // To differentiate each students created with the empty constructor
    private static int number_of_teachers = 0;
    public static int publications = 0 ;
    public static String reponseIsTopic = "";
    public static String[] allTopicsAtTheEnd;


    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------


    protected PublisherTeacher() throws Exception {
        this(1, 0, number_of_teachers++);
    }

    protected PublisherTeacher(int nbThreads, int nbSchedulableThreads, int number_teacher) throws Exception {
        super(nbThreads, nbSchedulableThreads);
        /*addRequiredInterface(PublicationCI.class);
        addRequiredInterface(ManagementCI.class);*/
        this.number_teacher = number_teacher;

        this.MY_PUBLISHER_PLUGIN_URI = this.MY_PUBLISHER_PLUGIN_URI + number_teacher;
        this.MY_MANAGEMENT_PLUGIN_URI = this.MY_MANAGEMENT_PLUGIN_URI + number_teacher;



        // Display to logs in to right position
        this.tracer.setTitle("Teacher " + this.number_teacher) ;
        if(this.number_teacher==1||this.number_teacher==2) {
            this.tracer.setRelativePosition(this.number_teacher, 1);
        }
        if(this.number_teacher==3||this.number_teacher==4) {
            this.tracer.setRelativePosition(this.number_teacher, 2);
        }
        if(! new File(TestsIntegration.LOG_FOLDER).exists()) new File(TestsIntegration.LOG_FOLDER).mkdir();
        Logger logger = new Logger(TestsIntegration.LOG_FOLDER);
        logger.toggleLogging();
        this.setLogger(logger);
    }


    // -------------------------------------------------------------------------
    // Life cycle
    // -------------------------------------------------------------------------
    @Override
    public void			start() throws ComponentStartException
    {
        super.start() ;
        this.logMessage("starting teacher  component.") ;
    }
    static int i = 0 ;
    @Override
    public void			execute() throws Exception
    {



        // Install the plug-in.

        if(this.number_teacher==1||this.number_teacher==2){
            this.plugin = new PublisherPublicationPlugin(CVM.BROKER_COMPONENT_URI);
            this.pluginManagement = new PublisherSubscriberManagementPlugin(CVM.BROKER_COMPONENT_URI);
        }
        if(this.number_teacher==3||this.number_teacher==4){
            this.plugin = new PublisherPublicationPlugin(CVM.BROKER_COMPONENT_URI2);
            this.pluginManagement = new PublisherSubscriberManagementPlugin(CVM.BROKER_COMPONENT_URI2);
        }

        // Install the plug-in.
    //    this.plugin = new PublisherPublicationPlugin() ;
        plugin.setPluginURI(MY_PUBLISHER_PLUGIN_URI) ;
        this.installPlugin(plugin) ;
        pluginManagement.setPluginURI(MY_MANAGEMENT_PLUGIN_URI) ;

        this.installPlugin(pluginManagement) ;

        switch(this.number_teacher){
            case 1:
                /*                          TEST SCENARIO:
                 Dans ce test on va tester tous les tests, le souscripteur 1
                 va s'abonner au topic "CPS" avec tous ces filtres et va bien recevoir exclusivement
                 le message "Bonjour, je vais tester tous les filtres." et non pas le message
                "Bonjour, je ne teste pas de filtre moi". Notez qu'il y a une sémantique
                d'entrelacement où le souscripteur 1 ne reçoit pas ce message. Cela est juste
                dû à que les threads d'envoi des message à envoyer le message avant que le
                souscripteur 1 ait eu le temps de se souscrire au topic CPS avec filtres */

                Thread.sleep(000);
                TimeStamp ts = new TimeStamp(System.currentTimeMillis(), "TimeStamper");
                Message m1 = new Message(""+ts.getTimeStamper()+ ts.hashCode(),ts, new Properties(),"Bonjour, je vais tester tous les filtres. ");
                Properties p1 = m1.getProperties();

                p1.putProp("UE obligatoire", true);
                p1.putProp("Première lettre de l'UE",'c');
                p1.putProp("Random Double",2.00);
                p1.putProp("Random Float",(float) 2.50);
                p1.putProp("Random Integer", 3);
                p1.putProp("Random Long",(long) 3);
                p1.putProp("Random Short",(short) 3);
                p1.putProp("Random String","random");

                    publish(m1,"CPS");
                this.publications ++;
                Message m2 = new Message("Bonjour, je vais tester le filtre de EnseigneParMalenfant ");
                Properties p2 = m2.getProperties();

                p2.putProp("professeur", "Malenfant");

                publish(m2,"CPS");
                this.publications ++;

                createTopic("CA");
                createTopics(new String[]{"ALASCA", "SRCS"});
                // Pour que le broker ait le temps de créer le sujet ALASCA avant de le détruire
                Thread.sleep(1000);
                this.allTopicsAtTheEnd = getTopics();
                destroyTopic("SRCS");
                if(isTopic("ALASCA")){
                    this.reponseIsTopic += " ALASCA est bien un topic ";
                }else{
                    // ce qui ne devrait jamais arriver
                    this.reponseIsTopic += " ALASCA est pas un topic ";
                }

                break;

            case 2:
                /*                          TEST SCENARIO:
                 Dans ce test on va tester toutes les méthodes de Publication CI. Nous pouvons faire
                 la vérification de la sauverade de ces messages en regardant dans la JVM du Broker,
                 nous voyons bien que Broker affiche "Message 'uri du message' sauvegardé dans le
                  sujet 'topic' au moment "+m.getTimeStamp().getTime() ); pour ces 12 messages.
                  Ces messages vont être effacé dans le Broker au fûr et à mesure. Pour plus de
                  détails voir la classe du Broker. */
                Thread.sleep(6000);
                publish(new Message("La semaine prochaine nous verrons PROMELA"),"PC3R");
                this.publications++;
                publish(new Message("Je ferai cours sur TWITCH"), new String[]{"PC3R", "PAF"});
                this.publications++;
                this.publications++;
                publish(new MessageI[]{
                        new Message("Le sujet 0 sera à l'examen"),
                        new Message("Le sujet 1 sera à l'examen"),
                        new Message("Le sujet 2 sera à l'examen")
                }, "PAF");
                this.publications++;
                this.publications++;
                this.publications++;
                publish(new MessageI[]{
                        new Message("Je ferai cours sur TWITCH lundi "),
                        new Message("Je ferai cours sur TWITCH jeudi"),
                        new Message("Je ferai cours sur TWITCH vendredi")
                },  new String[]{"PC3R", "PAF"});
                this.publications= this.publications+6;

                for(int i=0; i <50; i++){

                    publish (new Message("Le Coronavirus est partout "+i), "CPS");
                    this.publications ++;
                }Thread.sleep(1000);
                for(int i=50; i <100; i++){

                    publish (new Message("Le Coronavirus est partout "+i), "CPS");
                    this.publications ++;
                }
                break;

            case 3:
                Thread.sleep(6000);
                this.logMessage("il pleut");
                publish (new Message("La vie est un long voyage "), "APS");
                this.publications++;
                break;

            case 4:
                Thread.sleep(6000);
                this.logMessage("en revoir");
                publish(new Message("Prenez vos crayons! ") , new String[]{"PC3R", "CPA"});
                this.publications++;
                this.publications++;
                break;


        }

    }
    @Override
    public void			finalise() throws Exception
    {
        this.logMessage("stopping publisher teacher component.") ;
        this.printExecutionLogOnFile(TestsIntegration.LOG_FOLDER + TestsIntegration.PUBLISHER_LOG_FILE + this.number_teacher);
        super.finalise();
    }

    @Override
    public void			shutdown() throws ComponentShutdownException
    {
        super.shutdown();
    }
    /**
     * @see fr.sorbonne_u.components.AbstractComponent#shutdownNow()
     */
    @Override
    public void			shutdownNow() throws ComponentShutdownException
    {
        super.shutdownNow();
    }

    // ALL THE METHODS OF PUBLICATIONSCI

    /**
     * Method to publish 1 message in 1 topic
     *
     * @param m     the message
     * @param topic the topic
     * @throws Exception the exception
     */
    @Override
    public void publish(MessageI m, String topic) throws Exception {
        logMessage("Publishing message " + m.getMessage()+ " to the topic : "+ topic );
        //this.plugin.publish(m,topic);
        try {
            ((PublisherPublicationPlugin) this.getPlugin(MY_PUBLISHER_PLUGIN_URI)).publish(m, topic);
        }catch (Exception e){
            System.out.println("tata");
        }
    }


    /**
     * Method to publish 1 message in several topics
     *
     * @param m      the message
     * @param topics the topics
     * @throws Exception the exception
     */
    @Override
    public void publish(MessageI m, String[] topics) throws Exception {
        String str= " ";
        for (String s : topics) {
            str += s+ " ";
        }
        logMessage("Publishing message " + m.getMessage()+ " to the topics : "+str);
        ((PublisherPublicationPlugin)this.getPlugin(MY_PUBLISHER_PLUGIN_URI)).publish(m,topics);;
    }


    /**
     * Method to publish several messages in 1 topic
     *
     * @param ms     the messages
     * @param topics the topics
     * @throws Exception the exception
     */
    @Override
    public void publish(MessageI[] ms, String topics) throws Exception {
        String str= "\n";
        for (MessageI s : ms) {
            str += " - " + s.getURI()+ "\n";
        }
        logMessage("Publishing messages " + str+ " to the topic : "+topics);
        ((PublisherPublicationPlugin)this.getPlugin(MY_PUBLISHER_PLUGIN_URI)).publish(ms,topics);
    }


    /**
     * Method to publish several messages in several topics
     *
     * @param ms     the messages
     * @param topics the topics
     * @throws Exception the exception
     */
    @Override
    public void publish(MessageI[] ms, String[] topics) throws Exception {
        String str= " ";
        for (MessageI s : ms) {
            str += s.getURI()+ " ";
        }
        String str2= " ";
        for (String s : topics) {
            str2 += s+ " ";
        }
        logMessage("Publishing messages " + str+ " to the topics : "+str2);
        ((PublisherPublicationPlugin)this.getPlugin(MY_PUBLISHER_PLUGIN_URI)).publish(ms,topics);
    }

    // TOUTES LES METHODES DE MANAGEMENTCI


    /**
     * Method to subscribe to one topic
     *
     * @param topic          the topic
     * @param inboundPortURI the inbound port uri
     * @throws Exception the exception
     */
    @Override
    public void subscribe(String topic, String inboundPortURI)throws Exception {
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).subscribe(topic, inboundPortURI);


    }

    /**
     * Method to subscribe to several topics
     *
     * @param topics         the topics
     * @param inboundPortURI the inbound port uri
     * @throws Exception the exception
     */
    @Override
    public void subscribe(String[] topics, String inboundPortURI)throws Exception {
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).subscribe(topics, inboundPortURI);

    }

    /**
     * Method to subscribe to one topic with a filter
     *
     * @param topic          the topic
     * @param filter         the filter
     * @param inboundPortURI the inbound port uri
     * @throws Exception the exception
     */
    @Override
    public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception{
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).subscribe(topic,filter, inboundPortURI);

    }

    /**
     * Method to modify a filter
     *
     * @param topic          the topic
     * @param newFilter      the new filter
     * @param inboundPortURI the inbound port uri
     * @throws Exception the exception
     */
    @Override
    public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception{
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).subscribe(topic, newFilter, inboundPortURI);
    }

    /**
     * Method to unsubscribe
     *
     * @param topic          the topic
     * @param inboundPortUri the inbound port uri
     * @throws Exception the exception
     */
    @Override
    public void unsubscribe(String topic, String inboundPortUri) throws Exception {
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).unsubscribe(topic, inboundPortUri);
    }

    /**
     * Method to create a topic.
     *
     * @param topic the topic
     * @throws Exception the exception
     */
    @Override
    public void createTopic(String topic)throws Exception {
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).createTopic(topic);

    }

    /**
     * Method to create several topics.
     *
     * @param topics the topics
     * @throws Exception the exception
     */
    @Override
    public void createTopics(String[] topics) throws Exception{
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).createTopics(topics);

    }

    /**
     * Method to destroy a topic.
     *
     * @param topic the topic
     * @throws Exception the exception
     */
    @Override
    public void destroyTopic(String topic)throws Exception {
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).destroyTopic(topic);

    }

    /**
     * Is topic boolean.
     *
     * @param topic the topic
     * @return the boolean
     * @throws Exception the exception
     */
    @Override
    public boolean isTopic(String topic) throws Exception{
        return  ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).isTopic(topic);

    }

    /**
     * Get topics string [ ].
     *
     * @return the string [ ]
     * @throws Exception the exception
     */
    @Override
    public String[] getTopics() throws Exception{
        return ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).getTopics();

    }

    /**
     * Gets publication port uri.
     *
     * @return the publication port uri
     * @throws Exception the exception
     */
    @Override
    public String getPublicationPortURI() throws Exception{
        return ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).getPublicationPortURI();

    }

}
