package smells;

import metrics.FileMetrics;
import metrics.MethodMetrics;

import java.util.Map;

// long class/methods/ids (use in tandem with cyclomatic complexity to get a more accurate view of the files)
public class BloatedCodeSmell extends AbstractCodeSmell {
    private final static String smellName = "Bloated Code";
    private static String[] resultComments = {
            "",
            "",
            "",
            ""
    };

    @Override
    public void detectSmell(FileMetrics metrics) {
        //avg lines per 30 (excl. comments and spacing)
        //max 1000 lines of code per class (excl. method declarations)
        //max 30 methods

        int avgMethodLength=0;
        for(Map.Entry<String, MethodMetrics> entry : metrics.getMethodsMetrics().entrySet()){
            avgMethodLength += entry.getValue().getNumOfLines(); //extract number of lines from MethodMetrics of the file
        }
        avgMethodLength = (metrics.getNumOfMethods() >0) ?  avgMethodLength/metrics.getNumOfMethods() : 0;


        if(metrics.getNumOfMethods() > 50){
            if(avgMethodLength > 30 || metrics.getClassLengths().get(0) > 1000){
                severity = 3;
            }
            else if(avgMethodLength > 20 || metrics.getClassLengths().get(0) > 850){
                severity = 2;
            }
            else if(avgMethodLength > 10 || metrics.getClassLengths().get(0) > 700) {
                severity = 1;
            }
            else{
                severity = 0;
            }
        }
        else if(metrics.getNumOfMethods() > 40){
            if(avgMethodLength > 40 || metrics.getClassLengths().get(0) > 1150){
                severity = 3;
            }
            else if(avgMethodLength > 30 || metrics.getClassLengths().get(0) > 1000){
                severity = 2;
            }
            else if(avgMethodLength > 20 || metrics.getClassLengths().get(0) > 850) {
                severity = 1;
            }
            else{
                severity = 0;
            }
        }
        else if(metrics.getNumOfMethods() > 30){
            if(avgMethodLength > 50 || metrics.getClassLengths().get(0) > 1300){
                severity = 3;
            }
            else if(avgMethodLength > 40 || metrics.getClassLengths().get(0) > 1150){
                severity = 2;
            }
            else if(avgMethodLength > 30 || metrics.getClassLengths().get(0) > 1000) {
                severity = 1;
            }
            else{
                severity = 0;
            }
        }
        else{
            if(avgMethodLength > 60 || metrics.getClassLengths().get(0) > 1450){
                severity = 3;
            }
            else if(avgMethodLength > 50 || metrics.getClassLengths().get(0) > 1300){
                severity = 2;
            }
            else if(avgMethodLength > 40 || metrics.getClassLengths().get(0) > 1150) {
                severity = 1;
            }
            else{
                severity = 0;
            }
        }
    }

    @Override
    public String getSmellName() {
        return smellName;
    }

    @Override
    public String getResultComment() {
        return resultComments[severity];
    }
}
