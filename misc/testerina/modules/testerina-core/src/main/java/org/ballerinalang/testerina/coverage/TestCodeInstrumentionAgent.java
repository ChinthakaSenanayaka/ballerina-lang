package org.ballerinalang.testerina.coverage;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.PrintStream;
import java.lang.instrument.Instrumentation;

public class TestCodeInstrumentionAgent {

    private static PrintStream outStream = System.err;

    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation instrumentation){

        /*System.out.println("==========inside premain=========");

        TestCodeInstrumentionAgent.instrumentation = instrumentation;

        instrumentation.addTransformer((classLoader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> {

            if ("org.ballerinalang.bre.bvm.CPU".equals(className)) {
                try {
                    ClassPool cp = ClassPool.getDefault();
                    CtClass cc = cp.get("org.ballerinalang.bre.bvm.CPU");

                    CtClass ctxClass = cp.get("org.ballerinalang.bre.bvm.WorkerExecutionContext");
                    CtClass[] paramArgs = new CtClass[]{ctxClass};

                    CtMethod execMethod = cc.getDeclaredMethod("exec", paramArgs);
                    execMethod.insertAfter("org.ballerinalang.bre.bvm.CPU.initInstructionHandler(ctx);");

                    CtMethod tryExecMethod = cc.getDeclaredMethod("tryExec", paramArgs);
                    tryExecMethod.insertAt(186, "org.ballerinalang.bre.bvm.CPU.handleInstruction(ctx);");

                    byte[] byteCode = cc.toBytecode();
                    cc.detach();
                    return byteCode;
                } catch (Throwable ex) {
                    outStream.println("Error injecting the coverage instrumentation, error - "
                            + ex.getMessage());
                }
            }
            return new byte[]{};
        });*/
    }

}
