package smells;


import metrics.FileMetrics;
import results.Resultable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCodeSmell implements Resultable {

    int severity = 0;
    List<Integer> occurrences = new ArrayList<>();

    //method which detects the smell and assigns the relevant values to severity and occurrences
    public abstract void detectSmell(FileMetrics metrics);

    //return the name of the smell
    public abstract String getSmellName();

    @Override
    public int getSeverity() {
        return severity;
    }

    //returns the occurrences of the smell, and null if not applicable
    @Override
    public List<Integer> getOccurrences() {
        if(occurrences.isEmpty())
            return null;

        return occurrences;
    }
}
