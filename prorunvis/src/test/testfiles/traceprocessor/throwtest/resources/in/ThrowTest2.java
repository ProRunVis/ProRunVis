public class ThrowTest2 {

    public static void foo(int i) throws IllegalArgumentException, IllegalStateException {

        int l = i + 1;

        try {
            if (i == 0) {
                throw new IndexOutOfBoundsException();
            } else if (i == 1) {
                throw new IllegalArgumentException();
            } else if (i == 2){
                throw new IllegalStateException();
            }
            l = 5;
        } catch (IndexOutOfBoundsException e) {}
        l = 10;
        return;
    }
}