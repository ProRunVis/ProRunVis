package prorunvis.trace.modifier;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class ForLoopModifierTest {

    void visit() {
        proRunVisTrace("8");
        int x = 0;
        for (int i = 0; i < 7; i++) {
            proRunVisTrace("9");
            x--;
        }
        for (int i = 0; i < 7; i++) {
            proRunVisTrace("10");
            x--;
        }
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
}>
