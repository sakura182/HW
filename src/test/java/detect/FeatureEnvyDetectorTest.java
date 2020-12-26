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
            long t1 = System.currentTimeMillis();
            FeatureEnvyDetector featureEnvyDetector = new FeatureEnvyDetector(sourceFilePaths,encodings,classPaths,nnModelPath,embeddingModelPath);
            featureEnvyDetector.detect();
            //还需要讲候选目标类中的非本项目类筛选掉
            featureEnvyDetector.displayResult();
            long t2 = System.currentTimeMillis();
            //30.5s
            System.out.println(t2-t1);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}