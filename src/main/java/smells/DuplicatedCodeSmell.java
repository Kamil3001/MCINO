package smells;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.lang.reflect.Modifier;

import java.util.*;

import metrics.FileMetrics;
import metrics.MethodMetrics;
import results.Occurrence;
import utils.RabinKarp;

public class DuplicatedCodeSmell extends AbstractCodeSmell {

    private static int NO_BLOCKS_TO_CHECK = 3;
    private final static String smellName = "Duplicated Code";
    private String text;
    private List<String> methodNames;
    private HashMap methods;
    private RabinKarp rk;
    private int methodStartLine;
    private int methodEndLine;

    @Override
    public void detectSmell(FileMetrics metrics) {
        if(getFieldsAndMethods(metrics)) checkMethods();

    }
    /** instantiate file name, local HashMap of methods and List of method names
     * @param metrics the current file to analyze
     * @return true if file has methods i.e. is not annotation type**/
    private boolean getFieldsAndMethods(FileMetrics metrics){
        methodNames = new ArrayList<>();
        if(!metrics.getMethodsMetrics().keySet().isEmpty() ) {
            methods = metrics.getMethodsMetrics();
            Collection<String> collection = metrics.getMethodsMetrics().keySet();
            methodNames.addAll(collection);
            return true;
        }
        return false;
    }

    /** iterates through each method, removing it from the local HashMap
     * and if not Abstract, call findBlockDuplicates() on it's body **/
    private void checkMethods() {
        MethodMetrics aMethod;
        while(!methods.isEmpty()){
            aMethod = (MethodMetrics) methods.remove(methodNames.get(0));
            methodNames.remove(methodNames.get(0));
            if (!Modifier.isAbstract(aMethod.getModifiers())) {
                findBlockDuplicates(aMethod.getBody());

            }
        }
    }

    /** @param block the block to be checked for duplicates
     *   if this blocks number of statements are >= NO_BLOCKS_TO_CHECK
     *  then for each block of size = NO_BLOCKS_TO_CHECK, check against:
     *  1. The rest of the method body if its number of statements >= 2*NO_BLOCKS_TO_CHECK (i.e. duplicate in method)
     *  and/or
     *  2. The rest of the methods in the file.
     *   **/
    private void findBlockDuplicates(BlockStmt block){
        int size = block.getStatements().size();
        if(size >= NO_BLOCKS_TO_CHECK) {
            // Iterate through blocks of |NO_BLOCKS_TO_CHECK|
            for(int endIndex = 0; endIndex<size- NO_BLOCKS_TO_CHECK; endIndex+=3) {
                // combine statements, removing comments and whitespace
                String pattern = combineStatements(block, endIndex + (NO_BLOCKS_TO_CHECK - 1), false);
                if(block.getStatement(endIndex).getBegin().isPresent()
                        && block.getStatement(endIndex + (NO_BLOCKS_TO_CHECK - 1)).getEnd().isPresent()) {
                    methodStartLine = block.getStatement(endIndex).getBegin().get().line;
                    methodEndLine = block.getStatement(endIndex + (NO_BLOCKS_TO_CHECK - 1)).getEnd().get().line;
                }
                // instantiate RabinKarp setting the pattern to compare to
                rk = new RabinKarp(pattern);
                // If method size is big enough and we haven't reached last place the duplicate could exist
                if(size >= (2*NO_BLOCKS_TO_CHECK) && endIndex < (size-(2* NO_BLOCKS_TO_CHECK -1)) ) {
                    // for each block of |NO_BLOCKS_TO_CHECK| remaining
                    for (int startIndex = endIndex + NO_BLOCKS_TO_CHECK; startIndex < size - (NO_BLOCKS_TO_CHECK -1); startIndex++) {
                        // combine statements, removing comments and whitespace
                        text = combineStatements(block, startIndex, true);
                        // search text for instance of pattern returning location of first character
                        searchPattern(pattern, text, block);
                    }
                }
                // call method to check files other methods for pattern
                checkAllOtherMethods(pattern);

            }
        }
    }

    /**This method checks all remaining methods contained in the file for a duplicate of pattern.
     * Again it places any duplicate Occurrences in the HashMap
     * @param pattern is the pattern we're searching for
     *  **/
    private void checkAllOtherMethods(String pattern){
        for (String methodName : methodNames) {
            MethodMetrics methodMetrics = (MethodMetrics) methods.get(methodName);
            BlockStmt aBody = methodMetrics.getBody();
            if (methodMetrics.getNumOfStatements() >= NO_BLOCKS_TO_CHECK) {
                text = combineStatements(aBody, 0, true);
                searchPattern(pattern, text, aBody);
            }
        }
    }

    private void searchPattern(String pattern, String text, BlockStmt block){
        int result = rk.search(text);
        if (result > -1) {
            double similar = getSimilarityPercentage(pattern, text.substring(result, result + pattern.length()));
            if (similar == 100.0) {
                int lineNumber = lineNumberOfChar(result, block);
                Occurrence first = new Occurrence(methodStartLine , methodEndLine);
                Occurrence second = new Occurrence(first, lineNumber , (lineNumber+(methodEndLine-methodStartLine)));
                first.setLinkedOccurrence(second);
                occurrences.add(second);
                severity = 3;
            }
            else if(similar > 97) severity = 2;
            else if(similar > 95) severity = 1;
        }
    }

    /** For a given String block, this method returns a String with all the comments
     * of various types, (i.e. Multiline or Single Line), removed
     * @param block String to return without comments
     * @return String without comments but spaces and newline characters remain**/
    private String removeComments(String block) {
        String str = block.trim();
        str = str.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
        return str;
    }

    /** This method removes all whitespace and newline characters in
     * @param block String to remove whitespace and newline characters from
     * @return block with no whitespace or newline characters*/
    private String removeWhitespaceAndNewline(String block){
        return block.replaceAll("\\s", "");
    }

    /** This method combines statements in a method into blocks of size NO_OF_BLOCK_TO_CHECK,
     * returning a String with comments, whitespace and newline characters removed
     * @param block block we want statements combine from
     * @param index either start or end index depending for looping through statements
     * @param indexIsStart true if index is start false if index is end */
    private String combineStatements(BlockStmt block, int index, boolean indexIsStart){
        StringBuilder str= new StringBuilder();
        List<Statement> list = block.getStatements();
        for(int i=0;i<list.size();i++) {
            if (indexIsStart && i >= index) {
                str.append(removeComments(list.get(i).toString()));
            } else if (!indexIsStart && i <= index && i > index- NO_BLOCKS_TO_CHECK) {
                str.append(removeComments(list.get(i).toString()));
            }
        }
        return removeWhitespaceAndNewline(str.toString());
    }

    /** This method returns the similarity percentage of two strings as per the formula:
     * no. of equal characters / total no. of characters of longest string * 100
     * @param text1 first string to check
     * @param text2 second string to check
     * @return percentage similar */
    private double getSimilarityPercentage(String text1, String text2){
        double percent=0.0;
        String str = removeWhitespaceAndNewline(removeComments(text1));
        String str2 = removeWhitespaceAndNewline(removeComments(text2));
        int length = str.length()>str2.length() ? str2.length() : str.length();
        for(int i=0;i<length;i++){
            if(str.charAt(i) == str2.charAt(i)) percent+=1.0;
        }
        percent /= length;
        return percent*100;
    }

    private String getType(String str){ return str.substring(0, str.lastIndexOf(" ")); }

    private String getName(String str){ return str.substring(str.lastIndexOf(" ")); }

    /** For a given character number returned from rk.search() in RabinKarp this method
     * returns the line number that the duplicateion blocks start
     * @param result output from .search() i.e. character number not including whitespace or newline
     * @param block the block we need to find the line number of
     * @return line number of charAt(result)*/
    private int lineNumberOfChar(int result, BlockStmt block){
        int size = block.getStatements().size();
        List<Statement> statements = block.getStatements();
        for(int i=0;i<size;i++){
            int length = removeWhitespaceAndNewline(removeComments(statements.get(i).toString())).length();
            if(result < length) return statements.get(i).getBegin().isPresent() ? statements.get(i).getBegin().get().line : -1;
            result-=length;
        }
        return -1;
    }

    /** NOT USED ATM but could be used to search for methods with low severity similarity
     *  i.e. methods that return >97 from getSimilarityPercentage
     *  but return -1 from rk.search(text) so wont get checked
     * This method will replace all parameters of equal type in a method */
    private String replaceParameters(MethodMetrics aMethod, MethodMetrics anotherMethod){

        NodeList params = aMethod.getMethodDeclaration().getParameters();
        NodeList parameters = anotherMethod.getMethodDeclaration().getParameters();
        String block = aMethod.getBody().toString();
        for (int k = 0; k < params.size() && k < parameters.size(); k++) {
            String type = getType(params.get(k).toString());
            String name = getName(params.get(k).toString());
            if(getType(parameters.get(k).toString()).equals(type)){
                block = block.replaceAll(name, getName(parameters.get(k).toString()));
            }
        }
        return block;
    }

    @Override
    public String getSmellName() {
        return smellName;
    }

    @Override
    public int getSeverity() {
        return severity;
    }

    /* DUPLICATE CODE EXAMPLE - Uncomment for match with combineStatements() */
//    private String aMethod(BlockStmt block, int index, boolean indexIsStart){
//        StringBuilder str= new StringBuilder();
//        List<Statement> list = block.getStatements();
//        for(int i=0;i<list.size();i++) {
//            if (indexIsStart && i >= index) {
//                str.append(removeComments(list.get(i).toString()));
//            } else if (!indexIsStart && i <= index && i > index- NO_BLOCKS_TO_CHECK) {
//                str.append(removeComments(list.get(i).toString()));
//            }
//        }
//        return str.toString();
//    }
}