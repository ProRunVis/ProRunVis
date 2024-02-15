class Test1 {

    public static void main(String[] args) {
        prorunvis.ProRunVis.proRunVisTrace("0");
        int x = 0;
        for (int i = 0; i < 7; i++) {
            prorunvis.ProRunVis.proRunVisTrace("1");
            x--;
        }
        for (int i = 0; i < 7; i++) {
            prorunvis.ProRunVis.proRunVisTrace("2");
            x--;
        }
    }
}
