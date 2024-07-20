class Test1 {

    public static void main(String[] args) {
        prorunvis.Trace.next_elem(0);
        int x = 0;
        for (int i = 0; i < 7; i++) {
            prorunvis.Trace.next_elem(1);
            x--;
        }
        for (int i = 0; i < 7; i++) {
            prorunvis.Trace.next_elem(2);
            x--;
        }
    }
}
