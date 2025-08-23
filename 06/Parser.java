import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.regex.Pattern;

/**
 * Encapsulates access to the input code. Reads an assembly language command,
 * parses it, and provides convenient access to the command’s components 
 * (fields and symbols). In addition, removes all white space and comments.
 */
public class Parser {
    private BufferedReader reader;
    private String line;
    private String nextLine;
    private String command;

    public Parser(String program) throws IOException {
        Charset charset = Charset.forName("US-ASCII");
        Path path = Paths.get(program);
        reader = Files.newBufferedReader(path, charset);
        nextLine = reader.readLine();
        command = "";
    }
    
    public boolean hasMoreCommands() throws IOException {
        return nextLine != null;
    }

    // Call only if hasMoreCommands is true
    public void advance() throws IOException {    
            line = nextLine;
            nextLine = reader.readLine();

            formatLine();

            // Ignore non-commands
            if (!line.equals("")) {
                command = line;
                
            } else {
                command = "NO_COMMAND";
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
        Pattern patternC = Pattern.compile("^(?:(?<dest>[ADM]{1,3})=)?(?<comp>0|1|-1|D|A|!D|!A|-D|-A|D\\+1|A\\+1|D-1|A-1|D\\+A|D-A|A-D|D&A|D\\|A|M|!M|-M|M\\+1|M-1|D\\+M|D-M|M-D|D&M|D\\|M)(?:;(?<jump>JGT|JEQ|JGE|JLT|JNE|JLE|JMP))?$");
        if (patternC.matcher(command).find()) {
            return "C_COMMAND";
        }
        return "NO_COMMAND";
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

    public void close() throws IOException {
        reader.close();
    }

}
