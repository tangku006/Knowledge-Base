package lab1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implements a simple database for YAGO facts.
 * 
 * @author Jonathan Lajus
 */
public class SimpleDatabase {

  /** A map from an entity x to all entities y, where r(x,y) or r(y,x) in YAGO*/
  public final Map<String, Set<String>> links = new HashMap<>();

  /** A map from entity x to all labels y, where label(x,y) in YAGO*/
  public final Map<String, Set<String>> labels = new HashMap<>();

  /** A map from a label x to all entities y, where label(y, x) in YAGO*/
  public final Map<String, Set<String>> reverseLabels = new HashMap<>();

  /** Constructs a simple database from a file of entity links and a file of labels*/
  public SimpleDatabase(File yagoLinks, File yagoLabels) throws IOException {
    load(yagoLinks, links, false);
    load(yagoLabels, labels, false);
    load(yagoLabels, reverseLabels, true);
  }

  /** Loads a file of TAB-separated facts into a map*/
  public static void load(File f, Map<String, Set<String>> container, boolean reverse) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
    String line;
    while ((line = reader.readLine()) != null && line != "") {
      String[] split = line.split("\t");
      if (split.length != 2) {
        System.err.println("Error while loading");
        break;
      }
      String s = split[0].trim();
      String o = split[1].trim().replaceAll("^\"", "").replaceAll("\"$", "");
      if (reverse) {
        String t = s;
        s = o;
        o = t;
      }
      // If there are memory problems, try this:
      //s = s.intern();
      //o = o.intern();
      Set<String> l = container.get(s);
      if (l == null) {
        l = new LinkedHashSet<String>();
        container.put(s, l);
      }
      l.add(o);
    }
    reader.close();
  }
}