package prorunvis.trace.modifier;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Return {

    void test() {
        proRunVisTrace("7");
        int x = 0;
        /*if (x==0)
            return;
        switch(x) {
            case 1:
                return;
            case 2:
                return;
        }*/
        return;
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
