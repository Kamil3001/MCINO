package results;

public class Occurrence {
    private int startLine;
    private int endLine;
    private Occurrence linkedOccurrence;
    private boolean hasLink;

    public Occurrence(int startLine, int endLine){
        this.startLine = startLine;
        this.endLine = endLine;
        this.hasLink = false;
    }
    public Occurrence(Occurrence linkedOccurrence, int startLine, int endLine) {
        this(startLine, endLine);
        this.linkedOccurrence = linkedOccurrence;
        this.hasLink = true;
    }

    public int getStartLine(){
        return startLine;
    }

    public int getEndLine(){
        return endLine;
    }

    public Occurrence getLinkedOccurrence(){
        return hasLink ? linkedOccurrence : null;
    }

    public void setLinkedOccurrence(Occurrence occurrence){
        this.hasLink = true;
        this.linkedOccurrence = occurrence;
    }

    public boolean hasLink(){ return hasLink; }
}