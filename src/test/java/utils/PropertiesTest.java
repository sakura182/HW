package utils;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesTest {
    public static void main(String[] args) {
        try {
            Properties p = new Properties();
            FileInputStream f = new FileInputStream("./src/config.properties");
            p.load(f);
            f.close();
            System.out.println(p.getProperty("ip"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
