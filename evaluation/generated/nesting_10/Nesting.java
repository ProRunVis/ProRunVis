class Nesting {

    static int compute(int x) {
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            x = x + i;
        if (x % 12 == 0) {
            if (x % 11 == 0) {
                if (x % 10 == 0) {
                    if (x % 9 == 0) {
                        if (x % 8 == 0) {
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
                        } else {
                            sum += 6;
                        }
                    } else {
                        sum += 7;
                    }
                } else {
                    sum += 8;
                }
            } else {
                sum += 9;
            }
        } else {
            sum += 10;
        }
        }
        return sum;
    }

    public static void main(String[] args) {
        int result = compute(42);
        System.out.println("result = " + result);
    }
}
