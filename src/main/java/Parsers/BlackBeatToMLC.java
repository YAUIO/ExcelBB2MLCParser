package Parsers;

import Entities.BlackBeatEntry;
import Entities.MLCEntry;
import Entities.Person;
import Entities.PersonList;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class BlackBeatToMLC {
    public static ArrayList<MLCEntry> convert(ArrayList<BlackBeatEntry> sourceData) {
        System.out.println("Started converting to MLC table");
        ArrayList<MLCEntry> outputData = new ArrayList<>();
        Field[] fields = MLCEntry.class.getDeclaredFields();
        PersonList list = null;
        for (BlackBeatEntry bb : sourceData) {
            list = new PersonList();
            for (String s : bb.track_composer.split(",")) {
                list.add(new Person(s, "C"));
            }

            for (String s : bb.track_author.split(",")) {
                list.add(new Person(s, "A"));
            }

            for (int i = 0; i < list.size(); i++) {
                MLCEntry entry = new MLCEntry();
                if (i == 0) {
                    for (Field f : fields) {
                        Object obj = switch (f.getName()) {
                            case "PRIMARY_TITLE", "RECORDING_TITLE" -> bb.track_title;
                            case "WRITER_LAST_NAME" -> list.get(i).LastName;
                            case "WRITER_FIRST_NAME" -> list.get(i).FirstName;
                            case "WRITER_ROLE_CODE" -> list.get(i).code;
                            case "MLC_PUBLISHER_NUMBER" -> "P359J1";
                            case "PUBLISHER_NAME" -> "INFINITY MUSIC";
                            case "PUBLISHER_IPI_NUMBER" -> 1234433973L;
                            case "COLLECTION_SHARE" -> bb.publishing_share;
                            case "RECORDING_ARTIST_NAME" -> bb.track_artist;
                            case "RECORDING_ISRC" -> bb.track_isrc;
                            case "RECORDING_LABEL" -> bb.label;
                            default -> null;
                        };

                        try {
                            f.set(entry, obj);
                        } catch (IllegalAccessException _) {
                        }
                    }
                } else {
                    for (Field f : fields) {
                        Object obj = switch (f.getName()) {
                            case "WRITER_LAST_NAME" -> list.get(i).LastName;
                            case "WRITER_FIRST_NAME" -> list.get(i).FirstName;
                            case "WRITER_ROLE_CODE" -> list.get(i).code;
                            default -> null;
                        };

                        try {
                            f.set(entry, obj);
                        } catch (IllegalAccessException _) {
                        }
                    }
                }
            }
        }
        System.out.println("Finished converting to MLC table\n");
        return outputData;
    }
}
