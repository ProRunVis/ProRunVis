class Worker13 {

    static int process(int x) {
        int result = 0;
        if (x % 3 == 0) {
            result = x + 13;
        } else if (x % 3 == 1) {
            result = x - 13;
        } else {
            result = x * 14;
        }

        for (int j = 0; j < 10; j++) {
            if (j % 2 == 0) {
                result += j;
            }
        }

        return result + Worker14.process(x + 1);
    }
}
