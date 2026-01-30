package dev.basmathy.link_reducer;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.time.Instant;

import static dev.basmathy.link_reducer.Config.*;

public class Database {

    public static Map<String, List<String>> placeToDict(Map<String, List<String>> db, String shortLink, String uuid, String longLink) {
        String linksMaxTime = Long.toString(Instant.now().getEpochSecond() + expirationTime);
        String clicksMaxNumber = Integer.toString(clicksNumber);
        db.put(shortLink, Arrays.asList(uuid, longLink, linksMaxTime, clicksMaxNumber));
        return db;
    }

    public static Map<String, List<String>> reduceClicks(Map<String, List<String>> db, String shortLink) {
        List<String> metaCurrentLink = db.get(shortLink);
        String currentClicksNums = String.valueOf((Integer.parseInt(metaCurrentLink.get(lastClicksIndex)) - 1));
        metaCurrentLink.set(lastClicksIndex, currentClicksNums);
        db.put(shortLink, metaCurrentLink);
        return db;
    }

    // выгружаем данные из файла в программу
    public static Map<String, List<String>> load() {
        Path path = Path.of(fileName);
        Map<String, List<String>> db = new HashMap<>();

        if (!Files.exists(path)) {
            return db;
        }

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitLine = line.split(" ");
                if (Integer.parseInt(splitLine[4]) <= 0) {
                    continue;
                } else if (Long.parseLong(splitLine[3]) < Instant.now().getEpochSecond()) {
                    continue;
                }
                db.put(splitLine[0], Arrays.asList(
                        splitLine[1],
                        splitLine[2],
                        splitLine[3],
                        splitLine[4]
                ));
            }
        } catch (IOException error) {
            System.out.println("Ops, the file wasn't correctly read to the end. The programm's fallen down - boom...");
            System.out.println(error.getMessage());
        }
        return db;
    }

    public static void save(Map<String, List<String>> db) {
        Path path = Path.of(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            db.forEach((key, values) -> {
                try {
                    writer.write(key);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                for (String i : values) {
                    try {
                        writer.write(" " + i);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            System.out.println("Ops, the file wasn't correctly save to the end. The programm's fallen down - boom...");
            System.out.println(e.getMessage());
        }
    }
}