import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {
    BufferedWriter writer;
    static int labelCount = 0;

    CodeWriter(String outputFile) {
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
        } catch (IOException e) {
            System.out.println("Couldn't open " + outputFile);
        }
    }

    // public void setFileName(String filename) {
    // }

    public void writeArithmetic(String command) throws IOException {
        //writer.write("// " + command + "\n");
        // Access top value
        writer.write("@SP\n");
        writer.write("M=M-1\n");
        writer.write("A=M\n");
        writer.write("D=M\n");

        if (command.equals("neg")) {
            writer.write("M=-D\n");
            writer.write("@SP\n");
            writer.write("M=M+1\n");
            return;
        } else if (command.equals("not")) {
            writer.write("M=!D\n");
            writer.write("@SP\n");
            writer.write("M=M+1\n");
            return;
        }
        // Access next top value
        writer.write("@SP\n");
        writer.write("M=M-1\n");
        writer.write("A=M\n");
        
        if (command.equals("add")) {
            writer.write("M=D+M\n");

        } else if (command.equals("sub")) {
            writer.write("M=M-D\n");

        } else if (command.equals("eq") || command.equals("lt") || command.equals("gt")) {
            String jumpCommand = "D;J" + command.toUpperCase() + "\n";
            writer.write(
                "D=M-D\n" +
                "@TRUE_" + labelCount + "\n" +
                jumpCommand +
                "\n" +
                "// Not true\n" +
                "@SP\n" +
                "A=M\n" +
                "M=0\n" +
                "@SKIP" + labelCount + "\n" +
                "0;JMP\n" +
                "\n" +
                "(TRUE_" + labelCount + ")\n" +
                "@SP\n" +
                "A=M\n" +
                "M=-1\n" +
                "(SKIP" + labelCount + ")\n"
            );
            labelCount++;
        } else if (command.equals("and")) {
            writer.write("M=D&M\n");

        } else if (command.equals("or")) {
            writer.write("M=D|M\n");
        }

        writer.write("@SP\n");
        writer.write("M=M+1\n");
    }

    public void writePushPop(String command, String segment, int index) throws IOException {
        if (command.equals("C_PUSH")) {
            writer.write("// " + "push " + segment + " " + index + "\n");
            if (segment.equals("constant")) {
                writer.write("@" + index + "\n");
                writer.write("D=A\n");
                writer.write("@SP\n");
                writer.write("A=M\n");
                writer.write("M=D\n");
                writer.write("@SP\n");
                writer.write("M=M+1\n");
            }
        } else {
            writer.write("// " + "pop " + segment + " " + index + "\n");
        }
    }

    public void close() throws IOException {
        writer.close();
    }
}