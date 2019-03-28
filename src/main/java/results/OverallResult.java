package results;


import java.util.HashMap;
import java.util.List;

public class OverallResult {
    /*TODO
        this class will gather all individual smell results and combine them into one overall result for the project highlighting
        the major smells and the smelliest files/classes
     */

    HashMap<String, Integer> avgSmellSeverities;
    HashMap<String, Integer> avgSmellOccurrences;
    String[] resultCommentsforAvgSeverity;
    String[] resultCommentsforAvgOccurrences;

    public OverallResult(List<SmellResult> smellResults){
        avgSmellOccurrences = new HashMap<>();
        avgSmellSeverities = new HashMap<>();
        fillAvgSmellOccurrences(smellResults);
        fillAvgSmellSeverities(smellResults);
    }

    private void fillAvgSmellSeverities(List<SmellResult> smellResults){

    }

    private void fillAvgSmellOccurrences(List<SmellResult> smellResults){

    }

}
