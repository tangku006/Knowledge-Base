package lab1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.*;

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
                    Set<String> entities = db.reverseLabels.get(pageLabel);    //entities has the label: pageLabel
                    Iterator it = entities.iterator();
                    Map<Object, Integer> map = new HashMap<>();                 // for the title_i
                    boolean b = false;
                    // System.out.println("Title: " + pageTitle);
                    // System.out.println(entities);
                    while (it.hasNext()){
                        Object entity = it.next();
                        // System.out.println(entity);
                        Set<String> relatedLinks  = db.links.get(entity);
                        Set<String> relatedLabels = db.labels.get(entity);
                        Iterator it1 = relatedLinks.iterator();
                        Iterator it2 = relatedLabels.iterator();
                        map.put(entity, 0);

                        // compare the labels
                        while( it1.hasNext() ){
                            Object linkEntity = it1.next();
                            if(db.labels.containsKey((String) linkEntity)){
                                // System.out.println(linkEntity);
                                Set<String> links = db.labels.get(linkEntity);
                                Iterator i = links.iterator();
                                while(i.hasNext())
                                    if (pageContent.contains( (String) i.next() )) map.put(entity, map.get(entity) + 1);
                            }
                        }
                        while(it2.hasNext())
                            if (pageContent.contains( (String) it2.next() )) map.put(entity, map.get(entity) + 1);

                        System.out.println(entity +": "+ map.get(entity));
                    }

                    // return corresponding entity
                    if(map != null){
                        Collection<Integer> c = map.values();
                        Object [] obj = c.toArray();
                        Arrays.sort(obj);
                        if(obj.length >= 2)
                            if((int)(obj[obj.length-1]) == (int)(obj[obj.length-2]) ){
                                b = true;
                            }
                        // System.out.println(obj.length);
                        // System.out.println(b);

                        for(Map.Entry<Object, Integer> m: map.entrySet()) {
                            if (m.getValue() == obj[obj.length-1]) {
                                correspondingYagoEntity = (String) m.getKey();
                                System.out.println(m.getKey() + ": " + correspondingYagoEntity + " " + m.getValue());
                                break;
                            }
                        }
                    }
                    // when not sure for the entity
                    if(map.get(correspondingYagoEntity) <= 1 || b) continue;

                    out.write(pageTitle + "\t" + correspondingYagoEntity + "\n");
                }
            }
        }
    }
}