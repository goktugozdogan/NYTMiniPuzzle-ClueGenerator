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
            boolean success = (new File("puzzles/" + date)).mkdirs();

            if ( !success )
                return;

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
        System.out.println("Date: " + puzzleDate + "\n-----CLUES-----");

        for (Clue clue : clues)
            System.out.println(clue.toString());

        System.out.println("\n-----ANSWERS-----");

        int i = 0;
        for (Square a : puzzleLay)
            System.out.println((i++) + "#\t" + a.toString());
    }
}
