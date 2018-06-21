import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CSVfileAnalisis {

    public static void main(String[] args) {

        try (Reader cin = new FileReader("C:\\Users\\P B\\IdeaProjects\\Sdasredniozaawansowane1\\src\\main\\resources\\MERGED2012_PP.csv"))
        //file to big to put into repository on github
        {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(cin);
            Map<String, ArrayList<Double>> mapOfData = new HashMap<>();
            for (CSVRecord record : records) {

                if (record.get("SAT_AVG_ALL").equals("NULL")) {
                    continue;
                }
                if (!mapOfData.containsKey(record.get("STABBR"))) {
                    ArrayList<Double> objects = new ArrayList<>();
                    objects.add(1.0);//size
                    objects.add(Double.parseDouble(record.get("SAT_AVG_ALL")));//average
                    objects.add(Double.parseDouble(record.get("SAT_AVG_ALL")));//min
                    objects.add(Double.parseDouble(record.get("SAT_AVG_ALL")));//max
                    mapOfData.put(record.get("STABBR"), objects);

                } else {
                    ArrayList<Double> objects = mapOfData.get(record.get("STABBR"));
                    Double first = objects.get(0);
                    Double second = objects.get(1);
                    Double third = objects.get(2);
                    Double forth = objects.get(3);

                    objects.set(0, first + 1.0);
                    objects.set(1, ((second * first) + Double.parseDouble(record.get("SAT_AVG_ALL"))) / (first + 1));
                    if (third > Double.parseDouble(record.get("SAT_AVG_ALL"))) {
                        objects.set(2, Double.parseDouble(record.get("SAT_AVG_ALL")));
                    }
                    if (forth < Double.parseDouble(record.get("SAT_AVG_ALL"))) {
                        objects.set(3, Double.parseDouble(record.get("SAT_AVG_ALL")));
                    }
                    mapOfData.replace(record.get("STABBR"), objects);
                }
            }
            System.out.println(mapOfData);

            File file = new File("AFTER.csv");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);

            CSVPrinter printer = CSVFormat.RFC4180.withHeader("Group", "Size", "Avg. Value", "Min. Value", "Max. Value").print(writer);
//            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.RFC4180.withHeader("Group", "Size", "Avg. Value", "Min. Value", "Max. Value"));
            ArrayList<Double> objects;

            for (Map.Entry<String, ArrayList<Double>> entry : mapOfData.entrySet()) {
                String object = entry.getKey();
                objects = mapOfData.get(object);
                printer.printRecord(object, objects.get(0), objects.get(1), objects.get(2), objects.get(3));
            }
            printer.flush();
            printer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

