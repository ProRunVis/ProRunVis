public class ThrowTest {

    public static void main(String[] args) throws IllegalStateException, IllegalArgumentException{

        ThrowTest2.foo(0);
        try {
           ThrowTest2.foo(1);
        } catch (IllegalArgumentException e) {
            ThrowTest2.foo(2);
        }

        int o = 0;
    }
}