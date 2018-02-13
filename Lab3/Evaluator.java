package lab3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Skeleton for an evaluator
 */
public class Evaluator {

    /**
     * Takes as arguments (1) the gold standard and (2) the output of a program.
     * Prints to the screen one line with the precision
     * and one line with the recall.
     */
    public static void main(String[] args) throws Exception {
        Map<String, String> goldStandard = new HashMap<>();
        try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] split = line.split("\t");
                goldStandard.put(split[0], split[1]);
            }
        }
        int correct = 0;
        int incorrect = 0;
        try (BufferedReader in = new BufferedReader(new FileReader(args[1]))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] split = line.split("\t");
                String desiredTarget = goldStandard.get(split[0]);
                if (desiredTarget == null) continue;
                if (desiredTarget.equals(split[1])) correct++;
                else incorrect++;
            }
        }
        System.out.println(correct * 100 / (correct + incorrect));
        System.out.println(correct * 100 / goldStandard.size());

    }
}