package org.yevgeny;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import java.lang.reflect.*;

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
    public void execute()
    {
        try {
            Class c = Class.forName("org.jacoco.agent.rt.RT");
            Method m = c.getMethod("getAgent");
            System.out.println("JaCoCo Agent: " + m.invoke(null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
