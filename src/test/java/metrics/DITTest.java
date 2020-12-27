package metrics;

import org.eclipse.jdt.core.dom.*;
import utils.Utils;

import java.io.*;

public class DITTest {
    public static void main(String[] args){
        try {
            String[] sourceFilePaths = {"D:\\huawei\\HW\\HW\\src\\main\\java\\"};
            String[] encodings = {"UTF-8"};
            String[] classPaths = {""};
            ASTParser parser = Utils.getNewASTParser(sourceFilePaths, encodings, classPaths);
            File f = new File("D:\\huawei\\HW\\HW\\src\\main\\java\\metrics\\DepthOfInheritanceTree.java");
            parser.setSource(getCodeFromFile(f).toCharArray());
            CompilationUnit cu = (CompilationUnit)parser.createAST(null);
            cu.accept(new Visitor());
            //System.out.println(getCodeFromFile(f));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String getCodeFromFile(File f) throws FileNotFoundException,IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(f));
        byte[] input = new byte[bufferedInputStream.available()];
        bufferedInputStream.read(input);
        bufferedInputStream.close();
        return new String(input);
    }
}

class Visitor extends ASTVisitor{
    public boolean visit(TypeDeclaration node){
//        ITypeBinding b = node.resolveBinding();
//        System.out.println("Type Code: "+node.toString());
//        System.out.println(b.getQualifiedName());
//        System.out.println(b.getSuperclass().getQualifiedName());
//        System.out.println(b.getSuperclass().getSuperclass().getQualifiedName());
        //System.out.println(b.getSuperclass().getSuperclass().getSuperclass().getQualifiedName());
        return true;
    }

    public boolean visit(FieldDeclaration node){
//        VariableDeclarationFragment f = (VariableDeclarationFragment)node.fragments().get(0);
//        System.out.println("Filed: "+f.getName());
//        ITypeBinding typeBinding = f.resolveBinding().getType();
//        System.out.println(typeBinding.getQualifiedName()+"  "+typeBinding.isFromSource());
        return true;
    }

    public boolean visit(MethodDeclaration node){
        System.out.println("Method: "+node.toString().replace(node.getBody().toString(),""));
        String code = node.toString().replace(node.getBody().toString(),"");
        if(node.getJavadoc() != null)
            code = code.replace(node.getJavadoc().toString(),"");
        System.out.println(code);

        return true;
    }
}
