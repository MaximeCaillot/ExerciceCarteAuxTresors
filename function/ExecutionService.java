package function;

import java.io.File;
import model.Carte;

public class ExecutionService {
    /**
     * Méthode qui permet de lancé le fichier d'exemple
     */
    public static void exemple() {
        System.out.println("");
        System.out.println("######## La carte aux trésors : Exemple ########");
        System.out.println("");
        File fichier = new File("exemple/Carte.txt");

        try {
            Carte carte = FileReader.fileToCarte(fichier);
            System.out.println("Ma Carte");
            System.out.println(carte.toString());

            System.out.println("---- Déplacement ----");
            carte = carte.deplacerAventuriers(true);
            System.out.println("---- Fin Déplacement ----");

            System.out.println(carte.toStringAdvanced());
            FileReader.CarteToFile(carte, fichier);
        } catch (Exception e) {
            System.out.println("Erreur lors de l'exécution : ");
            System.out.println(e);
        }

        System.out.println();
        System.out.println("######## Fin de la carte aux trésors ########");
    }

    /**
     * Méthode qui permet de lancé les fichiers de tests
     */
    public static void test() {
        System.out.println("######## Teste des fichiers ########");
        System.out.println();

        for(File fichier : new File("test").listFiles()) {
            System.out.println("Test : " + fichier.getName());
            try {
                Carte carte = FileReader.fileToCarte(fichier);
                carte = carte.deplacerAventuriers(false);
                FileReader.CarteToFile(carte, fichier);
            } catch (Exception e) {
                System.out.println("Erreur lors de l'exécution : ");
                System.out.println(e);
            }
        }

        System.out.println();
        System.out.println("######## Fin des tests ########");
    }

    /**
     * Méthode qui permet de lancé le fichier en argument
     */
    public static void execute(File fichier) {
        try {
            Carte carte = FileReader.fileToCarte(fichier);
            carte = carte.deplacerAventuriers(false);
            FileReader.CarteToFile(carte, fichier);
        } catch (Exception e) {
            System.out.println("Erreur lors de l'exécution : ");
            System.out.println(e);
        }
    }
}
