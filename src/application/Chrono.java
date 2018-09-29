package application;

public class Chrono {

    /** Declaration des parametres de debut, fin et duree du temps de jeu     */
    private long duree=0;
    private long debutTemps=0;
    private long finTemps=0;


    /** Activation du chronometre */
    public void start()
        {
        debutTemps=System.currentTimeMillis();
        finTemps=0;
        duree=0;
        }

    /** Arret du chronometre */
    public void stop()
        {
        if(debutTemps==0) {return;}
        finTemps=System.currentTimeMillis();
        duree=(finTemps-debutTemps) ;
        debutTemps=0;
        finTemps=0;

        }


    /** Retourne la duree */
     public String getDureeTxt()
        {
        return tempsHMS(duree/1000);
        }



    public static String tempsHMS(long tempsS) {

        /** Donne le temps au format texte : "2 h 8 min 2 s"  <br>
         * Inspiré d'une méthode de JF Gazet*/

        int h = (int) (tempsS / 3600);
        int m = (int) ((tempsS % 3600) / 60);
        int s = (int) (tempsS % 60);

        String r="";

        if(h>0) {r+=h+" h ";}
        if(m>0) {r+=m+" min ";}
        if(s>0) {r+=s+" s";}
        if(h<=0 && m<=0 && s<=0) {r="0 s";}

        return r;
        }

    }

