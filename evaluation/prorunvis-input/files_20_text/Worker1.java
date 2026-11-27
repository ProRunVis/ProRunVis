class Worker1 {

    static int process(int x) {
        int result = 0;
        if (x % 3 == 0) {
            result = x + 1;
        } else if (x % 3 == 1) {
            result = x - 1;
        } else {
            result = x * 2;
        }

        for (int j = 0; j < 10; j++) {
            if (j % 2 == 0) {
                result += j;
            }
        }

        return result + Worker2.process(x + 1);
    }
}
