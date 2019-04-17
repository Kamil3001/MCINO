package results;

public class Occurrence {
    private int startLine;
    private int endLine;

    public Occurrence(int startLine, int endLine){
        this.startLine = startLine;
        this.endLine = endLine;
    }

    public int getStartLine(){
        return startLine;
    }

    public int getEndLine(){
        return endLine;
    }
}