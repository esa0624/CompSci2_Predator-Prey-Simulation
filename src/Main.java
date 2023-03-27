import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        JFrame f = new JFrame();
        Simulator p = new Simulator(500,100,45,6,6);
        f.add(p);
        f.setSize(1000, 1000);
        f.setTitle("World");
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //System.out.println("It takes time to run 500 years.");

    }
}