import java.io.CharArrayWriter;
import java.io.IOException;

class Converter {
    public static char[] convert(String[] words) throws IOException {
        // implement the method
        CharArrayWriter cw = new CharArrayWriter();
        for (String w : words) {
            cw.write(w);
        }
        return cw.toCharArray();
    }
}