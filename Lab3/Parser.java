package lab3;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements an iterator over the pages in the Wikipedia corpus.
 *
 * @author Fabian M. Suchanek
 *
 */

public class Parser implements Closeable, Iterator<Page> {

    /** Holds the reader*/
    protected BufferedReader wikipediaReader;

    /** TRUE if we have treated the last page*/
    protected boolean hasReachedEnd = false;

    /** Constructs a parser from the Wikipedia corpus file*/
    public Parser(File wikipedia) throws IOException {
        wikipediaReader = new BufferedReader(new InputStreamReader(new FileInputStream(wikipedia), "UTF-8"));
    }

    /** Returns the next page*/
    @Override
    public Page next() {
        try {
            String title = wikipediaReader.readLine();
            if (title == null) throw new NoSuchElementException("Reached end of Wikipedia file");
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = wikipediaReader.readLine()) != null && line.length() > 0) {
                content.append(line).append(" ");
            }
            if (line == null) hasReachedEnd = true;
            return (new Page(title, content.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** TRUE if hasNext() will return a new page*/
    @Override
    public boolean hasNext() {
        return !hasReachedEnd;
    }

    /** Closes the reader*/
    @Override
    public void close() throws IOException {
        wikipediaReader.close();
    }

    /** Unsupported*/
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove in Wikipedia parser");
    }
}