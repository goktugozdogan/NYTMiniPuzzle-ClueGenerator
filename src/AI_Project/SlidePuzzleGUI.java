package AI_Project;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class SlidePuzzleGUI extends JPanel {
    Fetch f ;
    boolean newGameFlag = false;
    JTextField dateTextField1 = new JTextField(15);
    JButton newGameButton;
    JButton getNewCluesButton;
    BorderLayout allClues = new BorderLayout();

    private GraphicsPanel    _puzzleGraphics;
    private JTextArea text;
    private JTextArea textD;
    JLabel textDate;
    String date;
    JButton todaysPuzzleButton;



    public SlidePuzzleGUI(String date, boolean dataFlag) {
        System.out.println("consturctor enter");
        this.date = date;

        f = new Fetch();


        if ( dataFlag )
            f.readPuzzle(date);

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
        newGameButton = new JButton("Open Puzzle at specified date ! ");
        todaysPuzzleButton = new JButton("Open Puzzle at this day ! ");

        JPanel controlPanel = new JPanel();
        JPanel clueLayout = new JPanel();

        JPanel dateLayout = new JPanel();


        menuPanel.add(todaysPuzzleButton);
        menuPanel.add(getNewCluesButton);
        menuPanel.add(newGameButton);
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

    public void setDate(String date){
        this.date = date;
    }
    public  String getDate(){
        System.out.println(this.date);
        return this.date;
    }

    public void setfetct(Fetch f){
        this.f = f;

    }

    class GraphicsPanel extends JPanel implements ActionListener {
        private static final int ROWS = 5;
        private static final int COLS = 5;

        private static final int CELL_SIZE = 80; // Pixels
        private Font _biggerFont;

        public GraphicsPanel() {
            _biggerFont = new Font("SansSerif", Font.BOLD, CELL_SIZE/2);
            this.setPreferredSize(new Dimension(CELL_SIZE * COLS, CELL_SIZE*ROWS));

            todaysPuzzleButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            f.getClues();
                            f.getSolutions();

                            date = f.puzzleDate;
                            setDate(date);
                            SlidePuzzleGUI newGUI =  new SlidePuzzleGUI(date, true);
                            _puzzleGraphics = newGUI._puzzleGraphics;
                            setfetct(newGUI.f);
                            clueSet(newGUI.f);
                            clueGetter(true);

                            newGameFlag = true;
                        }
                    }
            );



            newGameButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            date = dateTextField1.getText();
                            setDate(date);
                            SlidePuzzleGUI newGUI =  new SlidePuzzleGUI(date, true);
                            _puzzleGraphics = newGUI._puzzleGraphics;
                            setfetct(newGUI.f);
                            clueSet(newGUI.f);
                            clueGetter(true);
                            newGameFlag = true;
                        }
                    }
            );

            getNewCluesButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            NewClue nc = new NewClue(f.puzzleDate);
                            nc.GetBestClues();
                            nc.PrintNewClues();
                            String newText =text.getText();
                            String newTextD =textD.getText();
                            newText += "\n\n                                                                " +
                                    "New Clues \nACROSS \n------------------\n";
                            newTextD +="\n \n \nDOWN \n------------------\n";
                            for(int i = 0; i < 10; i++){
                               // newText +=  nc.newClues[i].clueText;
                                if(nc.newClues[i].clueNumber.charAt(0) == 'A') {
                                    newText +=  nc.newClues[i].clueNumber.charAt(1) + ": "+ nc.newClues[i].clueText + "\n";
                                }
                                if(nc.newClues[i].clueNumber.charAt(0) == 'D') {
                                    newTextD +=  nc.newClues[i].clueNumber.charAt(1) + ": " + nc.newClues[i].clueText + "\n";
                                }
                            }

                            text.setText(newText);
                            textD.setText(newTextD);

                        }
                    }
            );

        }

        int partition(Square arr[], int low, int high)
        {
            int pivot = ((int)(arr[high].clueNumber.charAt(1)));
            int i = (low-1); // index of smaller element
            for (int j=low; j<high; j++)
            {
                // If current element is smaller than or
                // equal to pivot
                if (((int)(arr[high].clueNumber.charAt(1) )<= pivot))
                {
                    i++;

                    // swap arr[i] and arr[j]
                    Square temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }

            // swap arr[i+1] and arr[high] (or pivot)
            Square temp = arr[i+1];
            arr[i+1] = arr[high];
            arr[high] = temp;

            return i+1;
        }


        /* The main function that implements QuickSort()
          arr[] --> Array to be sorted,
          low  --> Starting index,
          high  --> Ending index */
        void sort(Square arr[], int low, int high)
        {
            if (low < high)
            {
            /* pi is partitioning index, arr[pi] is
              now at right place */
                int pi = partition(arr, low, high);

                // Recursively sort elements before
                // partition and after partition
                sort(arr, low, pi-1);
                sort(arr, pi+1, high);
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

    public void clueSet(Fetch ff){
        this.f.clues = ff.clues;

    }
    public String clueGetter(boolean s){
        String str = "                                                                " +
                "Original Clues "+"\n ACROSS \n ------------------  \n";

        String strdown = "\n DOWN \n ------------------  \n";
        if(s )
            for(int i = 0; i < 10; ++i){
                if(f.clues.get(i).loc.equals("ACROSS")) {
                    str += f.clues.get(i).id + " : " + f.clues.get(i).text + "\n";
                }
                if(f.clues.get(i).loc.equals("DOWN")) {
                    strdown += f.clues.get(i).id + " : " + f.clues.get(i).text + "\n";
                }
            }
        if(!s)
        {
            str = "**   Wellcome to Puzzle Clue Generator    *** ";
            strdown = "***** Göktuğ Özdoğan ***** \n ***** Mehmet Eren Turanboy ***** \n ***** Derviş Mehmet Barutçu ***** \n ***** Onur Kocahan ***** \n ***** Yiğit Kutay Gülden *****";
        }
        text.setText(str + "\n");
        text.setBackground(Color.gray);
        textD.setText(strdown + "\n");
        textDate.setText(f.puzzleDate);

        textDate.setHorizontalAlignment(SwingConstants.RIGHT);

        return str;
    }

    public void update(){

        this.revalidate();
        this.repaint();

    }
}