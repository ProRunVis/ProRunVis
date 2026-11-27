class BubbleSort {

    static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    static int[] generateArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = size - i;
        }
        return arr;
    }

    public static void main(String[] args) {
        int size = 100;
        if (args.length > 0) {
            size = Integer.parseInt(args[0]);
        }
        int[] arr = generateArray(size);
        bubbleSort(arr);
        System.out.println("Sorted " + size + " elements.");
    }
}
