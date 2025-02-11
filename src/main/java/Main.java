import Entities.BlackBeatEntry;
import Entities.MLCEntry;
import Parsers.BlackBeatToList;
import Parsers.BlackBeatToMLC;

import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<BlackBeatEntry> sourceData = BlackBeatToList.parse(new File("example/from.xlsx"));
        ArrayList<MLCEntry> outData = BlackBeatToMLC.convert(sourceData);
    }
}
