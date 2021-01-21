package detect;

import detector.FeatureEnvyDetector;

public class FeatureEnvyDetectorTest {
    public static void main(String[] args){
        try{
            String[] sourceFilePaths={"D:\\TSE\\Resources\\HW\\testproject\\extra166y\\"};
            String[] encodings={"UTF-8"};
            String[] classPaths={"D:\\TSE\\Resources\\HW\\testproject\\lib\\"};
            //String nnModelPath = "D:\\TSE\\Resources\\HW\\FeatureEnvyModel.h5";
            //String embeddingModelPath = "D:\\TSE\\Resources\\HW\\new.bin";
            String hostIP="localhost";
            FeatureEnvyDetector featureEnvyDetector = new FeatureEnvyDetector(sourceFilePaths,encodings,classPaths,hostIP);
            featureEnvyDetector.detect();
            //还需要讲候选目标类中的非本项目类筛选掉
            //featureEnvyDetector.displayResult();
            //featureEnvyDetector.displayText("D:\\result.csv");
            //featureEnvyDetector.saveToDataBase();
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
