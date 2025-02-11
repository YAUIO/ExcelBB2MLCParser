import Entities.BlackBeatEntry;
import Entities.Human;
import Entities.Init;
import Entities.MLCEntry;
import Parsers.BlackBeatToList;
import Parsers.BlackBeatToMLC;
import Parsers.MLCListToXLSX;
import jakarta.persistence.EntityManager;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Usage program.jar datapath outpath");
        }

        Init.setDB("Names");
        File f = new File("datanames.txt");
        if (f.exists()) {
            try {
                FileInputStream fi = new FileInputStream(f);
                BufferedReader br = new BufferedReader(new InputStreamReader(fi));
                while (br.ready()) {
                    Human h = new Human();
                    h.name = br.readLine();
                    if (Init.getEntityManager().createQuery("SELECT h from Human h where h.name=:name").setParameter("name",h.name).getSingleResultOrNull() == null) {
                        try {
                            EntityManager em = Init.getEntityManager();
                            em.getTransaction().begin();
                            em.persist(h);
                            em.getTransaction().commit();
                        } catch (Exception e) {
                        }
                    }
                }
            } catch (Exception ex) {

            }
        }
        ArrayList<BlackBeatEntry> sourceData = BlackBeatToList.parse(new File(args[0]));
        ArrayList<MLCEntry> outData = BlackBeatToMLC.convert(sourceData);
        String path = "new.xlsx";
        if (args.length == 2) {
            path = args[1];
        }
        MLCListToXLSX.record(new File(path),outData);
        try {
            FileOutputStream fo = new FileOutputStream(f);
            PrintWriter p = new PrintWriter(fo, true);
            Init.getEntityManager().createQuery("SELECT h from Human h").getResultList()
                    .stream()
                    .forEach(h -> p.println(((Human)h).name));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
