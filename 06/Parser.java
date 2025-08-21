import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Encapsulates access to the input code. Reads an assembly language command,
 * parses it, and provides convenient access to the command’s components 
 * (fields and symbols). In addition, removes all white space and comments.
 */
public class Parser {
    private BufferedReader reader;
    private String line;
    public String command;
    private int commandNum;

    public Parser(String program) throws IOException {
        Charset charset = Charset.forName("US-ASCII");
        Path path = Paths.get(program);
        reader = Files.newBufferedReader(path, charset);
        line = reader.readLine();
        commandNum = 0;
    }
    
    public boolean hasMoreCommands() throws IOException {
        if (line == null) {
            return false;
        }
        return true;
    }

    public void advance() throws IOException {
        if (hasMoreCommands()) {

            formatLine();

            // Ignore non-commands
            if (!line.equals("")) {
                command = line;
                System.out.println(commandNum + "| " + command);

                    if (commandType().equals("A_COMMAND") || commandType().equals("L_COMMAND")) {
                        System.out.println(symbol());
                    } else if (commandType().equals("C_COMMAND") ) {
                        System.out.println(dest());
                        System.out.println(comp());
                        System.out.println(jump());
                    }

                commandNum++;
            }

            line = reader.readLine();
        } else {
            reader.close();
        }
    }

    private void formatLine() {
        line = line.replace(" ", "");
            int commentIndex = line.indexOf("//");
            if (commentIndex != -1) {
                 line = line.substring(0, commentIndex);
            }
    }

    public String commandType() {
        Pattern patternA = Pattern.compile("^@[a-zA-Z0-9]+$");
        if (patternA.matcher(command).find()) {
            return "A_COMMAND";
        }

        Pattern patternL = Pattern.compile("^\\([a-zA-Z]+\\)$");
         if (patternL.matcher(command).find()) {
            return "L_COMMAND";
        }
        return "C_COMMAND";
    }

    // Call only on A or L commands
    public String symbol() {
        // if A command
        if (command.charAt(0) == '@') {
            return command.substring(1, command.length());
        } else  { // if L command
            return command.substring(1, command.length() - 1);
        }
    }

    // Call only on C commands. The destination field is optional in a C command
    public String dest() {
        int equalsIndex = command.indexOf('=');
        if (equalsIndex != -1 ) {
            return command.substring(0, equalsIndex);
        } else {
            return "";
        }
    }

    // Call only on C commands.
    public String comp() {
        String commandField = command;
        int equalsIndex = command.indexOf('=');

        if (equalsIndex != -1) {
            commandField = commandField.substring(equalsIndex + 1);
        }
        
        int semicolonIndex = commandField.indexOf(';');

        if (semicolonIndex != -1) {
            commandField = commandField.substring(0, semicolonIndex);
        }

        return commandField;
    }

    // Call only on C commands. The jump field is optional in a C command
    public String jump() {
        int semicolonIndex = command.indexOf(';');
        if (semicolonIndex != -1) {
            return command.substring(semicolonIndex + 1);
        }
        return "";
    }

}
