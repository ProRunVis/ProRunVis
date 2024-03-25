public class ReturnTest {

    public static void main(String[] args) {

        foo();
        int l = 0;
        int i = 3;
        if (i == 3) return;
        i = 8;
    }

    public static void foo() {

        int l = 8;

        for (int i = 5; i < l; i++) {

            if (i > 8) {
                l++;
                return;
            } else if (i == 8) {
                i++;
                return;
            } else {
                l = i + 5;
            }
            int m = l;
        }
    }
}