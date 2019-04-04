package smells;


import metrics.FileMetrics;

//Too many comments make the code hard to read, better practice to have code that is self explanatory where possible
public class HeavyCommentingSmell extends AbstractCodeSmell{
    private final static String smellName = "Heavy Commenting";
    private static String[] resultComments = {
            "No heavy commenting here. All clear",
            "Traces of heavy commenting,relax.",
            "A bit too much commenting going on. Investigate.",
            "Comments everywhere ,unnecessary, fix immediately."
    };

    public HeavyCommentingSmell(){
        severity = 0;
    }

    @Override
    public void detectSmell(FileMetrics metrics) {

        /*assuming suggested length of method is 30, we shouldn't have more than half of each one commented
        also instead of commenting every line, user should comment stumps of code if the stump needs it*/

        int numOfComments = metrics.getCompilationUnit().getComments().size();

        if(numOfComments > metrics.getNumOfMethods() * 20) {
            //way too many comments which likely means code is hard to read
            severity = 3;
        }
        else if(numOfComments > metrics.getNumOfMethods()*15){
            //the number of comments is still dangerously high
            severity = 2;
        }
        else if(numOfComments > metrics.getNumOfMethods() * 10){
            //the number of comments is okay but is approaching a large number
            severity = 1;
        }
        else{
            severity = 0;
        }
    }

    @Override
    public String getSmellName() {
        return smellName;
    }

    @Override
    public String getResultComment(){
        return resultComments[severity];
    }
}
