package smells;

import com.github.javaparser.ast.CompilationUnit;
import metrics.FileMetrics;
import results.SmellResult;
import utils.Setup;

import java.util.ArrayList;

public class SmellDetector {

    private FileMetrics[] metrics;
    private CompilationUnit[] CUs;
    private AbstractCodeSmell[] smells;
    private ArrayList<SmellResult> smellResults;

    public SmellDetector(String dirPath){
        Setup setup = new Setup(dirPath);
        CUs = setup.run();
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
        smells[6] = new LazyClassSmell();
        smells[7] = new RefusedBequestSmell();

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
                //todo atm jsut calls the method for each smell but should take results and do something with them
                smell.detectSmell(fm);
                result.addOccurrences(fm.getClassNames().get(0), smell.getOccurrences());
                result.addSeverity(fm.getClassNames().get(0), smell.getSeverity());
            }
        }
    }
}