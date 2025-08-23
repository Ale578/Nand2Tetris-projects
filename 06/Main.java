import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class Main {
    public static void main(String[] args) {
        String program = "Add.asm";
        String binaryCommand;
        Path path = Paths.get(program.substring(0, program.indexOf(".")) + ".hack");
        List<String> binaryCommands = new ArrayList<>();

        try {
            Parser parser = new Parser(program);
            while (parser.hasMoreCommands()) {
                parser.advance();

                if (parser.commandType().equals("C_COMMAND")) {
                    binaryCommand = "111" + Code.comp(parser.comp()) + Code.dest(parser.dest()) + Code.jump(parser.jump());
                    binaryCommands.add(binaryCommand); 
                    System.out.println(parser.command + " -> " + binaryCommand);

                }  else if (parser.commandType().equals("A_COMMAND")) {
                    binaryCommand = Code.number(parser.symbol());
                    binaryCommands.add(binaryCommand); 
                    System.out.println(parser.command + " -> " + binaryCommand);
                }
                // else if (parser.commandType().equals("L_COMMAND")) {
                //     
                //     System.out.println("L_COMMAND");
                // }
            }

            Files.write(path, binaryCommands);
        } catch (IOException e) {
            System.err.println("Unable to open" + program);
        }
    }
    
}