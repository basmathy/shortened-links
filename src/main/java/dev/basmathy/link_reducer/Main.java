package dev.basmathy.link_reducer;


import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;

import static dev.basmathy.link_reducer.Authorisation.*;
import static dev.basmathy.link_reducer.Config.*;
import static dev.basmathy.link_reducer.Database.*;
import static dev.basmathy.link_reducer.Links.*;

public class Main {
    public static void main(String[] args) {
        String uuid = null;
        Map<String, List<String>> linkDatabase = load();
        //ключ - shortLink, значение [UUID user, стартовая ссылка, tLive(max-1), clickNumbers(max-1)]...

        System.out.println("Hello, unknown user!");
        System.out.println("Choose one of the option: ");
        System.out.println("signin -  sign in by UUID");
        System.out.println("reduce -  reduce my link");
        System.out.println("edit -  delete the existed link");
        System.out.println("use -  use the short link");
        System.out.println("signout -  sign out");
        System.out.println("quit -  quit");

        Scanner input = new Scanner(System.in);
        String request = "hello";

        while (!request.equals("quit")) {
            request = input.next();
            input.nextLine();
            switch (request) {

                case "signin":
                    if (!isAuthorised(uuid)) {
                        System.out.println("Please, paste your UUID: ");
                        String enteredUUID = input.next();
                        input.nextLine();
                        if (authoriseUser(enteredUUID, linkDatabase)) {
                            uuid = enteredUUID;
                            System.out.println("You're authorised.");
                        } else System.out.println("There's no such UUID in the system.");
                    } else System.out.println("You have already authorised.");
                    break;

                case "reduce":
                    if (!isAuthorised(uuid)) {
                        uuid = uuidGenerator();
                    }
                    System.out.println("Please, paste the link that need to be shortened: ");
                    String longLink = input.next();
                    input.nextLine();
                    if (isRealLongLink(longLink)) {
                        String shortLink = shortenedLink();
                        while (!isLinkUnique(shortLink, linkDatabase.keySet())) {
                            shortLink = shortenedLink();
                        }
                        linkDatabase = placeToDict(linkDatabase, shortLink, uuid, longLink);
                        System.out.println("Here's your shortened link: " + shortLink);
                    } else System.out.println("It feels like it's not the real http(s) link.");
                    break;

                case "edit":
                    if (isAuthorised(uuid)) {
                        showMyShortLongLinks(uuid, linkDatabase);
                        String toDeleteShortLink = input.next();
                        input.nextLine();
                        linkDatabase = deleteLinkByKey(linkDatabase, uuid, toDeleteShortLink);
                    } else {
                        System.out.println("You're not authorised and have no links.");
                    }
                    break;

                case "use":
                    if (isAuthorised(uuid)) {
                        System.out.println("Paste your short link: ");
                        String usingLink = input.next();
                        input.nextLine();
                        if (linkDatabase.containsKey(usingLink) && linkDatabase.get(usingLink).get(uuidIndex).equals(uuid)) {
                            if (!isExpired(usingLink, linkDatabase)) {
                                try {
                                    URL url = new URL(linkDatabase.get(usingLink).get(longLinkIndex));
                                    Desktop.getDesktop().browse(url.toURI());
                                    linkDatabase = reduceClicks(linkDatabase, usingLink);
                                } catch (URISyntaxException | IOException e) {
                                    System.out.println("Your saved link can't be open because it's not a link itself.");
                                }
                            } else {
                                System.out.println("This link is off. It doesn't work anymore.");
                                linkDatabase = deleteLinkByKey(linkDatabase, uuid, usingLink);
                            }
                        } else System.out.println("There's no such a link.");
                    } else System.out.println("You're not authorised.");
                    break;

                case "signout":
                    if (uuid != null) {
                        uuid = null;
                        System.out.println("You signed out successfully.");
                    } else System.out.println("You're already not authorised.");
                    break;
                case "quit":
                    break;
                default:
                    System.out.println("Please, choose the existed option.");
            }
        }
        save(linkDatabase);
        System.out.println("Bye-bye!");
    }
}