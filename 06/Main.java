import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Drives translation process from an assembly language program to a machine code program.
 */
public class Main {
    public static void main(String[] args) {
        String program = "RectL.asm";
        String binaryCommand;
        Path path = Paths.get(program.substring(0, program.indexOf(".")) + ".hack");
        List<String> binaryCommands = new ArrayList<>();

        try {
            Parser parser = new Parser(program);
            while (parser.hasMoreLines()) {
                parser.advance();

                if (parser.commandType().equals("C_COMMAND")) {
                    binaryCommand = "111" + Code.comp(parser.comp()) + Code.dest(parser.dest()) + Code.jump(parser.jump());
                    binaryCommands.add(binaryCommand); 

                }  else if (parser.commandType().equals("A_COMMAND")) {
                    binaryCommand = Code.number(parser.symbol());
                    binaryCommands.add(binaryCommand); 
                }
            }
            parser.close();

            Files.write(path, binaryCommands);
        } catch (IOException e) {
            System.err.println("Unable to open" + program);
        }
    }
}