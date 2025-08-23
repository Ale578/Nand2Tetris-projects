import java.util.HashMap;

/**
 * Keeps correspondence between symbolic labels and numeric addresses.
 */
public class SymbolTable {
    private HashMap<String, String> table;

    public SymbolTable() {
        table = new HashMap<>();

        // Add predefined symbols
        table.put("SP", "0");
        table.put("LCL", "1");
        table.put("ARG", "2");
        table.put("THIS", "3");
        table.put("THAT", "4");

        for (int i = 0; i < 16; i++) {
            table.put("R" + i, String.valueOf(i));
        }

        table.put("SCREEN", "16384");
        table.put("KBD", "24576");
    }
}
