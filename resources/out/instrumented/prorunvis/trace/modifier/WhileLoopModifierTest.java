package prorunvis.trace.modifier;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class WhileLoopModifierTest {

    void visit() {
        proRunVisTrace("0");
        int x = 4;
        while (x > 0) {
            proRunVisTrace("1");
            x--;
        }
        do {
            proRunVisTrace("2");
            x++;
        } while (x < 3);
    }

    private static void proRunVisTrace(String trace) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/home/fred/Bp/prorunvis/resources/TraceFile.tr", true));
            writer.write(trace + System.lineSeparator());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
