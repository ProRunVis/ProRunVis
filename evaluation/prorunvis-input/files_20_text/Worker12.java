class Worker12 {

    static int process(int x) {
        int result = 0;
        if (x % 3 == 0) {
            result = x + 12;
        } else if (x % 3 == 1) {
            result = x - 12;
        } else {
            result = x * 13;
        }

        for (int j = 0; j < 10; j++) {
            if (j % 2 == 0) {
                result += j;
            }
        }

        return result + Worker13.process(x + 1);
    }
}
