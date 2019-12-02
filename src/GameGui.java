
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameGui extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new GameGui();
    }

    public GameGui() {
        super("Maze, a dumb game...");
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
            JLabel diamondsReminder = new JLabel("You need " + Architect.getDimondsLeft() + " more diamonds!! U got this!", JLabel.CENTER);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Exit":
                Handler.endGame(updateCursorAction);
                break;
            case "New Game":
                return;
            case "EnterName":
                Handler.insertUserName();
                break;
            case "HighScore":
                Handler.seeHighScore();
                break;
            case "SaveScore":
                Handler.addNewHighScore(userScore);
                break;
            case "Open":
                Handler.openFile();
                loadMatrixGui("newLoad");
                break;
            default:
                break;
        }
    }

    public void loadMatrixGui(String event) {
        if (event == "newLoad") {
            remove(newPanel);
            if (progBarPanel != null) {
                remove(progBarPanel);
            }
            String[][] temp = fileLoad.getGameMatrix();
            guiMatrix = new String[fileLoad.getMatrixSizeRow()][fileLoad.getMatrixSizeColumn()];
            for (int i = 0; i < guiMatrix.length; i++) {
                for (int j = 0; j < guiMatrix[i].length; j++) {
                    guiMatrix[i][j] = temp[i][j];
                }
            }
            timeCalc = new TimeCalculator();
            timeCalc.playerTimeForLevel(fileLoad.totalOfDiamonds(), fileLoad.getMatrixSizeRow(), fileLoad.getMatrixSizeColumn());
            timeLeft = timeCalc.getMinutes();
            ix = timeCalc.getSeconds();
            jx = 0;
            timely = new Timer(1000, updateCursorAction);
            timely.start();
            progBarPanel = new JPanel();
            progressBar = new JProgressBar(0, timeCalc.getMinutes() * 100);
            progressBar.setStringPainted(true);
            progBarPanel.add(progressBar);
            mainContainer.add(progBarPanel, BorderLayout.NORTH);
            newPanel = new JPanel();
            newPanel.setLayout(new GridLayout(fileLoad.getMatrixSizeRow(), fileLoad.getMatrixSizeColumn()));
            labelMatrix = new JLabel[fileLoad.getMatrixSizeRow()][fileLoad.getMatrixSizeColumn()];
            newPanel.addKeyListener(new MyKeyHandler());
        } else if (event == "updateLoad") {
            guiMatrix = Architect.getUpdatedMatrix();
            remove(newPanel);
            newPanel = new JPanel();
            newPanel.setLayout(new GridLayout(fileLoad.getMatrixSizeRow(), fileLoad.getMatrixSizeColumn()));
            newPanel.addKeyListener(new MyKeyHandler());
            newPanel.grabFocus();
        }
        for (int i = 0; i < labelMatrix.length; i++) {
            for (int j = 0; j < labelMatrix[i].length; j++) {
                labelMatrix[i][j] = mo = new mazeObject(guiMatrix[i][j]);
            }
        }
        mainContainer.add(newPanel);
        remove(shagLabel);
        System.gc();
        pack();
        setVisible(true);
        newPanel.grabFocus();
    }

    public class mazeObject extends JLabel {

        private JLabel imageLabel;

        public mazeObject(String fileName) {
            fileName += ".png";
            JLabel fancyLabel;
            fancyLabel = new JLabel("", new ImageIcon(fileName), JLabel.LEFT);
            newPanel.add(fancyLabel);
        }
    }

    public void nextLevelLoad() {
        levelNum += 1;
        timeKeeper.TimeKeeper(timeLeft, ix);
        timely.stop();
        Architect = new TheArchitect();
        catFileName += 01;
        String fileName = "level" + catFileName + ".maz";
        System.gc();
        fileLoad.loadFile(fileName);
        guiMatrix = fileLoad.getGameMatrix();
        Architect.setExit(fileLoad.ExitXCord(), fileLoad.ExitYCord());
        loadMatrixGui("newLoad");
    }

    Action updateCursorAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) throws SlowAssPlayer {
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
            }
            progressBar.setValue(jx);
            progressBar.setString(timeLeft + ":" + ix);
        }
    };

    private class SlowAssPlayer extends RuntimeException {

        public SlowAssPlayer(String event) {
            userScore.addHighScore(playerName, timeKeeper.getMinutes(), timeKeeper.getSeconds(), levelNum);
            JFrame frame = new JFrame("Warning");
            JOptionPane.showMessageDialog(frame, "You made it!, dumbass uwu");
        }
    }

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
