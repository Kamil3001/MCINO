package smells;

import metrics.FileMetrics;

//Too many comments make the code hard to read, better practice to have code that is self explanatory where possible
public class HeavyCommentingSmell extends AbstractCodeSmell{

    @Override
    public void detectSmell(FileMetrics metrics) {

        //assuming suggested length of method is 30, we shouldn't have more than half of that commented
        //also instead of commenting every line, user should comment stumps of code if the stump needs it

        if(metrics.getCompilationUnit().getComments().size() > metrics.getNumOfMethods() * 15) {
            //likely way too many comments implying that code is not self descriptive
            //suggest utilising more descriptive/indicative names
        }
    }
}
