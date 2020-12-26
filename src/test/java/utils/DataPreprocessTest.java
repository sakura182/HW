package utils;

import java.util.ArrayList;

public class DataPreprocessTest {
    public static void main(String[] args){
        String name = "/**\n" +
                "     * id\n" +
                "     */\n" +
                "    @Id\n" +
                "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
                "   //this is test\n" +
                "    private Long id;\n" +
                "\n" +
                "    /**\n" +
                "     * 订单号\n" +
                "     */\n" +
                "    private String orderNum;\n" +
                "\n" +
                "    /**\n" +
                "     * 订单创建时间\n" +
                "     */\n" +
                "    private Long createTime;";
        System.out.println(name);
        System.out.println("---------------------------------------");
        System.out.println(Utils.deleteComments(name));
    }
}
