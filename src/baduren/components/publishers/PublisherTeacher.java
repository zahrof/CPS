package baduren.components.publishers;

import baduren.interfaces.MessageFilterI;
import baduren.interfaces.MessageI;
import baduren.message.Message;
import baduren.message.Properties;
import fr.sorbonne_u.components.AbstractComponent;
import baduren.plugins.PublisherPublicationPlugin;
import baduren.plugins.PublisherSubscriberManagementPlugin;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

public class PublisherTeacher extends AbstractComponent {


    // -------------------------------------------------------------------------
    // Component variables and constants
    // -------------------------------------------------------------------------

    /** the URI that will be used for the plug-in (assumes a singleton).	*/
    protected String MY_PUBLISHER_PLUGIN_URI = "publisher-client-plugin-uri" ;
    protected String MY_MANAGEMENT_PLUGIN_URI = "management-client-plugin-uri" ;
    private final PublisherSubscriberManagementPlugin pluginManagement;
    private final PublisherPublicationPlugin plugin;

    // To know what senario each teacher should follow
    private int number_teacher;

    // To differentiate each students created with the empty constructor
    private static int number_of_teachers = 0;


    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------


    protected PublisherTeacher() throws Exception {
        this(1, 0, number_of_teachers++);
    }

    protected PublisherTeacher(int nbThreads, int nbSchedulableThreads, int number_teacher) throws Exception {
        super(nbThreads, nbSchedulableThreads);

        this.number_teacher = number_teacher;

        this.MY_PUBLISHER_PLUGIN_URI = this.MY_PUBLISHER_PLUGIN_URI + number_teacher;
        this.MY_MANAGEMENT_PLUGIN_URI = this.MY_MANAGEMENT_PLUGIN_URI + number_teacher;

        // Install the plug-in.
        this.plugin = new PublisherPublicationPlugin() ;
        plugin.setPluginURI(MY_PUBLISHER_PLUGIN_URI) ;
        this.installPlugin(plugin) ;

        // Install the plug-in.
        this.pluginManagement = new PublisherSubscriberManagementPlugin();
        pluginManagement.setPluginURI(MY_MANAGEMENT_PLUGIN_URI) ;
        this.installPlugin(pluginManagement) ;

        //this.tracer.setTitle(MY_PUBLISHER_PLUGIN_URI) ;
        this.tracer.setTitle("Teacher " + this.number_teacher) ;
        this.tracer.setRelativePosition(this.number_teacher, 1) ;
    }


    // -------------------------------------------------------------------------
    // Life cycle
    // -------------------------------------------------------------------------
    @Override
    public void			start() throws ComponentStartException
    {
        super.start() ;
        this.logMessage("starting teacher 2 component.") ;
    }
    @Override
    public void			execute() throws Exception
    {

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

                Message m = new Message("Bonjour, je vais tester tous les filtres. ");
                Properties p = m.getProperties();

                p.putProp("UE obligatoire", true);
                p.putProp("Première lettre de l'UE",'c');
                p.putProp("Random Double",2.00);
                p.putProp("Random Float",(float) 2.50);
                p.putProp("Random Integer", 3);
                p.putProp("Random Long",(long) 3);
                p.putProp("Random Short",(short) 3);
                p.putProp("Random String","random");

               // publish(m,"CPS");
                break;

            case 2:
                /*                          TEST SCENARIO:
                 Dans ce test on va tester toutes les méthodes de Publication CI. Nous pouvons faire
                 la vérification de la sauverade de ces messages en regardant dans la JVM du Broker,
                 nous voyons bien que Broker affiche "Message 'uri du message' sauvegardé dans le
                  sujet 'topic' au moment "+m.getTimeStamp().getTime() ); pour ces 12 messages.
                  Ces messages vont être effacé dans le Broker au fûr et à mesure. Pour plus de
                  détails voir la classe du Broker. */

                publish(new Message("La semaine prochaine nous verrons PROMELA"),"PC3R");
                publish(new Message("Je ferai cours sur TWITCH"), new String[]{"PC3R", "PAF"});
                publish(new MessageI[]{
                        new Message("Le sujet 0 sera à l'examen"),
                        new Message("Le sujet 1 sera à l'examen"),
                        new Message("Le sujet 2 sera à l'examen")
                }, "PAF");
                publish(new MessageI[]{
                        new Message("Je ferai cours sur TWITCH lundi "),
                        new Message("Je ferai cours sur TWITCH jeudi"),
                        new Message("Je ferai cours sur TWITCH vendredi")
                },  new String[]{"PC3R", "PAF"});
                break;


        }

    }

    // TOUTES LES METHODES DE PUBLICATIONSCI

    /**
     * Publish.
     *
     * @param m     the m
     * @param topic the topic
     * @throws Exception the exception
     */
    public void publish(MessageI m, String topic) throws Exception {
        logMessage("Publishing message " + m.getURI()+ " to the topic : "+ topic );
        //this.plugin.publish(m,topic);
        ((PublisherPublicationPlugin)this.getPlugin(MY_PUBLISHER_PLUGIN_URI)).publish(m,topic);
    }


    /**
     * Publish.
     *
     * @param m      the m
     * @param topics the topics
     * @throws Exception the exception
     */
    public void publish(MessageI m, String[] topics) throws Exception {
        String str= " ";
        for (String s : topics) {
            str += s+ " ";
        }
        logMessage("Publishing message " + m.getURI()+ " to the topics : "+str);
        ((PublisherPublicationPlugin)this.getPlugin(MY_PUBLISHER_PLUGIN_URI)).publish(m,topics);;
    }


    /**
     * Publish.
     *
     * @param ms     the ms
     * @param topics the topics
     * @throws Exception the exception
     */
    public void publish(MessageI[] ms, String topics) throws Exception {
        String str= " ";
        for (MessageI s : ms) {
            str += s.getURI()+ " ";
        }
        logMessage("Publishing messages " + str+ " to the topic : "+topics);
        ((PublisherPublicationPlugin)this.getPlugin(MY_PUBLISHER_PLUGIN_URI)).publish(ms,topics);
    }


    /**
     * Publish.
     *
     * @param ms     the ms
     * @param topics the topics
     * @throws Exception the exception
     */
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
     * Subscribe.
     *
     * @param topic          the topic
     * @param inboundPortURI the inbound port uri
     * @throws Exception the exception
     */
    public void subscribe(String topic, String inboundPortURI)throws Exception {
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).subscribe(topic, inboundPortURI);


    }

    /**
     * Subscribe.
     *
     * @param topics         the topics
     * @param inboundPortURI the inbound port uri
     * @throws Exception the exception
     */
    public void subscribe(String[] topics, String inboundPortURI)throws Exception {
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).subscribe(topics, inboundPortURI);

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
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).subscribe(topic,filter, inboundPortURI);

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
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).subscribe(topic, newFilter, inboundPortURI);
    }

    /**
     * Unsubscribe.
     *
     * @param topic          the topic
     * @param inboundPortUri the inbound port uri
     * @throws Exception the exception
     */
    public void unsubscribe(String topic, String inboundPortUri) throws Exception {
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).unsubscribe(topic, inboundPortUri);
    }

    /**
     * Create topic.
     *
     * @param topic the topic
     * @throws Exception the exception
     */
    public void createTopic(String topic)throws Exception {
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).createTopic(topic);

    }

    /**
     * Create topics.
     *
     * @param topics the topics
     * @throws Exception the exception
     */
    public void createTopics(String[] topics) throws Exception{
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).createTopics(topics);

    }

    /**
     * Destroy topic.
     *
     * @param topic the topic
     * @throws Exception the exception
     */
    public void destroyTopic(String topic)throws Exception {
        ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).createTopic(topic);

    }

    /**
     * Is topic boolean.
     *
     * @param topic the topic
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean isTopic(String topic) throws Exception{
        return  ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).isTopic(topic);

    }

    /**
     * Get topics string [ ].
     *
     * @return the string [ ]
     * @throws Exception the exception
     */
    public String[] getTopics() throws Exception{
        return ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).getTopics();

    }

    /**
     * Gets publication port uri.
     *
     * @return the publication port uri
     * @throws Exception the exception
     */
    public String getPublicationPortURI() throws Exception{
        return ((PublisherSubscriberManagementPlugin)this.getPlugin(MY_MANAGEMENT_PLUGIN_URI)).getPublicationPortURI();

    }

}
