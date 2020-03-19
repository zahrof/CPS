package baduren.components.Publishers;

import baduren.interfaces.MessageFilterI;
import baduren.interfaces.MessageI;
import baduren.message.Message;
import baduren.message.Properties;
import fr.sorbonne_u.components.AbstractComponent;
import baduren.plugins.PublisherPublicationPlugin;
import baduren.plugins.PublisherSubscriberManagementPlugin;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

public class Publisher_Teacher extends AbstractComponent {


    // -------------------------------------------------------------------------
    // Component variables and constants
    // -------------------------------------------------------------------------

    /** the URI that will be used for the plug-in (assumes a singleton).	*/
    protected final static String MY_PUBLISHER_PLUGIN_URI = "publisher-client-plugin-uri" ;
    protected final static String MY_MANAGEMENT_PLUGIN_URI = "management-client-plugin-uri" ;
    private final PublisherSubscriberManagementPlugin pluginManagement;
    private final PublisherPublicationPlugin plugin;

    private int number_teacher; // To know what senario each teacher should follow


    // -------------------------------------------------------------------------
    // Utility method for empty constructor
    // -------------------------------------------------------------------------

    private static int number_of_teachers = 0;

    private static Integer[] get_a_teacher_number(){
        Integer args[] = {number_of_teachers++};
        return args;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------




    protected Publisher_Teacher(int nbThreads, int nbSchedulableThreads, int number_teacher) throws Exception {
        super(nbThreads, nbSchedulableThreads);

        this.number_teacher = number_teacher;

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
        this.tracer.setRelativePosition(1, this.number_teacher + 1) ;
    }

    protected Publisher_Teacher() throws Exception {
        this(1, 0, 1);
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
                try {
                    // Test scenario
                    Thread.sleep(200);

                    for (int i=0; i <5; i++) {
                        publish(new Message("Je vais pas faire cours de APS demain"+ i),"APS");
                    }
                    for (int i=0; i <5; i++) {
                        Message m = new Message("Le TD aura lieu le jour "+i+" ");
                        Properties p = m.getProperties();
                        p.putProp("Prévu à l'examen", true);
                        publish(m,"CPS");
                    }
                    publish(new Message("L'équipe baduren a bien travaillé!"),"CPS");

                    for (int i=0; i <5; i++) {
                        Message m = new Message("Le Sujet "+i+" est important ");
                        Properties p = m.getProperties();
                        p.putProp("Prévu à l'examen", true);
                        publish(m,"CPS");
                    }

                    for (int i=0; i <5; i++) {
                        Message m = new Message("Je ferai un appel vidéo pour faire le CM le jour "+i);
                        Properties p = m.getProperties();
                        p.putProp("professeur", "Malenfant");
                        publish(m,"CPS");
                    }

                } catch(Throwable t) {
                    t.printStackTrace();
                }
                break;

            case 2:
                // Test scenario
                for (int i=0; i <7; i++) {
                    publish(new Message("CPA Le sujet "+i+" n'est pas prévu à l'examen"), "CPA");
                }
                publish(new Message("CPS Le projet est à faire en binôme"),"CPS");
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
