class Worker17 {

    static int process(int x) {
        int result = 0;
        if (x % 3 == 0) {
            result = x + 17;
        } else if (x % 3 == 1) {
            result = x - 17;
        } else {
            result = x * 18;
        }

        for (int j = 0; j < 10; j++) {
            if (j % 2 == 0) {
                result += j;
            }
        }

        return result + Worker18.process(x + 1);
    }
}
