package AI_Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    /*public static void  main ( String[] args ) {
        Fetch f = new Fetch("https://www.nytimes.com/crosswords/game/mini");

        f.getClues();
        f.getSolutions();
        //f.writePuzzle("17.03.2019");
        //f.readPuzzle("12.03.2019");
        f.PrintPuzzle();
    }*/

    public static void main(String[] args) {
        UrbanDictionary ur = new UrbanDictionary();
        ur.getNewClues("Friday March 15, 2019");

/*        JFrame frame = new JFrame("ButtonTest");
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
        frame.setLocationRelativeTo(null);*/
    }
}
