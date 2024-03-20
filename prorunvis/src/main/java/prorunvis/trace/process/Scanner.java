package prorunvis.trace.process;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * A Scanner used by {@link TraceProcessor} to convert a
 * trace file to a stack of trace id's for further
 * processing.
 */
public class Scanner {

    /**
     * The stream of tokens from the trace file.
     */
    private final Stack<Integer> tokens;

    /**
     * The path to the trace file read by this scanner.
     */
    private final Path pathToTrace;

    /**
     * Constructs a scanner to read a token stream of trace ids.
     *
     * @param path The path to the trace file.
     */
    public Scanner(final String path) {
        this.tokens = new Stack<>();
        this.pathToTrace = Paths.get(path);
    }

    /**
     * Reads the file specified by {@link #pathToTrace} to {@link #tokens}
     * in reverse order.
     *
     * @return a stack of Integers containing the trace id's with the top element
     * being the first id in the trace
     * @throws IOException If the file does not exist or could not be
     *                     opened for other reasons.
     */
    public Stack<Integer> readFile() throws IOException {

        try {
            File traceFile = pathToTrace.toFile();
            BufferedReader reader = new BufferedReader(new FileReader(traceFile));
            List<String> temp = new ArrayList<>(reader.lines().toList());
            //reverse the list before pushing on the stack to get the correct order
            Collections.reverse(temp);
            temp.forEach(x -> tokens.push(Integer.parseInt(x)));
            //close reader
            reader.close();
        } catch (FileNotFoundException e) {
            throw new IOException("Could not read file.", e);
        }

        return tokens;
    }
}
