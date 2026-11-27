class Nesting {

    static int compute(int x) {
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            x = x + i;
        if (x % 7 == 0) {
            if (x % 6 == 0) {
                if (x % 5 == 0) {
                    if (x % 4 == 0) {
                        if (x % 3 == 0) {
                            sum += 1;
                        } else {
                            sum += 1;
                        }
                    } else {
                        sum += 2;
                    }
                } else {
                    sum += 3;
                }
            } else {
                sum += 4;
            }
        } else {
            sum += 5;
        }
        }
        return sum;
    }

    public static void main(String[] args) {
        int result = compute(42);
        System.out.println("result = " + result);
    }
}
