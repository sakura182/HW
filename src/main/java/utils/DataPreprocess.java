package utils;

import java.util.ArrayList;

public class DataPreprocess {

    public static ArrayList<String> splitWord(String word) {
        ArrayList<String> res = new ArrayList<String>();
        String item = "";
        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            if(Character.isUpperCase(c) || c == '_' || c == '$' || Character.isDigit(c)){
                if(!item.equals("")) res.add(item.toLowerCase());
                item = "";
            }
            if(c != '_' && c != '$') item += c;
        }
        if(!item.equals("")) res.add(item.toLowerCase());
        return res;
    }

    public static ArrayList<String> fixWordsSize(ArrayList<String> words, int size, String unit){
        ArrayList<String> res = new ArrayList<String>();
        if(words.size() > size){
            res.addAll(words.subList(0,size));
            return res;
        }
        for(int i = 0; i < size - words.size(); i++) res.add(unit);
        res.addAll(words);
        return res;
    }

    public static String tokenize(String line){
        String[] splitString = line.split("((?<=\\.)|(?=\\.))| |((?<=\\{)|(?=\\{))|((?<=\\})|(?=\\}))|((?<=\\()|(?=\\())|((?<=\\))|(?=\\)))|((?<=\\[)|(?=\\[))|((?<=\\])|(?=\\]))|((?<=\\;)|(?=\\;))|((?<=\\,)|(?=\\,))|((?<=\\>)|(?=\\>))|((?<=\\<)|(?=\\<))");
        String rline = "";
        for(String token : splitString){
            token = token.trim();
            if (token != null && !(token.equals(""))){
                token = token.replaceAll("\t", "");
                token = token.replaceAll(" ", "");
                token = token.replaceAll("/", " ");
                token = token.replaceAll("\n", " ");
                boolean isWord=token.matches("[a-zA-Z0-9_$]+");
                boolean isCapitalWord = token.matches("[A-Z_$]+")||token.matches("[A-Z]+")||token.matches("[A-Z_]+")||token.matches("[A-Z$]+");
                if(isWord)
                    if(!isCapitalWord){
                        token = token.replaceAll("[A-Z0-9]", " $0");
                        token = token.replaceAll("[_$]", " $0 ");
                        token = token.toLowerCase();
                        rline += token + " ";
                    }
                    else{
                        token = token.toLowerCase();
                        token = token.replaceAll("[0-9_$]", " $0 ");
                        rline += token + " ";
                    }
            }

        }
        rline = rline.trim();
        rline = rline.replaceAll("[_$]", "");
        rline = rline.replaceAll("   ", " ");
        rline = rline.replaceAll("  ", " ");
        return rline;
    }

}
