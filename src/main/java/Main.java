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
                        } catch (Exception _) {
                        }
                    }
                }
            } catch (Exception _) {

            }
        }
        ArrayList<BlackBeatEntry> sourceData = BlackBeatToList.parse(new File("example/from.xlsx"));
        ArrayList<MLCEntry> outData = BlackBeatToMLC.convert(sourceData);
        MLCListToXLSX.record(new File("example/new.xlsx"),outData);
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
