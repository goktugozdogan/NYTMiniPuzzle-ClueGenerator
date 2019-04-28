package AI_Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class Main {
    public static void main(String[] args) {
/*      NewClue nc;

        nc = new NewClue("Friday March 15, 2019");
        nc.GetBestClues();
        nc.writeNewClues("Friday March 15, 2019");

        nc = new NewClue("Monday February 4, 2019");
        nc.GetBestClues();
        nc.writeNewClues("Monday February 4, 2019");

        nc = new NewClue("Monday February 11, 2019");
        nc.GetBestClues();
        nc.writeNewClues("Monday February 11, 2019");

        nc = new NewClue("Monday March 18, 2019");
        nc.GetBestClues();
        nc.writeNewClues("Monday March 18, 2019");

        nc = new NewClue("Sunday April 28, 2019");
        nc.GetBestClues();
        nc.writeNewClues("Sunday April 28, 2019");

        nc = new NewClue("Sunday March 17, 2019");
        nc.GetBestClues();
        nc.writeNewClues("Sunday March 17, 2019");

        nc = new NewClue("Tuesday February 5, 2019");
        nc.GetBestClues();
        nc.writeNewClues("Tuesday February 5, 2019");*/

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
