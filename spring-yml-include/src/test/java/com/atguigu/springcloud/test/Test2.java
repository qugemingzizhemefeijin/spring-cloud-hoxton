package com.atguigu.springcloud.test;

import com.atguigu.springcloud.test.ansi.AnsiProcess;
import com.atguigu.springcloud.test.ansi.LogoBanner;

public class Test2 {

    public static String lineSeparator = System.getProperty("line.separator");

    public static void printStart(AnsiProcess process) {
        process.output("@|green *** Welcome to Java World|@ @|green ***|@");
        process.output(lineSeparator);
        process.output(LogoBanner.welcome());
        process.output(lineSeparator);
        process.output("@|green Initiator : AAA |@");
        process.output("@|green version   : 2.1.1-RELEASE |@");
        process.output("@|green site      : 1.23.3 |@");
        process.output("@|green github    : 63 |@");
        process.output(lineSeparator);
    }

    public static void main(String[] args) {
        // Pattern ansiRemovePattern = Pattern.compile("(@\\|\\w* )|( ?\\|@)");

        //String line = "@|magenta *** Welcome to XPocket-Cli|@ @|green ***|@";

        //System.out.println(ansiRemovePattern.matcher(line).replaceAll(""));

        //System.out.println(ansi().render(line));

        Test2.printStart(new AnsiProcess(System.out));
    }

}
