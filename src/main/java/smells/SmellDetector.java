package smells;

import com.github.javaparser.ast.CompilationUnit;
import metrics.FileMetrics;
import results.SmellResult;
import utils.Setup;

import java.util.ArrayList;
import java.util.HashMap;

public class SmellDetector {

    private FileMetrics[] metrics;
    private CompilationUnit[] CUs;
    private AbstractCodeSmell[] smells;
    private ArrayList<SmellResult> smellResults;
    private HashMap<String,String> sourceFiles;

    public SmellDetector(String dirPath){
        Setup setup = new Setup(dirPath);
        CUs = setup.run();
        sourceFiles = setup.getSourceFiles();
        metrics = new FileMetrics[CUs.length];
        getMetrics();
        instantiateSmells();
    }

    private void instantiateSmells(){
        smells = new AbstractCodeSmell[8];
        smells[0] = new BloatedCodeSmell();
        smells[1] = new CyclomaticComplexitySmell();
        smells[2] = new DataClumpsSmell();
        smells[3] = new DuplicatedCodeSmell();
        smells[4] = new FeatureEnvySmell();
        smells[5] = new HeavyCommentingSmell();
        smells[6] = new LazyClassSmell(metrics);
        smells[7] = new RefusedBequestSmell(CUs, metrics);

    }

    private void getMetrics(){
        for(int i=0; i<metrics.length; i++){
            metrics[i] = new FileMetrics(CUs[i]);
        }
    }

    public void detectSmells() {
        smellResults = new ArrayList<>();
        for(AbstractCodeSmell smell : smells){
            SmellResult result = new SmellResult(smell.getSmellName());

            for(FileMetrics fm : metrics) {
                smell.detectSmell(fm);
                result.addOccurrences(fm.getClassNames().get(0), smell.getOccurrences());
                result.addSeverity(fm.getClassNames().get(0), smell.getSeverity());
                smellResults.add(result);
            }
        }
    }

    public ArrayList<SmellResult> getSmellResults(){
        return smellResults;
    }

    public AbstractCodeSmell[] getSmells(){
        return smells;
    }

    public CompilationUnit[] getCUs()
    {
        return CUs;
    }

    public HashMap<String,String> getSourceFiles()
    {
        return this.sourceFiles;
    }

}