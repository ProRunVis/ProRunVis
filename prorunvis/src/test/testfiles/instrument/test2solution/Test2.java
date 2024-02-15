class Test2 {

    public static void main(String[] args) {
        prorunvis.ProRunVis.proRunVisTrace("0");
        int x = 1;
        int i = 0;
        switch(x) {
            case 2:
                prorunvis.ProRunVis.proRunVisTrace("1");
                i++;
                break;
            case 1:
                prorunvis.ProRunVis.proRunVisTrace("2");
                i--;
                break;
            default:
                prorunvis.ProRunVis.proRunVisTrace("3");
                i = 0;
                break;
        }
    }
}
