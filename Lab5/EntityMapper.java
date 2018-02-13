package lab5;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;
import java.math.*;

/**
 * Skeleton class for a program that maps the entities from one KB to the
 * entities of another KB.
 *
 * @author Fabian
 *
 */
public class EntityMapper {

    /**
     * Takes as input (1) one knowledge base (2) another knowledge base.
     *
     * Prints "entity1 TAB entity2 NEWLINE" to the file results.tsv, if the first
     * entity from the first knowledge base is the same as the second entity
     * from the second knowledge base. Output 0 or 1 line per entity1.
     */

    public static Object getMaxKey( Map<String, Integer> map ){
        String result = null;
        if( map == null ) return null;
        Collection<Integer> c = map.values();
        Object[] obj = c.toArray();
        Arrays.sort(obj);
        for( String s : map.keySet() ){
            if ( map.get(s).equals(obj[obj.length - 1]) ){
                if( result == null ) result = s;
                else return null;
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        KnowledgeBase kb1 = new KnowledgeBase(new File(args[0]));
        KnowledgeBase kb2 = new KnowledgeBase(new File(args[1]));
        try (Writer out = new OutputStreamWriter(new FileOutputStream("results.tsv"), "UTF-8")) {
            for (String entity1 : kb1.facts.keySet()) {
                String mostLikelyCandidate = null;

                // Something smart here
                Map<String, Set<String>> relations1 = kb1.facts.get(entity1);
                Map<String, Integer> score = new HashMap<>();

                Set<String> str1 = new HashSet<>();
                for(String r : relations1.keySet()){
                    str1.addAll(relations1.get(r));
                }
                for(String entity2 : kb2.facts.keySet()){
                    Map<String, Set<String>> relations2 = kb2.facts.get(entity2);
                    Set<String> str2 = new HashSet<>();
                    Set<String> str = new HashSet<>();

                    int common = 0;

                    for (String r : relations2.keySet()) {
                        str2.addAll(relations2.get(r));
                    }

                    str.addAll(str1); str.addAll(str2);
                    common = str1.size() + str2.size() - str.size();
                    score.put(entity2, common);

                }
                mostLikelyCandidate = (String) getMaxKey(score);

                if (mostLikelyCandidate != null) {
                    out.write(entity1 + "\t" + mostLikelyCandidate + "\n");
                }
            }
        }
    }
}