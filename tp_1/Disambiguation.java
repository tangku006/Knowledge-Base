package lab1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Skeleton class to perform disambiguation
 * 
 * @author Jonathan Lajus
 *
 */
public class Disambiguation {

    /**
     * This program takes 3 command line arguments, namely the paths to: 
     * - yagoLinks.tsv 
     * - yagoLabels.tsv 
     * - wikipedia-ambiguous.txt 
     * in this order.
     * 
     * The program prints statements of the following form into the file
     * results.tsv: 
     *    <pageTitle> TAB <yagoEntity> NEWLINE 
     * It is OK to skip articles.
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("usage: Disambiguation <yagoLinks> <yagoLabels> <wikiText>");
            return;
        }
        File dblinks = new File(args[0]);
        File dblabels = new File(args[1]);
        File wiki = new File(args[2]);

        SimpleDatabase db = new SimpleDatabase(dblinks, dblabels);

        try (Parser parser = new Parser(wiki)) {
            try (Writer out = new OutputStreamWriter(new FileOutputStream("results.tsv"), "UTF-8")) {
                while (parser.hasNext()) {
                    Page nextPage = parser.next();
                    String pageTitle = nextPage.title; // "Clinton_1"
                    String pageContent = nextPage.content; // "Hillary Clinton was..."
                    String pageLabel = nextPage.label(); // "Clinton"
                    String correspondingYagoEntity = "<For_you_to_find>";
                    /**
                     * TODO CODE HERE to disambiguate the entity.
                     */
                    out.write(pageTitle + "\t" + correspondingYagoEntity + "\n");
                }
            }
        }
    }
}