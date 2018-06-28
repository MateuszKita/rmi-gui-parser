package server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import rmi.api.Group;
import rmi.api.Parser;

public class ParserImplementation implements Parser {

    @Override
    public List<Group> parseGroups() throws RemoteException {
        String regexGroup = "\\d\\d?[a-zA-Z]{1,3}-[a-zA-Z]{2,3}\\D{0,1}[a-zA-Z]{0,3}";
        String regexUrl = "grupy_plan\\D{1,}\\d{5}";

        List<Group> groupsList = new ArrayList<>();

        try {
            Document doc = (Document) Jsoup.connect(urlPrefix + groupsIt).get();
            Elements groups = doc.select("tr td");

            for (int index = 0; index < groups.size(); index++) {
                String group = groups.get(index).toString();
                String name = "";
                String url = "";

                Pattern patternGroup = Pattern.compile(regexGroup, Pattern.CASE_INSENSITIVE);
                Matcher matcherGroup = patternGroup.matcher(group);
                while (matcherGroup.find()) {
                    int startGroup = matcherGroup.start();
                    int endGroup = matcherGroup.end();
                    name = group.substring(startGroup, endGroup);
                }

                Pattern patternUrl = Pattern.compile(regexUrl, Pattern.CASE_INSENSITIVE);
                Matcher matcherUrl = patternUrl.matcher(group);

                while (matcherUrl.find()) {
                    int startUrl = matcherUrl.start();
                    int endUrl = matcherUrl.end();
                    url = "/" + group.substring(startUrl, endUrl);
                }

                if (name.length() > 0 && url.length() > 0) {
                    groupsList.add(new Group(name, url));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ParserImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return groupsList;
    }

    @Override
    public List<String> parseSchedule(String url) throws RemoteException {
        List<String> schedulesList = new ArrayList<>();
        String regex = "(Poniedziałek|Wtorek|Środa|Czwartek|Piątek|Sobota|Niedziela|\\d{1,2}:\\d{1,2})|>([\\p{IsAlphabetic}\\d]|\\s){5,40}<";
        try {

            String urlSchedule = urlPrefix + url;
            Document doc = (Document) Jsoup.connect(urlSchedule).get();
            Elements schedules = doc.select("tr");
            schedules.remove(0);

            schedules = schedules.select("td");

            for (int x = 0; x < schedules.size(); x++) {
                Pattern patternGroup = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcherGroup = patternGroup.matcher(schedules.get(x).toString());
                while (matcherGroup.find()) {
                    int start = matcherGroup.start();
                    int end = matcherGroup.end();
                    String name = schedules.get(x).toString().substring(start, end);
                    name.replaceAll("(>|<)", "<sup>$1</sup>");
                    schedulesList.add(name);
                }
            }

            for (int i = 0; i < schedulesList.size(); i++) {
                String a = schedulesList.get(i).replaceAll("(<|>)", "");
                schedulesList.set(i, a);
            }
        } catch (IOException ex) {
            Logger.getLogger(ParserImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return schedulesList;
    }
}
