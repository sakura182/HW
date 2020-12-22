package parser;

public class FeatureEnvyParserTest {
    public static void main(String[] args){
        try{
            String[] sourceFilePaths={"D:\\huawei\\HW\\testproject\\grinder-3.6\\src\\"};
            String[] encodings={"UTF-8"};
            String[] classPaths={"D:\\huawei\\HW\\testproject\\grinder-3.6\\lib\\"};
            FeatureEnvyParser fep = new FeatureEnvyParser(sourceFilePaths,encodings,classPaths);
            fep.parse();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
