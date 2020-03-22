package baduren;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tests {
    private static final String LOG_FOLDER = "./logs/";
    private static final String BROKER_LOG_FILE = "brokerlog.log";
    private static final String SUBSCRIBER_LOG_FILE = "subscriberlog.log";
    private static final String PUBLISHER_LOG_FILE = "publisherlog.log";

    public static void main(String [] args){

        // On vérifie que le dossier qui contiendra les résulats de tests existe bien
        assert new File(LOG_FOLDER).exists();


        // On lance la CVM
        CVM.main(new String[]{});

        System.out.println("gfdgdfgdf");



        try {
            // VERIFICATION DE LA VALIDITE DES LOGS

            System.out.println("" + count_pattern_occurence("Le Coronavirus est partout", BROKER_LOG_FILE));



        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static int count_pattern_occurence(String pattern_str, String logfile) throws IOException {
        File fichier = new File(LOG_FOLDER + logfile);
        assert fichier.exists();
        assert fichier.canRead();

        String contenu_fichier = new String(Files.readAllBytes(fichier.toPath().toAbsolutePath()));

        Pattern pattern = Pattern.compile(pattern_str);
        Matcher m = pattern.matcher(contenu_fichier);
        List<String> match_list = new ArrayList<String>();

        // On récupere toutes les parties du message qui match avec le pattern
        while (m.find()) match_list.add(m.group());

        return match_list.size();
    }
}
