package results;

import java.util.List;

public interface Resultable {
    List<Integer> getOccurrences();
    int getSeverity();
    String getResultComment();
}
