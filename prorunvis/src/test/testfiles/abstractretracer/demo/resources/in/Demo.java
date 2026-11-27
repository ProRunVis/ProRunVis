class Demo {

    static int demo(int x) {
        if (x < 0) {
            return -1;
        }

        int sum = 0;
        for (int i = 0; i < x; i++) {
            sum += i;
        }

        return sum;
    }

    public static void main(String[] args) {
        int result = demo(3);
        System.out.println("result = " + result);
    }
}
