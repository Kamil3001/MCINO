package results;

import java.util.HashMap;
import java.util.List;

//The following class contains the smell result for each file in the directory

  /*
    Severity of smell measured as follows:
        0 - none
        1 - low
        2 - medium
        3 - high
    Allows to easily compute average
     */

public class SmellResult {
    private String smellName;
    private int totalSeverity = 0; //default value for severity of the smell in entire directory
    private int totalOccurrences = 0;
    private HashMap<String, List<Integer>> occurrencesPerFile; //stored as line numbers rather than entire strings (null for smells like cyclomatic complexity)
    private HashMap<String, Integer> severityPerFile;

    public SmellResult(String smellName){
        this.smellName = smellName;
        occurrencesPerFile = new HashMap<>();
        severityPerFile = new HashMap<>();
    }

    //returns false if cannot add to hashmap (due to incorrect format or such)
    //overwriting occurrences is not permitted to avoid unexpected changes to results
    public boolean addOccurrences(String fileName, List<Integer> occurrences){
        if(occurrencesPerFile.containsKey(fileName)) {
            return false;
        }
        else if(occurrences == null) {
            occurrencesPerFile.put(fileName, null);
            return true;
        }

        //occurrences cannot be negative
        int sum = 0;
        for(Integer i : occurrences){
            if(i < 0)
                return false;
            sum += i;
        }

        totalOccurrences += sum;
        occurrencesPerFile.put(fileName, occurrences);
        return true;
    }

    //like addOccurences() this also returns false if cannot be added to the hashmap
    //and similarly overwriting is not permitted
    public boolean addSeverity(String fileName, int severity){
        if(severity < 0 || severity > 3 || severityPerFile.containsKey(fileName)) //score out of bounds or update to severity attempted
            return false;

        severityPerFile.put(fileName, severity);
        totalSeverity += severity;

        return true;
    }

    public String getSmellName() {
        return smellName;
    }

    public double getAverageSeverity() {
        return Math.round((totalSeverity*1.0) / severityPerFile.size());
    }

    public double getAverageOccurrences() {
        return Math.round((totalOccurrences*1.0) / occurrencesPerFile.size());
    }

    public HashMap<String, Integer> getSeverityPerFile() {
        return severityPerFile;
    }

    public HashMap<String, List<Integer>> getOccurrencesPerFile() {
        return occurrencesPerFile;
    }
}
