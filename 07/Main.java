import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java Main <program>");
            return;
        }

        File f = new File(args[0]);
        ArrayList<String> files = new ArrayList<>();

        if (f.isFile()) {
            files.add(f.getPath());
            
        } else if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                files.add(file.getPath());
            }
        }

        CodeWriter codeWriter = new CodeWriter("output.asm");

        Parser parser;
        for (String file : files) {
            try {
                parser = new Parser(file);
                while (parser.hasMoreCommands()) {
                    parser.advance();

                    codeWriter.writeArithmetic("H\n");
                }
            } catch (IOException e) {
                System.err.println("Unable to open " + file);
            }
        }
        try {
            codeWriter.close();
        } catch (IOException e) {
                System.err.println("Unable to close");
        }
        

    }
}