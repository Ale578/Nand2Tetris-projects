import java.util.HashMap;

/**
 * Keeps correspondence between symbolic labels and numeric addresses.
 */
public class SymbolTable {
    private HashMap<String, Integer> symbolTable;

    public SymbolTable() {
        symbolTable = new HashMap<>();

        // Add predefined symbols
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);

        for (int i = 0; i < 16; i++) {
            symbolTable.put("R" + i, i);
        }

        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);
    }

    public void addEntry(String symbol, int address) {
        symbolTable.put(symbol, address);
    }

    public Boolean contains(String symbol) {
        return symbolTable.containsKey(symbol);
    }

    public int GetAddress(String symbol) {
        return symbolTable.get(symbol);
    }
}
