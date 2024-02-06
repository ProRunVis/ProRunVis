import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class ForLoopTest {

    public static void main(String[] args) {
        proRunVisTrace("0");
        int x = 0;
        for (int i = 0; i < 7; i++) {
            proRunVisTrace("1");
            x--;
        }
        for (int i = 0; i < 7; i++) {
            proRunVisTrace("2");
            x--;
        }
    }

    private static void proRunVisTrace(String trace) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/home/fred/Bp/prorunvis/prorunvis/src/test/testfiles/instrument/test1result/TraceFile.tr", true));
            writer.write(trace + System.lineSeparator());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
