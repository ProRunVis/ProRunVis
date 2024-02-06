package prorunvis.trace.modifier;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class SwitchModifierTest {

    SwitchModifierTest() {
    }

    void visit() {
        proRunVisTrace("3");
        int x = 1;
        int i = 0;
        switch(x) {
            case 2:
                proRunVisTrace("4");
                i++;
                break;
            case 1:
                proRunVisTrace("5");
                i--;
                break;
            default:
                proRunVisTrace("6");
                i = 0;
                break;
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
}
