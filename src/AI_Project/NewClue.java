package AI_Project;

import java.io.*;
import java.util.Arrays;

class NewClue {
    // VARIABLES
    private OxfordDictionary ox;
    private CambridgeDictionary ca;
    private UrbanDictionary ur;
    private String date;
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
        System.out.println("\nComputing best possible new clues from cambridge, Oxford and Urban Dictionaries...");

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
            int len = 7;

            if ( ! ca.newCluesDefinition[i].clueText.equals("") ) {
                if (newClues[i].clueText.length() > ca.newCluesDefinition[i].clueText.length())
                    newClues[i] = new Square(ca.newCluesDefinition[i].clueNumber, ca.newCluesDefinition[i].clueText);
                else if (newClues[i].clueText.length() < len)
                    newClues[i] = new Square(ca.newCluesDefinition[i].clueNumber, ca.newCluesDefinition[i].clueText);
            }

            if ( !(newClues[i].clueText.length() > 1) ) {
                if (!ox.newCluesDefinition[i].clueText.equals("")) {
                    if (newClues[i].clueText.length() > ox.newCluesDefinition[i].clueText.length())
                        newClues[i] = new Square(ox.newCluesDefinition[i].clueNumber, ox.newCluesDefinition[i].clueText);
                    else if (newClues[i].clueText.length() < len)
                        newClues[i] = new Square(ox.newCluesDefinition[i].clueNumber, ox.newCluesDefinition[i].clueText);
                }
            }

            if ( !(newClues[i].clueText.length() > 1) ) {
                if (!ur.newCluesDefinition[i].clueText.equals("")) {
                    if (newClues[i].clueText.length() > ur.newCluesDefinition[i].clueText.length())
                        newClues[i] = new Square(ur.newCluesDefinition[i].clueNumber, ur.newCluesDefinition[i].clueText);
                    else if (newClues[i].clueText.length() < len)
                        newClues[i] = new Square(ur.newCluesDefinition[i].clueNumber, ur.newCluesDefinition[i].clueText);
                }
            }
        }

        System.out.println("Best possible clues were computed.");
    }

    void PrintNewClues () {
        System.out.println("-----NEW CLUES-----");
        for ( Square s: newClues ) {
            System.out.println(s.clueNumber + "  " + s.clueText);
        }
    }

    void PrintAllPossibleClues () {
        System.out.println("\n-----ALL POSSIBLE CLUES-----");
        for ( int i = 0; i < 10; i++ ) {
            System.out.println(ca.newCluesDefinition[i].clueNumber + "  Cambridge:" + ca.newCluesDefinition[i].clueText);
            System.out.println(ox.newCluesDefinition[i].clueNumber + "  Oxford:" + ox.newCluesDefinition[i].clueText);
            System.out.println(ur.newCluesDefinition[i].clueNumber + "  Urban:" + ur.newCluesDefinition[i].clueText);
            System.out.println();
        }
    }

    void writeNewClues (String date) {
        try {
            boolean success = (new File("newclues/" + date)).mkdirs();

            if ( !success )
                return;

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
                System.out.println("Searching new possible clues in Oxford Dictionary...");
                ox.getNewClues(date);
                System.out.println("Oxford Dictionary search completed.");
            } catch (Exception e) {
                // Throwing an exception
                System.out.println (Arrays.toString(e.getStackTrace()));
            }
        }
    }

    class CambridgeThread extends Thread {
        public void run() {
            try {
                System.out.println("Searching new possible clues in Cambridge Dictionary...");
                ca.getNewClues(date);
                System.out.println("Cambridge Dictionary search completed.");
            } catch (Exception e) {
                // Throwing an exception
                System.out.println (Arrays.toString(e.getStackTrace()));
            }
        }
    }

    class UrbanThread extends Thread {
        public void run() {
            try {
                System.out.println("Searching new possible clues in Urban Dictionary...");
                ur.getNewClues(date);
                System.out.println("Urban Dictionary search completed.");
            } catch (Exception e) {
                // Throwing an exception
                System.out.println (Arrays.toString(e.getStackTrace()));
            }
        }
    }
}
