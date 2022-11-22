package com.atguigu.springcloud.test.ansi;

import com.atguigu.springcloud.test.Test2;

import java.io.OutputStream;
import java.io.PrintStream;

public class AnsiProcess {

    protected Output output;
    protected OutputStream outputStream;

    public AnsiProcess(OutputStream outputStream) {
        this.outputStream = outputStream;
        if (outputStream instanceof PrintStream) {
            output = new Output((PrintStream) outputStream, true);
        }
    }

    public void output(String output) {
        try {
            String[] lines = output.split(Test2.lineSeparator);
            if(lines.length == 0) {
                this.output.print(output);
            } else {
                for(String line : lines) {
                    this.output.print(simpleFormatOutput(line));
                }
            }
            outputStream.flush();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private String simpleFormatOutput(String output) {
        String result = output;

        if (!result.startsWith(" ")) {
            result = " " + result;
        }

        if (!result.endsWith(Test2.lineSeparator)) {
            result += Test2.lineSeparator;
        }

        return result;
    }

}
