package utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Setup {

    private String javaDir;
    private File[] files;
    private HashMap<String, String> javaFiles;

    public Setup(String dirPath){
        javaFiles = new HashMap<>();
        findJavaFiles(dirPath);
    }

    private void getFiles() {
        File directory = new File(this.javaDir);
        this.files = directory.listFiles();

        if (this.files != null) {
            for (File file : this.files) {
                if (isJava(file)) {

                    String javaFileName = file.getName().substring(0, file.getName().indexOf(".")); //removing .java extension from file name

                    try{
                        Scanner sc = new Scanner(file);
                        StringBuilder code = new StringBuilder();
                        while(sc.hasNextLine())
                        {
                           code.append(sc.nextLine()).append("\n");
                        }
                        sc.close();

                        this.javaFiles.put(javaFileName,code.toString());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
    private void findJavaFiles(String smellyCodeDir) {
        if(getDirectory(smellyCodeDir)){
            getFiles();
            for(File f: this.files)
            {
                if(f.isDirectory()) {
                    findJavaFiles(f.getAbsolutePath());
                }
            }
        }
    }

    private boolean getDirectory(String directoryName)
    {

        File directory = new File(directoryName);
        File[] fileList = directory.listFiles();

        boolean found = false;

        int i = 0;

        if (fileList != null && fileList.length > 0) {
            while (i < fileList.length && !found) {
                if (fileList[i].isDirectory()) {
                    found = getDirectory(fileList[i].getAbsolutePath());
                } else if (isJava(fileList[i])) {
                    this.javaDir = fileList[i].getAbsolutePath().substring(0, fileList[i].getAbsolutePath().indexOf(fileList[i].getName()));
                    found = true;
                }
                i++;
            }
            return found;
        }
        return false;
    }

    private boolean isJava(File file)
    {
        return getFileExtension(file).equals(".java");
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");

        if (lastIndexOf == -1) {
            return ""; // empty extension
        }

        return name.substring(lastIndexOf);
    }


    public CompilationUnit[] run(){
        CompilationUnit[] cUnit = new CompilationUnit[javaFiles.keySet().size()];
        int index = 0;
        System.out.println(javaFiles.keySet());

        for(Map.Entry<String, String> code: javaFiles.entrySet())
        {
            cUnit[index] = JavaParser.parse(code.getValue());
            index++;
        }

        return cUnit;

    }

    public File[] filesAccessor(){
        return this.files;
    }

    public HashMap<String, String> getSourceFiles(){
        return this.javaFiles;
    }

    /*public String[] getFileNames()
    {
        String[] fileNames = new String[javaFiles.keySet().size()];
        int index = 0;
        for(Map.Entry<String, String> code: javaFiles.entrySet())
        {
            fileNames[index] = code.getKey();
            index++;
        }
        return fileNames;
    }*/
}