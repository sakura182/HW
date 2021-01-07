package detect;

import detector.FeatureEnvyDetector;

public class FeatureEnvyDetectorTest {
    public static void main(String[] args){
        try{
            String[] sourceFilePaths={"D:\\huawei\\HW\\testproject\\grinder-3.6\\src\\"};
            String[] encodings={"UTF-8"};
            String[] classPaths={"D:\\huawei\\HW\\testproject\\grinder-3.6\\lib\\"};
            String nnModelPath = "D:\\huawei\\HW\\FeatureEnvyModel.h5";
            String embeddingModelPath = "D:\\huawei\\HW\\bin2vec\\src\\test\\java\\resources\\new.bin";
            FeatureEnvyDetector featureEnvyDetector = new FeatureEnvyDetector(sourceFilePaths,encodings,classPaths,nnModelPath,embeddingModelPath);
            featureEnvyDetector.detect();
            featureEnvyDetector.displayResult();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
