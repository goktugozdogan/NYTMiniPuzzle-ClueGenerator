package AI_Project;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Arrays;

class OxfordDictionary {
    // VARIABLES
    private String url;
    Square[] newCluesDefinition;

    OxfordDictionary() {
        url = "https://en.oxforddictionaries.com/definition/";
        newCluesDefinition = new Square[10];
    }

    void getNewClues (String date) {
        Fetch f = new Fetch();

        try {
            f.readPuzzle(date);
            f.getWords();
        }catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        System.setProperty("webdriver.chrome.driver","lib/selenium/chromedriver.exe");
        ChromeDriver browser = new ChromeDriver();

        int def_count = 0;
        for ( Square s: f.words ) {
            browser.get( url+s.clueText );

            try {
                // DEFINITION
                WebElement element = browser.findElementByXPath("//*[@id=\"content\"]/div[1]/div[2]/div/div/div/div[1]/section[1]/ul/li/div/p/span[2]");
                newCluesDefinition[def_count] = new Square(s.clueNumber, element.getText());

            }catch (Exception e) {
                newCluesDefinition[def_count] = new Square(s.clueNumber, "");
            }
            def_count++;
        }

        browser.close();
    }
}
