package dev.basmathy.link_reducer;


import java.awt.*;
import java.util.*;
import java.util.List;
import java.time.Instant;

import static dev.basmathy.link_reducer.Config.*;

public class Links {

    public static String shortenedLink() {
        String elements = elementsLine;
        Random random = new Random();
        final int length = shortLinkLength;
        StringBuilder newLink = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(elements.length());
            newLink.append(elements.charAt(index));
        }
        return "clck.ru/" + newLink.toString();

    }

    public static boolean isLinkUnique(String link, Set<String> keys) { // передать сокращенную строку и Hashmap.keySet();
        return !keys.contains(link);
    }

    public static boolean isRealLongLink(String link) {
        return link.startsWith("https://") || link.startsWith("http://");
    }

    public static boolean isExpired(String shortLink, Map<String, List<String>> db) {
        if (Integer.parseInt(db.get(shortLink).get(lastClicksIndex)) <= 0) {
            return true;
        } else if (Long.parseLong(db.get(shortLink).get(expirationTimeIndex)) < Instant.now().getEpochSecond()) {
            return true;
        } else return false;
    }

    public static void showMyShortLongLinks(String uuid, Map<String, List<String>> db) {
        System.out.println("Which link do you want to delete? Paste the only short link that you want to be deleted.");
        for (Map.Entry<String, List<String>> entry : db.entrySet()) {
            List<String> list = entry.getValue();
            if (list != null && !list.isEmpty()) {
                if (list.get(uuidIndex).equals(uuid))
                    System.out.println(entry.getKey() + " -- " + list.get(longLinkIndex));
            }
        }
    }

    public static Map<String, List<String>> deleteLinkByKey(Map<String, List<String>> db, String uuid, String shortLink) {
        if (db.containsKey(shortLink) && db.get(shortLink).get(uuidIndex).equals(uuid)) {

            db.remove(shortLink);
            System.out.println("Your link was deleted.");
        } else {
            System.out.println("Man, what're u doing here, hm?");
        }
        return db;
    }
}