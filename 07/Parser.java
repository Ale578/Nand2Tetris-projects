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

    public boolean isValidCommand() {
        return !commandType().equals("Invalid command");
    }

    public String formatLine(String aLine) {
        if (aLine != null) {
             aLine = aLine.trim();
             int commentIndex = aLine.indexOf("//");
            if (commentIndex != -1) {
                aLine = aLine.substring(0, commentIndex);
            }
        }
        return aLine;
    }

    public boolean hasMoreCommands() throws IOException {
        nextLine = formatLine(nextLine);
        while (nextLine != null) {
            if (isValidCommand()) {
                nextCommand = nextLine;
                nextLine = reader.readLine();
                return true;
            }
            nextLine = reader.readLine();
        }
        return false;
    }

    public void advance() throws IOException {  
        currentCommand = nextCommand;
    }
}