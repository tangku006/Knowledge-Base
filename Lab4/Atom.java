package lab4;

import java.util.Set;

/**
 * A logical atom, which can be positive or negative
 *
 * @author Fabian Suchanek
 *
 */
public class Atom {
    /** Predicate of the atom*/
    public String predicate;
    /** Polarity of the atom*/
    public boolean isPositive;

    /** Constructs an atom*/
    public Atom(String predicate, boolean isPositive) {
        this.predicate = predicate;
        this.isPositive = isPositive;
    }

    /** Constructs an atom from a string of the form -ABC or ABC*/
    public Atom(String atom) {
        atom=atom.trim().toLowerCase();
        if(!atom.matches("-?[a-z_0-9]+")) throw new RuntimeException("Malformed atom: "+atom);
        if (atom.startsWith("-")) {
            predicate = atom.substring(1);
            isPositive = false;
        } else {
            predicate = atom;
            isPositive = true;
        }
    }

    /** Duplicates an atom*/
    public Atom(Atom atom) {
        this(atom.predicate, atom.isPositive);
    }

    /** Returns an copy of this atom with swapped polarity*/
    public Atom negation() {
        return (new Atom(predicate, !isPositive));
    }

    /** Returns a copy of this atom that is positive*/
    public Atom asPositive() {
        return (new Atom(predicate, true));
    }

    /** TRUE if this atom is positive*/
    public boolean isPositive() {
        return (isPositive);
    }

    /** TRUE if this atom is negative*/
    public boolean isNegative() {
        return (!isPositive);
    }

    /** TRUE if this atom is satisfied in the KB
     * -- either because it appears in the KNB or
     * because it is negative and does not appear in the KB*/
    public boolean isSatisfiedIn(Set<Atom> kb) {
        if (kb.contains(this)) return (true);
        if (isNegative() && !kb.contains(asPositive())) return (true);
        return (false);
    }

    @Override
    public String toString() {
        return (isPositive ? "" : "-") + predicate;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Atom) obj).isPositive == isPositive && ((Atom) obj).predicate.equals(predicate);
    }

    @Override
    public int hashCode() {
        return (predicate.hashCode() ^ (isPositive ? 1 : 0));
    };
}