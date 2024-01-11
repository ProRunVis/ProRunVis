package prorunvis.trace;

import com.google.gson.Gson;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class TraceProcessor {

    /**
     * This should not be called.
     */
    private TraceProcessor() {
        throw new IllegalStateException("This class should not be instantiated");
    }

    /**
     * Takes in a trace file and reads the entries into a list.
     * @param traceFile a trace file, created by an instrumented program during runtime
     * @return A List of all entries
     * @throws IOException if an error occurs during reading
     */
    public static List<CodeEntry> parseToCodeEntries(final File traceFile) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(traceFile));
        List<CodeEntry> entries = new ArrayList<>();

        for (String currentEntry = reader.readLine(); currentEntry != null; currentEntry = reader.readLine()) {
            String[] splitEntry = currentEntry.split(",");
            for (int i = 0; i < splitEntry.length; i++) {
                splitEntry[i] = splitEntry[i].trim();
            }
            entries.add(new CodeEntry(splitEntry[0], splitEntry[1], Integer.parseInt(splitEntry[2]), Integer.parseInt(splitEntry[3])));
        }

        return entries;
    }

    /**
     * Takes in a list of {@link CodeEntry}s and returns a continuous string of corresponding JSON entries.
     * @param entries the list
     * @return the created JSON string
     */
    public static String toJSON(final List<CodeEntry> entries) {
        Gson gson = new Gson();
        StringBuilder jsonString = new StringBuilder();
        entries.forEach(entry -> jsonString.append("\n").append(gson.toJson(entry)));
        return jsonString.toString();
    }
}
