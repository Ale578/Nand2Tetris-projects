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
    private String command;
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
                commandType();

                commandNum++;
            }

            line = reader.readLine();
        } else {
            reader.close();
        }
    }

    private void formatLine() {
        line = line.replace(" ", "");
            int comment = line.indexOf("//");
            if (comment != -1) {
                 line = line.substring(0, comment);
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
}
