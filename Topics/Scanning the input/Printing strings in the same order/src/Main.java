import java.util.ArrayList;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 4;) {
            String in = scanner.nextLine();
            String[] token = in.split(" ");
            for (String w : token) {
                list.add(w);
                i++;
            }
        }
        for (String w : list) {
            System.out.println(w);
        }
    }
}