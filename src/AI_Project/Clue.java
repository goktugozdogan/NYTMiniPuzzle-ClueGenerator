package AI_Project;

import java.io.Serializable;

class Clue implements Serializable {
    // VARIABLES
    private static final long serialVersionUID = 1L;
    String loc;
    int id;
    String text;

    Clue(String loc, int id, String text) {
        this.loc = loc;
        this.id = id;
        this.text = text;
    }

    @Override
    public String toString () {
        return "Location: " + loc + "\nID: " + id + "\nText: " + text;
    }
}
