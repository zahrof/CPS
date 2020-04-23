package baduren;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestsIntegration {
    public static final String LOG_FOLDER = "logs/";
    public static final String BROKER_LOG_FILE = "brokerlog";
    public static final String SUBSCRIBER_LOG_FILE = "subscriberlog";
    public static final String PUBLISHER_LOG_FILE = "publisherlog";

    public static void main(String [] args){

        // On vérifie que le dossier qui contiendra les résulats de tests existe bien
        if(! new File(LOG_FOLDER).exists()) new File(LOG_FOLDER).mkdir();
        assert new File(LOG_FOLDER).exists();


        // On lance la CVM
        CVM.main(new String[]{});


        // VERIFICATION DE LA VALIDITE DES LOGS

        System.out.print("\n[ ");

        // Test pour le Teacher 1
        verify(1, "Publishing message Bonjour, je vais tester tous les filtres.  to the topic : CPS", PUBLISHER_LOG_FILE + 1);
        verify(1, "Publishing message Bonjour, je vais tester le filtre de EnseigneParMalenfant  to the topic : CPS", PUBLISHER_LOG_FILE + 1);

        // Test pour le Teacher 2
        verify(1, "Publishing message La semaine prochaine nous verrons PROMELA to the topic : PC3R", PUBLISHER_LOG_FILE + 2);
        verify(1, "Publishing message Je ferai cours sur TWITCH to the topics :  PC3R PAF ", PUBLISHER_LOG_FILE + 2);
        verify(1, "Le sujet 0 sera à l'examen", PUBLISHER_LOG_FILE + 2);
        verify(1, "Le sujet 1 sera à l'examen", PUBLISHER_LOG_FILE + 2);
        verify(1, "Le sujet 2 sera à l'examen", PUBLISHER_LOG_FILE + 2);
        verify(100, "Publishing message Le Coronavirus est partout", PUBLISHER_LOG_FILE + 2);


        // Test pour le broker
        verify(1, "Subscribed student2 to topic PAF with no filter", BROKER_LOG_FILE );
        verify(1, "Subscribed student2 to topic APS with no filter", BROKER_LOG_FILE );
        verify(1, "Subscribed student1 to topic CPS with filterbaduren.components.subscribers.SubscriberStudent$TestTousLesFiltres", BROKER_LOG_FILE );
        verify(1, "Subscribed student2 to topic PC3R with no filter", BROKER_LOG_FILE);
        verify(1, "Subscribed student2 to topic CPS with no filter", BROKER_LOG_FILE);
        verify(1, "Message Bonjour, je vais tester tous les filtres.  stocked to topic CPS at the moment", BROKER_LOG_FILE);
        verify(1, "Message La semaine prochaine nous verrons PROMELA stocked to topic PC3R at the moment", BROKER_LOG_FILE);
        verify(1, "Suppression des messages Bonjour, je vais tester tous les filtres.  du topic CPS", BROKER_LOG_FILE);
        verify(1, "Suppression des messages La semaine prochaine nous verrons PROMELA du topic PC3R", BROKER_LOG_FILE);
        verify(1, "Message Bonjour, je vais tester le filtre de EnseigneParMalenfant  stocked to topic CPS at the moment", BROKER_LOG_FILE);
        verify(1, "Message Je ferai cours sur TWITCH stocked to topic PC3R at the moment", BROKER_LOG_FILE);
        verify(1, "Message Je ferai cours sur TWITCH stocked to topic PAF at the moment", BROKER_LOG_FILE);
        verify(1, "Suppression des messages Je ferai cours sur TWITCH du topic PAF", BROKER_LOG_FILE);
        verify(1, "Suppression des messages Bonjour, je vais tester le filtre de EnseigneParMalenfant  du topic CPS", BROKER_LOG_FILE);
        verify(1, "Creation of topic CA", BROKER_LOG_FILE);
        verify(1, "Suppression des messages Je ferai cours sur TWITCH du topic PC3R", BROKER_LOG_FILE);
        verify(3, "sera à l'examen stocked to topic PAF at the moment", BROKER_LOG_FILE);
        verify(24, "Je ferai cours sur TWITCH", BROKER_LOG_FILE);
        verify(100, "Message Le Coronavirus est partout", BROKER_LOG_FILE);
        verify(50, "Envoi du message Le Coronavirus est partout", BROKER_LOG_FILE);
        verify(3, "Suppression des messages Le sujet ", BROKER_LOG_FILE);
        verify(8, "Suppression des messages Je ferai cours sur TWITCH", BROKER_LOG_FILE);
        verify(8, "Envoi du message Je ferai cours sur TWITCH", BROKER_LOG_FILE);
        verify(100, "Suppression des messages Le Coronavirus est partout", BROKER_LOG_FILE);
        verify(1, "On enlève l'abonnement de student2 au topic CPS", BROKER_LOG_FILE);
        verify(1, "Destruction of the topic SRCS", BROKER_LOG_FILE);


        // Tests pour le Student 1
        verify(1, "Receiving/accepting the message Bravo tu viens de te souscrire au topic CPS avec le filtre TestTousLesFiltres", SUBSCRIBER_LOG_FILE + 1);
        verify(1, "Receiving/accepting the message Bonjour, je vais tester tous les filtres.", SUBSCRIBER_LOG_FILE + 1);

        // Tests pour le Student 2
        verify(1, "Ask a subscription at port: student2 to topic PAF", SUBSCRIBER_LOG_FILE + 2);
        verify(1, "Receiving/accepting the message Bravo tu viens de te souscrire au topic PAF sans filtres", SUBSCRIBER_LOG_FILE + 2);
        verify(1, "Receiving/accepting the message Bravo tu viens de te souscrire au topic APS sans filtres", SUBSCRIBER_LOG_FILE + 2);
        verify(1, "Receiving/accepting the message Bravo tu viens de te souscrire au topic PC3R sans filtres", SUBSCRIBER_LOG_FILE + 2);
        verify(1, "Receiving/accepting the message Bravo tu viens de te souscrire au topic CPS sans filtres", SUBSCRIBER_LOG_FILE + 2);
        verify(1, "Receiving/accepting the message Bonjour, je vais tester tous les filtres", SUBSCRIBER_LOG_FILE + 2);
        verify(1, "Receiving/accepting the message La semaine prochaine nous verrons PROMELA", SUBSCRIBER_LOG_FILE + 2);
        verify(1, "Receiving/accepting the message Bonjour, je vais tester le filtre de EnseigneParMalenfant ", SUBSCRIBER_LOG_FILE + 2);
        verify(8, "Receiving/accepting the message Je ferai cours sur TWITCH ", SUBSCRIBER_LOG_FILE + 2);
        verify(3, "Receiving/accepting the message Le sujet", SUBSCRIBER_LOG_FILE + 2);
        verify(1, "Receiving/accepting the message Tu viens de te desabonner au topic CPS", SUBSCRIBER_LOG_FILE + 2);

        System.out.print(" ]\n\n");


        System.out.println("=================================================");
        System.out.println(" Le test d'intégration a été passé avec succès ! ");
        System.out.println("=================================================");


    }

    private static void verify(int occurence, String str, String log_file){

        try {
            int real_occurence = count_pattern_occurence(str, log_file);
            if(real_occurence == occurence){
                System.out.print("#");
            } else {
                System.out.println(
                        "\n\nErreur ! " +
                        "\n- fichier affecté : " + log_file + ".log" +
                        "\n- log affecté :     " + str +
                        "\n- valeur attendue : " + occurence +
                        "\n- valeur obtenue :  " + real_occurence
                );
                System.exit(0) ;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static int count_pattern_occurence(String pattern_str, String logfile) throws IOException {
        File fichier = new File(LOG_FOLDER + logfile + ".log");
        assert fichier.exists();
        assert fichier.canRead();

        //System.out.println("chemin : " + fichier.toPath().toAbsolutePath());
        String contenu_fichier = new String(Files.readAllBytes(fichier.toPath().toAbsolutePath()));

        int lastIndex = 0;
        int count = 0;

        while(lastIndex != -1){

            lastIndex = contenu_fichier.indexOf(pattern_str,lastIndex);

            if(lastIndex != -1){
                count ++;
                lastIndex += pattern_str.length();
            }
        }

        return count;
    }
}
