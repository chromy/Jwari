import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

public class IOUtil {

    private static StreamTokenizer tokenizer = new StreamTokenizer(
            new InputStreamReader(System.in));

    public static double readDouble() {

        tokenizer.resetSyntax();
        tokenizer.whitespaceChars(0, ' ');
        tokenizer.wordChars(33, 255);

        try {
            if (tokenizer.nextToken() == StreamTokenizer.TT_EOF) {
                throw new IOException("End Of File found.");
            } else {
                return Double.parseDouble(tokenizer.sval);
            }
        } catch (NumberFormatException nfe) {
            return readDouble();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static int readInt() {

        try {

            tokenizer.resetSyntax();
            tokenizer.whitespaceChars(0, ' ');
            tokenizer.wordChars(33, 255);

            if (tokenizer.nextToken() == StreamTokenizer.TT_EOF) {
                throw new IOException("End Of File found.");
            } else {
                return (int) Double.parseDouble(tokenizer.sval);
            }
        } catch (NumberFormatException nfe) {
            return readInt();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

}
