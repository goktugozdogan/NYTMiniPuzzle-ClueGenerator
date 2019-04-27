package AI_Project;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

class OxfordDictionary {
    // VARIABLES
    String url;
    Square[] newCluesDefinition;
    Square[] newCluesExample;

    OxfordDictionary() {
        url = "https://en.oxforddictionaries.com/definition/";
        newCluesDefinition = new Square[10];
        newCluesExample = new Square[10];
    }

    void getNewClues (String date) {
        Fetch f = new Fetch();

        try {
            f.readPuzzle(date);
            f.getWords();
        }catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

        System.setProperty("webdriver.chrome.driver","lib/selenium/chromedriver.exe");
        ChromeDriver browser = new ChromeDriver();

        int ex_count = 0;
        int def_count = 0;
        boolean def_bool = false;
        for ( Square s: f.words ) {
            browser.get( url+s.clueText );

            try {
                // DEFINITION
                WebElement element = browser.findElementByXPath("//*[@id=\"content\"]/div[1]/div[2]/div/div/div/div[1]/section[1]/ul/li/div/p/span[2]");
                newCluesDefinition[def_count] = new Square(s.clueNumber, element.getText());
                def_bool = true;

                // EXAMPLE
                element = browser.findElementByXPath("//*[@id=\"content\"]/div[1]/div[2]/div/div/div/div[1]/section[1]/ul/li/div/div[1]/div/em");
                String ex = element.getText();
                if ( ex.toLowerCase().contains(s.clueText.toLowerCase()) )
                    ex = ex.replace(s.clueText.toLowerCase(), "_____");
                newCluesExample[ex_count] = new Square(s.clueNumber, ex);

            }catch (Exception e) {
                if ( def_bool )
                    newCluesExample[ex_count] = new Square(s.clueNumber, "");
                else {
                    newCluesDefinition[def_count] = new Square(s.clueNumber, "");
                    newCluesExample[ex_count] = new Square(s.clueNumber, "");
                }
            }
            def_count++;
            ex_count++;
            def_bool = false;
        }

        browser.close();
    }
}
