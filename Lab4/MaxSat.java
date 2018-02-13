package lab4;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Takes as argument a Max-Sat-file or a folder of Max-Sat-files,
 * writes a KB to the corresponding output file(s).
 * Does not take longer than 5 minutes PER PROBLEM.
 */
public class MaxSat {

    /** Start time*/
    public static long startTime;

    /** TRUE if we have to stop*/
    public static boolean haveToStop() {
        return (System.currentTimeMillis() - startTime > 5 * 60 * 1000);
    }


    public static void main(String[] args) throws IOException {
        File argument = new File(args[0]);
        for (File file : argument.isDirectory() ? argument.listFiles() : new File[] { argument }) {
            startTime = System.currentTimeMillis();
            List<Clause> rules = Clause.readFrom(file);
            Set<Atom> bestKB = new HashSet<>();

            // magic goes here
            Set<Atom> lastKB;
            Set<String> negAtoms = new HashSet<>();
            Set<String> posAtoms = new HashSet<>();
            Set<String> allAtoms  = new HashSet<>();

            for (Clause c : rules) {
                for (Atom a : c.atoms) {

                    String p = a.predicate;
                    if (a.isPositive()){
                        if ( !posAtoms.contains(p) && !allAtoms.contains(p) ) {
                            posAtoms.add(p);
                            allAtoms.add(p);
                        }

                        if ( negAtoms.contains(p) ) negAtoms.remove(p);
                    }
                    if (a.isNegative()){
                        if( !negAtoms.contains(p) && !allAtoms.contains(p) ) {
                            negAtoms.add(p);
                            allAtoms.add(p);
                        }

                        if ( posAtoms.contains(p) ) posAtoms.remove(p);
                    }
                }
            }
            for(String s: posAtoms) bestKB.add( new Atom(s, true) );

            do {
                lastKB = new HashSet<>(bestKB);
                for(Clause c: rules) {
                    for (Iterator<Atom> it = c.atoms.iterator(); it.hasNext();){
                        Atom a = it.next();
                        if( !a.isPositive() && bestKB.contains(new Atom(a.predicate, true)) ) {
                            System.out.println(c.atoms.size());
                            it.remove();
                            //c.atoms.remove(a);
                            System.out.println(c.atoms.size());
                            System.out.println("?");
                        }

                        if( c.atoms.size() == 1) {
                            if( a.isPositive() ) {
                                bestKB.add(a);
                            }
                            else {
                                for( Clause c2: rules){
                                    if(c2.atoms.size() == 1 && c2.atoms.get(0).equals(new Atom(a.predicate, true)))
                                        if (c.weight > c2.weight) {
                                            bestKB.remove(new Atom(a.predicate, true));
                                            //c.atoms.add(a);
                                            //System.out.println(c.toString());

                                        }

                                }

                            }
                        }
                        System.out.println("test");
                    }
                    System.out.println("bestKB " + bestKB);

                }

                if (haveToStop()) {
                    System.err.println("Longer than 5 minutes, stop!");
                    System.exit(1);
                }
            }while( !lastKB.equals(bestKB) );
            for(String s: negAtoms) bestKB.remove( new Atom(s, true) );

            try (Writer out = Files.newBufferedWriter(Paths.get(file.getName().replaceAll("\\.[a-z]+$", ".res")),
                    Charset.forName("UTF-8"))) {
                for (Atom var : bestKB)
                    if (var.isPositive()) out.write(var + "\n");
            }
        }
    }
}