package detect;


import detector.GodClassDetector;

public class GodClassDetectorTest {
    public static void main(String[] args){
        try{
            String[] sourceFilePaths={"D:\\huawei\\HW\\testproject\\grinder-3.6\\src\\"};
            String[] encodings={"UTF-8"};
            String[] classPaths={"D:\\huawei\\HW\\testproject\\grinder-3.6\\lib\\"};
            String nnModelPath = "D:\\huawei\\HW\\GodClassModel.h5";
            String embeddingModelPath = "D:\\huawei\\HW\\bin2vec\\src\\test\\java\\resources\\new.bin";
            long t1 = System.currentTimeMillis();
            GodClassDetector featureEnvyDetector = new GodClassDetector(sourceFilePaths,encodings,classPaths,nnModelPath,embeddingModelPath);
            featureEnvyDetector.detect();
            //还需要讲候选目标类中的非本项目类筛选掉
            featureEnvyDetector.displayResult();
            long t2 = System.currentTimeMillis();
            //33.4s
            System.out.println(t2-t1);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
