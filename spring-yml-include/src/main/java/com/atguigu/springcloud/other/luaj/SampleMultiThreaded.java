package com.atguigu.springcloud.other.luaj;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.IOException;

public class SampleMultiThreaded {

    static class Runner implements Runnable {
        final String script1, script2;
        Runner(String script1, String script2) {
            this.script1 = script1;
            this.script2 = script2;
        }
        public void run() {
            try {
                // Each thread must have its own Globals.
                Globals g = JsePlatform.standardGlobals();

                // Once a Globals is created, it can and should be reused
                // within the same thread.
                g.loadfile(script1).call();
                g.loadfile(script2).call();

            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }

    public static void main(final String[] args) throws IOException {
        final String script1 = args.length > 0? args[0]: "luaj/nsieve.lua";
        final String script2 = args.length > 1? args[1]: "luaj/binarytrees.lua";
        try {
            Thread[] thread = new Thread[10];
            for (int i = 0; i < thread.length; ++i)
                thread[i] = new Thread(new Runner(script1, script2),"Runner-"+i);
            for (int i = 0; i < thread.length; ++i)
                thread[i].start();
            for (int i = 0; i < thread.length; ++i)
                thread[i].join();
            System.out.println("done");
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
