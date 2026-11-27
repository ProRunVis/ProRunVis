class Nesting {

    static int compute(int x) {
        srctracer.Trace._FUNC(1);
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            srctracer.Trace._LOOP_BODY();
            x = x + i;
            if (x % 7 == 0) {
                srctracer.Trace._IF();
                if (x % 6 == 0) {
                    srctracer.Trace._IF();
                    if (x % 5 == 0) {
                        srctracer.Trace._IF();
                        if (x % 4 == 0) {
                            srctracer.Trace._IF();
                            if (x % 3 == 0) {
                                srctracer.Trace._IF();
                                sum += 1;
                            } else {
                                srctracer.Trace._ELSE();
                                sum += 1;
                            }
                        } else {
                            srctracer.Trace._ELSE();
                            sum += 2;
                        }
                    } else {
                        srctracer.Trace._ELSE();
                        sum += 3;
                    }
                } else {
                    srctracer.Trace._ELSE();
                    sum += 4;
                }
            } else {
                srctracer.Trace._ELSE();
                sum += 5;
            }
        }
        srctracer.Trace._LOOP_END();
        {
            int __srctracer_ret$0 = sum;
            srctracer.Trace._RETURN();
            return __srctracer_ret$0;
        }
    }

    public static void main(String[] args) {
        srctracer.Trace.trace_start("Nesting");
        try {
            srctracer.Trace._FUNC(2);
            int result = compute(42);
            System.out.println("result = " + result);
        } finally {
            srctracer.Trace.trace_end();
        }
    }
}
