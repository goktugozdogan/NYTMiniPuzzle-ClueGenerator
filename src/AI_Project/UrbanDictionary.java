package AI_Project;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

class UrbanDictionary {
    // VARIABLES
    String url;
    Square[] newCluesDefinition;
    Square[] newCluesExample;

    UrbanDictionary() {
        url = "https://www.urbandictionary.com/define.php?term=";
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

        int def_count = 0;
        for ( Square s: f.words ) {
            browser.get( url+s.clueText );

            try {
                // DEFINITION
                WebElement element = browser.findElementByXPath("//*[@id=\"content\"]/div[1]/div[3]");
                String def = element.getText();
                if ( def.contains("\n"))
                    def = def.substring(0, def.indexOf("\n"));
                if ( def.substring(0,1).equals("1"))
                    def = def.substring(2);
                if ( def.toLowerCase().contains(s.clueText.toLowerCase()) )
                    def = def.replace(s.clueText.toLowerCase(), "_____");
                newCluesDefinition[def_count] = new Square(s.clueNumber, def);

                /*element = browser.findElementByXPath("//*[@id=\"content\"]/div[2]/div[3]");
                newCluesDefinition[def_count].clueText += "\n" + element.getText();*/

            }catch (Exception e) {
                newCluesDefinition[def_count] = new Square(s.clueNumber, "");
            }
            def_count++;
        }

        browser.close();
    }
}