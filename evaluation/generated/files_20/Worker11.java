class Worker11 {

    static int process(int x) {
        int result = 0;
        if (x % 3 == 0) {
            result = x + 11;
        } else if (x % 3 == 1) {
            result = x - 11;
        } else {
            result = x * 12;
        }

        for (int j = 0; j < 10; j++) {
            if (j % 2 == 0) {
                result += j;
            }
        }

        return result + Worker12.process(x + 1);
    }
}
