import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {
    BufferedWriter writer;

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
        writer.write("// " + command + "\n");
        writer.write("@SP\n");
        writer.write("M=M-1\n");
        writer.write("A=M\n");
        if (command.equals("add")) {
            writer.write("D=M\n");
            writer.write("@SP\n");
            writer.write("M=M-1\n");
            writer.write("A=M\n");
            writer.write("M=D+M\n");
            writer.write("@SP\n");
            writer.write("M=M+1\n");
        } else if (command.equals("sub")) {

        }
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