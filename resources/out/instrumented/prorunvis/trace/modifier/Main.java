package prorunvis.trace.modifier;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        proRunVisTrace("11");
        int i = 8;
        for (; i < 12; i++) {
            proRunVisTrace("12");
            foo(i);
        }
        switch(i) {
            case 0:
                proRunVisTrace("13");
                return;
            case 5:
                proRunVisTrace("14");
                i = i++;
                break;
            case 6:
                proRunVisTrace("15");
                i = 0;
            case 11:
                proRunVisTrace("16");
                foo(3);
            default:
                proRunVisTrace("17");
                break;
        }
        bar("hii");
    }

    public static int foo(int in) {
        proRunVisTrace("18");
        if (in % 2 == 0) {
            proRunVisTrace("19");
            return in;
        } else {
            proRunVisTrace("20");
            return in + 1;
        }
    }

    public static String bar(String in) {
        proRunVisTrace("21");
        while (in.length() < 10) {
            proRunVisTrace("22");
            in = in + "l";
        }
        return in;
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
