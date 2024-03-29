package com.atguigu.springcloud.other.luaj;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaThread;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

// 协程
public class CollectingOrphanedCoroutines {

    static String script =
            "i,n = 0,0\n print(i)\n"
                    + "f = function() n=n+1; coroutine.yield(false) end\n"
                    + "while true do\n"
                    + "  local cor = coroutine.wrap(f)\n"
                    + "  cor()\n"
                    + "  i = i + 1\n"
                    + "  if i % 1000 == 0 then\n"
                    + "    collectgarbage()\n"
                    + "    print('threads:', i, 'executions:', n, collectgarbage('count'))\n"
                    + "  end\n"
                    + "end\n";

    public static void main(String[] args) throws InterruptedException {
        // This timer controls how often each Java thread wakes up and checks if
        // it has been orhaned or not. A large number here will produce a long
        // delay between orphaning and colleciton, and a small number here will
        // consumer resources polling for orphaned status if there are many threads.
        LuaThread.thread_orphan_check_interval = 500;

        // Should work with standard or debug globals.
        Globals globals = JsePlatform.standardGlobals();
        // Globals globals = JsePlatform.debugGlobals();

        // Should work with plain compiler or lua-to-Java compiler.
        // org.luaj.vm2.luajc.LuaJC.install(globals);;

        // Load and run the script, which launches coroutines over and over forever.
        LuaValue chunk = globals.load(script, "main");
        chunk.call();
    }

}
