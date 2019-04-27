package AI_Project;

import java.io.*;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class Fetch {
    // VARIABLES
    private String url;
    private Document doc;
    ArrayList<Clue> clues;
    Square[] puzzleLay;
    String puzzleDate;
    Square[] words;

    // constructor with url
    Fetch () {
        url = "https://www.nytimes.com/crosswords/game/mini";
        clues = new ArrayList<>();
        puzzleLay = new Square[25];
        words = new Square[10];
    }

    void getClues () {
        try {
            doc = Jsoup.connect(url).userAgent("Chrome").get(); // get document
        }catch (Exception e) {
            e.printStackTrace();
        }

        // count location of clues
        Elements locCount = doc.select("div.ClueList-wrapper--3m-kd");
        int downCount = 0;
        for ( Element e : locCount ) {
            String loc = e.select("span.Clue-label--2IdMY").text().replace(" ", "");
            downCount = loc.length();
        }

        // get clues
        Elements clueList = doc.select("li.Clue-li--1JoPu");
        int count = 0;
        for ( Element e : clueList ) {
            String loc;
            if ( count < downCount)
                loc = "ACROSS";
            else
                loc = "DOWN";
            int id = Integer.parseInt(e.text().substring(0, 1));
            String text = e.text().substring(1);
            clues.add(new Clue(loc, id, text));
            count++;
        }
    }

    void getSolutions () {
        System.setProperty("webdriver.chrome.driver","lib/selenium/chromedriver.exe");
        ChromeDriver browser = new ChromeDriver();
        browser.get(url);

        WebElement element0 = browser.findElementByXPath("//*[@id=\"root\"]/div/div/div[4]/div/main/div[2]/div/div[2]/div[2]/article/div[2]/button");
        Actions a0 = new Actions(browser);
        a0.moveToElement(element0).click().perform();

        WebElement element1 = browser.findElementByXPath("//*[@id=\"root\"]/div/div/div[4]/div/main/div[2]/div/div/ul/div[1]/li[2]/button");
        Actions a1 = new Actions(browser);
        a1.moveToElement(element1).click().perform();

        WebDriverWait wait0 = new WebDriverWait(browser,10);
        wait0.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"root\"]/div/div/div[4]/div/main/div[2]/div/div/ul/div[1]/li[2]/ul/li[3]/a")));

        WebElement element2 = browser.findElementByXPath("//*[@id=\"root\"]/div/div/div[4]/div/main/div[2]/div/div/ul/div[1]/li[2]/ul/li[3]/a");
        Actions a2 = new Actions(browser);
        a2.moveToElement(element2).click().perform();

        WebDriverWait wait1 = new WebDriverWait(browser,10);
        wait1.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/article/div[2]/button[2]")));

        WebElement element3 = browser.findElementByXPath("//*[@id=\"root\"]/div/div[2]/div[2]/article/div[2]/button[2]");
        Actions a3 = new Actions(browser);
        a3.moveToElement(element3).click().perform();

        for ( int i = 1; i <= 25; i++ ) {
            String xpath = "//*[@id=\"xwd-board\"]/*[name()=\"g\"][1]/*[name()=\"g\"][" + i + "]";

            WebElement element4 = browser.findElementByXPath(xpath);

            if ( element4.getText().length() == 3 )
                puzzleLay[i-1] = new Square(element4.getText().substring(0,1), element4.getText().substring(2));
            else if ( element4.getText().length() == 1 )
                puzzleLay[i-1] = new Square(null, element4.getText());
            else if ( element4.getText().length() == 0 )
                puzzleLay[i-1] = new Square(null, null);
        }

        // get date
        WebElement element5 = browser.findElementByXPath("//*[@id=\"root\"]/div/div/div[4]/div/main/div[2]/header/div/div/div/div[1]");
        puzzleDate = element5.getText();

        browser.close();
    }

    void getWords () {
        int count = 0;

        // ACROSS
        boolean once = true;
        for ( int j = 0, begin = 0, end = 4; j <= 4; j++, once = true, begin += 5, end += 5, count++ ) {
            for ( int i = begin; i <= end; i++ ) {
                if ( puzzleLay[i].clueText != null ) {
                    if ( once ){
                        once = false;
                        words[count] = new Square("", "");
                        words[count].clueNumber = "A" + puzzleLay[i].clueNumber;
                    }
                    words[count].clueText += puzzleLay[i].clueText;
                }
            }
        }

        // DOWN
        once = true;
        for ( int j = 0, begin = 0, end = 20; j <= 4; j++, once = true, begin++, end++, count++ ) {
            for ( int i = begin; i <= end; i += 5 ) {
                if ( puzzleLay[i].clueText != null ) {
                    if ( once ){
                        once = false;
                        words[count] = new Square("", "");
                        words[count].clueNumber = "D" + puzzleLay[i].clueNumber;
                    }
                    words[count].clueText += puzzleLay[i].clueText;
                }
            }
        }
    }

    void writePuzzle (String date) {
        try {
            String path;
            // write clues array to file
            path = "puzzles/" + date + "/clues.ser";
            FileOutputStream fosC = new FileOutputStream(path);
            ObjectOutputStream oosC = new ObjectOutputStream(fosC);
            oosC.writeObject(clues);
            oosC.close();

            // write puzzleLayout array to file
            path = "puzzles/" + date + "/puzzleLayout.ser";
            FileOutputStream fosP = new FileOutputStream(path);
            ObjectOutputStream oosP = new ObjectOutputStream(fosP);
            oosP.writeObject(puzzleLay);
            oosP.close();

            // write date
            path = "puzzles/" + date + "/date.ser";
            FileOutputStream fosD = new FileOutputStream(path);
            ObjectOutputStream oosD = new ObjectOutputStream(fosD);
            oosD.writeObject(puzzleDate);
            oosD.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    void readPuzzle (String date) {
        try {
            // read clues array from file
            String path = "puzzles/" + date + "/clues.ser";
            FileInputStream fisC = new FileInputStream(path);
            ObjectInputStream oisC = new ObjectInputStream(fisC);
            clues = (ArrayList<Clue>) oisC.readObject();
            oisC.close();

            // read puzzleLayout array from file
            path = "puzzles/" + date + "/puzzleLayout.ser";
            FileInputStream fisP = new FileInputStream(path);
            ObjectInputStream oisP = new ObjectInputStream(fisP);
            puzzleLay = (Square[]) oisP.readObject();
            oisP.close();

            // read date from file
            path = "puzzles/" + date + "/date.ser";
            FileInputStream fisD = new FileInputStream(path);
            ObjectInputStream oisD = new ObjectInputStream(fisD);
            this.puzzleDate = (String) oisD.readObject();
            oisD.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    void PrintPuzzle () {
        System.out.println("Date: " + puzzleDate + "\n");

        for (Clue clue : clues)
            System.out.println(clue.toString() + "\n");

        for (Square a : puzzleLay)
            System.out.println(a.toString());
    }
}

/* 12.03.2019
// constructor with url
    public Fetch () {
        this.url = url;
        clues = new ArrayList<Clue>();
        clues.add(new Clue("ACROSS", 1, "Bailout recipients during the 2008 financial crisis"));
        clues.add(new Clue("ACROSS", 6, "Bird-related"));
        clues.add(new Clue("ACROSS", 7, "Democratic Republic of the _____"));
        clues.add(new Clue("ACROSS", 8, "Religious headscarf"));
        clues.add(new Clue("ACROSS", 9, "Brown who wrote \"The Da Vinci Code\""));
        clues.add(new Clue("DOWN", 1, "Beethoven called him \"the immortal god of harmony\""));
        clues.add(new Clue("DOWN", 2, "Stay away from"));
        clues.add(new Clue("DOWN", 3, "Skilled assassin"));
        clues.add(new Clue("DOWN", 4, "Supreme Court justice appointed between Sotomayor and Gorsuch"));
        clues.add(new Clue("DOWN", 5, "TODO"));

        puzzleLay = new Square[25];

        puzzleLay[0] = new Square("1", "B");
        puzzleLay[1] = new Square("2", "A");
        puzzleLay[2] = new Square("3", "N");
        puzzleLay[3] = new Square("4", "K");
        puzzleLay[4] = new Square("5", "S");
        puzzleLay[5] = new Square("6", "A");
        puzzleLay[6] = new Square(null, "V");
        puzzleLay[7] = new Square(null, "I");
        puzzleLay[8] = new Square(null, "A");
        puzzleLay[9] = new Square(null, "N");
        puzzleLay[10] = new Square("7", "C");
        puzzleLay[11] = new Square(null, "O");
        puzzleLay[12] = new Square(null, "N");
        puzzleLay[13] = new Square(null, "G");
        puzzleLay[14] = new Square(null, "O");
        puzzleLay[15] = new Square("8", "H");
        puzzleLay[16] = new Square(null, "I");
        puzzleLay[17] = new Square(null, "J");
        puzzleLay[18] = new Square(null, "A");
        puzzleLay[19] = new Square(null, "B");
        puzzleLay[20] = new Square(null, null);
        puzzleLay[21] = new Square("9", "D");
        puzzleLay[22] = new Square(null, "A");
        puzzleLay[23] = new Square(null, "N");
        puzzleLay[24] = new Square(null, null);
    }
 */

/* 14.03.2019
// constructor with url
    public Fetch () {
        this.url = url;
        clues = new ArrayList<Clue>();
        clues.add(new Clue("ACROSS", 1, "Pickle holder"));
        clues.add(new Clue("ACROSS", 4, "Annoyingly self-assertive"));
        clues.add(new Clue("ACROSS", 6, "Andrews who starred in the original \"Mary Poppins\""));
        clues.add(new Clue("ACROSS", 7, "Tilt to one side"));
        clues.add(new Clue("ACROSS", 8, "_____ Balls (Hostess snack food)"));
        clues.add(new Clue("DOWN", 1, "Popular vaping devices"));
        clues.add(new Clue("DOWN", 2, "Lion in \"The Lion, the Witch and the Wardrobe\""));
        clues.add(new Clue("DOWN", 3, "Horned African mammal"));
        clues.add(new Clue("DOWN", 4, "Jammies"));
        clues.add(new Clue("DOWN", 5, "Up to this point"));

        puzzleLay = new Square[25];

        puzzleLay[0] = new Square(null,null);
        puzzleLay[1] = new Square("1", "J");
        puzzleLay[2] = new Square("2", "A");
        puzzleLay[3] = new Square("3", "R");
        puzzleLay[4] = new Square(null,null);
        puzzleLay[5] = new Square("4", "P");
        puzzleLay[6] = new Square(null, "U");
        puzzleLay[7] = new Square(null, "S");
        puzzleLay[8] = new Square(null, "H");
        puzzleLay[9] = new Square(null, "Y");
        puzzleLay[10] = new Square("6", "J");
        puzzleLay[11] = new Square(null, "U");
        puzzleLay[12] = new Square(null, "L");
        puzzleLay[13] = new Square(null, "I");
        puzzleLay[14] = new Square(null, "E");
        puzzleLay[15] = new Square("7", "S");
        puzzleLay[16] = new Square(null, "L");
        puzzleLay[17] = new Square(null, "A");
        puzzleLay[18] = new Square(null, "N");
        puzzleLay[19] = new Square(null, "T");
        puzzleLay[20] = new Square(null, null);
        puzzleLay[21] = new Square("8", "S");
        puzzleLay[22] = new Square(null, "N");
        puzzleLay[23] = new Square(null, "O");
        puzzleLay[24] = new Square(null, null);
    }
 */

/*
    public void wrDate (String date) {
        // write date
        try {
            String path = "puzzles/" + date + "/date.ser";
            FileOutputStream fosD = null;
            fosD = new FileOutputStream(path);
            ObjectOutputStream oosD = new ObjectOutputStream(fosD);
            oosD.writeObject("Friday March 15, 2019");
            oosD.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
 */

/* 4 feb
// constructor with url
    public Fetch (String date) {
        puzzleDate = date;
        clues = new ArrayList<Clue>();
        clues.add(new Clue("ACROSS", 1, "Post-lecture session, for short"));
        clues.add(new Clue("ACROSS", 6, "Maneuver needed after missing a GPS direction"));
        clues.add(new Clue("ACROSS", 7, "Annoyed"));
        clues.add(new Clue("ACROSS", 8, "Breast, thigh or wing"));
        clues.add(new Clue("ACROSS", 9, "Inquires"));
        clues.add(new Clue("DOWN", 1, "Witty one-liner"));
        clues.add(new Clue("DOWN", 2, "Heart chambers"));
        clues.add(new Clue("DOWN", 3, "Weapons at the heart of a disputed U.S.-Russia treaty"));
        clues.add(new Clue("DOWN", 4, "Rubbish"));
        clues.add(new Clue("DOWN", 5, "Range that's home to the 22,831-foot Mount Aconcagua"));

        puzzleLay = new Square[25];

        puzzleLay[0] = new Square("1","Q");
        puzzleLay[1] = new Square("2", "A");
        puzzleLay[2] = new Square("3", "N");
        puzzleLay[3] = new Square("4", "D");
        puzzleLay[4] = new Square("5","A");
        puzzleLay[5] = new Square("6", "U");
        puzzleLay[6] = new Square(null, "T");
        puzzleLay[7] = new Square(null, "U");
        puzzleLay[8] = new Square(null, "R");
        puzzleLay[9] = new Square(null, "N");
        puzzleLay[10] = new Square("7", "I");
        puzzleLay[11] = new Square(null, "R");
        puzzleLay[12] = new Square(null, "K");
        puzzleLay[13] = new Square(null, "E");
        puzzleLay[14] = new Square(null, "D");
        puzzleLay[15] = new Square("8", "P");
        puzzleLay[16] = new Square(null, "I");
        puzzleLay[17] = new Square(null, "E");
        puzzleLay[18] = new Square(null, "C");
        puzzleLay[19] = new Square(null, "E");
        puzzleLay[20] = new Square(null, null);
        puzzleLay[21] = new Square("9", "A");
        puzzleLay[22] = new Square(null, "S");
        puzzleLay[23] = new Square(null, "K");
        puzzleLay[24] = new Square(null, "S");
    }
 */

/* feb 5
// constructor with url
    public Fetch (String date) {
        puzzleDate = date;
        clues = new ArrayList<Clue>();
        clues.add(new Clue("ACROSS", 1, "What cheese and soy sauce add to dish"));
        clues.add(new Clue("ACROSS", 5, "Emotional request"));
        clues.add(new Clue("ACROSS", 7, "Sent in, as one's taxes"));
        clues.add(new Clue("ACROSS", 8, "What lemon and vinegar add to a dish"));
        clues.add(new Clue("ACROSS", 9, "The \"place\" for \"1\" in 2019"));
        clues.add(new Clue("DOWN", 1, "What chill and ginger add to a dish"));
        clues.add(new Clue("DOWN", 2, "Max bet for a poker player"));
        clues.add(new Clue("DOWN", 3, "English city near Manchester"));
        clues.add(new Clue("DOWN", 4, "Slightest bit"));
        clues.add(new Clue("DOWN", 6, "What olive oil and butter add to a dish"));

        puzzleLay = new Square[25];

        puzzleLay[0] = new Square(null,null);
        puzzleLay[1] = new Square("1", "S");
        puzzleLay[2] = new Square("2", "A");
        puzzleLay[3] = new Square("3", "L");
        puzzleLay[4] = new Square("4","T");
        puzzleLay[5] = new Square(null, null);
        puzzleLay[6] = new Square("5", "P");
        puzzleLay[7] = new Square(null, "L");
        puzzleLay[8] = new Square(null, "E");
        puzzleLay[9] = new Square(null, "A");
        puzzleLay[10] = new Square("6", "F");
        puzzleLay[11] = new Square(null, "I");
        puzzleLay[12] = new Square(null, "L");
        puzzleLay[13] = new Square(null, "E");
        puzzleLay[14] = new Square(null, "D");
        puzzleLay[15] = new Square("7", "A");
        puzzleLay[16] = new Square(null, "C");
        puzzleLay[17] = new Square(null, "I");
        puzzleLay[18] = new Square(null, "D");
        puzzleLay[19] = new Square(null, null);
        puzzleLay[20] = new Square("8", "T");
        puzzleLay[21] = new Square(null, "E");
        puzzleLay[22] = new Square(null, "N");
        puzzleLay[23] = new Square(null, "S");
        puzzleLay[24] = new Square(null, null);
    }
 */