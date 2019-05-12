package AI_Project;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class SlidePuzzleGUI extends JPanel {
    private Fetch f ;
    private boolean newGameFlag = false;
    private JTextField dateTextField1 = new JTextField(15);
    private JButton specifiedDateButton;
    private JButton getNewCluesButton;

    private GraphicsPanel _puzzleGraphics;
    private JTextArea text;
    private JTextArea textD;
    private JLabel textDate;
    private String date;
    private JButton todaysPuzzleButton;

    SlidePuzzleGUI(String date, boolean dataFlag) {
        //System.out.println("constructor enter");
        this.date = date;

        f = new Fetch();


        if ( dataFlag ) {
            f.readPuzzle(date);
            f.PrintPuzzle();
        }


        text = new JTextArea(30,30);
        text.setFont(new Font("Serif", Font.BOLD, 16));
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setOpaque(false);
        text.setEditable(false);
        textD = new JTextArea(30, 30);
        textD.setFont(new Font("Serif", Font.BOLD, 16));
        textD.setLineWrap(true);
        textD.setWrapStyleWord(true);
        textD.setOpaque(false);
        textD.setEditable(false);
        textDate= new JLabel();
        textDate.setHorizontalAlignment(SwingConstants.RIGHT);
        textDate.setLocation(550,550);
        clueGetter(false);

        JPanel menuPanel = new JPanel();
        getNewCluesButton = new JButton("Click to see new clues !");
        specifiedDateButton = new JButton("Open Puzzle at specified date ! ");
        todaysPuzzleButton = new JButton("Open Puzzle at this day ! ");

        JPanel controlPanel = new JPanel();
        JPanel clueLayout = new JPanel();

        menuPanel.add(todaysPuzzleButton);
        menuPanel.add(getNewCluesButton);
        menuPanel.add(specifiedDateButton);
        menuPanel.add(dateTextField1);

        clueLayout.setSize(new Dimension(300,600));
        clueLayout.add(text);
        clueLayout.add(textD);

        controlPanel.setLayout(new FlowLayout());

        _puzzleGraphics = new GraphicsPanel();

        this.setSize(new Dimension(600,600));
        this.setLayout(new BorderLayout());
        this.add(_puzzleGraphics, BorderLayout.CENTER);
        this.add(clueLayout, BorderLayout.EAST);

        this.add(menuPanel,BorderLayout.NORTH);
        this.add(textDate,BorderLayout.PAGE_END);
        this.update();
        this.revalidate();

        this.repaint();

    }

    private void setDate(String date){
        this.date = date;
    }

    private void setFetch(Fetch f){
        this.f = f;

    }

    class GraphicsPanel extends JPanel implements ActionListener {
        private static final int ROWS = 5;
        private static final int COLS = 5;

        private static final int CELL_SIZE = 80; // Pixels
        private Font _biggerFont;

        GraphicsPanel() {
            _biggerFont = new Font("SansSerif", Font.BOLD, CELL_SIZE/2);
            this.setPreferredSize(new Dimension(CELL_SIZE * COLS, CELL_SIZE*ROWS));

            todaysPuzzleButton.addActionListener(
                    e -> {
                        f.getClues();
                        f.getSolutions();

                        date = f.puzzleDate;
                        f.writePuzzle(date);
                        //f.PrintPuzzle();
                        setDate(date);
                        SlidePuzzleGUI newGUI =  new SlidePuzzleGUI(date, true);
                        _puzzleGraphics = newGUI._puzzleGraphics;
                        setFetch(newGUI.f);
                        clueSet(newGUI.f);
                        clueGetter(true);

                        newGameFlag = true;
                    }
            );

            specifiedDateButton.addActionListener(
                    e -> {
                        date = dateTextField1.getText();
                        setDate(date);
                        SlidePuzzleGUI newGUI =  new SlidePuzzleGUI(date, true);
                        _puzzleGraphics = newGUI._puzzleGraphics;
                        setFetch(newGUI.f);
                        clueSet(newGUI.f);
                        clueGetter(true);
                        newGameFlag = true;
                    }
            );

            getNewCluesButton.addActionListener(
                    e -> {
                        NewClue nc = new NewClue(f.puzzleDate);
                        date = f.puzzleDate;
                        nc.GetBestClues();
                        nc.writeNewClues(date);
                        nc.readNewClues(date);
                        nc.PrintAllPossibleClues();
                        nc.PrintNewClues();
                        sort(nc.newClues);
                        StringBuilder newText = new StringBuilder(text.getText());
                        StringBuilder newTextD = new StringBuilder(textD.getText());
                        newText.append("\n\n                                                                " + "New Clues \nACROSS \n------------------\n");
                        newTextD.append("\n \n \nDOWN \n------------------\n");
                        for(int i = 0; i < 10; i++){
                           // newText +=  nc.newClues[i].clueText;
                            if(nc.newClues[i].clueNumber.charAt(0) == 'A') {
                                newText.append(nc.newClues[i].clueNumber.charAt(1)).append(": ").append(nc.newClues[i].clueText).append("\n");
                            }
                            if(nc.newClues[i].clueNumber.charAt(0) == 'D') {
                                newTextD.append(nc.newClues[i].clueNumber.charAt(1)).append(": ").append(nc.newClues[i].clueText).append("\n");
                            }
                        }
                        text.setText(newText.toString());
                        textD.setText(newTextD.toString());
                    }
            );
        }

        void sort(Square[] arr) {
            int n = arr.length;
            for (int i = 0; i < n-1; i++)
                for (int j = 0; j < n-i-1; j++)
                    if (arr[j].clueNumber.charAt(1) > arr[j+1].clueNumber.charAt(1)) {
                        // swap arr[j+1] and arr[i]
                        Square temp = arr[j];
                        arr[j] = arr[j+1];
                        arr[j+1] = temp;
                    }
        }

        public void paintComponent(Graphics g) {

            int counter = 0;
            int xpos;
            int ypos;
            super.paintComponent(g);
            for (int r=0; r<ROWS; r++) {
                for (int c=0; c<COLS; c++) {

                    int x = c * CELL_SIZE;
                    int y = r * CELL_SIZE;
                    if(newGameFlag ) {
                        if (f.puzzleLay[counter].clueText == null && f.puzzleLay[counter].clueNumber == null) {
                            g.setColor(Color.black);
                            g.fillRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
                        }
                        if (f.puzzleLay[counter].clueText != null) {
                            g.setColor(Color.white);
                            g.fillRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
                            g.setColor(Color.black);
                            g.setFont(_biggerFont);

                            if (f.puzzleLay[counter].clueText != null) {
                                xpos = x + 20;
                                ypos = (y + (3 * CELL_SIZE) / 4);
                                g.drawString(f.puzzleLay[counter].clueText, x + 20, y + (3 * CELL_SIZE) / 4);
                                if (f.puzzleLay[counter].clueText != null && f.puzzleLay[counter].clueNumber != null) {
                                    g.setFont(new Font("Comic Sans MS", Font.ITALIC, 15));
                                    g.drawString(f.puzzleLay[counter].clueNumber, xpos - 12, ypos - 25);
                                }
                            }

                        }
                    }
                    else{
                        g.setColor(Color.black);
                        g.fillRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
                    }
                    counter++;
                }
            }
            this.revalidate();
            this.repaint();

        }



        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private void clueSet(Fetch ff){
        this.f.clues = ff.clues;

    }
    private void clueGetter(boolean s){
        StringBuilder str = new StringBuilder("                                                                " +
                "Original Clues " + "\n ACROSS \n ------------------  \n");

        StringBuilder strdown = new StringBuilder("\n DOWN \n ------------------  \n");
        if(s )
            for(int i = 0; i < 10; ++i){
                if(f.clues.get(i).loc.equals("ACROSS")) {
                    str.append(f.clues.get(i).id).append(" : ").append(f.clues.get(i).text).append("\n");
                }
                if(f.clues.get(i).loc.equals("DOWN")) {
                    strdown.append(f.clues.get(i).id).append(" : ").append(f.clues.get(i).text).append("\n");
                }
            }
        if(!s)
        {
            str = new StringBuilder("***    Wellcome to Puzzle Clue Generator    ***");
            strdown = new StringBuilder(" ***** Göktuğ Özdoğan ***** \n ***** Mehmet Eren Turanboy ***** \n ***** Derviş Mehmet Barutçu ***** \n ***** Onur Kocahan ***** \n ***** Yiğit Kutay Gülden *****");
        }
        text.setText(str + "\n");
        text.setBackground(Color.gray);
        textD.setText(strdown + "\n");
        textDate.setText(f.puzzleDate);

        textDate.setHorizontalAlignment(SwingConstants.RIGHT);
    }

    private void update(){

        this.revalidate();
        this.repaint();

    }
}