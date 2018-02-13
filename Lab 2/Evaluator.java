package lab2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/** Evaluates the results of the NERC
 *
 * @author Fabian Suchanek
 *
 */
public class Evaluator {

    /** Takes as arguments (1) the result file and (2) the goldstandard of the NERC task,
     * prints precision and recall. Assumes that every labeled word occurs only one per sentence.*/
    public static void main(String[] args) throws IOException {
        Map<String, String> output = new HashMap<>();
        Files.lines(Paths.get(args[0])).map(s -> s.split("\t")).forEach(s -> output.put(s[0], s[1]));
        Map<String, String> goldstandard = new HashMap<>();
        Files.lines(Paths.get(args[1])).map(s -> s.split("\t")).forEach(s -> goldstandard.put(s[0], s[1]));
        int numCorrect = 0;
        int numOutput = 0;
        for (String o : output.keySet()) {
            numOutput++;
            if (goldstandard.get(o)!=null && goldstandard.get(o).equals(output.get(o))) numCorrect++;
        }
        System.out.println("Precison: " + 100.0 * numCorrect / numOutput + "%");
        System.out.println("Recall: " + 100.0 * numCorrect / goldstandard.size() + "%");
    }
}
