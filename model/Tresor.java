package model;

public class Tresor {
    int nombre;
    int x;
    int y;

    public Tresor(String chars) throws Exception {
        String[] taille = chars.replaceAll(" ", "").trim().split("-");

        if(taille.length != 4) {
            throw new Exception("Erreur sur le nombre de paramètres de la ligne d'un trésor");
        }

        try {
            this.x = Integer.parseInt(taille[1]);
            this.y = Integer.parseInt(taille[2]);
            this.nombre = Integer.parseInt(taille[3]);
        } catch(Exception e) {
            throw new Exception("Erreur sur la ligne représentant un trésor");
        }
    }

    public boolean isOnTresor(int x, int y) {
        return this.x == x && this.y == y;
    }

    public String toString() {
        return "T("+ nombre + ")";
    }

    public String toStringAdvanced() {
        return "T - " + x + " - " + y + " - " + nombre;
    }
}
