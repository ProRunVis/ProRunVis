package prorunvis.trace.process;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
     * @param path The path to the trace file.
     */
    public Scanner(final String path) {
        this.tokens = new Stack<>();
        this.pathToTrace = Paths.get(path);
    }

    /**
     * Reads the file specified by {@link #pathToTrace} to {@link #tokens}
     * in reverse order.
     * @throws IOException If the file does not exist or could not be
     *                     opened for other reasons.
     */
    public void readFile() throws IOException {

        try {
            File traceFile = pathToTrace.toFile();
            BufferedReader reader = new BufferedReader(new FileReader(traceFile));
            List<String> temp = new ArrayList<>(reader.lines().toList());
            Collections.reverse(temp);
            temp.forEach(x -> tokens.push(Integer.parseInt(x)));
        } catch (FileNotFoundException e) {
            throw new IOException("Could not read file.", e);
        }
    }

    /**
     * Gets the token stream.
     * @return A Stack of type Integer containing the read trace ids
     *         in reversed order.
     */
    public Stack<Integer> getTokens() {
        return this.tokens;
    }
}
