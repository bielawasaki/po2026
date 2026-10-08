import java.util.Arrays;
import java.util.Random;

public class Lotto {
    public static void main(String[] args) {
        int size = 6;

        int arr[] = new int[size];

        for (int i = 0; i < size; i++) {
            int randomNum = getRandomInt(1,49);
            if (Arrays.asList(arr).contains(randomNum)) {
                i--;
                continue;
            }
            arr[i] = getRandomInt(1,49);
        }

        System.out.println(Arrays.toString(arr));
    }

    public static int getRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(min, max + 1);
    }
}