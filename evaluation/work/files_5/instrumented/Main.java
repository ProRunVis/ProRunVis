class Main {

    public static void main(String[] args) {
        srctracer.Trace.trace_start("Main");
        try {
            srctracer.Trace._FUNC(1);
            int result = Worker0.process(7);
            System.out.println("result = " + result);
        } finally {
            srctracer.Trace.trace_end();
        }
    }
}
