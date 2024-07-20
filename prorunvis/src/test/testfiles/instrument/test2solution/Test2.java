class Test2 {

    public static void main(String[] args) {
        prorunvis.Trace.next_elem(0);
        int x = 1;
        int i = 0;
        switch(x) {
            case 2:
                prorunvis.Trace.next_elem(1);
                i++;
                break;
            case 1:
                prorunvis.Trace.next_elem(2);
                i--;
                break;
            default:
                prorunvis.Trace.next_elem(3);
                i = 0;
                break;
        }
    }
}
