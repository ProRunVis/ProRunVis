class TraceLen {

    static int compute(int n) {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                sum += i;
            } else {
                sum -= 1;
            }
            if (i % 5 == 0) {
                sum *= 2;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        int result = compute(50000);
        System.out.println("result = " + result);
    }
}
