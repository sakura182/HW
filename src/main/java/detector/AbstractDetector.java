package detector;

import utils.Config;

import java.io.IOException;
import java.sql.SQLException;

public abstract class AbstractDetector {
    protected String[] sourceFilePaths;
    protected String[] encodings;
    protected String[] classPaths;
    //protected String nnModelPath;

/*    public AbstractDetector(String[] sourceFilePaths, String[] encodings, String[] classPaths,String nnModelPath) throws IOException {
        this.sourceFilePaths = sourceFilePaths;
        this.encodings = encodings;
        this.classPaths = classPaths;
        this.nnModelPath = nnModelPath;
        Config.init();
    }*/
public AbstractDetector(String[] sourceFilePaths, String[] encodings, String[] classPaths) throws IOException {
    this.sourceFilePaths = sourceFilePaths;
    this.encodings = encodings;
    this.classPaths = classPaths;
    Config.init();
}

    public abstract void detect() throws IOException;

    public abstract void displayResult();

    public abstract void displayText(String path);

    public abstract void saveToDataBase();

}
