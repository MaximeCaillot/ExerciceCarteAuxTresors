package function;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import model.Aventurier;
import model.Carte;
import model.Montagne;
import model.Tresor;

public class FileReader {
    
    /**
     * Traduction d'un fichier en un objet Carte
     * Ajoute les Aventuriers, Montagnes et Trésors dans la Carte
     * @param file
     * @return la Carte
     * @throws Exception
     */
    public static Carte fileToCarte(File file) throws Exception {
        Carte carte = null;

        try (BufferedReader br = Files.newBufferedReader(Paths.get(file.getPath()))) {
            String line;
            boolean montagneDefined = false;
            boolean tresorDefined = false;
            boolean aventurierDefined = false;
            while ((line = br.readLine()) != null) {
                if(!line.equals("") && line.charAt(0) != '#') {
                    if(line.charAt(0) == 'C') {
                        carte = new Carte(line);
                    }
                    if(line.charAt(0) == 'M') {
                        if(Objects.isNull(carte)) {
                            throw new Exception("La carte doit être défini en premier dans le fichier.");
                        }
                        if(aventurierDefined || tresorDefined) {
                            throw new Exception("Une montagne ne peut pas être défini après un aventurier ou un trésor.");
                        }
                        montagneDefined = true;
                        Montagne montagne = new Montagne(line);
                        carte.ajouterMontagne(montagne);
                    }
                    if(line.charAt(0) == 'T') {
                        if(Objects.isNull(carte)) {
                            throw new Exception("La carte doit être défini en premier dans le fichier.");
                        }
                        if(!montagneDefined) {
                            throw new Exception("Les montagnes doivent être défini avant un trésor.");
                        }
                        if(aventurierDefined) {
                            throw new Exception("Un trésor ne peut pas être défini après un aventurier.");
                        }
                        tresorDefined = true;
                        Tresor tresor = new Tresor(line);
                        carte.ajouterTresor(tresor);
                    }
                    if(line.charAt(0) == 'A') {
                        if(Objects.isNull(carte)) {
                            throw new Exception("La carte doit être défini en premier dans le fichier.");
                        }
                        if(!tresorDefined || !montagneDefined) {
                            throw new Exception("Un aventurier doit être défini après les trésors et les montagnes.");
                        }
                        aventurierDefined = true;
                        Aventurier aventurier = new Aventurier(line);
                        carte.ajouterAventurier(aventurier);
                    }
                }
            }
        }

        return carte;
    }
    
    /**
     * Ecriture du résultat d'une carte dans un fichier output
     * @param carte
     * @param fichier
     * @throws Exception
     */
    public static void CarteToFile(Carte carte, File fichier) throws Exception {
        String str = carte.toStringForFile();
        FileOutputStream outputStream = new FileOutputStream("output/output-" + fichier.getName());
        byte[] strToBytes = str.getBytes();
        outputStream.write(strToBytes);
        outputStream.close();
    }
}
