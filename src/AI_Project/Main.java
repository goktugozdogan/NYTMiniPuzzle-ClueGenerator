package AI_Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

class Main {
    public static void main(String[] args) throws FileNotFoundException {
        PrintStream out = new PrintStream(new FileOutputStream("consoleOutput.txt"));
        System.setOut(out);

        JFrame frame = new JFrame("Clue Generator");
        frame.setSize(800, 800);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        Container contentPane = frame.getContentPane();
        contentPane.add(new SlidePuzzleGUI(null, false));

        frame.pack();
        frame.show();
        frame.setLocationRelativeTo(null);
    }
}
