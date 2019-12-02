
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameGui extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new GameGui();
    }

    public GameGui() {
        super("Maze, a game of wondering");
        mainContainer = getContentPane();
        shagLabel = new JLabel("", new ImageIcon("yeababyyea.jpg"), JLabel.LEFT);
        mainContainer.add(shagLabel);
        itemExit = new JMenuItem("Exit");
        itemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
        itemSaveScore = new JMenuItem("Save High Score");
        itemSaveScore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        itemHighScore = new JMenuItem("High Score");
        itemHighScore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK));
        itemEnterName = new JMenuItem("Enter Player Name");
        itemEnterName.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
        newGameItem = new JMenuItem("New Game");
        openFileItem = new JMenuItem("Open Maze File.");
        openFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
        newGameItem.setActionCommand("New Game");
        newGameItem.addActionListener(this);
        itemEnterName.setActionCommand("EnterName");
        itemEnterName.addActionListener(this);
        itemSaveScore.setActionCommand("SaveScore");
        itemSaveScore.addActionListener(this);
        itemHighScore.setActionCommand("HighScore");
        itemHighScore.addActionListener(this);
        itemExit.setActionCommand("Exit");
        itemExit.addActionListener(this);
        openFileItem.setActionCommand("Open");
        openFileItem.addActionListener(this);
        newMenu = new JMenu("File");
        newMenu.add(newGameItem);
        newMenu.add(itemEnterName);
        newMenu.add(openFileItem);
        newMenu.add(itemHighScore);
        newMenu.add(itemSaveScore);
        newMenu.add(itemExit);

        menuBar = new JMenuBar();
        menuBar.add(newMenu);
        setJMenuBar(menuBar);

        newPanel = new JPanel();
        userScore = new HighScore();
        timeKeeper = new TimeKeeper();
        pack();
        setVisible(true);
    }

    private class MyKeyHandler extends KeyAdapter {

        public void keyPressed(KeyEvent theEvent) {
            switch (theEvent.getKeyCode()) {
                case KeyEvent.VK_UP: {
                    Architect.playerMove(-1, 0, guiMatrix, fileLoad.totalOfDiamonds());
                    chargeNextLevel();
                    break;
                }
                case KeyEvent.VK_DOWN: {
                    Architect.playerMove(1, 0, guiMatrix, fileLoad.totalOfDiamonds());
                    chargeNextLevel();
                    break;
                }
                case KeyEvent.VK_LEFT: {
                    Architect.playerMove(0, -1, guiMatrix, fileLoad.totalOfDiamonds());
                    chargeNextLevel();
                    break;
                }
                case KeyEvent.VK_RIGHT: {
                    Architect.playerMove(0, 1, guiMatrix, fileLoad.totalOfDiamonds());
                    chargeNextLevel();
                    break;
                }
            }
            JLabel diamondsReminder = new JLabel("You need" + Architect.getDimondsLeft() + "" + "more!!", JLabel.CENTER);
            JPanel diamondsPanel = new JPanel();
            diamondsPanel.add(diamondsReminder);
            mainContainer.add(diamondsPanel, BorderLayout.SOUTH);
        }

        private void chargeNextLevel() {
            loadMatrixGui("updateLoad");
            if (Architect.getLevel() == true) {
                nextLevelLoad();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Exit")) {
            Handler.endGame(updateCursorAction);
        } else if (e.getActionCommand().equals("New Game")) {
            return; //maybe implent this feature later
        } else if (e.getActionCommand().equals("EnterName")) {
            Handler.insertUserName();
        } else if (e.getActionCommand().equals("HighScore")) {
            Handler.seeHighScore();
        } else if (e.getActionCommand().equals("SaveScore")) {
            Handler.addNewHighScore(userScore);
        } else if (e.getActionCommand().equals("Open")) {
            Handler.openFile();
            loadMatrixGui("newLoad");
        }
    }//end actionPerformed method

    public void loadMatrixGui(String event) {
        if (event == "newLoad") {
            remove(newPanel);//remove the previous level's game from the screen
            if (progBarPanel != null)//remove the progress bar from the gui as long as its already been created.
            {
                remove(progBarPanel);
            }
            String[][] temp = fileLoad.getGameMatrix();
            guiMatrix = new String[fileLoad.getMatrixSizeRow()][fileLoad.getMatrixSizeColumn()];
            for (int i = 0; i < guiMatrix.length; i++) {
                for (int j = 0; j < guiMatrix[i].length; j++) {
                    guiMatrix[i][j] = temp[i][j];//create a new matrix so we dont have a refrence to another objects matrix!
                }
            }//end double for loop
            timeCalc = new TimeCalculator();//create the time calculator used to determine how much time each level is given.
            timeCalc.playerTimeForLevel(fileLoad.totalOfDiamonds(), fileLoad.getMatrixSizeRow(), fileLoad.getMatrixSizeColumn());//let time calculator know the parameters of the game
            timeLeft = timeCalc.getMinutes();//get the minutes allowed for the level
            ix = timeCalc.getSeconds();//get the seconds allowed for the level;
            jx = 0;//reset the variable used for keeping time to zero since its a new level
            timely = new Timer(1000, updateCursorAction);//create a timer to update the progress bar
            timely.start();//start the timer
            progBarPanel = new JPanel();//panel for progress bar
            progressBar = new JProgressBar(0, timeCalc.getMinutes() * 100);//minutes returns a single digit, we have to multiply it for Bar.
            progressBar.setStringPainted(true);
            progBarPanel.add(progressBar);
            mainContainer.add(progBarPanel, BorderLayout.NORTH);
            newPanel = new JPanel();
            newPanel.setLayout(new GridLayout(fileLoad.getMatrixSizeRow(), fileLoad.getMatrixSizeColumn()));//set our panel for the game to the size of the matrix
            labelMatrix = new JLabel[fileLoad.getMatrixSizeRow()][fileLoad.getMatrixSizeColumn()];
            newPanel.addKeyListener(new MyKeyHandler());
        }//end if
        else if (event == "updateLoad")//every time the player moves the gui must be updated.
        {
            guiMatrix = Architect.getUpdatedMatrix();//get the new matrix to be displayed from the architect
            remove(newPanel);//remove the old game
            newPanel = new JPanel();
            newPanel.setLayout(new GridLayout(fileLoad.getMatrixSizeRow(), fileLoad.getMatrixSizeColumn()));
            newPanel.addKeyListener(new MyKeyHandler());
            newPanel.grabFocus();
        }
        for (int i = 0; i < labelMatrix.length; i++) {
            for (int j = 0; j < labelMatrix[i].length; j++) {
                labelMatrix[i][j] = mo = new mazeObject(guiMatrix[i][j]);//add our maze images into the gui
            }
        }//end double for loop
        mainContainer.add(newPanel);
        remove(shagLabel);//remove the constructors initial background
        System.gc();//force java to clean up memory use.
        pack();
        setVisible(true);
        newPanel.grabFocus();
    }//end loadMatrixGui method

    public class mazeObject extends JLabel//inner class for each maze object, aka wall, player etc
    {

        private JLabel imageLabel;

        public mazeObject(String fileName) {
            fileName += ".png";
            JLabel fancyLabel;
            fancyLabel = new JLabel("", new ImageIcon(fileName), JLabel.LEFT);
            newPanel.add(fancyLabel);
        }
    }//end inner class

    public void nextLevelLoad() {
        levelNum += 1;
        timeKeeper.TimeKeeper(timeLeft, ix);//The TimeKeeper object keeps a running tab of the total time the player has used.(for high score)
        timely.stop();//dont count while we are loading the next level.
        Architect = new TheArchitect();//flush everything from TheArchitect so we dont get goffee results
        catFileName += 01;//the next file to be loaded (number)
        String fileName = "level" + catFileName + ".maz";
        System.gc();
        fileLoad.loadFile(fileName);//load the file we need
        guiMatrix = fileLoad.getGameMatrix();//get the new matrix from the fileloader for the next level.
        Architect.setExit(fileLoad.ExitXCord(), fileLoad.ExitYCord());
        loadMatrixGui("newLoad");
    }

    Action updateCursorAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) throws SlowAssPlayer //this inner class generates an exeption if the player takes to long to finish a level
        {
            ix -= 1;
            jx += 1;
            if (ix < 0) {
                ix = 60;
                timeLeft -= 1;
            }
            if (timeLeft == 0 && ix == 0) {
                timely.stop();
                JLabel yousuckLabel = new JLabel("", new ImageIcon("yousuck.jpg"), JLabel.LEFT);
                mainContainer.add(yousuckLabel);
                remove(newPanel);
                remove(progBarPanel);
                pack();
                setVisible(true);
                timely.stop();
                catFileName -= 01;
                if (catFileName < 01) {
                    throw new SlowAssPlayer("Slow ass took to long.");
                } else {
                    loadMatrixGui("newLoad");
                }
            }//end first if
            progressBar.setValue(jx);
            progressBar.setString(timeLeft + ":" + ix);
        }//end actionPerformed
    };//end class

    private class SlowAssPlayer extends RuntimeException {

        public SlowAssPlayer(String event) {
            //the game is over, here we must tell our high score method to recond the details.
            userScore.addHighScore(playerName, timeKeeper.getMinutes(), timeKeeper.getSeconds(), levelNum);
            JFrame frame = new JFrame("Warning");
            JOptionPane.showMessageDialog(frame, "You Stupid Ass, Did you eat to much for dinner?  Move Faster!");//the entire game has ended.
        }
    }//end class

    static HighScore userScore;
    private int catFileName = 01;
    private Container mainContainer;
    private static FileLoader fileLoad = new FileLoader();
    //create menu items
    private JMenuBar menuBar;
    private JMenu newMenu;
    private JMenuItem itemExit;
    private JMenuItem newGameItem;
    private JMenuItem openFileItem;
    private JMenuItem itemEnterName;
    private JMenuItem itemHighScore;
    private JMenuItem itemSaveScore;
    //end create menu items
    private JLabel shagLabel;
    private int ix;
    private int jx;
    private int timeLeft;
    private JPanel progBarPanel;
    private JLabel[][] labelMatrix;
    private TimeCalculator timeCalc;
    private JProgressBar progressBar;
    private mazeObject mo;
    private JPanel newPanel;
    private static TheArchitect Architect = new TheArchitect();
    private String[][] guiMatrix;
    private Timer timely;
    private static TimeKeeper timeKeeper;
    private static String playerName;
    private static int levelNum = 1;

    public static int getLevelNum() {
        return levelNum;
    }

    public static String getPlayerName() {
        return playerName;
    }

    public static HighScore getUserScore() {
        return userScore;
    }

    public static FileLoader getFileLoader() {
        return fileLoad;
    }

    public static TheArchitect getArchitect() {
        return Architect;
    }

    public static TimeKeeper getTimeKeep() {
        return timeKeeper;
    }

    public static void setPlayerName(String name) {
        playerName = name;
    }
}
