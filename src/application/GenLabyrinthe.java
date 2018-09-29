package application;
import java.util.ArrayList;
import java.util.Random;
public class GenLabyrinthe {
    private int[][][] lab ;
    private char[][] type ;

    public GenLabyrinthe(){
        /** genere un labyrinthe de taille 4x4 pour les tests */
     System.out.println("creation du labyrinthe standard 4 x 4");

                /** 1101 par exemple veut dire que, dans l ordre, il y a un mur au nord (bit le plus a droite),
                *a l est et a l ouest, et pas de mur au sud.
                *L ordre est donc NESO*/

        char type[][] = new char[4][4];
        for (int i=0; i<4;i++){
            for (int j=0; j<4; j++){
                type[i][j]='p';

            }

        }
        type[0][0]='e'; type[3][3]='s';
        int[][][] lab = {{{1,0,1,1},{1,0,1,0},{1,1,0,0},{1,1,0,1}}
        ,{{1,0,0,1},{1,0,0,0},{0,0,0,0},{0,1,0,0}}
        ,{{0,1,1,1},{0,1,1,1},{0,1,0,1},{0,1,0,1}}
        ,{{1,0,1,1},{1,0,1,0},{0,1,1,0},{0,1,1,1}}
        };
    }

    public GenLabyrinthe (int n) {
        /**genere un labyrinthe de taille n * n, compose d'un tableau de listes binaires correspondants a l'emplacement des
       * murs pour chaque cases (une coordonnee du tableau correspond a une case du labyrinthe). La premiere valeur
       * de la liste d'une case correspond au mur nord (1 si il y en a un, 0 sinon), le deuxieme au mur est, le 3e
       * au mur sud et le 4e au mur ouest
       * genere egalement un tableau de caracteres, passage, entree ou sortie*/

        this.lab = new int [n][n][4];
        boolean [][] casesIsolees = new boolean [n][n];
        int[][] tableauCasesECD = new int [n][n];
        /**tableau binaire (0 ou 1) indiquant si la case correspondant aux coordonnees est isolee ou non*/

        this.type = new char[n][n];


        ArrayList<ArrayList<Object>> listeCasesECD = new ArrayList<ArrayList<Object>>();
        /**liste cases exploitables comme depart
       * c est une liste de liste ou l on peut facilement ajouter et modifier son contenu, et qui peut contenir
       * des objects differents. chaque liste de la liste contient des informations sur les cases qui peuvent servir de
       * depart a l algorithme de creation du labyrinthe. Une case peut servir de depart si elle n est plus isolee
       * et possede au moins un voisin isolee. chaque liste dans la liste contient donc : les coordonnees i(ligne)
       * et j (colonne) de la case qui peut servir de depart, puis en position suivante le nombre k de voisin(s) isole(s),
       * puis k caracteres ('n', 'e', 's', 'o') correspondant aux positions du/des voisin(s) isole(s)*/

        int compteurCasesIsolees =n*n-1;
        /**compteur qui permet de terminer l'algorithme*/


        /**initialisation des valeurs des variables
        *tous les murs du labyrinthe sont fermes, toutes les cases sont isolees, seul le type de la case est
        *defini (entree en position 0,0, sortie en position n-1,n-1*/
        for (int i=0; i<n;i++){
            for (int j=0; j<n; j++){
                for (int a=0; a<4;a++){
                    lab[i][j][a]= 1;
                }
                casesIsolees[i][j]= true;
                tableauCasesECD[i][j]=0;
                type[i][j]='p';
            }
        }
        type[0][0] = 'e'; type[n-1][n-1]='s';

        /**on prend aleatoirement une case du labyrinthe pour commencer l'algorithme, et on l'ajoute a listeCasesECD
        *on prend i et j dans les cases sans problemes aux bordures , c'est plus simple*/
        int i;
        int j;
        Random rand = new Random();
        i = rand.nextInt(n-3)+1; j=rand.nextInt(n-3)+1;

        /**on cree ici la liste que l on ajoute a listeCasesECD
        tous les voisins de la case selectionnee sont forcement isoles a ce stade, on ajoute donc (i,j,4,'n','e','s','o')*/
        ArrayList<Object> etatVoisinCaseInit = new ArrayList<Object> ();

        etatVoisinCaseInit.add(i);etatVoisinCaseInit.add(j);etatVoisinCaseInit.add(4);etatVoisinCaseInit.add('n');
        etatVoisinCaseInit.add('e');etatVoisinCaseInit.add('s');etatVoisinCaseInit.add('o');
        listeCasesECD.add(etatVoisinCaseInit);

        /**On considère que la case initialisee n est plus isolee*/
        casesIsolees[i][j]=false;

        /**debut de l algorithme*/
        while (compteurCasesIsolees != 0){
            /**on prend une case aleatoirement dans celles exploitables comme depart, l'aleatoire étant contenu dans val, on recupere ici les coordonnees de la case en question*/
            int val = rand.nextInt(listeCasesECD.size());/**genere une liste allant de 0(inclu) a
            la valeur entre parentheses (exclue)*/
            i=(int) listeCasesECD.get(val).get(0);
            j=(int) listeCasesECD.get(val).get(1);


            /**on prend ensuite une direction (n,s,e ou o) aleatoirement dans celles disponibles pour la case selectionnee
           * c'est l'indice k qui nous donne cette indication*/
            int k = rand.nextInt((int) listeCasesECD.get(val).get(2));
            char direction = (char) listeCasesECD.get(val).get(3+k);


            /** on maj les murs du labyrinthe, ie on supprime les murs de la case choisie et de la case voisine selectionnee(en fonction de la direction), et l ensemble casesIsolees*/
            if (direction=='n'){
                lab[i][j][0]=0;
                lab[i-1][j][2]=0;
                casesIsolees[i-1][j]=false;
            }

            else if (direction=='e'){
                lab[i][j][1]=0;
                lab[i][j+1][3]=0;
                casesIsolees[i][j+1]=false;
            }
            else if (direction=='s'){
                lab[i][j][2]=0;
                lab[i+1][j][0]=0;
                casesIsolees[i+1][j]=false;
            }

            else if (direction=='o'){
                lab[i][j][3]=0;
                lab[i][j-1][1]=0;
                casesIsolees[i][j-1]=false;
            }


        listeCasesECD=traiterCECD(listeCasesECD, direction, i, j, val, n, casesIsolees);

        /**on met a jour la liste des cases exploitables comme depart*/

        compteurCasesIsolees -=1;
        }
}



    private  ArrayList<ArrayList<Object>> traiterCECD(ArrayList<ArrayList<Object>> listeCasesECD, char direction, int i,
            int j, int val, int n, boolean[][] casesIsolees) {
        /**cette methode met a jour la liste des cases ECD; tout d'abord, elle supprime la direction utilisee de la
       * case utilsee comme depart; puis decremente le nombre de voisin isoles de 1. si ce nombre tombe a 0,
       * on supprime la case de la liste.

       * ensuite, on ajoute la case voisine anciennement isolee a la liste cases ECD (si elle possede au moins un
       * voisin isole)*/


        for (int x=3; x<listeCasesECD.get(val).size(); x++){
            if ((char) listeCasesECD.get(val).get(x) == direction){
                ArrayList<Object> casesECD2 = listeCasesECD.get(val);
                casesECD2.remove(x);
                casesECD2.set(2, (int) casesECD2.get(2)-1);
                listeCasesECD.set(val, casesECD2);
            }

        }
        if ((int) listeCasesECD.get(val).get(2)==0){
            listeCasesECD.remove(val);
        }





        ArrayList<Object> caseAAjouter = new ArrayList<Object>();

        if (direction=='n'){
            caseAAjouter.add(i-1); caseAAjouter.add(j); caseAAjouter.add(0);
            listeCasesECD.add(caseAAjouter);
            listeCasesECD=traitementVoisinNord(listeCasesECD, i-1, j, casesIsolees);
            listeCasesECD=traitementVoisinEst(listeCasesECD, i-1, j, casesIsolees, n);
            listeCasesECD=traitementVoisinOuest(listeCasesECD, i-1, j, casesIsolees);
            caseAAjouter=listeCasesECD.get(listeCasesECD.size()-1);
            if ((int) caseAAjouter.get(2)== 0) {
                listeCasesECD.remove(listeCasesECD.size()-1);
            }

        }

        else if (direction=='e'){
            caseAAjouter.add(i); caseAAjouter.add(j+1); caseAAjouter.add(0);
            listeCasesECD.add(caseAAjouter);
            listeCasesECD=traitementVoisinNord(listeCasesECD, i, j+1, casesIsolees);
            listeCasesECD=traitementVoisinEst(listeCasesECD, i, j+1, casesIsolees, n);
            listeCasesECD=traitementVoisinSud(listeCasesECD, i, j+1, casesIsolees, n);
            caseAAjouter=listeCasesECD.get(listeCasesECD.size()-1);
            if ((int) caseAAjouter.get(2)== 0) {
                listeCasesECD.remove(listeCasesECD.size()-1);
            }

        }

        else if (direction=='s'){
            caseAAjouter.add(i+1); caseAAjouter.add(j); caseAAjouter.add(0);
            listeCasesECD.add(caseAAjouter);
            listeCasesECD=traitementVoisinEst(listeCasesECD, i+1, j, casesIsolees, n);
            listeCasesECD=traitementVoisinSud(listeCasesECD, i+1, j, casesIsolees, n);
            listeCasesECD=traitementVoisinOuest(listeCasesECD, i+1, j, casesIsolees);
            caseAAjouter=listeCasesECD.get(listeCasesECD.size()-1);
            if ((int) caseAAjouter.get(2)== 0) {
                listeCasesECD.remove(listeCasesECD.size()-1);
            }

        }

        else if (direction=='o'){
            caseAAjouter.add(i); caseAAjouter.add(j-1); caseAAjouter.add(0);
            listeCasesECD.add(caseAAjouter);
            listeCasesECD=traitementVoisinNord(listeCasesECD, i, j-1, casesIsolees);
            listeCasesECD=traitementVoisinSud(listeCasesECD, i, j-1, casesIsolees, n);
            listeCasesECD=traitementVoisinOuest(listeCasesECD, i, j-1, casesIsolees);
            caseAAjouter=listeCasesECD.get(listeCasesECD.size()-1);
            if ((int) caseAAjouter.get(2)== 0) {
                listeCasesECD.remove(listeCasesECD.size()-1);
            }

        }

        return listeCasesECD;
    }


    private ArrayList<ArrayList<Object>> traitementVoisinOuest(ArrayList<ArrayList<Object>> listeCasesECD, int i, int j,
            boolean[][] casesIsolees) {
        /**Cette fonction modifie listeCasesECD en prenant comme entrée les coordonnées de la case qui a nouvellement ete ajoutee au labyrinthe, la fonction modifie la case a l est de la case qui a ete nouvellement ajoutee
         */
        ArrayList<Object> caseAAjouter= listeCasesECD.get((int) listeCasesECD.size() -1);/** la case nouvellement ajoutee est a la derniere place de la liste listeCasesECD */
        if (j-1<0){}/** on ne fait rien*/

        else if (casesIsolees[i][j-1]){
            /** si la case  a l ouest de la case nouvellement ajoutee est isolee, on rajoute ces infos dans caseAAjouter*/
            caseAAjouter.add('o');
            caseAAjouter.set(2,  (int) caseAAjouter.get(2)+1);
            listeCasesECD.set(listeCasesECD.size() - 1, caseAAjouter);
        }

        else if (! casesIsolees[i][j-1]){
            /** si la case a l ouest est deja dans le labyrinthe, il faut la mettre a jour, et donc la retrouver dans listeCasesECD*/
            ArrayList<Object> caseAModifier = new ArrayList<Object>();
            for (int val=0; val<listeCasesECD.size(); val++ ){
                if ((int) listeCasesECD.get(val).get(0)==i && (int) listeCasesECD.get(val).get(1)==j-1){
                    caseAModifier= listeCasesECD.get(val);
                    caseAModifier.set(2,  (int) caseAModifier.get(2)-1);
                    if ((int) caseAModifier.get(2)==0){
                        listeCasesECD.remove(val);
                    }
                    else {
                        for (int x=3; x<caseAModifier.size(); x++){
                            if ((char) caseAModifier.get(x)=='e'){
                                caseAModifier.remove(x);
                                listeCasesECD.set(val,  caseAModifier);
                            }
                        }
                    }

                }
            }
        }
        return listeCasesECD;
    }

    private ArrayList<ArrayList<Object>> traitementVoisinEst(ArrayList<ArrayList<Object>> listeCasesECD, int i, int j,
            boolean[][] casesIsolees, int n) {
        /**Cette fonction modifie listeCasesECD en prenant comme entrée les coordonnées de la case qui a nouvellement ete ajoutee au labyrinthe, la fonction modifie la case a l est de la case qui a ete nouvellement ajoutee
         */
        ArrayList<Object> caseAAjouter= listeCasesECD.get((int) listeCasesECD.size() -1);/** la case nouvellement ajoutee est a la derniere place de la liste listeCasesECD */
        if (j+1>n-1){}/** on ne fait rien*/

        else if (casesIsolees[i][j+1]){
            /** si la case  a l est de la case nouvellement ajoutee est isolee, on rajoute ces infos dans caseAAjouter*/
            caseAAjouter.add('e');
            caseAAjouter.set(2,  (int) caseAAjouter.get(2)+1);
            listeCasesECD.set(listeCasesECD.size() - 1, caseAAjouter);
        }

        else if (! casesIsolees[i][j+1]){
            /** si la case a l est est deja dans le labyrinthe, il faut la mettre a jour, et donc la retrouver dans listeCasesECD*/
            ArrayList<Object> caseAModifier = new ArrayList<Object>();
            for (int val=0; val<listeCasesECD.size(); val++ ){
                if ((int) listeCasesECD.get(val).get(0)==i && (int) listeCasesECD.get(val).get(1)==j+1){
                    caseAModifier= listeCasesECD.get(val);
                    caseAModifier.set(2,  (int) caseAModifier.get(2)-1);
                    if ((int) caseAModifier.get(2)==0){
                        listeCasesECD.remove(val);
                    }
                    else {
                        for (int x=3; x<caseAModifier.size(); x++){
                            if ((char) caseAModifier.get(x)=='o'){
                                caseAModifier.remove(x);
                                listeCasesECD.set(val,  caseAModifier);
                            }
                        }
                    }

                }
            }
        }
        return listeCasesECD;
    }

    private ArrayList<ArrayList<Object>> traitementVoisinNord(ArrayList<ArrayList<Object>> listeCasesECD, int i, int j,
            boolean[][] casesIsolees) {
        /**Cette fonction modifie listeCasesECD en prenant comme entrée les coordonnées de la case qui a nouvellement ete ajoutee au labyrinthe, la fonction modifie la case au nord de la case qui a ete nouvellement ajoutee
         */
        ArrayList<Object> caseAAjouter= listeCasesECD.get((int) listeCasesECD.size() -1);/** la case nouvellement ajoutee est a la derniere place de la liste listeCasesECD */
        if (i-1<0){}// on ne fait rien

        else if (casesIsolees[i-1][j]){
            /** si la case  au nord de la case nouvellement ajoutee est isolee, on rajoute ces infos dans caseAAjouter*/
            caseAAjouter.add('n');
            caseAAjouter.set(2,  (int) caseAAjouter.get(2)+1);
            listeCasesECD.set(listeCasesECD.size() - 1, caseAAjouter);
        }

        else if (! casesIsolees[i-1][j]){
            /** si la case au nord est deja dans le labyrinthe, il faut la mettre a jour, et donc la retrouver dans listeCasesECD*/
            ArrayList<Object> caseAModifier = new ArrayList<Object>();
            for (int val=0; val<listeCasesECD.size(); val++ ){
                if ((int) listeCasesECD.get(val).get(0)==i-1 && (int) listeCasesECD.get(val).get(1)==j){
                    caseAModifier= listeCasesECD.get(val);
                    caseAModifier.set(2,  (int) caseAModifier.get(2)-1);
                    if ((int) caseAModifier.get(2)==0){
                        listeCasesECD.remove(val);
                    }
                    else {
                        for (int x=3; x<caseAModifier.size(); x++){
                            if ((char) caseAModifier.get(x)=='s'){
                                caseAModifier.remove(x);
                                listeCasesECD.set(val,  caseAModifier);
                            }
                        }
                    }

                }
            }
        }
        return listeCasesECD;
    }


    private ArrayList<ArrayList<Object>> traitementVoisinSud(ArrayList<ArrayList<Object>> listeCasesECD, int i, int j,
            boolean[][] casesIsolees, int n) {
        /**Cette fonction modifie listeCasesECD en prenant comme entrée les coordonnées de la case qui a nouvellement ete ajoutee au labyrinthe, la fonction modifie la case au sud de la case qui a ete nouvellement ajoutee
         */

        ArrayList<Object> caseAAjouter= listeCasesECD.get((int) listeCasesECD.size() -1);/** la case nouvellement ajoutee est a la derniere place de la liste listeCasesECD */
        if (i+1>n-1){}// on ne fait rien

        else if (casesIsolees[i+1][j]){
            /** si la case  au sud de la case nouvellement ajoutee est isolee, on rajoute ces infos dans caseAAjouter*/
            caseAAjouter.add('s');
            caseAAjouter.set(2,  (int) caseAAjouter.get(2)+1);
            listeCasesECD.set(listeCasesECD.size() - 1, caseAAjouter);
        }

        else if (! casesIsolees[i+1][j]){
            /** si la case au sud est deja dans le labyrinthe, il faut la mettre a jour, et donc la retrouver dans listeCasesECD*/
            ArrayList<Object> caseAModifier = new ArrayList<Object>();
            for (int val=0; val<listeCasesECD.size(); val++ ){
                if ((int) listeCasesECD.get(val).get(0)==i+1 && (int) listeCasesECD.get(val).get(1)==j){
                    caseAModifier= listeCasesECD.get(val);
                    caseAModifier.set(2,  (int) caseAModifier.get(2)-1);
                    if ((int) caseAModifier.get(2)==0){
                        listeCasesECD.remove(val);
                    }
                    else {
                        for (int x=3; x<caseAModifier.size(); x++){
                            if ((char) caseAModifier.get(x)=='n'){
                                caseAModifier.remove(x);
                                listeCasesECD.set(val,  caseAModifier);
                            }
                        }
                    }

                }
            }
        }
        return listeCasesECD;
    }










    public int[][][] getLab(){
        return lab;
    }

    public void setLab(int[][][] lab) {
        this.lab = lab;
    }

    public void setType(char[][] type) {
        this.type = type;
    }

    public char[][] getType(){
        return type;
    }
    public static void main (String[] args){
        GenLabyrinthe labi= new GenLabyrinthe(4);

        for (int a=0; a<4; a++){
            for (int b=0; b<4; b++){
                System.out.print(java.util.Arrays.toString(labi.lab[a][b]));
            }
            System.out.println(".");
        }
    }
}





