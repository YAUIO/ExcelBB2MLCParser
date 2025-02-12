import Entities.BlackBeatEntry;
import Entities.Human;
import Entities.Init;
import Entities.MLCEntry;
import Parsers.*;
import jakarta.persistence.EntityManager;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Usage program.jar datapath outpath");
        }

        Init.setDB("Names");
        File f = new File("db.xlsx");
        if (f.exists()) {
            XLSXtoDB.load(f);
        }
        ArrayList<BlackBeatEntry> sourceData = BlackBeatToList.parse(new File(args[0]));
        ArrayList<MLCEntry> outData = BlackBeatToMLC.convert(sourceData);
        String path = "new.xlsx";
        if (args.length == 2) {
            path = args[1];
        }
        MLCListToXLSX.record(new File(path),outData);

        if (f.exists()) if (!f.delete()) System.out.println("Couldn't delete db.xlsx");

        DBtoXLSX.write(f);
    }
}
