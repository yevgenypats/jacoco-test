package org.yevgeny;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.ExecutionDataReader;
import org.jacoco.core.data.IExecutionDataVisitor;
import org.jacoco.core.data.ISessionInfoVisitor;
import org.jacoco.core.data.SessionInfo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.*;
import java.util.Date;

class A {
    private final int a;

    A(int a) {
        this.a = a;
    }

    void test() {
        System.out.println(this.a);
    }
}


@Mojo( name = "bug")
public class MyMojo extends AbstractMojo
{
    private int hitCount;

    private int getHitCount(final boolean[] data) {
        int count = 0;
        for (final boolean hit : data) {
            if (hit) {
                count++;
            }
        }
        return count;
    }

    public void execute()
    {
        try {
            Class c = Class.forName("org.jacoco.agent.rt.RT");
            Method m = c.getMethod("getAgent");
            Object Agent = m.invoke(null);
            System.out.println("JaCoCo Agent: " + Agent);
            m = Agent.getClass().getMethod("getExecutionData", boolean.class);
            m.setAccessible(true);
            long startTime = System.currentTimeMillis();
            int totalExecs = 1000;
            for (int i=0; i<totalExecs; i++) {
                this.hitCount = 0;
                byte[] a = (byte[]) m.invoke(Agent, false);
                InputStream is = new ByteArrayInputStream(a);
                final ExecutionDataReader reader = new ExecutionDataReader(is);
                reader.setSessionInfoVisitor(new ISessionInfoVisitor() {
                    public void visitSessionInfo(final SessionInfo info) {
                    }
                });
                reader.setExecutionDataVisitor(new IExecutionDataVisitor() {
                    public void visitClassExecution(final ExecutionData data) {
                        hitCount += getHitCount(data.getProbes());
                    }
                });
                reader.read();
                is.close();
            }
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.printf("Total Time: %d ms\n", elapsedTime);
            System.out.printf("Execs/s: %f\n", (double)totalExecs/(double) elapsedTime*1000);
            System.out.printf("Total Hit Count: %d\n", hitCount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
