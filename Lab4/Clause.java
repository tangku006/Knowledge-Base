package lab4;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.*;
import java.util.regex.*;

/**
 * A logical clause (= disjunction, rule)
 *
 * @author Fabian Suchanek
 *
 */
public class Clause {
    /** Body atoms*/
    public List<Atom> atoms = new ArrayList<>();
    /** Weight of this rule*/
    public double weight;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        for (Atom b : atoms)
            result.append(b).append(", ");
        if (!atoms.isEmpty()) result.setLength(result.length() - 2);
        return (result.append("} [").append(weight).append("]").toString());
    }

    /** Creates a clause*/
    public Clause(List<Atom> atoms, double weight) {
        // Make a copy of the list of atoms
        // to avoid that a change of the list
        // is reflected in the clause...
        this.atoms.addAll(atoms);
        this.weight=weight;
    }

    /** Creates a rule from a string of the following forms
     *    A & B & C => D [#]
     *    { A, B, C } [#]*/
    public Clause(String rule) {
        Matcher m=Pattern.compile("\\[?(\\d+(\\.\\d*)?)\\]? *$").matcher(rule);
        if(!m.find()) throw new RuntimeException("Rule or clause does not have a weight: "+rule);
        weight = Double.parseDouble(m.group(1));
        rule = rule.substring(0, m.start()).trim();
        if (rule.contains("=>")) {
            String[] hb = rule.split("=>");
            if (hb.length != 2) throw new RuntimeException("Rule must contain exactly one '=>': " + rule);
            for (String a : hb[0].split("[&,]"))
                if(!a.trim().isEmpty()) atoms.add(new Atom(a).negation());
            atoms.add(new Atom(hb[1]));
        } else if(rule.startsWith("{") && rule.endsWith("}")) {
            rule=rule.substring(1,rule.length()-1);
            for (String a : rule.split(","))
                atoms.add(new Atom(a));
        }
    }

    /** Duplicates a rule*/
    public Clause(Clause other) {
        this.atoms = other.atoms.stream().map(a -> new Atom(a)).collect(Collectors.toList());
        this.weight = other.weight;
    }

    /** TRUE if this clause is satisfied in the KB --
     * either because a positive atom appears in the KB
     * or because a negative atom is absent in the KB.*/
    public boolean isSatisfiedIn(Set<Atom> kb) {
        return (atoms.stream().anyMatch(a -> a.isSatisfiedIn(kb)));
    }

    /** Reads a list of clauses or rules from a file*/
    public static List<Clause> readFrom(File file) throws IOException {
        return (Files.readAllLines(file.toPath()).stream().filter(l -> !l.startsWith("#")).map(l -> new Clause(l))
                .collect(Collectors.toList()));
    }
}