package utils;

import com.github.javaparser.ast.CompilationUnit;

public class Setup {

    private String dirPath;


    public Setup(String dirPath){
        this.dirPath = dirPath;
    }
    /*
    Setup finds all java files and creates compilation units that
    can then be used to extract info in Metrics and Smell classes
     */

    private String getFiles(){
        //TODO
        return null;
    }

    public CompilationUnit[] run(){
        //TODO
        return null;
    }
}
