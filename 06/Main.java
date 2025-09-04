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
        String program = args[0];
        String binaryCommand;
        Path path = Paths.get(program.substring(17, program.indexOf(".")) + ".hack");
        List<String> binaryCommands = new ArrayList<>();
        SymbolTable symbolTable = new SymbolTable();
        Parser parser;

        try {
            // First pass: Populate symbol table with labels
            parser = new Parser(program);
            int commandAddress = 0;

            while (parser.hasMoreLines()) {
                parser.advance();

                boolean isL_Command = parser.commandType().equals("L_COMMAND");
                boolean inSymbolTable = !symbolTable.contains(parser.symbol());
                boolean isAnyCommand = !parser.commandType().equals("NO_COMMAND");

                if (isL_Command && inSymbolTable) {
                    symbolTable.addEntry(parser.symbol(), commandAddress);
                } else if (isAnyCommand) {
                    commandAddress++;
                }
            }

            // Second pass: Translate code from assembly to machine code and write output file
            parser = new Parser(program);
            int availableAddress = 16;
            while (parser.hasMoreLines()) {
                parser.advance();

                boolean isC_Command = parser.commandType().equals("C_COMMAND");
                boolean isA_Command = parser.commandType().equals("A_COMMAND");

                if (isC_Command) {
                    binaryCommand = "111" + Code.comp(parser.comp()) + Code.dest(parser.dest()) + Code.jump(parser.jump());
                    binaryCommands.add(binaryCommand); 

                } else if (isA_Command) {
                    String symbol = parser.symbol();
                    boolean isA_CommandWithLabel = symbol.matches("[a-zA-Z_.$][a-zA-Z0-9_.$]*");

                    if (isA_CommandWithLabel) {
                        if (symbolTable.contains(symbol)) {
                            symbol = Integer.toString(symbolTable.GetAddress(symbol));

                        } else {
                            symbolTable.addEntry(symbol, availableAddress);
                            symbol = Integer.toString(availableAddress);
                            availableAddress++;
                        }
                    }
                    binaryCommand = Code.number(Integer.parseInt(symbol));
                    binaryCommands.add(binaryCommand); 
                }
            }
            parser.close();
            Files.write(path, binaryCommands);
        } catch (IOException e) {
            System.err.println("Unable to open " + program);
        }

    }
}