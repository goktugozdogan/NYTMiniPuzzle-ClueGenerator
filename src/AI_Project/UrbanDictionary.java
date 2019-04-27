package AI_Project;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class UrbanDictionary {
    // VARIABLES
    String url;

    UrbanDictionary () {
        url = "https://www.urbandictionary.com/define.php?term=";
    }

    void getNewClues (String date) {
        Fetch f = new Fetch();

        try {
            f.readPuzzle(date);
            f.getWords();
            f.PrintPuzzle();
        }catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

        System.setProperty("webdriver.chrome.driver","lib/selenium/chromedriver.exe");
        ChromeDriver browser = new ChromeDriver();

        for ( Square s: f.words ) {
            browser.get( url+s.clueText );

            try {
                WebElement element0 = browser.findElementByXPath("//*[@id=\"content\"]/div[1]");
                System.out.println(element0.getText());
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
            }

        }
    }
}
