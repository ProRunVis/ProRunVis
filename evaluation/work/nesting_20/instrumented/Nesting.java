class Nesting {

    static int compute(int x) {
        srctracer.Trace._FUNC(1);
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            srctracer.Trace._LOOP_BODY();
            x = x + i;
            if (x % 22 == 0) {
                srctracer.Trace._IF();
                if (x % 21 == 0) {
                    srctracer.Trace._IF();
                    if (x % 20 == 0) {
                        srctracer.Trace._IF();
                        if (x % 19 == 0) {
                            srctracer.Trace._IF();
                            if (x % 18 == 0) {
                                srctracer.Trace._IF();
                                if (x % 17 == 0) {
                                    srctracer.Trace._IF();
                                    if (x % 16 == 0) {
                                        srctracer.Trace._IF();
                                        if (x % 15 == 0) {
                                            srctracer.Trace._IF();
                                            if (x % 14 == 0) {
                                                srctracer.Trace._IF();
                                                if (x % 13 == 0) {
                                                    srctracer.Trace._IF();
                                                    if (x % 12 == 0) {
                                                        srctracer.Trace._IF();
                                                        if (x % 11 == 0) {
                                                            srctracer.Trace._IF();
                                                            if (x % 10 == 0) {
                                                                srctracer.Trace._IF();
                                                                if (x % 9 == 0) {
                                                                    srctracer.Trace._IF();
                                                                    if (x % 8 == 0) {
                                                                        srctracer.Trace._IF();
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
                                                                    } else {
                                                                        srctracer.Trace._ELSE();
                                                                        sum += 6;
                                                                    }
                                                                } else {
                                                                    srctracer.Trace._ELSE();
                                                                    sum += 7;
                                                                }
                                                            } else {
                                                                srctracer.Trace._ELSE();
                                                                sum += 8;
                                                            }
                                                        } else {
                                                            srctracer.Trace._ELSE();
                                                            sum += 9;
                                                        }
                                                    } else {
                                                        srctracer.Trace._ELSE();
                                                        sum += 10;
                                                    }
                                                } else {
                                                    srctracer.Trace._ELSE();
                                                    sum += 11;
                                                }
                                            } else {
                                                srctracer.Trace._ELSE();
                                                sum += 12;
                                            }
                                        } else {
                                            srctracer.Trace._ELSE();
                                            sum += 13;
                                        }
                                    } else {
                                        srctracer.Trace._ELSE();
                                        sum += 14;
                                    }
                                } else {
                                    srctracer.Trace._ELSE();
                                    sum += 15;
                                }
                            } else {
                                srctracer.Trace._ELSE();
                                sum += 16;
                            }
                        } else {
                            srctracer.Trace._ELSE();
                            sum += 17;
                        }
                    } else {
                        srctracer.Trace._ELSE();
                        sum += 18;
                    }
                } else {
                    srctracer.Trace._ELSE();
                    sum += 19;
                }
            } else {
                srctracer.Trace._ELSE();
                sum += 20;
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
