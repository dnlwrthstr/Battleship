import java.io.BufferedReader;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("");
        char[] input = new char[50];
        int count = 0;
        while (true) {
            int c = reader.read();
            if (c == -1 || c == '\n') {
                break;
            }
            input[count] = (char) c;
            count++;
        }
        reader.close();

        for (int i = --count; i >= 0; i--) {
            System.out.print(input[i]);
        }
    }
}