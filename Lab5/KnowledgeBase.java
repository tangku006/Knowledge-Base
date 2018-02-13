package lab5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Implements a simple database for YAGO facts.
 *
 * @author Fabian Suchanek
 */
public class KnowledgeBase {

    /**
     * A map from an entity x to a relation r and a set of y, where r(x,y) in
     * YAGO
     */
    public final Map<String, Map<String, Set<String>>> facts = new HashMap<>();

    /**
     * Constructs a simple database from a file of entity links and a file of
     * labels
     */
    public KnowledgeBase(File yago) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(yago), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("\t");
                if (split.length != 3) {
                    throw new IOException("Error while loading");
                }
                Map<String, Set<String>> map = facts.get(split[0]);
                if (map == null) facts.put(split[0], map = new TreeMap<>());
                Set<String> set = map.get(split[1]);
                if (set == null) map.put(split[1], set = new TreeSet<>());
                set.add(split[2]);
            }
        }
    }

    /** Prints facts about one entity (for debugging). */
    public void tellMeAbout(String entity) {
        System.out.println(entity + ":");
        if (!facts.containsKey(entity)) {
            System.out.println("  unknown");
            return;
        }
        for (String relation : facts.get(entity).keySet()) {
            System.out.println("  " + relation + ": " + facts.get(entity).get(relation));
        }
    }

}