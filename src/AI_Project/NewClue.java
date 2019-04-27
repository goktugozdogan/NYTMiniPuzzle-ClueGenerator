package AI_Project;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class NewClue {
    // VARIABLES
    OxfordDictionary ox;
    CambridgeDictionary ca;
    UrbanDictionary ur;
    String date;
    Square[] newClues;

    NewClue ( String date) {
        ox = new OxfordDictionary();
        ca = new CambridgeDictionary();
        ur = new UrbanDictionary();

        newClues = new Square[10];
        for ( int i = 0; i < 10; i++ )
            newClues[i] = new Square("", "");

        this.date = date;
    }

    void GetBestClues () {
        OxfordThread oxford = new OxfordThread() ;
        oxford.start();

        CambridgeThread cambridge = new CambridgeThread();
        cambridge.start();

        UrbanThread urban = new UrbanThread();
        urban.start();

        try {
            oxford.join();
            cambridge.join();
            urban.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for ( int i = 0; i < 10; i++ ) {
            if ( ! ox.newCluesDefinition[i].clueText.equals("") )
                if ( newClues[i].clueText.length() > ox.newCluesDefinition[i].clueText.length() )
                    newClues[i] = new Square(ox.newCluesDefinition[i].clueNumber, ox.newCluesDefinition[i].clueText);
                else if ( newClues[i].clueText.length() < 2 )
                    newClues[i] = new Square(ox.newCluesDefinition[i].clueNumber, ox.newCluesDefinition[i].clueText);

            if ( ! ox.newCluesExample[i].clueText.equals("") )
                if ( newClues[i].clueText.length() > ox.newCluesExample[i].clueText.length() )
                    newClues[i] = new Square(ox.newCluesExample[i].clueNumber, ox.newCluesExample[i].clueText);
                else if ( newClues[i].clueText.length() < 2 )
                    newClues[i] = new Square(ox.newCluesExample[i].clueNumber, ox.newCluesExample[i].clueText);

            if ( ! ur.newCluesDefinition[i].clueText.equals("") )
                if ( newClues[i].clueText.length() > ur.newCluesDefinition[i].clueText.length() )
                    newClues[i] = new Square(ur.newCluesDefinition[i].clueNumber, ur.newCluesDefinition[i].clueText);
                else if ( newClues[i].clueText.length() < 2 )
                    newClues[i] = new Square(ur.newCluesDefinition[i].clueNumber, ur.newCluesDefinition[i].clueText);

            if ( ! ca.newCluesDefinition[i].clueText.equals("") )
                if ( newClues[i].clueText.length() > ca.newCluesDefinition[i].clueText.length() )
                    newClues[i] = new Square(ca.newCluesDefinition[i].clueNumber, ca.newCluesDefinition[i].clueText);
                else if ( newClues[i].clueText.length() < 2 )
                    newClues[i] = new Square(ca.newCluesDefinition[i].clueNumber, ca.newCluesDefinition[i].clueText);
        }
    }

    void PrintNewClues () {
        for ( Square s: newClues ) {
            System.out.println(s.clueNumber + " " + s.clueText);
        }
    }

    void PrintAllPossibleClues () {
        for ( int i = 0; i < 10; i++ ) {
            System.out.println(ox.newCluesDefinition[i].clueNumber + " " + ox.newCluesDefinition[i].clueText);
            System.out.println(ox.newCluesExample[i].clueNumber + " " + ox.newCluesExample[i].clueText);
            System.out.println(ur.newCluesDefinition[i].clueNumber + " " + ur.newCluesDefinition[i].clueText);
            System.out.println(ca.newCluesDefinition[i].clueNumber + " " + ca.newCluesDefinition[i].clueText);
            System.out.println();
        }
    }

    void writeNewClues (String date) {
        try {
            String path;
            // write puzzleLayout array to file
            path = "newclues/" + date + "/newclues.ser";
            FileOutputStream fosP = new FileOutputStream(path);
            ObjectOutputStream oosP = new ObjectOutputStream(fosP);
            oosP.writeObject(newClues);
            oosP.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    void readNewClues (String date) {
        try {
            // read clues array from file
            String path = "newclues/" + date + "/newclues.ser";
            FileInputStream fisC = new FileInputStream(path);
            ObjectInputStream oisC = new ObjectInputStream(fisC);
            newClues = (Square[]) oisC.readObject();
            oisC.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    class OxfordThread extends Thread {
        public void run() {
            try {
                ox.getNewClues(date);
            } catch (Exception e) {
                // Throwing an exception
                System.out.println (e.getStackTrace());
            }
        }
    }

    class CambridgeThread extends Thread {
        public void run() {
            try {
                ca.getNewClues(date);
            } catch (Exception e) {
                // Throwing an exception
                System.out.println (e.getStackTrace());
            }
        }
    }

    class UrbanThread extends Thread {
        public void run() {
            try {
                ur.getNewClues(date);
            } catch (Exception e) {
                // Throwing an exception
                System.out.println (e.getStackTrace());
            }
        }
    }
}
