package lab5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/** Evaluates the results of the entity Mapping
 *
 * @author Fabian Suchanek
 *
 */
public class Evaluator {

    /** Takes as arguments (1) the result file and (2) the goldstandard of the mapping task,
     * prints precision and recall. Assumes that every entity is mapped to at most one image entity.*/
    public static void main(String[] args) throws IOException {
        Map<String, String> output = new HashMap<>();
        Files.lines(Paths.get(args[0])).map(s -> s.split("\t")).forEach(s -> output.put(s[0], s[1]));
        Map<String, String> goldstandard = new HashMap<>();
        Files.lines(Paths.get(args[1])).map(s -> s.split("\t")).forEach(s -> goldstandard.put(s[0], s[1]));
        int numCorrect = 0;
        int numOutput = 0;
        for (String o : output.keySet()) {
            if (goldstandard.get(o) != null) {
                numOutput++;
                if (goldstandard.get(o).equals(output.get(o))) numCorrect++;
            }
        }
        System.out.println("Precison: " + 100.0 * numCorrect / numOutput + "%");
        System.out.println("Recall: " + 100.0 * numCorrect / goldstandard.size() + "%");
    }
}