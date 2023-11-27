package prorunvis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[]args){

        if(args.length == 0
                || args[0].length() < 6){
            System.out.println("Invalid input");
            return;
        }

        if(!args[0].endsWith(".java")) {
            System.out.println("No .java file found");
            return;
        }

        Path path = Paths.get(args[0]);
        File file = path.toFile().getAbsoluteFile();

        if(!file.exists()) {
            System.out.println("Specified file does not exist");
            return;
        }

        CompilationUnit cu;
        try {
            cu = StaticJavaParser.parse(file);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
