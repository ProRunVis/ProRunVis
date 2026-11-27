class Worker19 {

    static int process(int x) {
        srctracer.Trace._FUNC(1);
        int result = 0;
        if (x % 3 == 0) {
            srctracer.Trace._IF();
            result = x + 19;
        } else {
            srctracer.Trace._ELSE();
            if (x % 3 == 1) {
                srctracer.Trace._IF();
                result = x - 19;
            } else {
                srctracer.Trace._ELSE();
                result = x * 20;
            }
        }
        for (int j = 0; j < 10; j++) {
            srctracer.Trace._LOOP_BODY();
            if (j % 2 == 0) {
                srctracer.Trace._IF();
                result += j;
            } else {
                srctracer.Trace._ELSE();
            }
        }
        srctracer.Trace._LOOP_END();
        {
            int __srctracer_ret$0 = result + x * 2;
            srctracer.Trace._RETURN();
            return __srctracer_ret$0;
        }
    }
}
