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
        VariableDeclarationFragment f = (VariableDeclarationFragment)node.fragments().get(0);
        System.out.println("Filed: "+f.getName());
        System.out.println(node.toString());
        boolean ff=false;
        for(Object obj : node.modifiers()) if(obj instanceof Modifier){
            Modifier modifier = (Modifier)obj;
            if(modifier.isPrivate()) ff=true;
        }
        System.out.println(ff);
        return true;
    }

    public boolean visit(MethodDeclaration node){
        System.out.println("Method: "+node.toString().replace(node.getBody().toString(),""));
//        for(Object obj : node.parameters()) if(obj instanceof SingleVariableDeclaration){
//            SingleVariableDeclaration para = (SingleVariableDeclaration)obj;
//            System.out.println("Parameter Type: "+para.toString()+" || "+para.getName().toString());
//        }
        return true;
    }
}
