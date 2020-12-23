package utils;

import java.util.ArrayList;

public class DataPreprocessTest {
    public static void main(String[] args){
        ArrayList<String> res = DataPreprocess.splitWord("JinJiahao_zhang$");
        for(String t : res){
            System.out.println(t);
        }
        res = DataPreprocess.fixWordsSize(res,5,"*");
        System.out.println("------------------------");
        for(String t : res){
            System.out.println(t);
        }
    }
}
