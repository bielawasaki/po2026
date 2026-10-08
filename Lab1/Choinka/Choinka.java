public class Choinka {
    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }

        String arg1 = args[0];
        int size = Integer.parseInt(arg1);
        System.out.println("Size: " + String.valueOf(size));

        if (size < 1) {
            return;
        }

        String stars = "*";
        for (int i = 0; i < size; i++) {
            System.out.println(stars);
            stars = stars + "*";
        }
    }
}