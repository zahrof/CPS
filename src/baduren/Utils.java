package baduren;

import baduren.interfaces.MessageFilterI;

public class Utils {
    public static String filterToString (MessageFilterI filter){
        String filter_str = "";
        try {
            // On doit faire ça car la méthode getName() n'est pas dans l'interface donc il n'y a pas de garantie pour Java qu'elle existe
            filter_str = (String) filter.getClass().getMethod("getName").invoke(filter);
            if(filter_str == null) throw new Exception();
        } catch (Exception e){
            filter_str = filter.toString();
        }
        return filter_str;
    }
}
