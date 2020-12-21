package parser;

public class GodClassParserTest {
    public static void main(String[] args){
        try{
            String[] sourceFilePaths={"D:\\huawei\\HW\\testproject\\grinder-3.6\\src\\"};
            String[] encodings={"UTF-8"};
            String[] classPaths={"D:\\huawei\\HW\\testproject\\grinder-3.6\\lib\\"};
            GodClassParser gcp = new GodClassParser(sourceFilePaths,encodings,classPaths);
            gcp.parse();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
