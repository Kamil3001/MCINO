package results;

/* Class which stores an Occurrence of a smell in terms of start and end lines */
public class Occurrence {
    private int startLine;
    private int endLine;
    private Occurrence linkedOccurrence; //Linked occurrence for duplicated code occurrences
    private boolean hasLink; //

    //constructor w/o linked occurrence
    public Occurrence(int startLine, int endLine){
        this.startLine = startLine;
        this.endLine = endLine;
        this.hasLink = false;
    }

    //constructor w/ linked occurrence
    public Occurrence(Occurrence linkedOccurrence, int startLine, int endLine) {
        this(startLine, endLine);
        this.linkedOccurrence = linkedOccurrence;
        hasLink = true;
    }

    //assign linked occurrence (or clear it if null passed in)
    public void setLinkedOccurrence(Occurrence occurrence){
        linkedOccurrence = occurrence;
        if(occurrence == null)
            hasLink = false;
        else
            hasLink = true;
    }

    public boolean hasLink(){ return hasLink; }

    /* GETTERS */
    public int getStartLine(){
        return startLine;
    }

    public int getEndLine(){
        return endLine;
    }

    public Occurrence getLinkedOccurrence(){
        return hasLink ? linkedOccurrence : null;
    }
}