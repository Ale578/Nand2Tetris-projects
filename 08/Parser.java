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
    private String currentCommand;

    public Parser(String file) throws IOException {
        Charset charset = Charset.forName("US-ASCII");
        Path path = Paths.get(file);
        reader = Files.newBufferedReader(path, charset);
        nextLine = reader.readLine();
    }

    public String getCurrentCommand() {
        return currentCommand;
    }

    public String commandType(String command) {
        int firstSpaceIndex = command.indexOf(" ");
        if (firstSpaceIndex != -1) {
                command = command.substring(0, firstSpaceIndex);
        }
        command = command.trim();

        switch (command) {
            case "add":
            case "sub":
            case "neg":
            case "eq":
            case "gt":
            case "lt":
            case "and":
            case "or":
            case "not":
                return "C_ARITHMETIC";
            case "push":
                return "C_PUSH";
            case "pop":
                return "C_POP";
            case "label":
                return "C_LABEL";
            case "goto":
                return "C_GOTO";
            case "if-goto":
                return "C_IF";
            case "function":
                return "C_FUNCTION";
            case "return":
                return "C_RETURN";
            case "call":
                return "C_CALL";
            default:
                return "Invalid command";
        }
    }

    public boolean isValidCommand(String line) {
        return !commandType(line).equals("Invalid command");
    }

    public String formatLine(String aLine) {
        int commentIndex = aLine.indexOf("//");
        if (commentIndex != -1) {
            aLine = aLine.substring(0, commentIndex);
        }
        return aLine.trim();
    }

    public boolean hasMoreCommands() throws IOException {
        while (nextLine != null) {
            nextLine = formatLine(nextLine);
            if (isValidCommand(nextLine)) {
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

    // Should not be called if the current command is C_RETURN
    public String arg1() {
        if (commandType(currentCommand).equals("C_ARITHMETIC")) {
            return currentCommand;
            
        } else  { //(commandType(currentCommand).equals("C_PUSH") || commandType(currentCommand).equals("C_POP")) {
            int firstSpaceIndex = currentCommand.indexOf(" ");
            String firstArg = currentCommand.substring(firstSpaceIndex + 1);
            int secondSpaceIndex = firstArg.indexOf(" ");
            if (secondSpaceIndex == -1) {
                return firstArg;
            }
            return firstArg.substring(0, secondSpaceIndex);
        }
    }

    // Should only be called if the current command is C_PUSH, C_POP, C_FUNCTION or C_CALL
    public int arg2() {
        int firstSpaceIndex = currentCommand.indexOf(" ");
        String secondArg = currentCommand.substring(firstSpaceIndex + 1);
        int secondSpaceIndex = secondArg.indexOf(" ");
        return Integer.parseInt(secondArg.substring(secondSpaceIndex + 1));
    }
}