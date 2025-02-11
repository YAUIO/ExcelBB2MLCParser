package Parsers;

import Entities.BlackBeatEntry;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BlackBeatToList {
    public static ArrayList<BlackBeatEntry> parse(File source) {
        System.out.println("Started parsing BlackBeats table");
        FileInputStream file = null;
        Workbook sourceBook = null;
        try {
            file = new FileInputStream(new File("example/from.xlsx"));
            sourceBook = new XSSFWorkbook(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Sheet sourceSheet = sourceBook.getSheet("BLACK BEATS");
        Row header = sourceSheet.getRow(0);

        Class<?> entryClass = BlackBeatEntry.class;
        Field[] fields = entryClass.getDeclaredFields();

        ArrayList<BlackBeatEntry> data = new ArrayList<>();

        for (int i = 3; i <= sourceSheet.getLastRowNum(); i++) {
            Row row = sourceSheet.getRow(i);
            BlackBeatEntry entity = new BlackBeatEntry();
            for (int cell = 0; cell < fields.length; cell++) {
                Cell val = row.getCell(cell);

                Class<?> expectedType = fields[cell].getType();
                Object obj = null;

                if (val != null) {
                    if (expectedType == Date.class) {
                        try {
                            obj = val.getDateCellValue();
                        } catch (Exception _) {
                            try {
                                obj = DateFormat.getDateInstance().parse(val.getStringCellValue());
                            } catch (Exception _) {
                            }
                        }
                    } else if (expectedType == String.class) {
                        try {
                            obj = val.getStringCellValue();
                        } catch (Exception _) {
                            if (val.getCellType() == CellType.NUMERIC) {
                                long number = (long) val.getNumericCellValue();
                                obj = String.valueOf(number);
                            }
                        }
                    } else if (expectedType == Integer.class) {
                        try {
                            obj = Double.valueOf(val.getNumericCellValue()).intValue();
                        } catch (Exception _) {
                        }
                    } else if (expectedType == Time.class) {
                        try {
                            obj = new Time(val.getDateCellValue().getTime());
                        } catch (Exception _) {
                        }
                    }
                }

                try {
                    fields[cell].set(entity, obj);
                } catch (IllegalAccessException e) {
                    System.out.println(e.getMessage());
                }

                data.add(entity);
            }
            System.out.println("Parsed row " + (i + 1) + "/" + (sourceSheet.getLastRowNum() + 1) + " | " + entity);
        }

        System.out.println("Finished parsing BlackBeats table\n");

        return data;
    }
}
