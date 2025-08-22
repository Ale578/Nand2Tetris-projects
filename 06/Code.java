import java.util.Set;

/**
 * Translates Hack assembly language mnemonics into binary codes.
 */
public class Code {

    public static String dest(String mnemonic) {
        char[] binaryCode = {'0', '0', '0'};

        Set<String> validDestMnemonics = Set.of("", "M", "D", "MD", "A",
            "AM", "AD", "AMD");

        if (!validDestMnemonics.contains(mnemonic)) {
            throw new IllegalArgumentException("Invalid dest mnemonic: " + mnemonic);
        }

        if (mnemonic.contains("M")) {
            binaryCode[2] = '1';
        }

        if (mnemonic.contains("D")) {
            binaryCode[1] = '1';
        }

        if (mnemonic.contains("A")) {
            binaryCode[0] = '1';
        }

        return new String(binaryCode);
    }

    public static String comp(String mnemonic) {
        String binaryCode = "";

        switch (mnemonic) {
            case "0":
                binaryCode = "101010";
                break;
            case "1":
                binaryCode = "111111";
                break;
            case "-1":
                binaryCode = "101010";
                break;
            case "D":
                binaryCode = "001100";
                break;
            case "A":
            case "M":
                binaryCode = "110000";
                break;
            case "!D":
                binaryCode = "001101";
                break;
            case "!A":
            case "!M":
                binaryCode = "110001";
                break;
            case "-D":
                binaryCode = "001111";
                break;
            case "-A":
            case "-M":
                binaryCode = "110011";
                break;
            case "D+1":
                binaryCode = "001111";
                break;
            case "A+1":
                binaryCode = "110111";
                break;
            case "D-1":
                binaryCode = "001110";
                break;
            case "A-1":
            case "M-1":
                binaryCode = "110010";
                break;
            case "D+A":
            case "D+M":
                binaryCode = "000010";
                break;
            case "D-A":
            case "D-M":
                binaryCode = "010011";
                break;
            case "A-D":
            case "M-D":
                binaryCode = "000111";
                break;
            case "D&A":
            case "D&M":
                binaryCode = "000000";
                break;
            case "D|A":
            case "D|M":
                binaryCode = "010101";
                break;
            default:
                throw new IllegalArgumentException("Invalid comp mnemonic: " + mnemonic);
        }

        if (mnemonic.contains("M")) {
            return "1" + binaryCode;
        } else {
            return "0" + binaryCode;
        }
    }

    public static String jump(String mnemonic) {
        switch (mnemonic) {
            case "":
                return "000";
            case "JGT":
                return "001";
            case "JEQ":
                return "010";
            case "JGE":
                return "011";
            case "JLT":
                return "100";
            case "JNE":
                return "101";
            case "JLE":
                return "110";
            case "JMP":
                return "111";
            default:
                throw new IllegalArgumentException("Invalid jump mnemonic: " + mnemonic);
        }
    }
}
