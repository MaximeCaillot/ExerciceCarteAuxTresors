import function.ExecutionService;
import java.io.File;

public class CarteAuxTresors {

    public static void main(String[] args) throws Exception {
        if(args.length > 2) {
            throw new Exception("Le nombre d'argument maximum est 1");
        }

        else if(args.length == 0) {
            System.out.println("Aucun argument");
            System.out.println("Le fichier d'exemple est lancé.");
            ExecutionService.exemple();
        }

        else if(args[0].equals("test")) {
            System.out.println("Les fichiers de testes sont lancés.");
            ExecutionService.test();
        }

        else {
            ExecutionService.execute(new File(args[0]));
        }
    }
}