package prorunvis.trace;

import com.google.gson.Gson;
import java.util.List;

public final class TraceProcessor {

    /**
     * This should not be called.
     */
    private TraceProcessor() {
        throw new IllegalStateException("This class should not be instantiated");
    }

    /**
     * @param entries the list
     * @return the created JSON string
     */
    public static String toJSON(final List<TraceNode> entries) {
        Gson gson = new Gson();
        StringBuilder jsonString = new StringBuilder();
        entries.forEach(entry -> jsonString.append("\n").append(gson.toJson(entry)));
        return jsonString.toString();
    }
}
