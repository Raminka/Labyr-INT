package application;

import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

    /** initialisation des données **/
    int taille =10;

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    // initialisation de dimensionnement de labyrinthe
    int debutX = 20;
    int debutY = 20;
    int finX = 930;
    int finY = 930;

    GenLabyrinthe labi = new GenLabyrinthe(getTaille());
    int[][][] lab = labi.getLab();

    public int[][][] getLaby() {
        return lab;
    }

    public void setLaby(int[][][] lab) {
        this.lab = lab;
    }

    // definition de position joueur en x
    int x = 0;

    public int getPositionX() {
        return x;
    }

    public void setPositionX(int x) {
        this.x = x;
    }

    // definition de position joueur en y
    int y = 0;

    public int getPositionY() {
        return y;
    }

    public void setPositionY(int y) {
        this.y = y;
    }

    // determination de la dimension d'une case de labyrinthe
    int tailleCase = (finX - debutX) / getTaille();

    public int getTailleCase() {
        return (finX - debutX) / getTaille();
    }

    // dimension d'un stage:
    int dimX = finX - debutX + 500;
    int dimY = finY - debutY + 100;

    // calcul de nombre de mvmt fait par joueur
    int mvmt = 0;

    public void setMvmt(int mvmt) {
        this.mvmt = mvmt;
    }

    public int getMvmt() {
        return mvmt;
    }

    // calcul de mvmt minimum necessaires pour gagner au labyrinthe
    int mvmtNecessaire = 0;

    public void setMvmtNecessaire(int mvmtN) {
        this.mvmtNecessaire = mvmtN;
    }

    public int getMvmtNecessaire() {
        return mvmtNecessaire;
    }

    /** methodes de creation du labyrinth **/
    // creation d'une droite horizontale à partir du point i,j au point i+1,j
    void droiteHor(int j, int i, Group root) {
        Line line = new Line();
        line.setStartX(debutX + i * getTailleCase());
        line.setStartY(debutY + j * getTailleCase());
        line.setEndX(debutX + (i + 1) * getTailleCase());
        line.setEndY(debutY + j * getTailleCase());
        line.setStroke(Color.BLUE);
        line.setStrokeWidth(2);
        /// ajoute de line dans l'ensemble de l'interface graphique
        root.getChildren().add(line);
    };

    // creation d'une droite verticale à partir du point i,j au point i,j+1
    void droiteVer(int j, int i, Group root) {
        Line line = new Line();
        line.setStartX(debutX + i * getTailleCase());
        line.setStartY(debutY + j * getTailleCase());
        line.setEndX(debutX + i * getTailleCase());
        line.setEndY(debutY + (j + 1) * getTailleCase());
        line.setStroke(Color.BLUE);
        line.setStrokeWidth(2);
        root.getChildren().add(line);
    };

    // presence de mur à Ouest?
    int murOuest(int i, int j) {
        if (i == 0 & j == 0) {
            return 0;
        }
        // si on depasse le tableau, on prend l'element voisin et on regarde la
        // presence du mur à est
        if (j == getTaille()) {
            if (i == getTaille() - 1) {
                return 0;
            }
            return getLaby()[i][j - 1][1];
        }
        return getLaby()[i][j][3];
    };

    int murNord(int i, int j) {
        // si on depasse le tableau, on prend l'element voisin et on regarde la
        // presence du mur à sud
        if (i == taille) {
            return getLaby()[i - 1][j][2];
        }
        return getLaby()[i][j][0];
    };

    /** le methode de resolution du labyrinthe **/
    public boolean solutionRecursive(Group root, int positionX, int positionY, int departX, int departY) {
        Line line = new Line();
        line.setStartX(debutX + (0.5 + positionX) * getTailleCase());
        line.setStartY(debutY + (0.5 + positionY) * getTailleCase());
        line.setEndX(debutX + (0.5 + departX) * getTailleCase());
        line.setEndY(debutY + (0.5 + departY) * getTailleCase());
        line.setStroke(Color.RED);
        line.setStrokeWidth(2);
        if (getLaby().length - 1 == positionX & getLaby().length - 1 == positionY) {
            root.getChildren().add(line);
            return true;
        }
        if (lab[positionY][positionX][0] == 0 & positionY - 1 != departY) {
            if (solutionRecursive(root, positionX, positionY - 1, positionX, positionY)) {
                root.getChildren().add(line);
                return true;
            }
        }
        if (lab[positionY][positionX][1] == 0 & positionX + 1 != departX) {
            if (solutionRecursive(root, positionX + 1, positionY, positionX, positionY)) {
                root.getChildren().add(line);
                return true;
            }
        }
        if (lab[positionY][positionX][2] == 0 & positionY + 1 != departY) {
            if (solutionRecursive(root, positionX, positionY + 1, positionX, positionY)) {
                root.getChildren().add(line);
                return true;
            }
        }
        if (getLaby()[positionY][positionX][3] == 0 & positionX - 1 != departX) {
            if (solutionRecursive(root, positionX - 1, positionY, positionX, positionY)) {
                root.getChildren().add(line);
                return true;
            }
        }
        return false;
    }

    // initialisation
    void resolution(Group root, int positionX, int positionY) {
        Line line = new Line();
        line.setStartX(debutX + (0.5 + positionX) * getTailleCase());
        line.setStartY(debutY + (0.5 + positionY) * getTailleCase());
        line.setStroke(Color.RED);
        line.setStrokeWidth(2);
        if (getLaby().length - 1 == positionX & getLaby().length - 1 == positionY) {
            line.setEndX(debutX + (getTaille() + 1) * getTailleCase());
            line.setEndY(debutY + (getTaille() + 1) * getTailleCase());
            root.getChildren().add(line);
        }
        if (lab[positionY][positionX][0] == 0) {
            if (solutionRecursive(root, positionX, positionY - 1, positionX, positionY)) {
                line.setEndX(debutX + (0.5 + positionX) * getTailleCase());
                line.setEndY(debutY + (0.5 + positionY - 1) * getTailleCase());
                root.getChildren().add(line);
            }
        }
        if (lab[positionY][positionX][1] == 0) {
            if (solutionRecursive(root, positionX + 1, positionY, positionX, positionY)) {
                line.setEndX(debutX + (0.5 + positionX + 1) * getTailleCase());
                line.setEndY(debutY + (0.5 + positionY) * getTailleCase());
                root.getChildren().add(line);
            }
        }
        if (lab[positionY][positionX][2] == 0) {
            if (solutionRecursive(root, positionX, positionY + 1, positionX, positionY)) {
                line.setEndX(debutX + (0.5 + positionX) * getTailleCase());
                line.setEndY(debutY + (0.5 + positionY + 1) * getTailleCase());
                root.getChildren().add(line);
            }
        }
        if (getLaby()[positionY][positionX][3] == 0) {
            if (solutionRecursive(root, positionX - 1, positionY, positionX, positionY)) {
                line.setEndX(debutX + (0.5 + positionX - 1) * getTailleCase());
                line.setEndY(debutY + (0.5 + positionY) * getTailleCase());
                root.getChildren().add(line);
            }
        }
    }

    /** le methode de creation du labyrinthe sombre **/
 /**
    // le tableau contenant les cases sombres ; 0 = case inconnue, 1= case decouverte
    int[][] casesInconnues = new int[getTaille()][getTaille()];

    public int[][] getCasesInconnues() {
        return casesInconnues;
    }

    public void setCasesInconnues(int[][] casesInconnues) {
        this.casesInconnues = casesInconnues;
    }

    // initialisation de remplissage du tableau (l'entréé et la sortie sont
    // eclairées
    void initCasesInconnues() {
        int[][] T = getCasesInconnues();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                T[i][j] = 1;
                T[getTaille() - 1 - i][getTaille() - 1 - j] = 1;
                setCasesInconnues(T);
            }
        }
    }

    // remplissage du tableau en fonction du position du joueur
    void remplissageCasesInconnues() {
        int[][] T = getCasesInconnues();
        for (int i = 0; i <= getTaille() - 1; i++) {
            for (int j = 0; j <= getTaille() - 1; j++) {
                if (Math.abs(getPositionX() - j) <= 2 & Math.abs(getPositionY() - i) <= 2) {
                    T[i][j] = 1;
                }
            }
        }
        setCasesInconnues(T);
    }

    // gestion de modeCachee, creation du figure
    void creationModeExploration(Group root) {
        root.getChildren().clear();
        if (getPositionX() == 0 & getPositionY() == 0) {
            initCasesInconnues();
        } else {
            remplissageCasesInconnues();
        }
        for (int i = 0; i < getTaille(); i++) {
            for (int j = 0; j < getTaille(); j++) {
                if (getCasesInconnues()[i][j] == 0) {
                    Rectangle carre = new Rectangle();
                    carre.setFill(Color.ANTIQUEWHITE);
                    carre.setY(debutY + i * getTailleCase());
                    carre.setX(debutX + j * getTailleCase());
                    carre.setWidth(getTailleCase());
                    carre.setHeight(getTailleCase());
                    root.getChildren().add(carre);
                }
            }
        }
    }

    // initialisation de la figure de cachant le labyrinthe
    Group jeuExploration = new Group();
    boolean modeExploration = false;
   **/


    /** Methode de creation du labyrinthe sombre <br>
    Tableau contenant les cases sombres ; 0 = case inconnue, 1= case decouverte */
   int[][] casesInconnues = new int[getTaille()][getTaille()];

   public int[][] getCasesInconnues() {
       return casesInconnues;
   }

   public void setCasesInconnues(int[][] casesInconnues) {
       this.casesInconnues = casesInconnues;
   }

   /** Initialisation du remplissage du tableau (l'entree et la sortie sont eclairees initialement */
    void initCasesInconnues() {
    	int[][] T = new int[getTaille()][getTaille()];
       setCasesInconnues(T);
       for (int i = 0; i < 3; i++) { /** On peut voir dans un perimetre de 3 cases autour de la position */
           for (int j = 0; j < 3; j++) {
               T[i][j] = 1;
               T[getTaille() - 1 - i][getTaille() - 1 - j] = 1;
               setCasesInconnues(T);
           }
       }
   }

   /**Remplissage du tableau en fonction du position du joueur */
   void remplissageCasesInconnues() {
       int[][] T = getCasesInconnues();
       for (int i = 0; i <= getTaille() - 1; i++) {
           for (int j = 0; j <= getTaille() - 1; j++) {
               if (Math.abs(getPositionX() - j) <= 2 & Math.abs(getPositionY() - i) <= 2) { /** Perimetre de 2 cases */
                   T[i][j] = 1;
               }
           }
       }
       setCasesInconnues(T);
   }

   /** Gestion de modeExploration, creation de la figure recouvrant les parties non visibles du labyrinthe */
   void creationModeExploration(Group root) {
       root.getChildren().clear(); /**A chaque deplacement du joueur on supprime puis redessine la figurant qui change */
       if (getPositionX() == 0 & getPositionY() == 0 & 0==getCasesInconnues()[0][0]) { /** Position initiale */
           initCasesInconnues();
       } else {
           remplissageCasesInconnues();
       }
       for (int i = 0; i < getTaille(); i++) {
           for (int j = 0; j < getTaille(); j++) {
               if (getCasesInconnues()[i][j] == 0) { /** On recouvre chaque position inconnue d'un carree */
                   Rectangle carre = new Rectangle();
                   carre.setFill(Color.ANTIQUEWHITE);
                   carre.setY(debutY + i * getTailleCase());
                   carre.setX(debutX + j * getTailleCase());
                   carre.setWidth(getTailleCase());
                   carre.setHeight(getTailleCase());
                   root.getChildren().add(carre);
               }
           }
       }
   }

   /** Initialisation de la figure */
   Group jeuExploration = new Group();
   boolean modeExploration = false;







    /** le methode de creation du labyrinthe sombre **/
    // le tableau contenant les cases sombres ; 0 = case inconnue, 1= case decouverte


    // initialisation de remplissage du tableau (l'entréé et la sortie sont
    // eclairées


    // gestion de modeCachee, creation du figure
    void creationModeExploration2(Group root) {
        root.getChildren().clear();

        for (int i = 0; i < getTaille(); i++) {
            for (int j = 0; j < getTaille(); j++) {
                    if ((Math.abs(getPositionX() - j) <= 2 & Math.abs(getPositionY() - i) <= 2)
                            ||(j <= 2 & i <= 2)
                            ||(getTaille() - j <= 3 & getTaille() - i <= 3)){
                        }
                    else{
                    Rectangle carre = new Rectangle();
                    carre.setFill(Color.ANTIQUEWHITE);
                    carre.setY(debutY + i * getTailleCase());
                    carre.setX(debutX + j * getTailleCase());
                    carre.setWidth(getTailleCase());
                    carre.setHeight(getTailleCase());
                    root.getChildren().add(carre);

                }
            }
        }
    }

    // initialisation de la figure de cachant le labyrinthe
    Group jeuExploration2 = new Group();
    boolean modeExploration2 = false;





    /**
     * le methode indice permet de donner au joueur à une position precise de
     * savoir la direction de sortie
     **/
    public boolean indiceRecursive(int positionX, int positionY, int departX, int departY) {
        if (getLaby().length - 1 == positionX & getLaby().length - 1 == positionY) {
            setMvmtNecessaire(getMvmtNecessaire() + 1);
            return true;
        }
        if (lab[positionY][positionX][0] == 0 & positionY - 1 != departY) {
            if (indiceRecursive(positionX, positionY - 1, positionX, positionY)) {
                setMvmtNecessaire(getMvmtNecessaire() + 1);
                return true;
            }
        }
        if (lab[positionY][positionX][1] == 0 & positionX + 1 != departX) {
            if (indiceRecursive(positionX + 1, positionY, positionX, positionY)) {
                setMvmtNecessaire(getMvmtNecessaire() + 1);
                return true;
            }
        }
        if (lab[positionY][positionX][2] == 0 & positionY + 1 != departY) {
            if (indiceRecursive(positionX, positionY + 1, positionX, positionY)) {
                setMvmtNecessaire(getMvmtNecessaire() + 1);
                return true;
            }
        }
        if (getLaby()[positionY][positionX][3] == 0 & positionX - 1 != departX) {
            if (indiceRecursive(positionX - 1, positionY, positionX, positionY)) {
                setMvmtNecessaire(getMvmtNecessaire() + 1);
                return true;
            }
        }
        return false;
    }

    // initialisation du methode
    void indice(Polygon triangle, int positionX, int positionY) {
        triangle.setStroke(Color.BLACK);
        triangle.setFill(Color.BLACK);
        triangle.setStrokeWidth(5);
        if (getLaby().length - 1 == positionX & getLaby().length - 1 == positionY) {
            triangle.getPoints().setAll((debutX + (0.9 + positionX) * getTailleCase()),
                    (debutY + (0.3 + positionY) * getTailleCase()), (debutX + (1.1 + positionX) * getTailleCase()),
                    (debutY + (0.5 + positionY) * getTailleCase()), (debutX + (0.9 + positionX) * getTailleCase()),
                    (debutY + (0.7 + positionY) * getTailleCase()));
        }
        if (lab[positionY][positionX][0] == 0) {
            if (indiceRecursive(positionX, positionY - 1, positionX, positionY)) {
                triangle.getPoints().setAll((debutX + (0.3 + positionX) * getTailleCase()),
                        (debutY + (0.1 + positionY) * getTailleCase()), (debutX + (0.5 + positionX) * getTailleCase()),
                        (debutY + (-0.1 + positionY) * getTailleCase()), (debutX + (0.7 + positionX) * getTailleCase()),
                        (debutY + (0.1 + positionY) * getTailleCase()));
            }
        }
        if (lab[positionY][positionX][1] == 0) {
            if (indiceRecursive(positionX + 1, positionY, positionX, positionY)) {
                triangle.getPoints().setAll((debutX + (0.9 + positionX) * getTailleCase()),
                        (debutY + (0.3 + positionY) * getTailleCase()), (debutX + (1.1 + positionX) * getTailleCase()),
                        (debutY + (0.5 + positionY) * getTailleCase()), (debutX + (0.9 + positionX) * getTailleCase()),
                        (debutY + (0.7 + positionY) * getTailleCase()));
            }
        }
        if (lab[positionY][positionX][2] == 0) {
            if (indiceRecursive(positionX, positionY + 1, positionX, positionY)) {
                triangle.getPoints().setAll((debutX + (0.3 + positionX) * getTailleCase()),
                        (debutY + (0.9 + positionY) * getTailleCase()), (debutX + (0.5 + positionX) * getTailleCase()),
                        (debutY + (1.1+ positionY) * getTailleCase()), (debutX + (0.7 + positionX) * getTailleCase()),
                        (debutY + (0.9 + positionY) * getTailleCase()));
            }
        }
        if (getLaby()[positionY][positionX][3] == 0) {
            if (indiceRecursive(positionX - 1, positionY, positionX, positionY)) {
                triangle.getPoints().setAll((debutX + (0.1 + positionX) * getTailleCase()),
                        (debutY + (0.3 + positionY) * getTailleCase()), (debutX + (-0.1 + positionX) * getTailleCase()),
                        (debutY + (0.5 + positionY) * getTailleCase()), (debutX + (0.1 + positionX) * getTailleCase()),
                        (debutY + (0.7 + positionY) * getTailleCase()));
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Group rootAcc = new Group();
            Scene scene = new Scene(rootAcc, dimX, dimY, Color.ANTIQUEWHITE);

            // Bouton de sortie
            Button closebutton = new Button();
            closebutton.setOnAction(e -> primaryStage.close());
            closebutton.setText("Exit");
            closebutton.setMinSize(dimX / 8, dimY / 12);
            closebutton.setLayoutX(dimX - dimX / 8);
            closebutton.setLayoutY(dimY - dimY / 12);
            closebutton.setFont(Font.font("Verdana", 20));
            closebutton.setTextFill(Color.RED);
            rootAcc.getChildren().add(closebutton);



            // Bouton Choix de taille
            Button choixTaille = new Button();
            choixTaille.setText("Choix de la taille");
            choixTaille.setMinSize(dimX / 8, dimY / 12);
            choixTaille.setLayoutX(0);
            choixTaille.setLayoutY(dimY - dimY / 12);
            choixTaille.setFont(Font.font("Verdana", 20));
            choixTaille.setTextFill(Color.BLUE);
            choixTaille.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    // Stage du menu de choix
                    Stage ChoixNiveau = new Stage();
                    primaryStage.close();
                    Group rootNiveau = new Group();
                    // Instructions
                    Text indication = new Text();
                    indication.setText("     Choisissez un niveau");
                    indication.setFont(Font.font("Verdana", 40));
                    indication.setFill(Color.BLUE);
                    indication.setLayoutX(scene.getWidth()*2/7);
                    indication.setLayoutY(scene.getHeight() / 6);
                    rootNiveau.getChildren().addAll(indication);

                    // Retour Menu
                    Button btnRetour = new Button();
                    btnRetour.setLayoutX(dimX - dimX / 7);
                    btnRetour.setLayoutY(dimY - dimY / 7);
                    btnRetour.setMinSize(dimX / 10, dimY / 14);
                    btnRetour.setFont(Font.font("Verdana", 20));
                    btnRetour.setTextFill(Color.BLUE);
                    btnRetour.setText("Retour au menu");
                    btnRetour.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });
                    rootNiveau.getChildren().addAll(btnRetour);

/**definition des differents niveaux*/
                    Text classique = new Text();
                    classique.setText("MODE CLASSIQUE:");
                    classique.setFont(Font.font("Verdana", 20));
                    classique.setFill(Color.BLUE);
                    classique.setX(dimX*2/5);
                    classique.setY(dimY*2/7);
                    rootNiveau.getChildren().addAll(classique);

                    Button classiqueL1 = new Button();
                    classiqueL1.setLayoutX(dimX *4/ 24);
                    classiqueL1.setLayoutY(dimY* 2.5/ 7);
                    classiqueL1.setMinSize(dimX / 10, dimY / 14);
                    classiqueL1.setFont(Font.font("Verdana", 18));
                    classiqueL1.setTextFill(Color.BLUE);
                    classiqueL1.setText("Niveau 1\n 10*10");
                    rootNiveau.getChildren().addAll(classiqueL1);
                    classiqueL1.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(10);
                            modeExploration2 = false;
                            modeExploration = false;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button classiqueL2 = new Button();
                    classiqueL2.setLayoutX(dimX * 7 / 24);
                    classiqueL2.setLayoutY(dimY *2.5/ 7);
                    classiqueL2.setText("Niveau 2\n 30*30");
                    classiqueL2.setMinSize(dimX / 10, dimY / 14);
                    classiqueL2.setFont(Font.font("Verdana", 18));
                    classiqueL2.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(classiqueL2);
                    classiqueL2.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(30);
                            modeExploration2 = false;
                            modeExploration = false;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button classiqueL3 = new Button();
                    classiqueL3.setLayoutX(dimX * 10 / 24);
                    classiqueL3.setLayoutY(dimY *2.5/7);
                    classiqueL3.setText("Niveau 3\n 50*50");
                    classiqueL3.setMinSize(dimX / 10, dimY / 14);
                    classiqueL3.setFont(Font.font("Verdana", 18));
                    classiqueL3.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(classiqueL3);
                    classiqueL3.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(50);
                            modeExploration2 = false;
                            modeExploration = false;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button classiqueL4 = new Button();
                    classiqueL4.setLayoutX(dimX * 13 / 24);
                    classiqueL4.setLayoutY(dimY *2.5/ 7);
                    classiqueL4.setText("Niveau 4\n 70*70");
                    classiqueL4.setMinSize(dimX / 10, dimY / 14);
                    classiqueL4.setFont(Font.font("Verdana", 18));
                    classiqueL4.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(classiqueL4);
                    classiqueL4.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(70);
                            modeExploration2 = false;
                            modeExploration = false;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button classiqueL5 = new Button();
                    classiqueL5.setLayoutX(dimX * 16 / 24);
                    classiqueL5.setLayoutY(dimY *2.5/7);
                    classiqueL5.setText("Niveau 5\n 90*90");
                    classiqueL5.setMinSize(dimX / 10, dimY / 14);
                    classiqueL5.setFont(Font.font("Verdana", 18));
                    classiqueL5.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(classiqueL5);
                    classiqueL5.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(90);
                            modeExploration2 = false;
                            modeExploration = false;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });



                    Text exploration = new Text();
                    exploration.setText("MODE EXPLORATION:");
                    exploration.setFont(Font.font("Verdana", 20));
                    exploration.setFill(Color.BLUE);
                    exploration.setX(dimX*2/5);
                    exploration.setY(dimY*3.5/7);
                    rootNiveau.getChildren().addAll(exploration);

                    Button explorationL1 = new Button();
                    explorationL1.setLayoutX(dimX* 4/ 24);
                    explorationL1.setLayoutY(dimY *4 / 7);
                    explorationL1.setMinSize(dimX / 10, dimY / 14);
                    explorationL1.setFont(Font.font("Verdana", 18));
                    explorationL1.setTextFill(Color.BLUE);
                    explorationL1.setText("Niveau 1\n"
                            + " 10*10");
                    rootNiveau.getChildren().addAll(explorationL1);
                    explorationL1.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(10);
                            modeExploration = true;
                            modeExploration2 = false;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button explorationL2 = new Button();
                    explorationL2.setLayoutX(dimX * 7/24);
                    explorationL2.setLayoutY(dimY*4 / 7);
                    explorationL2.setText("Niveau 2\n 30*30");
                    explorationL2.setMinSize(dimX / 10, dimY / 14);
                    explorationL2.setFont(Font.font("Verdana", 18));
                    explorationL2.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(explorationL2);
                    explorationL2.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(30);
                            modeExploration = true;
                            modeExploration2 = false;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button explorationL3 = new Button();
                    explorationL3.setLayoutX(dimX * 10 / 24);
                    explorationL3.setLayoutY(dimY * 4 / 7);
                    explorationL3.setText("Niveau 3\n 50*50");
                    explorationL3.setMinSize(dimX / 10, dimY / 14);
                    explorationL3.setFont(Font.font("Verdana", 18));
                    explorationL3.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(explorationL3);
                    explorationL3.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(50);
                            modeExploration = true;
                            modeExploration2 = false;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button explorationL4 = new Button();
                    explorationL4.setLayoutX(dimX * 13 / 24);
                    explorationL4.setLayoutY(dimY * 4 / 7);
                    explorationL4.setText("Niveau 4\n 70*70");
                    explorationL4.setMinSize(dimX / 10, dimY / 14);
                    explorationL4.setFont(Font.font("Verdana", 18));
                    explorationL4.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(explorationL4);
                    explorationL4.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(70);
                            modeExploration = true;
                            modeExploration2 = false;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button explorationL5 = new Button();
                    explorationL5.setLayoutX(dimX * 16 / 24);
                    explorationL5.setLayoutY(dimY * 4 / 7);
                    explorationL5.setText("Niveau 5\n 90*90");
                    explorationL5.setMinSize(dimX / 10, dimY / 14);
                    explorationL5.setFont(Font.font("Verdana", 18));
                    explorationL5.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(explorationL5);
                    explorationL5.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(90);
                            modeExploration = true;
                            modeExploration2 = false;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });



                    Text exploration2 = new Text();
                    exploration2.setText("MODE OBSCURITE:");
                    exploration2.setFont(Font.font("Verdana", 20));
                    exploration2.setFill(Color.BLUE);
                    exploration2.setX(dimX*2/5);
                    exploration2.setY(dimY*5/7);
                    rootNiveau.getChildren().addAll(exploration2);

                    Button exploration2_L1 = new Button();
                    exploration2_L1.setLayoutX(dimX* 4/ 24);
                    exploration2_L1.setLayoutY(dimY *5.5/7);
                    exploration2_L1.setMinSize(dimX / 10, dimY / 14);
                    exploration2_L1.setFont(Font.font("Verdana", 18));
                    exploration2_L1.setTextFill(Color.BLUE);
                    exploration2_L1.setText("Niveau 1\n"
                            + " 10*10");
                    rootNiveau.getChildren().addAll(exploration2_L1);
                    exploration2_L1.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(10);
                            modeExploration = false;
                            modeExploration2 = true;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button exploration2_L2 = new Button();
                    exploration2_L2.setLayoutX(dimX * 7/24);
                    exploration2_L2.setLayoutY(dimY*5.5/7);
                    exploration2_L2.setText("Niveau 2\n 30*30");
                    exploration2_L2.setMinSize(dimX / 10, dimY / 14);
                    exploration2_L2.setFont(Font.font("Verdana", 18));
                    exploration2_L2.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(exploration2_L2);
                    exploration2_L2.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(30);
                            modeExploration = false;
                            modeExploration2 = true;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button exploration2_L3 = new Button();
                    exploration2_L3.setLayoutX(dimX * 10 / 24);
                    exploration2_L3.setLayoutY(dimY * 5.5/7);
                    exploration2_L3.setText("Niveau 3\n 50*50");
                    exploration2_L3.setMinSize(dimX / 10, dimY / 14);
                    exploration2_L3.setFont(Font.font("Verdana", 18));
                    exploration2_L3.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(exploration2_L3);
                    exploration2_L3.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(50);
                            modeExploration = false;
                            modeExploration2 = true;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button exploration2_L4 = new Button();
                    exploration2_L4.setLayoutX(dimX * 13 / 24);
                    exploration2_L4.setLayoutY(dimY * 5.5/7);
                    exploration2_L4.setText("Niveau 4\n 70*70");
                    exploration2_L4.setMinSize(dimX / 10, dimY / 14);
                    exploration2_L4.setFont(Font.font("Verdana", 18));
                    exploration2_L4.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(exploration2_L4);
                    exploration2_L4.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(70);
                            modeExploration = false;
                            modeExploration2 = true;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button exploration2_L5 = new Button();
                    exploration2_L5.setLayoutX(dimX * 16 / 24);
                    exploration2_L5.setLayoutY(dimY * 5.5/7);
                    exploration2_L5.setText("Niveau 5\n 90*90");
                    exploration2_L5.setMinSize(dimX / 10, dimY / 14);
                    exploration2_L5.setFont(Font.font("Verdana", 18));
                    exploration2_L5.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(exploration2_L5);
                    exploration2_L5.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(90);
                            modeExploration = false;
                            modeExploration2 = true;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

/**      **/

                    // Creation du menu de choix
                    Scene sceneNiveau = new Scene(rootNiveau, dimX, dimY, Color.ANTIQUEWHITE);
                    ChoixNiveau.setScene(sceneNiveau);
                    ChoixNiveau.show();
                }
            });
            rootAcc.getChildren().add(choixTaille);

            // Bouton Jouer
            Button btnPlay = new Button();
            btnPlay.setLayoutX(dimX / 3);
            btnPlay.setLayoutY(dimY / 2);
            btnPlay.setText("Play");
            btnPlay.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    Stage stageLab = new Stage();
                    primaryStage.close();
                    Group rootLab = new Group();
                    Scene sceneLab = new Scene(rootLab, dimX, dimY, Color.ANTIQUEWHITE);
                    Polygon triangle = new Polygon(3);
                    GenLabyrinthe labi = new GenLabyrinthe(taille);
                    int[][][] lab = labi.getLab();
                    setLaby(lab);
                    setMvmtNecessaire(0);
                    Chrono chrono = new Chrono();
                    chrono.start();
                    // cercle symbolisant la position du joueur
                    Circle circle = new Circle();
                    circle.setCenterX((getPositionX() + 0.5) * getTailleCase() + debutX);
                    circle.setCenterY((getPositionY() + 0.5) * getTailleCase() + debutY);
                    circle.setRadius(getTailleCase() * 3 / 7);
                    circle.setFill(Color.HOTPINK);
                    ;
                    rootLab.getChildren().add(circle);
                    //
                    setMvmt(0);
                    // determination du minimum de mvmt
                    indice(triangle, 0, 0);
                    Text tMvmtNecessaire = new Text();
                    tMvmtNecessaire.setText("   nombre de mouvements minimum : " + getMvmtNecessaire());
                    tMvmtNecessaire.setFont(Font.font("Verdana", 20));
                    tMvmtNecessaire.setFill(Color.HOTPINK);
                    tMvmtNecessaire.setLayoutX(2.5 * sceneLab.getWidth() / 3);
                    tMvmtNecessaire.setLayoutY(2.5 * sceneLab.getHeight() / 10);
                    tMvmtNecessaire.setWrappingWidth(sceneLab.getWidth() / 6);
                    rootLab.getChildren().add(tMvmtNecessaire);
                    // calcul de mvmt du joueur
                    Text tMvmt = new Text();
                    tMvmt.setText("   nombre de mouvements : " + getMvmt());
                    tMvmt.setFont(Font.font("Verdana", 20));
                    tMvmt.setFill(Color.HOTPINK);
                    tMvmt.setLayoutX(2.5 * sceneLab.getWidth() / 3);
                    tMvmt.setLayoutY(sceneLab.getHeight() / 10);
                    tMvmt.setWrappingWidth(sceneLab.getWidth() / 6);
                    rootLab.getChildren().add(tMvmt);

                    sceneLab.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent e) {
                            rootLab.getChildren().remove(triangle);
                            if (x == 0 & y == 0) {
                                if (e.getCode().equals(KeyCode.RIGHT)) {// touche
                                                                        // est
                                    if (getLaby()[y][x][1] == 0) {
                                        x += 1;
                                        setMvmt(getMvmt() + 1);
                                    }
                                }
                                if (e.getCode().equals(KeyCode.DOWN)) {// touche
                                                                        // sud
                                    if (getLaby()[y][x][2] == 0) {
                                        y += 1;
                                        setMvmt(getMvmt() + 1);
                                    }
                                }
                            } else if (x == getTaille() - 1 & y == getTaille() - 1) {
                                if (e.getCode().equals(KeyCode.RIGHT)) {// touche
                                                                        // est
                                    Stage FinDuJeu = new Stage();
                                    chrono.stop();
                                    // System.out.println(chrono.getDureeTxt());
                                    stageLab.close();
                                    Group rootFin = new Group();

                                    // Texte de compliments
                                    Text felicitations = new Text();
                                    felicitations.setText("Bravo!");
                                    felicitations.setFont(Font.font("Verdana", 100));
                                    felicitations.setFill(Color.HOTPINK);
                                    felicitations.setLayoutX(scene.getWidth() / 3);
                                    felicitations.setLayoutY(scene.getHeight() / 3);
                                    rootFin.getChildren().addAll(felicitations);

                                    Text Score = new Text();
                                    Score.setText("Vous avez mis " + chrono.getDureeTxt());
                                    Score.setFont(Font.font("Verdana", 40));
                                    Score.setFill(Color.HOTPINK);
                                    Score.setLayoutX(scene.getWidth() / 3);
                                    Score.setLayoutY(scene.getHeight() * 4 / 7);
                                    rootFin.getChildren().addAll(Score);

                                    // Bouton de retour au menu principal
                                    Button btnRetour = new Button();
                                    btnRetour.setLayoutX(dimX - dimX / 5);
                                    btnRetour.setLayoutY(dimY - dimY / 7);
                                    btnRetour.setMinSize(dimX / 10, dimY / 14);
                                    btnRetour.setFont(Font.font("Verdana", 20));
                                    btnRetour.setTextFill(Color.BLUE);
                                    btnRetour.setText("Retour au menu");
                                    btnRetour.setOnAction(new EventHandler<ActionEvent>() {
                                        public void handle(ActionEvent event) {
                                            FinDuJeu.close();
                                            start(primaryStage);
                                        }
                                    });
                                    rootFin.getChildren().addAll(btnRetour);

                                    setPositionX(0);
                                    setPositionY(0);
                                    // Creation du menu de fin
                                    Scene sceneFin = new Scene(rootFin, dimX, dimY, Color.ANTIQUEWHITE);// Color.AQUAMARINE
                                    FinDuJeu.setScene(sceneFin);
                                    FinDuJeu.show();
                                }

                                if (e.getCode().equals(KeyCode.LEFT)) {// touche
                                                                        // ouest
                                    if (getLaby()[y][x][3] == 0) {
                                        x -= 1;
                                        setMvmt(getMvmt() + 1);
                                    }
                                }
                                if (e.getCode().equals(KeyCode.UP)) {// touche
                                                                        // nord
                                    if (getLaby()[y][x][0] == 0) {
                                        y -= 1;
                                        setMvmt(getMvmt() + 1);
                                    }
                                }
                            } else {
                                if (e.getCode().equals(KeyCode.RIGHT)) {// touche
                                                                        // est
                                    if (getLaby()[y][x][1] == 0) {
                                        x += 1;
                                        setMvmt(getMvmt() + 1);
                                    }
                                }
                                if (e.getCode().equals(KeyCode.DOWN)) {// touche
                                                                        // sud
                                    if (getLaby()[y][x][2] == 0) {
                                        y += 1;
                                        setMvmt(getMvmt() + 1);
                                    }
                                }
                                if (e.getCode().equals(KeyCode.LEFT)) {// touche
                                                                        // ouest
                                    if (getLaby()[y][x][3] == 0) {
                                        x -= 1;
                                        setMvmt(getMvmt() + 1);
                                    }
                                }
                                if (e.getCode().equals(KeyCode.UP)) {
                                    if (getLaby()[y][x][0] == 0) {
                                        y -= 1;
                                        setMvmt(getMvmt() + 1);
                                    }
                                }
                            }
                            tMvmt.setText("nombre de mouvements : " + getMvmt());
                            circle.setCenterX((x + 0.5) * getTailleCase() + debutX);
                            circle.setCenterY((y + 0.5) * getTailleCase() + debutY);
                            setPositionX(x);
                            setPositionY(y);
                            if (modeExploration2) {
                                jeuExploration2.getChildren().clear();
                                creationModeExploration2(jeuExploration2);
                            }
                            if (modeExploration) {
                                jeuExploration.getChildren().clear();
                                creationModeExploration(jeuExploration);
                            }
                        }
                    });
                    // ceci permet de construire une grille
                    for (int y1 = 0; y1 <= getTaille(); y1++) {
                        for (int x1 = 0; x1 <= getTaille(); x1++) {
                            if (y1 != taille) {
                                // verification si un mur present à Nord
                                // si oui on trace un mur
                                if (murNord(x1, y1) == 1) {
                                    droiteHor(x1, y1, rootLab);
                                }
                            }
                            if (x1 != getTaille()) {
                                // verification si un mur est present à Ouest
                                if (murOuest(x1, y1) == 1) {
                                    droiteVer(x1, y1, rootLab);
                                }
                            }
                        }
                    }
                    if (!modeExploration2) {
                        jeuExploration2.getChildren().clear();
                    }
                    if (modeExploration2) {
                        creationModeExploration2(jeuExploration2);
                    }
                    rootLab.getChildren().add(jeuExploration2);
                    if (!modeExploration) {
                        jeuExploration.getChildren().clear();
                    }
                    if (modeExploration) {
                        creationModeExploration(jeuExploration);
                    }
                    rootLab.getChildren().add(jeuExploration);

                    Button btnIndice = new Button();
                    btnIndice.setLayoutX(dimX - dimX / 5);
                    btnIndice.setLayoutY(3 * dimY / 7);
                    btnIndice.setMinSize(dimX / 10, dimY / 14);
                    btnIndice.setFont(Font.font("Verdana", 20));
                    btnIndice.setTextFill(Color.BLUE);
                    btnIndice.setText("       Indice        ");
                    btnIndice.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            indice(triangle, getPositionX(), getPositionY());
                            rootLab.getChildren().add(triangle);
                        }
                    });
                    rootLab.getChildren().addAll(btnIndice);

                    Group rootResolution = new Group();
                    Button btnResolution = new Button();
                    btnResolution.setLayoutX(dimX - dimX / 5);
                    btnResolution.setLayoutY(dimY - 2 * dimY / 7);
                    btnResolution.setMinSize(dimX / 10, dimY / 14);
                    btnResolution.setFont(Font.font("Verdana", 20));
                    btnResolution.setTextFill(Color.BLUE);
                    btnResolution.setText("     Solution       ");
                    btnResolution.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            resolution(rootResolution, getPositionX(), getPositionY());
                            rootLab.getChildren().add(rootResolution);
                            modeExploration2=false;
                            jeuExploration2.getChildren().clear();
                            modeExploration=false;
                            jeuExploration.getChildren().clear();
                        }
                    });
                    rootLab.getChildren().addAll(btnResolution);

                    Button btnRecommencer = new Button();
                    btnRecommencer.setLayoutX(dimX - dimX / 5);
                    btnRecommencer.setLayoutY(dimY - 3 * dimY / 7);
                    btnRecommencer.setMinSize(dimX / 10, dimY / 14);
                    btnRecommencer.setFont(Font.font("Verdana", 20));
                    btnRecommencer.setTextFill(Color.BLUE);
                    btnRecommencer.setText(" Recommencer ");
                    btnRecommencer.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            circle.setCenterX(0.5 * getTailleCase() + debutX);
                            circle.setCenterY(0.5 * getTailleCase() + debutY);
                            setPositionX(0);
                            setPositionY(0);
                            setMvmt(0);
                            rootLab.getChildren().remove(rootResolution);
                            tMvmt.setText("nombre de mouvements : " + getMvmt());
                            if (modeExploration2) {
                                creationModeExploration2(jeuExploration2);
                            }
                            if (modeExploration) {
                                creationModeExploration(jeuExploration);
                            }
                        }
                    });
                    rootLab.getChildren().add(btnRecommencer);
                    // Retour Menu
                    Button btnRetour = new Button();
                    btnRetour.setLayoutX(dimX - dimX / 5);
                    btnRetour.setLayoutY(dimY - dimY / 7);
                    btnRetour.setMinSize(dimX / 10, dimY / 14);
                    btnRetour.setFont(Font.font("Verdana", 20));
                    btnRetour.setTextFill(Color.BLUE);
                    btnRetour.setText("Retour au menu");
                    btnRetour.setOnAction(new EventHandler<ActionEvent>() {

                        public void handle(ActionEvent event) {
                            setPositionX(0);
                            setPositionY(0);
                            stageLab.close();
                            start(primaryStage);
                        }

                    });
                    rootLab.getChildren().addAll(btnRetour);

                    stageLab.setScene(sceneLab);
                    stageLab.show();

                }
            });

            btnPlay.setMinSize(400, 100);
            btnPlay.setFont(Font.font("Verdana", 40));
            btnPlay.setTextFill(Color.BLUE);
            rootAcc.getChildren().add(btnPlay);

            // Titre du Jeu
            Text t = new Text();
            t.setText("  Labyr'INT");
            t.setFont(Font.font("Verdana", 70));
            t.setFill(Color.HOTPINK);
            t.setLayoutX(scene.getWidth() / 3);
            t.setLayoutY(scene.getHeight() / 3);
            rootAcc.getChildren().add(t);

            // Affichage de la scene
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (java.lang.NullPointerException e) {
            System.out.println("coucou");
        }
        ;
    };
}


