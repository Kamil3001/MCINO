package results;


import java.util.HashMap;
import java.util.List;

public class OverallResult {

    private HashMap<String, Double> avgSmellSeverities;
    private HashMap<String, Double> avgSmellOccurrences;
    private String[] resultCommentsforAvgSeverity = {
            "", // x < 0.5
            "", // 0.5 <= x < 1.5
            "", // 1.5 <= x < 2.5
            ""  // 2.5 <= x
    };

    public OverallResult(List<SmellResult> smellResults){
        avgSmellOccurrences = new HashMap<>();
        avgSmellSeverities = new HashMap<>();
        fillAvgHashMaps(smellResults);
    }

    private void fillAvgHashMaps(List<SmellResult> smellResults){

        for(SmellResult sr : smellResults){
            avgSmellSeverities.put(sr.getSmellName(), sr.getAverageSeverity());
        }

        for(SmellResult sr : smellResults){
            avgSmellOccurrences.put(sr.getSmellName(), sr.getAverageOccurrences());
        }
    }

    public HashMap<String, Double> getOverallSeverities(){
        return avgSmellSeverities;
    }

    public HashMap<String, Double> getOverallOccurrences(){
        return avgSmellOccurrences;
    }

    public String getAvgSeverityComment(double avgSeverity){
        int i = (int)Math.round(avgSeverity);
        return resultCommentsforAvgSeverity[i];
    }

    public String getAvgOccurrencesComment(String smellName){
        double avg = avgSmellOccurrences.get(smellName);
        return "The average occurrence of the " + smellName + " smell is " + avg + ".";
    }

    public String getComments(String smellName){
        double avgSev = avgSmellSeverities.get(smellName);
        double avgOcc = avgSmellOccurrences.get(smellName);

        String result = smellName + " smell has an average severity rating of: " + avgSev +
                " and average number of occurrences per file is: " + avgOcc + ".\n";

        //todo create a text file with solutions for different smells and build the string using the relevant line in the text
        // use regex to find the relevant comments
        return result;
    }



}
