package lab2;

/**
 * Represents a window of tokens in a corpus. Allows 4 operations on a window:
 *
 * - getWordAt(position)
 * - getPosTagAt(position)
 * - getSentenceNumberAt(position)
 * - getClassAt(position) // can return NULL
 *
 * @author Fabian Suchanek
 *
 */
public class Window {
    /** Points to the 0-position in the window */
    protected int position;
    /** Holds the words in a circular array */
    protected String[] words;
    /** Holds the POS tags in a circular array */
    protected String[] posTags;
    /** Holds the classes in a circular array */
    protected Nerc.Class[] classes;
    /** Holds the sentence numbers in a circular array */
    protected String[] sentenceNumbers;
    /** Width of the window */
    protected final int width;

    /** Creates a window of a given width */
    public Window(int width) {
        words = new String[width * 2 + 1];
        posTags = new String[width * 2 + 1];
        classes = new Nerc.Class[width * 2 + 1];
        sentenceNumbers = new String[width * 2 + 1];
        this.width = width;
    }

    /**
     * Adds a tab-separated line of sentence number, word, posTag, and
     * optionally a class
     */
    public void add(String line) {
        position++;
        String[] split = line.split("\t");
        sentenceNumbers[(position + words.length + width) % words.length] = split[0];
        words[(position + words.length + width) % words.length] = split[1];
        posTags[(position + posTags.length + width) % posTags.length] = split[2];
        classes[(position + classes.length + width) % classes.length] = split.length > 3 ? Nerc.Class.valueOf(split[3])
                : null;
    }

    /** Returns the tag at a relative position */
    public String getTagAt(int pos) {
        return (posTags[(position + pos + posTags.length) % posTags.length]);
    }

    /** Returns the class at a relative position (or null) */
    public Nerc.Class getClassAt(int pos) {
        return (classes[(position + pos + classes.length) % classes.length]);
    }

    /** Returns the word at a relative position */
    public String getWordAt(int pos) {
        return (words[(position + pos + words.length) % words.length]);
    }

    /** Returns the sentence number at a relative position */
    public String getSentenceNumberAt(int pos) {
        return (sentenceNumbers[(position + pos + posTags.length) % posTags.length]);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = -width; i < width + 1; i++) {
            if (i == 0) result.append("***");
            result.append(getWordAt(i) + "/" + getTagAt(i));
            if (getClassAt(i) != null) result.append("/").append(getClassAt(i));
            if (i == 0) result.append("***");
            result.append(" ");
        }
        return (result.toString());
    }
}