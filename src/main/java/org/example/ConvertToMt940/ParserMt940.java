package org.example.ConvertToMt940;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserMt940 {

    public static List<Map<String, String>> parserMT940notHadears(String filePath) throws IOException{
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        List<Map<String, String>> records = new ArrayList<>();

        Pattern fieldPattern = Pattern.compile("^:(\\d{2}[A-Z]?):(.*)$");

        Map<String, String> currentRecord = null;

        for (String line : lines) {
            Matcher matcher = fieldPattern.matcher(line);

            if (matcher.find()) {
                String field = matcher.group(1);
                String value = matcher.group(2).trim();

                // Start a new record when encountering :61:
                if (field.equals("61")) {
                    if (currentRecord != null) {
                        records.add(currentRecord);
                    }
                    currentRecord = new LinkedHashMap<>();
                }

                // Continue adding fields to the current record
                if (currentRecord != null) {
                    currentRecord.put(field, value);
                }
            }
        }

        // Add the last record if exists
        if (currentRecord != null) {
            records.add(currentRecord);
        }

        return records;
    }
}
