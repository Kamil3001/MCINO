package results;

// Stores a range which is used for occurences of smells
public class Range {
    private int start, end;

    public Range(int start, int end){
        this.start = start;
        this.end = end;
    }

    public int getStart(){
        return start;
    }

    public int getEnd(){
        return end;
    }
}
