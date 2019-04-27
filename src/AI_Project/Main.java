package AI_Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        String date3 = "Sunday March 17, 2019";
        String date4 = "Monday March 18, 2019";
        String date5 = "Friday March 15, 2019";

        NewClue nc;
        nc = new NewClue(date3);
        nc.readNewClues(date3);
        nc.PrintNewClues();

        System.out.println();

        nc = new NewClue(date4);
        nc.readNewClues(date4);
        nc.PrintNewClues();

        System.out.println();

        nc = new NewClue(date5);
        nc.readNewClues(date5);
        nc.PrintNewClues();
        /*
        String date3 = "Sunday March 17, 2019";
        String date4 = "Monday March 18, 2019";
        String date5 = "Friday March 15, 2019";
        NewClue nc;

        nc = new NewClue(date3);
        nc.GetBestClues();
        nc.writeNewClues(date3);
        nc.readNewClues(date3);
        nc.PrintNewClues();

        System.out.println();

        nc = new NewClue(date4);
        nc.GetBestClues();
        nc.writeNewClues(date4);
        nc.readNewClues(date4);
        nc.PrintNewClues();

        System.out.println();

        nc = new NewClue(date5);
        nc.GetBestClues();
        nc.writeNewClues(date5);
        nc.readNewClues(date5);
        nc.PrintNewClues();
         */

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
