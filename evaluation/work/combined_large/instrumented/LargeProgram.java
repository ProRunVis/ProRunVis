class LargeProgram {

    static int heavyLoop(int n) {
        srctracer.Trace._FUNC(1);
        int sum = 0;
        for (int i = 0; i < n; i++) {
            srctracer.Trace._LOOP_BODY();
            if (i % 2 == 0) {
                srctracer.Trace._IF();
                if (i % 4 == 0) {
                    srctracer.Trace._IF();
                    if (i % 8 == 0) {
                        srctracer.Trace._IF();
                        sum += i * 3;
                    } else {
                        srctracer.Trace._ELSE();
                        sum += i * 2;
                    }
                } else {
                    srctracer.Trace._ELSE();
                    sum += i;
                }
            } else {
                srctracer.Trace._ELSE();
                if (i % 3 == 0) {
                    srctracer.Trace._IF();
                    sum -= i;
                } else {
                    srctracer.Trace._ELSE();
                    sum += 1;
                }
            }
            boolean __srctracer_switch$0 = true;
            switch(i % 4) {
                case 0:
                    if (__srctracer_switch$0) {
                        srctracer.Trace._CASE(0);
                        __srctracer_switch$0 = false;
                    }
                    sum += 10;
                    break;
                case 1:
                    if (__srctracer_switch$0) {
                        srctracer.Trace._CASE(1);
                        __srctracer_switch$0 = false;
                    }
                    sum -= 5;
                    break;
                case 2:
                    if (__srctracer_switch$0) {
                        srctracer.Trace._CASE(2);
                        __srctracer_switch$0 = false;
                    }
                    sum *= 2;
                    break;
                default:
                    if (__srctracer_switch$0) {
                        srctracer.Trace._CASE(3);
                        __srctracer_switch$0 = false;
                    }
                    sum += 3;
            }
        }
        srctracer.Trace._LOOP_END();
        {
            int __srctracer_ret$0 = sum;
            srctracer.Trace._RETURN();
            return __srctracer_ret$0;
        }
    }

    static int recursive(int n) {
        srctracer.Trace._FUNC(2);
        if (n <= 0) {
            srctracer.Trace._IF();
            {
                int __srctracer_ret$1 = 0;
                srctracer.Trace._RETURN();
                return __srctracer_ret$1;
            }
        } else {
            srctracer.Trace._ELSE();
        }
        if (n % 2 == 0) {
            srctracer.Trace._IF();
            {
                int __srctracer_ret$3 = n + recursive(n - 1);
                srctracer.Trace._RETURN();
                return __srctracer_ret$3;
            }
        } else {
            srctracer.Trace._ELSE();
            {
                int __srctracer_ret$2 = recursive(n - 1);
                srctracer.Trace._RETURN();
                return __srctracer_ret$2;
            }
        }
    }

    public static void main(String[] args) {
        srctracer.Trace.trace_start("LargeProgram");
        try {
            srctracer.Trace._FUNC(3);
            int a = heavyLoop(5000);
            int b = recursive(100);
            System.out.println("result = " + (a + b));
        } finally {
            srctracer.Trace.trace_end();
        }
    }
}
