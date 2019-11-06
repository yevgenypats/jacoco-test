package org.yevgeny;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.jacoco.agent.rt.IAgent;
import org.jacoco.agent.rt.RT;

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
        IAgent agent = RT.getAgent();
        byte[] data = agent.getExecutionData(false);
        System.out.println("data length = " + data.length);
        A a = new A(2);
        a.test();
        System.out.println("hello mojo");
    }
}
