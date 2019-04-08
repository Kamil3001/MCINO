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

    private static int NO_BLOCKS_TO_CHECK = 2;
    private final static String smellName = "Duplicated Code";
    private static String[] resultComments = {
            "No duplicated code here. Run along.",
            "Showing signs of duplicated code. Investigate.",
            "Code heavily duplicated. Fix as soon as possible.",
            "Duplicated code everywhere. Solve immediately."
    };
    private String file;
    private List<String> methodNames;
    private HashMap methods;
    private int instances = 0;
    private Map<Occurrence, Occurrence> duplicateLines;
    private RabinKarp rk;
    private int methodStartLine;
    private String currentMethod;

    @Override
    public void detectSmell(FileMetrics metrics) {
        duplicateLines = new HashMap<>();
        if(getFieldsAndMethods(metrics)) checkMethods();

    }
    /** instantiate file name, local HashMap of methods and List of method names
     * @param metrics the current file to analyze
     * @return true if file has methods i.e. is not annotation type**/
    private boolean getFieldsAndMethods(FileMetrics metrics){
        if(metrics.getCompilationUnit().getTypes().size()>0)
        file = metrics.getCompilationUnit().getType(0).getName().asString();
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
                currentMethod = aMethod.getMethodDeclaration().getNameAsString();
                methodStartLine = aMethod.getStartLine();
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
        String text, pattern;
        int result;
        int size = block.getStatements().size();
        if(size >= NO_BLOCKS_TO_CHECK) {
            // Iterate through blocks of |NO_BLOCKS_TO_CHECK|
            for(int endIndex = 0; endIndex<size- NO_BLOCKS_TO_CHECK; endIndex+=3) {
                // combine statements, removing comments and whitespace
                pattern = combineStatements(block, endIndex+(NO_BLOCKS_TO_CHECK-1), false);
                methodStartLine = block.getStatement(endIndex).getBegin().isPresent() ? block.getStatement(endIndex).getBegin().get().line : -1;
                // instantiate RabinKarp setting the pattern to compare to
                rk = new RabinKarp(pattern);
                // If method size is big enough and we haven't reached last place the duplicate could exist
                if(size >= (2*NO_BLOCKS_TO_CHECK) && endIndex < (size-(2* NO_BLOCKS_TO_CHECK -1)) ) {
                    // for each block of |NO_BLOCKS_TO_CHECK| remaining
                    for (int startIndex = endIndex + NO_BLOCKS_TO_CHECK; startIndex < size - (NO_BLOCKS_TO_CHECK -1); startIndex++) {
                        int offset = block.getStatement(startIndex).getBegin().isPresent() ? block.getStatement(startIndex).getBegin().get().line : 0;
                        // combine statements, removing comments and whitespace
                        text = combineStatements(block, startIndex, true);
                        // search text for instance of pattern returning location of first character
                        result = rk.search(text);
                        if (result > -1) { // if duplicate found
                            // ensure not false equivalence due to same hashCode
                            if(getSimilarityPercentage(pattern, text.substring(result, result + pattern.length()))==100.0) {
                                Occurrence first = new Occurrence(getSmellName(), file, currentMethod, methodStartLine);
                                Occurrence second = new Occurrence(getSmellName(), file, currentMethod, offset);
                                duplicateLines.put(first, second); // put instance of duplication in HashMap
                                instances++;
                            }
                        }
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
        String text;
        int result;
        for (String methodName : methodNames) {
            MethodMetrics methodMetrics = (MethodMetrics) methods.get(methodName);
            BlockStmt aBody = methodMetrics.getBody();
            if (methodMetrics.getNumOfStatements() >= NO_BLOCKS_TO_CHECK) {
                text = combineStatements(aBody, 0, true);
                result = rk.search(text);
                if (result > -1) {
                    if (getSimilarityPercentage(pattern, text.substring(result, result + pattern.length())) == 100.0) {
                        Occurrence first = new Occurrence(getSmellName(), file, currentMethod, methodStartLine);
                        Occurrence second = new Occurrence(getSmellName(), file, methodMetrics.getMethodDeclaration().getNameAsString(), lineNumberOfChar(result, aBody));
                        duplicateLines.put(first, second);
                        instances++;
                    }
                }
            }
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
     * @param result output from .search() i.e. character number not including whitespcae or newline
     * @param block the block we need to find the line number of
     * @return line number of charAt(result)*/
    private int lineNumberOfChar(int result, BlockStmt block){
        int size = block.getStatements().size();
        List<Statement> statements = block.getStatements();
        for(int i=0;i<size;i++){
            int length = removeComments(statements.get(i).toString()).length();
            if(result < length) return statements.get(i).getBegin().isPresent() ? statements.get(i).getBegin().get().line : -1;
            result-=length;
        }
        return -1;
    }

    /**                     NOT USED ATM
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
    public List<Integer> getOccurrences() {
        List<Integer> list = new ArrayList<>();
        for (Occurrence occ : duplicateLines.keySet()) {
            list.add(occ.getLineNumber());
        }
        return list;
    }

    @Override
    public int getSeverity() {
        return instances;
    }
    public Map getDuplicates(){ return duplicateLines; }

    @Override
    public String getResultComment() {
        return resultComments[severity];
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