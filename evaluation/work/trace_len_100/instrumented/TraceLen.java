class TraceLen {

    static int compute(int n) {
        srctracer.Trace._FUNC(1);
        int sum = 0;
        for (int i = 0; i < n; i++) {
            srctracer.Trace._LOOP_BODY();
            if (i % 2 == 0) {
                srctracer.Trace._IF();
                sum += i;
            } else {
                srctracer.Trace._ELSE();
                sum -= 1;
            }
            if (i % 5 == 0) {
                srctracer.Trace._IF();
                sum *= 2;
            } else {
                srctracer.Trace._ELSE();
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
        srctracer.Trace.trace_start("TraceLen");
        try {
            srctracer.Trace._FUNC(2);
            int result = compute(100);
            System.out.println("result = " + result);
        } finally {
            srctracer.Trace.trace_end();
        }
    }
}
