package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carte {
    int taille;
    Map<Integer,List<String>> carte;

    List<Aventurier> aventuriers;
    List<Montagne> montagnes;
    List<Tresor> tresors;
    int maxX;
    int maxY;

    public Carte() {
        this.taille = 0;
        this.maxX = 0;
        this.maxY = 0;
        this.carte = new HashMap<>();
        this.aventuriers = new ArrayList<>();
        this.montagnes = new ArrayList<>();
        this.tresors = new ArrayList<>();
    }

    /**
     * Constructeur d'une Carte à partir d'une chaîne de charactère
     * ex : C - 3 - 4
     * Crée une Carte vide de taille 12 (3 abcisse, 4 ordonnée)
     * @param chars
     * @throws Exception
     */
    public Carte(String chars) throws Exception {
        this.aventuriers = new ArrayList<>();
        this.montagnes = new ArrayList<>();
        this.tresors = new ArrayList<>();

        String[] taille = chars.replaceAll(" ", "").trim().split("-");

        if(taille.length != 3 || !taille[0].equals("C")) {
            throw new Exception("Erreur sur le nombre de paramètres de la ligne repésentant la carte");
        }

        try {
            this.maxX = Integer.parseInt(taille[1]);
            this.maxY = Integer.parseInt(taille[2]); 
            this.taille = maxX * maxY;
        } catch(Exception e) {
            throw new Exception("Erreur sur la ligne représentant la carte");
        }

        if(this.taille == 0) {
            throw new Exception("La taille de la carte est de 0");
        }
        
        this.carte = new HashMap<>();
        for(int i = 0 ; i < maxY ; i++) {
            List<String> tmp = new ArrayList<>();
            for(int j = 0 ; j < maxX ; j++) {
                tmp.add(".");
            }
            this.carte.put(i, tmp);
        }
    }

    /**
     * Ajoute un Aventurier à la Carte
     * @param aventurier
     * @return la Carte
     * @throws Exception 
     */
    public Carte ajouterAventurier(Aventurier aventurier) throws Exception {
        boolean isOnAventurier = this.aventuriers.stream().anyMatch(a -> a.isOnAventurier(aventurier.x, aventurier.y));
        boolean isOnMontagne = this.montagnes.stream().anyMatch(m -> m.isOnMontagne(aventurier.x, aventurier.y));
        if(aventurier.x >= this.maxX || aventurier.y >= this.maxY || isOnAventurier || isOnMontagne) {
            throw new Exception("Position de l'aventurier impossible sur la carte.");
        }
        this.aventuriers.add(aventurier);
        List<String> line = this.carte.get(aventurier.y);
        line.set(aventurier.x, aventurier.toString());
        return this;
    }

    /**
     * Ajoute un Trésor à la Carte
     * @param tresor
     * @return la Carte
     * @throws Exception 
     */
    public Carte ajouterTresor(Tresor tresor) throws Exception {
        boolean isOnMontagne = this.montagnes.stream().anyMatch(m -> m.isOnMontagne(tresor.x, tresor.y));
        boolean isOnTresor = this.tresors.stream().anyMatch(a -> a.isOnTresor(tresor.x, tresor.y));
        if(tresor.x >= this.maxX || tresor.y >= this.maxY || isOnMontagne || isOnTresor) {
            throw new Exception("Position du trésor impossible sur la carte.");
        }
        this.tresors.add(tresor);
        List<String> line = this.carte.get(tresor.y);
        line.set(tresor.x, tresor.toString());
        return this;
    }

    /**
     * Ajoute une Montagne à la Carte
     * @param montagne
     * @return la Carte
     * @throws Exception
     */
    public Carte ajouterMontagne(Montagne montagne) throws Exception {
        boolean isOnMontagne = this.montagnes.stream().anyMatch(m -> m.isOnMontagne(montagne.x, montagne.y));
        if(montagne.x >= this.maxX || montagne.y >= this.maxY || isOnMontagne) {
            throw new Exception("Position de la montagne impossible sur la carte.");
        }
        this.montagnes.add(montagne);
        List<String> line = this.carte.get(montagne.y);
        line.set(montagne.x, montagne.toString());
        return this;
    }

    /**
     * Effectue tous les déplacements des aventuriers sur la Carte (Tour par Tour)
     * @return la Carte avec les déplacements effectués
     */
    public Carte deplacerAventuriers(boolean print) {
        int maxMouv = 0;
        for(Aventurier a : this.aventuriers) {
            if(a.mouvement.size() > maxMouv) {
                maxMouv = a.mouvement.size();
            }
        }

        for(int i = 0 ; i < maxMouv ; i++) {
            this.deplacer();
            this.updateCarte();
            if(print) {
                System.err.println("-- Tour "+ i + " --");
                System.err.println(this.toStringAdvanced());
            }
        }

        return this;
    }

    /**
     * Effectue un déplacement des aventuriers sur la Carte (un Tour)
     * @return la Carte
     */
    public Carte deplacer() {
        for(Aventurier a : this.aventuriers) {
            int oldX = a.x;
            int oldY = a.y;
            a = a.deplacer(this);
            // Un aventurier doit avoir bouger d'emplacement pour récupérer un trésor
            if(a.x != oldX || a.y != oldY) {
                this.recupererTresor(a);
            }
        }
        return this;
    }

    /**
     * Permet aux aventuriers de récupérer le trésor selon certaines conditions
     * Il faut qu'un aventurier se soit déplacer au tour d'avant (Avancé)
     * @param aventurier
     * @return la Carte
     */
    private Carte recupererTresor(Aventurier aventurier) {
        for(Tresor t : this.tresors) {
            if(t.isOnTresor(aventurier.x, aventurier.y)) {
                t.nombre -= 1;
                aventurier.nbTresor += 1;
            }
        }

        List<Tresor> newTresors = new ArrayList<>();
        for(Tresor t : this.tresors) {
            if(t.nombre != 0) {
                newTresors.add(t);
            }
        }

        this.tresors = newTresors;

        return this;
    }

    /**
     * Mets à jour la Carte
     * @return la Carte
     */
    private Carte updateCarte() {
        for(int i = 0 ; i < maxY ; i++) {
            List<String> tmp = new ArrayList<>();
            for(int j = 0 ; j < maxX ; j++) {
                tmp.add("."); 
            }
            this.carte.put(i, tmp);
        }

        for(Montagne m : this.montagnes) {
            List<String> line = this.carte.get(m.y);
            line.set(m.x, m.toString());
            this.carte.put(m.y,line);
        }
        for(Tresor t : this.tresors) {
            List<String> line = this.carte.get(t.y);
            line.set(t.x, t.toString());
            this.carte.put(t.y,line);
        }
        for(Aventurier a : this.aventuriers) {
            List<String> line = this.carte.get(a.y);
            line.set(a.x, a.toString());
            this.carte.put(a.y,line);
        }
        return this;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        for(List<String> line : this.carte.values()) {
            for(String s : line) {
                buffer.append(s);
                buffer.append("\t");
            }
            buffer.append("\n");
        }

        return buffer.toString();
    }

    public String toStringAdvanced() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("### Ma carte ### \n");
        buffer.append("Taille = " + this.taille + "\n");

        buffer.append("Aventuriers : \n");
        for(Aventurier a : this.aventuriers) {
            buffer.append(a.toStringAdvanced() + "\n");
        }
        buffer.append("Montagnes : \n");
        for(Montagne m : this.montagnes) {
            buffer.append(m.toStringAdvanced() + "\n");
        }
        buffer.append("Trésors : \n");
        for(Tresor t : this.tresors) {
            buffer.append(t.toStringAdvanced() + "\n");
        }
        buffer.append("\n");
        buffer.append("\n");
        buffer.append("La carte : \n");

        for(List<String> line : this.carte.values()) {
            for(String s : line) {
                buffer.append(s);
                buffer.append("\t");
            }
            buffer.append("\n");
        }

        return buffer.toString();
    }

    public String toStringForFile() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("C - " + maxX + " - " + maxY + "\n");
        for(Montagne m : this.montagnes) {
            buffer.append(m.toStringAdvanced() + "\n");
        }
        for(Tresor t : this.tresors) {
            buffer.append(t.toStringAdvanced() + "\n");
        }
        for(Aventurier a : this.aventuriers) {
            buffer.append(a.toStringAdvanced() + "\n");
        }

        return buffer.toString();
    }
}