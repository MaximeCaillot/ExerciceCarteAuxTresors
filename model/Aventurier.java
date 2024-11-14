package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Aventurier {
    String nom;
    String orientation;
    int x;
    int y;
    List<String> mouvement;
    int nbTresor = 0;

    public Aventurier(String chars) throws Exception {
        String[] taille = chars.replaceAll(" ", "").trim().split("-");

        if(taille.length != 6) {
            throw new Exception("Erreur sur le nombre de paramètres de la ligne d'un aventurier");
        }

        try{
            this.nom = taille[1];
            this.x = Integer.parseInt(taille[2]);
            this.y = Integer.parseInt(taille[3]);
            this.orientation = taille[4];
            this.mouvement = new ArrayList<>(Arrays.asList(taille[5].split("")));
            this.nbTresor = 0;
        } catch(Exception e) {
            throw new Exception("Erreur sur la ligne représentant un aventurier");
        }
    }

    public Aventurier deplacer(Carte carte) {
        if(this.mouvement.isEmpty()) {
            return this;
        }
        String deplacement = this.mouvement.get(0);
        switch (deplacement) {
            case "A" -> {
                if("N".equals(this.orientation) && this.y - 1 >= 0
                        && carte.montagnes.stream().noneMatch(m -> m.isOnMontagne(this.x, this.y - 1))
                        && carte.aventuriers.stream().noneMatch(a -> a.isOnAventurier(this.x, this.y - 1))) {
                    this.y = this.y-1;
                }
                else if("S".equals(this.orientation) && this.y + 1 < carte.maxY
                        && carte.montagnes.stream().noneMatch(m -> m.isOnMontagne(this.x, this.y + 1))
                        && carte.aventuriers.stream().noneMatch(a -> a.isOnAventurier(this.x, this.y + 1))) {
                    this.y = this.y+1;
                }
                else if("E".equals(this.orientation) && this.x + 1 < carte.maxX
                        && carte.montagnes.stream().noneMatch(m -> m.isOnMontagne(this.x + 1, this.y))
                        && carte.aventuriers.stream().noneMatch(a -> a.isOnAventurier(this.x + 1, this.y))) {
                    this.x = this.x+1;
                }
                else if("O".equals(this.orientation) && this.x - 1 >= 0
                        && carte.montagnes.stream().noneMatch(m -> m.isOnMontagne(this.x - 1, this.y))
                        && carte.aventuriers.stream().noneMatch(a -> a.isOnAventurier(this.x - 1, this.y))) {
                    this.x = this.x-1;
                }
            }
            case "G" -> {
                if(null != this.orientation) {
                    switch (this.orientation) {
                        case "N":
                            this.orientation = "O";
                            break;
                        case "S":
                            this.orientation = "E";
                            break;
                        case "E":
                            this.orientation = "N";
                            break;
                        case "O":
                            this.orientation = "S";
                            break;
                        default:
                            break; 
                        }
                    }
                }
            case "D" -> {
                if(null != this.orientation) {
                    switch (this.orientation) {
                        case "N":
                            this.orientation = "E";
                            break;
                        case "S":
                            this.orientation = "O";
                            break;
                        case "E":
                            this.orientation = "S";
                            break;
                        case "O":
                            this.orientation = "N";
                            break;
                        default:
                            break;
                        }
                    }
                }
            default -> {
            }
        }
        this.mouvement.remove(0);
        return this;
    }

    public boolean isOnAventurier(int x, int y) {
        return this.x == x && this.y == y;
    }

    public String toString() {
        return "A("+ nom + ")";
    }

    public String toStringAdvanced() {
        return "A - "+ nom + " - " + x + " - " + y + " - "+ orientation + " - " + nbTresor;
    }

    public String toStringWithMouvement() {
        return "A - "+ nom + " - " + x + " - " + y + " - "+ orientation + " - " + mouvement + " - " + nbTresor;
    }
}
