package com.atguigu.springcloud.other.jvm.attach;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.File;
import java.io.IOException;

public class JavaAttachTest {

    public static void main(String[] args) throws Exception {
        VirtualMachineDescriptor virtualMachineDescriptor = null;
        for (VirtualMachineDescriptor descriptor : VirtualMachine.list()) {
            System.out.println(descriptor.id() + "\t" + descriptor.displayName());
            if ("com.aaa.common.agent.example.Startup".equalsIgnoreCase(descriptor.displayName())) {
                virtualMachineDescriptor = descriptor;
                break;
            }
        }
        if (virtualMachineDescriptor == null) {
            System.out.println("virtualMachine is null");
            return;
        }

        VirtualMachine virtualMachine = null;
        try {
            // 如果通过attach的方式来替换类，就不能做到字节码在启动的时候替换了，得使用其他的办法。

            virtualMachine = VirtualMachine.attach(virtualMachineDescriptor);

            try {
                String agentJarPath = "E:\\file\\java-agent-api.jar";
                File agentFile = new File(agentJarPath);
                if (!agentFile.exists()) {
                    System.out.println("Agent JAR file not found at: " + agentJarPath);
                    return;
                }

                virtualMachine.loadAgent(agentJarPath, "server.name=top-server,server.host=127.0.0.1,app.jar.path=/e/file/lib/,app.proxy.package=com.cg.common.agent.example");
            } catch (IOException e) {
                if (e.getMessage() != null && e.getMessage().contains("Non-numeric value found")) {
                    System.out.println("It seems to use the lower version of JDK to attach the higher version of JDK.");
                    System.out.println("This error message can be ignored, the attach may have been successful, and it will still try to connect.");
                } else {
                    throw e;
                }
            } catch (com.sun.tools.attach.AgentLoadException ex) {
                if ("0".equals(ex.getMessage())) {
                    // https://stackoverflow.com/a/54454418
                    System.out.println("It seems to use the higher version of JDK to attach the lower version of JDK.");
                    System.out.println("This error message can be ignored, the attach may have been successful, and it will still try to connect.");
                } else {
                    throw ex;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != virtualMachine) {
                virtualMachine.detach();
            }
        }
    }

}
