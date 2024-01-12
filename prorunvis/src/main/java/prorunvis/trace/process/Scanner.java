package prorunvis.trace.process;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Scanner {

    private final Stack<Integer> tokens;
    private final Path pathToTrace;

    public Scanner(String path){
        this.tokens = new Stack<>();
        this.pathToTrace = Paths.get(path);
    }

    public void readFile() throws IOException{

        try{
            File traceFile = pathToTrace.toFile();
            BufferedReader reader = new BufferedReader(new FileReader(traceFile));
            List<String> temp = new ArrayList<>(reader.lines().toList());
            Collections.reverse(temp);
            temp.forEach(x -> tokens.push(Integer.parseInt(x)));
        } catch (FileNotFoundException e){
            throw new IOException("Could not read file.", e);
        }
    }

    public Stack<Integer> getTokens(){
        return this.tokens;
    }
}
