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



public class InterfaceLab extends Application {

    public static void main(String[] args) {
        Application.launch(InterfaceLab.class, args);
    }



    /** <h2>Initialisation des données </h2> */



    /** Taille du labyrinthe*/
    int taille =10;

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    /** Initialisation du dimensionnement de labyrinthe */
    int debutX = 20;
    int debutY = 20;
    int finX = 930;
    int finY = 930;


    /** Creation d'un objet de GenLabyrinthe: generation aleatoire d'un labyrinthe */
    GenLabyrinthe labi = new GenLabyrinthe(getTaille());

    int[][][] lab = labi.getLab();

    public int[][][] getLaby() {
        return lab;
    }

    public void setLaby(int[][][] lab) {
        this.lab = lab;
    }


    /** Definition de la position du joueur en x */
    int x = 0;

    public int getPositionX() {
        return x;
    }

    public void setPositionX(int x) {
        this.x = x;
    }

    /** Definition de la position du joueur en y */
    int y = 0;

    public int getPositionY() {
        return y;
    }

    public void setPositionY(int y) {
        this.y = y;
    }


    /** Definition de la dimension d'une case de labyrinthe */
    int tailleCase = (finX - debutX) / getTaille();

    public int getTailleCase() {
        return (finX - debutX) / getTaille();
    }

    /** Dimension d'un stage:  */
    int dimX = finX - debutX + 300;
    int dimY = finY - debutY + 100;

    /** Definition du nombre de mouvements fait par joueur */
    int mvmt = 0;

    public void setMvmt(int mvmt) {
        this.mvmt = mvmt;
    }

    public int getMvmt() {
        return mvmt;
    }

    /** Definition du nombre de mouvements minimum necessaires pour gagner au labyrinthe */
    int mvmtNecessaire = 0;

    public void setMvmtNecessaire(int mvmtN) {
        this.mvmtNecessaire = mvmtN;
    }

    public int getMvmtNecessaire() {
        return mvmtNecessaire;
    }





    /** <h2> Methodes de creation graphique du labyrinthe <h2>*/


    /** Creation d'une droite horizontale à partir du point i,j au point i+1,j */
    void droiteHor(int j, int i, Group root) {
        Line line = new Line();
        line.setStartX(debutX + i * getTailleCase());
        line.setStartY(debutY + j * getTailleCase());
        line.setEndX(debutX + (i + 1) * getTailleCase());
        line.setEndY(debutY + j * getTailleCase());
        line.setStroke(Color.BLUE);
        line.setStrokeWidth(2);
        /** Ajout d'une ligne dans l'ensemble de l'interface graphique */
        root.getChildren().add(line);
    };

    /** Creation d'une droite verticale à partir du point i,j au point i,j+1 */
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


    /** Detection d'un mur à l'ouest de la position (i,j) */
    int murOuest(int i, int j) {
        /**Entree toujours au nord ouest */
        if (i == 0 & j == 0) {
            return 0;
        }
        /** Si on depasse le tableau, on prend l'element voisin et on regarde la presence du mur à l'est */
        if (j == getTaille()) {
            if (i == getTaille() - 1) {
                return 0;
            }
            return getLaby()[i][j - 1][1];
        }
        return getLaby()[i][j][3];
    };


    /** Detection d'un mur au nord de la position (i,j) */
    int murNord(int i, int j) {
        /** Si on depasse le tableau, on prend l'element voisin et on regarde la presence du mur au sud */
        if (i == taille) {
            return getLaby()[i - 1][j][2];
        }
        return getLaby()[i][j][0];
    };




    /** Methode de resolution du labyrinthe <br>
     * Renvoie vrai s'il existe un passage entre la case de fin et la position actuelle; et rajoute la ligne correspondante si ce lien existe */
    /**solutionRecursive
     * @param root
     * @param positionX
     * @param positionY
     * @param departX
     * @param departY
     * @return
     */
    public boolean solutionRecursive(Group root, int positionX, int positionY, int departX, int departY) {
        Line line = new Line();
        line.setStartX(debutX + (0.5 + positionX) * getTailleCase());
        line.setStartY(debutY + (0.5 + positionY) * getTailleCase()); /** On se place au milieu de la case */
        line.setEndX(debutX + (0.5 + departX) * getTailleCase());
        line.setEndY(debutY + (0.5 + departY) * getTailleCase());
        line.setStroke(Color.RED);
        line.setStrokeWidth(2);
        if (getLaby().length - 1 == positionX & getLaby().length - 1 == positionY) { /** Si on est sur la case d'arrivee */
            root.getChildren().add(line);
            return true;
        }
        if (lab[positionY][positionX][0] == 0 & positionY - 1 != departY) { /** Si il n'y a pas de mur au nord et que l'on ne vient pas precedement de cette direction */
            if (solutionRecursive(root, positionX, positionY - 1, positionX, positionY)) {
                root.getChildren().add(line);
                return true;
            }
        }
        if (lab[positionY][positionX][1] == 0 & positionX + 1 != departX) { /**Idem est */
            if (solutionRecursive(root, positionX + 1, positionY, positionX, positionY)) {
                root.getChildren().add(line);
                return true;
            }
        }
        if (lab[positionY][positionX][2] == 0 & positionY + 1 != departY) {  /** Idem sud */
            if (solutionRecursive(root, positionX, positionY + 1, positionX, positionY)) {
                root.getChildren().add(line);
                return true;
            }
        }
        if (getLaby()[positionY][positionX][3] == 0 & positionX - 1 != departX) { /** Idem ouest */
            if (solutionRecursive(root, positionX - 1, positionY, positionX, positionY)) {
                root.getChildren().add(line);
                return true;
            }
        }
        return false;
    }

    /** Initialisation de la methode resolution*/
    /** Cette fois on peut revenir en arriere et aller dans les 4 directions contrairement a solutionRecursive */
    /**resolution
     * @param root
     * @param positionX
     * @param positionY
     */
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
        int[][] T = getCasesInconnues();
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
        if (getPositionX() == 0 & getPositionY() == 0 ) { /** Position initiale */
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




    /**Methode indiceRecursive <br>
     * Donne au joueur une direction precise pour
     * trouver la sortie
     * @param positionX
     * @param positionY
     * @param departX
     * @param departY
     * @return
     */
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

    /** Initialisation de la methode indice <br>
     * Raisonnement du code similaire a celui de resolution
     * @param triangle indiquant la direction conseillée
     * @param positionX
     * @param positionY
     */
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






    /** <h2> Construction des stages <h2> */
    @Override
    public void start(Stage primaryStage) {
        try {

            Group rootAcc = new Group();
            Scene scene = new Scene(rootAcc, dimX, dimY, Color.ANTIQUEWHITE);

            /** Bouton de Sortie */
            Button closebutton = new Button();
            closebutton.setOnAction(e -> primaryStage.close());
            closebutton.setText("Exit");
            closebutton.setMinSize(dimX / 8, dimY / 12);
            closebutton.setLayoutX(dimX - dimX / 8);
            closebutton.setLayoutY(dimY - dimY / 12);
            closebutton.setFont(Font.font("Verdana", 20));
            closebutton.setTextFill(Color.RED);
            rootAcc.getChildren().add(closebutton);


            /** Bouton Choix de taille */
            Button choixTaille = new Button();
            choixTaille.setText("Choix de la taille");
            choixTaille.setMinSize(dimX / 8, dimY / 12);
            choixTaille.setLayoutX(0);
            choixTaille.setLayoutY(dimY - dimY / 12);
            choixTaille.setFont(Font.font("Verdana", 20));
            choixTaille.setTextFill(Color.BLUE);
            choixTaille.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {

                    /** Stage du Menu de choix */
                    Stage ChoixNiveau = new Stage();
                    primaryStage.close();
                    Group rootNiveau = new Group();

                    /** Instructions */
                    Text indication = new Text();
                    indication.setText("    Choisissez un niveau");
                    indication.setFont(Font.font("Verdana", 40));
                    indication.setFill(Color.BLUE);
                    indication.setLayoutX(scene.getWidth() / 4);
                    indication.setLayoutY(scene.getHeight() / 3);
                    rootNiveau.getChildren().addAll(indication);

                    /** Retour au Menu */
                    Button btnRetour = new Button();
                    btnRetour.setLayoutX(dimX - dimX / 5);
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

                    /**Definition des differents niveaux*/
                    Text classique = new Text();
                    classique.setText("MODE CLASSIQUE:");
                    classique.setFont(Font.font("Verdana", 20));
                    classique.setFill(Color.BLUE);
                    classique.setX(dimX*2/5);
                    classique.setY(dimY*3/7);
                    rootNiveau.getChildren().addAll(classique);

                    Button classiqueL1 = new Button();
                    classiqueL1.setLayoutX(dimX *4/ 24);
                    classiqueL1.setLayoutY(dimY / 2);
                    classiqueL1.setMinSize(dimX / 10, dimY / 14);
                    classiqueL1.setFont(Font.font("Verdana", 18));
                    classiqueL1.setTextFill(Color.BLUE);
                    classiqueL1.setText("Niveau 1\n 10*10");
                    rootNiveau.getChildren().addAll(classiqueL1);
                    classiqueL1.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(10);
                            modeExploration = false;  /**Chaque bouton selectionne une taille et un mode */
                            ChoixNiveau.close();
                            start(primaryStage); /** Retour au Menu principal */
                        }
                    });

                    Button classiqueL2 = new Button();
                    classiqueL2.setLayoutX(dimX * 7 / 24);
                    classiqueL2.setLayoutY(dimY / 2);
                    classiqueL2.setText("Niveau 2\n 30*30");
                    classiqueL2.setMinSize(dimX / 10, dimY / 14);
                    classiqueL2.setFont(Font.font("Verdana", 18));
                    classiqueL2.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(classiqueL2);
                    classiqueL2.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(30);
                            modeExploration = false;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button classiqueL3 = new Button();
                    classiqueL3.setLayoutX(dimX * 10 / 24);
                    classiqueL3.setLayoutY(dimY / 2);
                    classiqueL3.setText("Niveau 3\n 50*50");
                    classiqueL3.setMinSize(dimX / 10, dimY / 14);
                    classiqueL3.setFont(Font.font("Verdana", 18));
                    classiqueL3.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(classiqueL3);
                    classiqueL3.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(50);
                            modeExploration = false;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button classiqueL4 = new Button();
                    classiqueL4.setLayoutX(dimX * 13 / 24);
                    classiqueL4.setLayoutY(dimY / 2);
                    classiqueL4.setText("Niveau 4\n 70*70");
                    classiqueL4.setMinSize(dimX / 10, dimY / 14);
                    classiqueL4.setFont(Font.font("Verdana", 18));
                    classiqueL4.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(classiqueL4);
                    classiqueL4.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(70);
                            modeExploration = false;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button classiqueL5 = new Button();
                    classiqueL5.setLayoutX(dimX * 16 / 24);
                    classiqueL5.setLayoutY(dimY / 2);
                    classiqueL5.setText("Niveau 5\n 90*90");
                    classiqueL5.setMinSize(dimX / 10, dimY / 14);
                    classiqueL5.setFont(Font.font("Verdana", 18));
                    classiqueL5.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(classiqueL5);
                    classiqueL5.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(90);
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
                    exploration.setY(dimY*4.8/7);
                    rootNiveau.getChildren().addAll(exploration);

                    Button explorationL1 = new Button();
                    explorationL1.setLayoutX(dimX* 4/ 24);
                    explorationL1.setLayoutY(dimY *3 / 4);
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
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button explorationL2 = new Button();
                    explorationL2.setLayoutX(dimX * 7/24);
                    explorationL2.setLayoutY(dimY*3 / 4);
                    explorationL2.setText("Niveau 2\n 30*30");
                    explorationL2.setMinSize(dimX / 10, dimY / 14);
                    explorationL2.setFont(Font.font("Verdana", 18));
                    explorationL2.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(explorationL2);
                    explorationL2.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(30);
                            modeExploration = true;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button explorationL3 = new Button();
                    explorationL3.setLayoutX(dimX * 10 / 24);
                    explorationL3.setLayoutY(dimY * 3 / 4);
                    explorationL3.setText("Niveau 3\n 50*50");
                    explorationL3.setMinSize(dimX / 10, dimY / 14);
                    explorationL3.setFont(Font.font("Verdana", 18));
                    explorationL3.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(explorationL3);
                    explorationL3.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(50);
                            modeExploration = true;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button explorationL4 = new Button();
                    explorationL4.setLayoutX(dimX * 13 / 24);
                    explorationL4.setLayoutY(dimY * 3 / 4);
                    explorationL4.setText("Niveau 4\n 70*70");
                    explorationL4.setMinSize(dimX / 10, dimY / 14);
                    explorationL4.setFont(Font.font("Verdana", 18));
                    explorationL4.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(explorationL4);
                    explorationL4.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(70);
                            modeExploration = true;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    Button explorationL5 = new Button();
                    explorationL5.setLayoutX(dimX * 16 / 24);
                    explorationL5.setLayoutY(dimY * 3 / 4);
                    explorationL5.setText("Niveau 5\n 90*90");
                    explorationL5.setMinSize(dimX / 10, dimY / 14);
                    explorationL5.setFont(Font.font("Verdana", 18));
                    explorationL5.setTextFill(Color.BLUE);
                    rootNiveau.getChildren().addAll(explorationL5);
                    explorationL5.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            setTaille(90);
                            modeExploration = true;
                            ChoixNiveau.close();
                            start(primaryStage);
                        }
                    });

                    /** Creation du Menu de choix */
                    Scene sceneNiveau = new Scene(rootNiveau, dimX, dimY, Color.ANTIQUEWHITE);
                    ChoixNiveau.setScene(sceneNiveau);
                    ChoixNiveau.show();
                }
            });
            rootAcc.getChildren().add(choixTaille);

            /** Bouton Jouer */
            Button btnPlay = new Button();
            btnPlay.setLayoutX(dimX / 3);
            btnPlay.setLayoutY(dimY / 2);
            btnPlay.setText("Play");
            btnPlay.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    /** Creation du stage de Jeu */
                    Stage stageLab = new Stage();
                    primaryStage.close();
                    Group rootLab = new Group();
                    Scene sceneLab = new Scene(rootLab, dimX, dimY, Color.ANTIQUEWHITE);
                    Polygon triangle = new Polygon(3);
                    GenLabyrinthe labi = new GenLabyrinthe(taille);
                    int[][][] lab = labi.getLab();
                    setLaby(lab);
                    setMvmtNecessaire(0);
                    setMvmt(0);
                    /**Activation du chronometre */
                    Chrono chrono = new Chrono();
                    chrono.start();

                    /** Cercle symbolisant la position du joueur */
                    Circle circle = new Circle();
                    circle.setCenterX((getPositionX() + 0.5) * getTailleCase() + debutX);
                    circle.setCenterY((getPositionY() + 0.5) * getTailleCase() + debutY);
                    circle.setRadius(getTailleCase() * 3 / 7);
                    circle.setFill(Color.HOTPINK);
                    ;
                    rootLab.getChildren().add(circle);


                    /** Determination du nombre minimum de mouvements */
                    indice(triangle, 0, 0);
                    Text tMvmtNecessaire = new Text();
                    tMvmtNecessaire.setText("   Nombre de mouvements minimum : " + getMvmtNecessaire());
                    tMvmtNecessaire.setFont(Font.font("Verdana", 20));
                    tMvmtNecessaire.setFill(Color.HOTPINK);
                    tMvmtNecessaire.setLayoutX(2.5 * sceneLab.getWidth() / 3);
                    tMvmtNecessaire.setLayoutY(2.5 * sceneLab.getHeight() / 10);
                    tMvmtNecessaire.setWrappingWidth(sceneLab.getWidth() / 6);
                    rootLab.getChildren().add(tMvmtNecessaire);

                    /** Calcul du nombre de mouvements du joueur */
                    Text tMvmt = new Text();
                    tMvmt.setText("   Nombre de mouvements : " + getMvmt());
                    tMvmt.setFont(Font.font("Verdana", 20));
                    tMvmt.setFill(Color.HOTPINK);
                    tMvmt.setLayoutX(2.5 * sceneLab.getWidth() / 3);
                    tMvmt.setLayoutY(sceneLab.getHeight() / 10);
                    tMvmt.setWrappingWidth(sceneLab.getWidth() / 6);
                    rootLab.getChildren().add(tMvmt);


                    /** Detection des touches, deplacement du joueur */
                    sceneLab.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent e) {
                            rootLab.getChildren().remove(triangle);
                            if (x == 0 & y == 0) { /** Case Intiale */
                                if (e.getCode().equals(KeyCode.RIGHT)) {/** touche est */
                                    if (getLaby()[y][x][1] == 0) { /** si absence du mur */
                                        x += 1;
                                        setMvmt(getMvmt() + 1);
                                    }
                                }
                                if (e.getCode().equals(KeyCode.DOWN)) {/** touche sud */
                                    if (getLaby()[y][x][2] == 0) {
                                        y += 1;
                                        setMvmt(getMvmt() + 1);
                                    }
                                }
                            } else if (x == getTaille() - 1 & y == getTaille() - 1) { /** Case finale */
                                if (e.getCode().equals(KeyCode.RIGHT)) {/** touche est */

                                    /** Creation du stage de fin */
                                    Stage FinDuJeu = new Stage();
                                    chrono.stop();
                                    stageLab.close();
                                    Group rootFin = new Group();

                                    /** Texte de compliments */
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

                                    /** Bouton de retour au Menu principal */
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

                                    /** Creation du Menu de fin */
                                    Scene sceneFin = new Scene(rootFin, dimX, dimY, Color.ANTIQUEWHITE);// Color.AQUAMARINE
                                    FinDuJeu.setScene(sceneFin);
                                    FinDuJeu.show();
                                }

                                if (e.getCode().equals(KeyCode.LEFT)) {/**touche ouest */
                                    if (getLaby()[y][x][3] == 0) {
                                        x -= 1;
                                        setMvmt(getMvmt() + 1);
                                    }
                                }
                                if (e.getCode().equals(KeyCode.UP)) {/** touche nord */
                                    if (getLaby()[y][x][0] == 0) {
                                        y -= 1;
                                        setMvmt(getMvmt() + 1);
                                    }
                                }
                            } else {
                                if (e.getCode().equals(KeyCode.RIGHT)) {/** touche est */
                                    if (getLaby()[y][x][1] == 0) {
                                        x += 1;
                                        setMvmt(getMvmt() + 1);
                                    }
                                }
                                if (e.getCode().equals(KeyCode.DOWN)) {/** touche sud */
                                    if (getLaby()[y][x][2] == 0) {
                                        y += 1;
                                        setMvmt(getMvmt() + 1);
                                    }
                                }
                                if (e.getCode().equals(KeyCode.LEFT)) {/** touche ouest */
                                    if (getLaby()[y][x][3] == 0) {
                                        x -= 1;
                                        setMvmt(getMvmt() + 1);
                                    }
                                }
                                if (e.getCode().equals(KeyCode.UP)) { /** touche nord */
                                    if (getLaby()[y][x][0] == 0) {
                                        y -= 1;
                                        setMvmt(getMvmt() + 1);
                                    }
                                }
                            }

                            /** Actualisation de la position et du nombre de mouvements <br> et du mode Exploration*/
                            tMvmt.setText("nombre de mouvements : " + getMvmt());
                            circle.setCenterX((x + 0.5) * getTailleCase() + debutX);
                            circle.setCenterY((y + 0.5) * getTailleCase() + debutY);
                            setPositionX(x);
                            setPositionY(y);

                            if (modeExploration) {
                                jeuExploration.getChildren().clear();
                                creationModeExploration(jeuExploration);
                            }
                        }
                    });


                    /** Construction de la grille du labyrinthe*/
                    for (int y1 = 0; y1 <= getTaille(); y1++) {
                        for (int x1 = 0; x1 <= getTaille(); x1++) {
                            if (y1 != taille) {
                                /** Si un mur present au Nord <br> si oui on trace un mur */
                                if (murNord(x1, y1) == 1) {
                                    droiteHor(x1, y1, rootLab);
                                }
                            }
                            if (x1 != getTaille()) {
                                /** Si un mur est present au l' Ouest */
                                if (murOuest(x1, y1) == 1) {
                                    droiteVer(x1, y1, rootLab);
                                }
                            }
                        }
                    }

                    /** Gestion du mode exploration */
                    if (!modeExploration) {
                        jeuExploration.getChildren().clear();
                    }
                    if (modeExploration) {
                        int[][] casesInconnues = new int[getTaille()][getTaille()];
                        setCasesInconnues(casesInconnues);
                        creationModeExploration(jeuExploration);
                    }
                    rootLab.getChildren().add(jeuExploration);

                    /** Bouton indice */
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

                    /** Bouton Resolution */
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
                            rootLab.getChildren().remove(triangle);
                            resolution(rootResolution, getPositionX(), getPositionY());
                            rootLab.getChildren().add(rootResolution);
                            jeuExploration.getChildren().clear();
                        }
                    });
                    rootLab.getChildren().addAll(btnResolution);

                    /**Bouton Recommencer */
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
                            rootLab.getChildren().remove(triangle);
                            rootLab.getChildren().remove(rootResolution);
                            tMvmt.setText("Nombre de mouvements : " + getMvmt());
                            if (modeExploration) {
                                int[][] casesInconnues = new int[getTaille()][getTaille()];
                                setCasesInconnues(casesInconnues);
                                creationModeExploration(jeuExploration);
                            }
                        }
                    });
                    rootLab.getChildren().add(btnRecommencer);

                    /** Retour au Menu */
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

            /** Titre du Jeu */
            Text t = new Text();
            t.setText("  Labyr'INT");
            t.setFont(Font.font("Verdana", 70));
            t.setFill(Color.HOTPINK);
            t.setLayoutX(scene.getWidth() / 3);
            t.setLayoutY(scene.getHeight() / 3);
            rootAcc.getChildren().add(t);

            /** Affichage de la scene */
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (java.lang.NullPointerException e) {
            System.out.println("java.lang.NullPointerException");
                    }
        ;
    };
}




