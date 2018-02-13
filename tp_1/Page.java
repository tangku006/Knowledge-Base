package lab1;

/**
 * Represents a Wikipedia page
 * 
 * @author Fabian M. Suchanek
 * 
 */
public class Page {

  /** Holds the page title*/
  public String title;

  /** Holds the page content*/
  public String content;

  /** Returns the label (for ambiguous pages only) */
  public String label() {
    return (title.substring(1, title.lastIndexOf('_')).replace('_', ' '));
  }

  /** Creates a page with title and content*/
  public Page(String title, String content) {
    super();
    this.title = title;
    this.content = content;
  }

  /** Returns the first sentence of the page, without brackets */
  public String firstSentence() {
    int dotPos = content.indexOf(". ");
    if (dotPos == -1) 
    	return (content.replaceAll("\\(.*?\\)", "") + ".");
    return (content.substring(0, dotPos).replaceAll("\\(.*?\\)", "") + ".");
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((content == null) ? 0 : content.hashCode());
    result = prime * result + ((title == null) ? 0 : title.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Page other = (Page) obj;
    if (content == null) {
      if (other.content != null) 
    	  return false;
    } else if (!content.equals(other.content)) 
    	return false;
    if (title == null) {
      if (other.title != null) 
    	  return false;
    } else if (!title.equals(other.title)) 
    	return false;
    
    return true;
  }

  @Override
  public String toString() {
    return "Page [title=" + title + "]";
  }

}