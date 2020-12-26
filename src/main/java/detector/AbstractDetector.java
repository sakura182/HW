package detector;

import java.io.IOException;

public abstract class AbstractDetector {
    protected String[] sourceFilePaths;
    protected String[] encodings;
    protected String[] classPaths;
    protected String nnModelPath;

    public AbstractDetector(String[] sourceFilePaths, String[] encodings, String[] classPaths,String nnModelPath){
        this.sourceFilePaths = sourceFilePaths;
        this.encodings = encodings;
        this.classPaths = classPaths;
        this.nnModelPath = nnModelPath;
    }

    public abstract void detect() throws IOException;

    public abstract void displayResult();

}
