package lab2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/** Skeleton for NERC task.
 *
 * @author Fabian M. Suchanek
 *
 */
public class Nerc {

    /** Labels that we will attach to the words*/
    public enum Class {
        ARTIFACT, EVENT, GEO, NATURAL, ORGANIZATION, PERSON, TIME, OTHER
    }

    /** Determines the class for the word at position 0 in the window*/
    public static Class findClass(Window window) {

        // Magic goes here
        int[] functions = new int[8];
        Class[] categories = {  Class.ARTIFACT, Class.EVENT, Class.GEO, Class.NATURAL,
                                Class.ORGANIZATION, Class.PERSON, Class.TIME, Class.OTHER };
        if(window.getTagAt(0).equals("NNP") || window.getTagAt(0).equals("JJ") ||
                window.getTagAt(0).equals("NN") || window.getTagAt(0).equals("NNPS")){

                    // Artifact
                    if (window.getTagAt(-1).equals("IN") && window.getWordAt(0).matches("^[A-Z]")) functions[0] += 1;
                    if (window.getClassAt(-1) != null && window.getClassAt(-1).equals(Class.ARTIFACT)) functions[0] += 1;

                    // Event
                    if(window.getWordAt(0).matches("^[A-Z]") && window.getTagAt(-1).equals("CD") ||
                            window.getTagAt(-1).equals("VBD") || window.getTagAt(-1).equals("DT"))
                        functions[1] += 1;
                    if (window.getClassAt(-1) != null && window.getClassAt(-1).equals(Class.EVENT)) functions[1] += 1;

                    // Geo
                    if (window.getTagAt(-1).equals("IN") || window.getTagAt(-1).matches("^(VB)") ||
                            window.getTagAt(-1).equals("DT") || window.getTagAt(-1).equals("CC") ||
                            window.getTagAt(-1).equals(".")  || window.getTagAt(-1).equals("CD") ||
                            window.getTagAt(-1).equals("TO")) functions[2] += 1;
                    if( window.getClassAt(-1) != null && window.getClassAt(-1).equals(Class.GEO)) functions[2] += 1;
                    functions[2] *= 4;

                    // Natural
                    if (window.getWordAt(0).matches("^[A-Z]") && window.getTagAt(-1).equals("IN")) functions[3] += 1;
                    if( window.getClassAt(-1) != null && window.getClassAt(-1).equals(Class.NATURAL)) functions[3] += 1;

                    // Organi
                    if (window.getTagAt(-1).equals("IN") || window.getTagAt(-1).equals("DT") ||
                            window.getTagAt(-1).equals("VBG") || window.getTagAt(-1).equals("POS") ||
                            window.getTagAt(-1).equals("LRB") || window.getTagAt(-1).equals(".") ||
                            window.getTagAt(-1).equals("CC")) functions[4] += 1;
                    if( window.getClassAt(-1) != null && window.getClassAt(-1).equals(Class.ORGANIZATION)) functions[4] += 1;
                    functions[4] *= 3;

                    // Person
                    if(window.getWordAt(0).matches("^[A-Z]") && window.getTagAt(-1).equals("NN") ||
                            window.getTagAt(-1).matches("^(VB)") || window.getTagAt(-1).equals("',") ||
                            window.getTagAt(-1).equals("NNP") ||  window.getTagAt(-1).equals(".") ||
                            window.getTagAt(-1).equals("CC") || window.getTagAt(-1).equals("IN")) functions[5] += 1;
                    if( window.getClassAt(-1) != null && window.getClassAt(-1).equals(Class.PERSON)) functions[5] += 1;
                    functions[5] *= 3;

                    // Time
                    if (window.getTagAt(-1).equals("DT") || window.getTagAt(-1).equals("IN") ||
                            window.getTagAt(-1).matches("^(VBG)") || window.getTagAt(0).equals("CD") ||
                            window.getTagAt(-1).matches("^(NNP)")) functions[6] += 1;
                    if( window.getClassAt(-1) != null && window.getClassAt(-1).equals(Class.PERSON)) functions[6] += 1;
                    functions[6] *= 2;
                    if( window.getTagAt(0).matches("[0-9]*")) return Class.TIME;

            int max = getMax(functions);
            return (categories[max]);
        }
        // Other
        else return (Class.OTHER);

    }
    public static int getMax(int[] arr){
        int max=arr[0];
        int max_num = 0;
        for(int i=1;i<arr.length;i++){
            if(arr[i]>max){
                max=arr[i];
                max_num = i;
            }
        }
        if(max <= 1)
            return 7;
        return max_num;
    }


    /** Takes as arguments:
     * (1) a testing file with sentences
     * (2) optionally: a training file with labeled sentences
     *
     *  Writes to the file result.tsv lines of the form
     *     X-WORD \t CLASS
     *  where X is a sentence number, WORD is a word, and CLASS is a class.
     */
    public static void main(String[] args) throws IOException {
        args = new String[] { "./corpus/ner-test.tsv", "./corpus/ner-train.tsv" };

        // EXPERIMENTAL: If you wish, you can train a KNN classifier here
        // on the file args[1].
        // KNN<Nerc.Class> knn = new KNN<>(5);
        // knn.addTrainingExample(Nerc.Class.ARTIFACT, 1, 2, 3);

        try (BufferedWriter out = Files.newBufferedWriter(Paths.get("result.tsv"))) {
            try (BufferedReader in = Files.newBufferedReader(Paths.get(args[0]))) {
                String line;
                Window window = new Window(5);

                while (null != (line = in.readLine())) {
                    window.add(line);
                    if (window.getWordAt(-window.width) == null) continue;
                    Class c = findClass(window);
                    if (c != null && c != Class.OTHER)
                        out.write(window.getSentenceNumberAt(0) + "-" + window.getWordAt(0) + "\t" + c + "\n");
                }
            }
        }
    }
}