package smells;

import com.github.javaparser.ast.CompilationUnit;
import metrics.FileMetrics;
import utils.Setup;

public class SmellDetector {

    private FileMetrics[] metrics;
    private CompilationUnit[] CUs;

    public SmellDetector(String dirPath){
        Setup setup = new Setup(dirPath);
        CUs = setup.run();
        metrics = new FileMetrics[CUs.length];
        getMetrics(CUs);
        detectSmells();
    }

    private void getMetrics(CompilationUnit[] CUs){
        for(int i=0; i<metrics.length; i++){
            metrics[i] = new FileMetrics(CUs[i]);
        }
    }

    private void detectSmells() {
        //call all smells here passing in the relevant args
    }
}