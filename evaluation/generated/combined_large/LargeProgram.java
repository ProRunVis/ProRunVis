class LargeProgram {

    static int heavyLoop(int n) {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                if (i % 4 == 0) {
                    if (i % 8 == 0) {
                        sum += i * 3;
                    } else {
                        sum += i * 2;
                    }
                } else {
                    sum += i;
                }
            } else {
                if (i % 3 == 0) {
                    sum -= i;
                } else {
                    sum += 1;
                }
            }

            switch (i % 4) {
                case 0:
                    sum += 10;
                    break;
                case 1:
                    sum -= 5;
                    break;
                case 2:
                    sum *= 2;
                    break;
                default:
                    sum += 3;
            }
        }
        return sum;
    }

    static int recursive(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n % 2 == 0) {
            return n + recursive(n - 1);
        } else {
            return recursive(n - 1);
        }
    }

    public static void main(String[] args) {
        int a = heavyLoop(5000);
        int b = recursive(100);
        System.out.println("result = " + (a + b));
    }
}
