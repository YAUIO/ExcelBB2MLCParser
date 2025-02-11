import Entities.BlackBeatEntry;
import Entities.Init;
import Entities.MLCEntry;
import Parsers.BlackBeatToList;
import Parsers.BlackBeatToMLC;
import Parsers.MLCListToXLSX;

import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Init.setDB("Names");
        //System.out.println(Init.getEntityManager().createQuery("SELECT h from Human h").getResultList());
        ArrayList<BlackBeatEntry> sourceData = BlackBeatToList.parse(new File("example/from.xlsx"));
        ArrayList<MLCEntry> outData = BlackBeatToMLC.convert(sourceData);
        MLCListToXLSX.record(new File("example/new.xlsx"),outData);
    }
}
