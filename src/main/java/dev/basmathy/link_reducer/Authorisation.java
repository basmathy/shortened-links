package dev.basmathy.link_reducer;


import java.util.List;
import java.util.Map;
import java.util.UUID;

import static dev.basmathy.link_reducer.Config.uuidIndex;

public class Authorisation {

    public static String uuidGenerator() {
        String uuid = UUID.randomUUID().toString();
        System.out.println("Your new UUID is " + uuid + ". Please, be sure to save it.");
        return uuid;
    }

    public static boolean isAuthorised(String id) {
        return id != null;
    }

    public static boolean authoriseUser(String uuid, Map<String, List<String>> db) {
        for (List<String> list : db.values()) {
            if (list.get(uuidIndex).equals(uuid)) return true;
        }
        return false;
    }
}