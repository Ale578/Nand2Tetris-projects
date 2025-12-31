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
        // Access top value
        writer.write("@SP" + " // " + command + "\n");
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
        String asmSegment = "";
        // TODO: TEMP

        if (segment.equals("local")) {
            asmSegment = "LCL";
        } else if (segment.equals("argument")) {
            asmSegment = "ARG";
        } else if (segment.equals("this")) {
            asmSegment = "THIS";
        } else if (segment.equals("that")) {
            asmSegment = "THAT";
        } else if (segment.equals("constant")) {
            asmSegment = String.valueOf(index);
        } 

        if (command.equals("C_PUSH")) {
            if (segment.equals("constant")) {
                // Get value
                writer.write("@" + index + " // " + "push " + segment + " " + index + "\n");
                writer.write("D=A\n");
            } else if (segment.equals("pointer")) {
                if (index == 0) {
                    writer.write("@THIS" + " // push " + segment + " " + index + "\n");
                } else if (index == 1) {
                    writer.write("@THAT" + " // push " + segment + " " + index + "\n");
                }
                writer.write("D=M\n");

            } else {
                writer.write(
                    // Determine where to push from
                    "@" + asmSegment + " // push " + segment + " " + index + "\n" +
                    "D=M\n" +
                    "@" + index + "\n" +
                    "D=D+A\n" +

                    // Get value
                    "A=D\n" +
                    "D=M\n"
                );
            }
            // Push to stack
            writer.write(
                "@SP\n" +
                "A=M\n" +
                "M=D\n" +
                "@SP\n" +
                "M=M+1\n"
            );
        } else {
            if (segment.equals("pointer")) {
                writer.write(
                    // Get topmost value on the stack
                    "@SP" + " // pop " + segment + " " + index + "\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n"
                ); 
                // Pop to segment
                if (index == 0) {
                    writer.write("@THIS\n");
                } else if (index == 1) {
                    writer.write("@THAT\n");
                }
                writer.write("M=D\n");

            } else {
                writer.write(
                    // Determine where to pop to
                    "@" + asmSegment + " // pop " + segment + " " + index + "\n" +
                    "D=M\n" +
                    "@" + index + "\n" +
                    "D=D+A\n" +
                    "@R13\n" +
                    "M=D\n" +

                    // Get topmost value on the stack
                    "@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +

                    // Pop to segment
                    "@R13\n" +
                    "A=M\n" +
                    "M=D\n"
                ); 
            }
        }
    }

    public void writeInit() throws IOException {
 
    }

    public void writeLabel(String label) throws IOException {
        writer.write("(" + label + ")\n");
    }

    public void writeGoto(String label) throws IOException {
        writer.write("@" + label + "\n");
        writer.write("0;JMP\n");
    }

    public void writeIf(String label) throws IOException {
        writer.write("@SP\n");
        writer.write("M=M-1\n");
        writer.write("A=M\n");
        writer.write("D=M\n");
        writer.write("@" + label + "\n");
        writer.write("D;JNE\n");
    }

    public void writeCall() throws IOException {
    
    }

    public void writeReturn() throws IOException {
    
    }

    public void writeFunction() throws IOException {
    
    }

    public void close() throws IOException {
        writer.close();
    }
}