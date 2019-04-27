package AI_Project;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

class CambridgeDictionary {
    // VARIABLES
    String url;
    Square[] newCluesDefinition;
    Square[] newCluesExample;

    CambridgeDictionary() {
        url = "https://dictionary.cambridge.org/dictionary/english/";
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
                WebElement element = browser.findElementByXPath("//*[@id=\"cald4-1-1-1\"]/div[1]/div/p/b");
                String def = element.getText();

                if ( def.contains("→  "))
                    def = def.substring(0, def.indexOf("→  "));
                if ( def.toLowerCase().contains(s.clueText.toLowerCase()) )
                    def = def.replace(s.clueText.toLowerCase(), "_____");
                newCluesDefinition[def_count] = new Square(s.clueNumber, def);

            }catch (Exception e) {
                newCluesDefinition[def_count] = new Square(s.clueNumber, "");
            }
            def_count++;
        }

        browser.close();
    }
}
