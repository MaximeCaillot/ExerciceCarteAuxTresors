package model;

public class Montagne {
    int x;
    int y;

    public Montagne(String chars) throws Exception {
        String[] taille = chars.replaceAll(" ", "").trim().split("-");

        if(taille.length != 3) {
            throw new Exception("Erreur sur le nombre de paramètres de la ligne d'une montagne");
        }

        try {
            this.x = Integer.parseInt(taille[1]);
            this.y = Integer.parseInt(taille[2]);
        } catch(Exception e) {
            throw new Exception("Erreur sur la ligne représentant une montagne");
        }
    }

    public boolean isOnMontagne(int x, int y) {
        return this.x == x && this.y == y;
    }

    public String toString() {
        return "M";
    }

    public String toStringAdvanced() {
        return "M - " + x + " - " + y;
    }
}
