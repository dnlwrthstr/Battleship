import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

class Main {
    public static void main(String[] args) throws Exception {
        InputStream inputStream = System.in;
        Reader reader = new InputStreamReader(inputStream);
        System.out.print("");
        while (true) {
            int c = reader.read();
            if (c == -1 || c == '\n')
                break;
            System.out.print(c);
        }
    }
}