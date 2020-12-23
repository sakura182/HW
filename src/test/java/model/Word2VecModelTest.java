package model;

public class Word2VecModelTest {
    public static void main(String[] args){
        try {
            Word2VecModel w = new Word2VecModel("D:\\huawei\\HW\\bin2vec\\src\\test\\java\\resources\\new.bin");
            System.out.println(w.getWordVector("test")[0]);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
