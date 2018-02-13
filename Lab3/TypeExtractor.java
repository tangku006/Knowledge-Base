package lab3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Skeleton code for a type extractor.
 */
public class TypeExtractor {

    static public String findType(Page nextPage) {
        String[] titles = nextPage.title.split(" ");
        String[] contents = nextPage.content.split(" ");
        String[] generalWords = {"kind", "kinds", "part", "type", "way", "form", "term", "emotion", "name"};
        String headNoun = null;

        for(String t: titles){
            t = t.replaceAll("[\\pP)]", "");
            if( t == null) return null;
            for(int i = 0; i < contents.length; i ++){
                String[] wordProperty = contents[i].split("/");
                if(wordProperty[0].toLowerCase().contains(t.toLowerCase()) && (wordProperty[1].matches("^(NN).*")
                        || wordProperty[1].matches("CD")) ){
                    if(wordProperty[1].matches("^(NN)[P]*")) headNoun = t;
                    int vbPos = -1, typePos = -1, dtPos = -1;      // TODO
                    for(int j = i+1; j < contents.length; j ++){
                        String[] typePro = contents[j].split("/");
                        // if(typePro[1].equals(".")) break;
                        if(typePro[1].equals("IN") || typePro[1].equals("WDT") || typePro[1].equals("CC")) {
                            if(typePos != -1) {
                                boolean b = true;
                                for (String word : generalWords)
                                    if (word.equals(contents[typePos].split("/")[0]))
                                        b = false;
                                if(b) break;
                                else continue;
                            }
                            vbPos = -1;
                            typePos = -1;
                            dtPos = -1;
                        }
                        if(typePro[1].matches("^(VB)[DZP]") && vbPos == -1){
                            if(!contents[j+1].matches("^(VB).*")) vbPos = j;
                        } else if(typePro[1].matches("^(NN).*") && vbPos != -1) {
                            typePos = j;
                        }

                        if(vbPos == -1) {
                            if (typePro[1].equals("DT")) {
                                dtPos = j;
                            } else if (typePro[1].matches("^(NN).*") && dtPos != -1) {
                                typePos = j;
                            }
                        }
                    }
                    if(typePos != -1) return contents[typePos].split("/")[0];

                }
            }
        }
        // To judge the headNoun
        // if(headNoun != null) return headNoun;
        return null;
    }
    /**
     Given as argument a Wikipedia file, the task is to run through all Wikipedia articles,
     and to extract for each article the type (=class) of which the article
     entity is an instance. For example, from a page starting with "Leicester is a city",
     you should extract "city".

     - extract just the head noun ("American rock star" -> "star")
     - if the type cannot reasonably be extracted ("Mathematics was invented in the 19th century"),
     skip the article (do not output anything)
     - take only the first item of a conjunction ("and")
     - do not extract too general words ("type of", "way", "form of")
     - keep the plural

     The output shall be printed to file "result.tsv" in the form
     entity TAB type NEWLINE
     with one or zero lines per entity.
     */
    public static void main(String args[]) throws IOException {
        try (Writer out = new OutputStreamWriter(new FileOutputStream("results.tsv"), "UTF-8")) {
            try (Parser parser = new Parser(new File(args[0]))) {
                int b = 1;
                while (parser.hasNext()) {
                    Page nextPage = parser.next();
                    String type = null;
                    type = findType(nextPage);
                    if (type != null) out.write(nextPage.title + "\t" + type + "\n");
                }
            }
        }
    }

}