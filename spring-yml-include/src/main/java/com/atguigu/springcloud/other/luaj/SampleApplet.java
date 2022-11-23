package com.atguigu.springcloud.other.luaj;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

import java.applet.Applet;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;

public class SampleApplet extends Applet implements ResourceFinder {

    private static final long serialVersionUID = 1L;

    Globals globals;
    LuaValue pcall;
    LuaValue graphics;
    Graphics coerced;

    // This applet property is searched for the name of script to load.
    static String script_parameter = "script";

    // This must be located relative to the document base to be loaded.
    static String default_script = "luaj/swingapplet.lua";

    public void init() {
        // Find the script to load from the applet parameters.
        String script = getParameter(script_parameter);
        if (script == null)
            script = default_script;
        System.out.println("Loading " + script);

        // Construct globals specific to the applet platform.
        globals = new Globals();
        globals.load(new JseBaseLib());
        globals.load(new PackageLib());
        globals.load(new Bit32Lib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new CoroutineLib());
        globals.load(new JseMathLib());
        globals.load(new JseIoLib());
        globals.load(new JseOsLib());
        globals.load(new AppletLuajavaLib());
        LoadState.install(globals);
        LuaC.install(globals);

        // Use custom resource finder.
        globals.finder = this;

        // Look up and save the handy pcall method.
        pcall = globals.get("pcall");

        // Load and execute the script, supplying this Applet as the only
        // argument.
        globals.loadfile(script).call(CoerceJavaToLua.coerce(this));
    }

    public void start() {
        pcall.call(globals.get("start"));
    }

    public void stop() {
        pcall.call(globals.get("stop"));
    }

    public void update(Graphics g) {
        LuaValue u = globals.get("update");
        if (!u.isfunction())
            super.update(g);
        else
            pcall.call(
                    u,
                    coerced == g ? graphics : (graphics = CoerceJavaToLua
                            .coerce(coerced = g)));
    }

    public void paint(Graphics g) {
        LuaValue p = globals.get("paint");
        if (!p.isfunction())
            super.paint(g);
        else
            pcall.call(
                    p,
                    coerced == g ? graphics : (graphics = CoerceJavaToLua
                            .coerce(coerced = g)));
    }

    // ResourceFinder implementation.
    public InputStream findResource(String filename) {
        InputStream stream = findAsResource(filename);
        if (stream != null) return stream;
        stream = findAsDocument(filename);
        if (stream != null) return stream;
        System.err.println("Failed to find resource " + filename);
        return null;
    }

    private InputStream findAsResource(String filename) {
        System.out.println("Looking for jar resource /" + filename);
        return getClass().getResourceAsStream("/" + filename);
    }

    private InputStream findAsDocument(String filename) {
        try {
            final URL base = getDocumentBase();
            System.out.println("Looking in document base " + base);
            return new URL(base, filename).openStream();
        } catch (Exception e) {
            return null;
        }
    }

    public static final class AppletLuajavaLib extends LuajavaLib {
        public AppletLuajavaLib() {
        }

        protected Class classForName(String name) throws ClassNotFoundException {
            // Use plain class loader in applets.
            return Class.forName(name);
        }
    }

}
