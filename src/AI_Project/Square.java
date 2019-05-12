package AI_Project;

import java.io.Serializable;

class Square implements Serializable {
    // VARIABLES
    private static final long serialVersionUID = 1L;
    String clueNumber;
    String clueText;

    Square(String clueNumber, String clueText) {
        this.clueNumber = clueNumber;
        this.clueText = clueText;
    }

    @Override
    public String toString () {
        return clueNumber + "\t" + clueText;
    }
}
