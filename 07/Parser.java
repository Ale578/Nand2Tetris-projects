import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * Handles the parsing of a single .vm file, and encapsulates access to the input code. 
 * It reads VM commands, parses them, and provides convenient access to their components. 
 * In addition, it removes all white space and comments.
 */

public class Parser {
    private BufferedReader reader;
    private String nextLine;
    private String nextCommand;
    public String currentCommand;

    public Parser(String file) throws IOException{
        Charset charset = Charset.forName("US-ASCII");
        Path path = Paths.get(file);
        reader = Files.newBufferedReader(path, charset);
        nextLine = reader.readLine();
    }

    public String commandType() {
        int firstSpaceIndex = nextLine.indexOf(" ");
        String command = nextLine;
        if (firstSpaceIndex != -1) {
                command = nextLine.substring(0, firstSpaceIndex);
        }

        switch (command) {
            case "add":
            case "sub":
            case "neg":
            case "eq":
            case "get":
            case "lt":
            case "and":
            case "or":
            case "not":
                return "C_ARITHMETIC";
            default:
                return "Invalid command";
        }
    }
}