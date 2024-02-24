public class BreakContinueTest {

    public static void main(String[] args) {

        int i = 0;

        while(i < 10) {
            if (i % 2 == 0) {
                i++;
                continue;
            }
            i++;
            if (i % 3 == 0) {
                i += 2;
                break;
            }
            i++;
        }

        switch (i) {
            case 10 :
                int l = 3;
                break;
            case 11 :
                i = 4;
                break;
            case 12 :
                i = 5;
                break;
            default:
                i = 6;
        }
    }
}