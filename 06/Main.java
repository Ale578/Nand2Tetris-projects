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
        String program = "assemblyPrograms/Max.asm";
        String binaryCommand;
        Path path = Paths.get(program.substring(17, program.indexOf(".")) + ".hack");
        List<String> binaryCommands = new ArrayList<>();
        SymbolTable symbolTable = new SymbolTable();

        // First pass: Populate symbol table with labels
        try {
            Parser symbolParser = new Parser(program);
            int commandAddress = 0;

            while (symbolParser.hasMoreLines()) {
                symbolParser.advance();

                if (symbolParser.commandType().equals("L_COMMAND") && !symbolTable.contains(symbolParser.symbol())) {
                    symbolTable.addEntry(symbolParser.symbol(), commandAddress);
                } else if (!symbolParser.commandType().equals("NO_COMMAND")) {
                    commandAddress++;
                }
            }
            symbolParser.close();
        } catch (IOException e) {
            System.err.println("Unable to open " + program);
        }

        // Second pass: Translate code from assembly to machine code and write output file
        try {
            Parser translationParser = new Parser(program);
            int availableAddress = 16;
            while (translationParser.hasMoreLines()) {
                translationParser.advance();

                if (translationParser.commandType().equals("C_COMMAND")) {
                    binaryCommand = "111" + Code.comp(translationParser.comp()) + Code.dest(translationParser.dest()) + Code.jump(translationParser.jump());
                    binaryCommands.add(binaryCommand); 

                } else if (translationParser.commandType().equals("A_COMMAND")) {
                    String symbol = translationParser.symbol();

                    // Handle labels
                    if (symbol.matches("[a-zA-Z_.$][a-zA-Z0-9_.$]*") && !symbol.matches("\\d+")) {
                        // If label is in symbol table, retrieve the corresponding address
                        if (symbolTable.contains(symbol)) {
                            symbol = Integer.toString(symbolTable.GetAddress(symbol));
                        // Add new variables with an available address to symbol table
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
            translationParser.close();

            Files.write(path, binaryCommands);
        } catch (IOException e) {
            System.err.println("Unable to open " + program);
        }
    }
}