import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java Main <program>");
            return;
        }

        String program = args[0];
        Parser parser;
        try {
            parser = new Parser(program);
            while (parser.hasMoreCommands()) {
                parser.advance();
                System.out.println(parser.currentCommand);
            }
        } catch (IOException e) {
            System.err.println("Unable to open " + program);
        }
    }

}