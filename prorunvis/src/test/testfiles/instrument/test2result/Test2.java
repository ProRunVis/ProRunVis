import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class Test2 {

    public static void main(String[] args) {
        proRunVisTrace("0");
        int x = 1;
        int i = 0;
        switch(x) {
            case 2:
                proRunVisTrace("1");
                i++;
                break;
            case 1:
                proRunVisTrace("2");
                i--;
                break;
            default:
                proRunVisTrace("3");
                i = 0;
                break;
        }
    }

    private static void proRunVisTrace(String trace) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/home/fred/Bp/prorunvis/prorunvis/src/test/testfiles/instrument/test2result/TraceFile.tr", true));
            writer.write(trace + System.lineSeparator());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
