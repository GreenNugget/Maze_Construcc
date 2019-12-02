
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author Naomi
 */
public class Handler {

    public static void endGame(Action updateCursorAction) {
        new Timer(1000, updateCursorAction).stop();
        System.exit(0);
    }

    public static void insertUserName() {
        JOptionPane optionPane = new JOptionPane();
        String name = optionPane.showInputDialog("Please Enter your Name..de quién más tho...");
        GameGui.setPlayerName(name);
    }

    public static void seeHighScore() {
        ScoreGui sg = new ScoreGui();
        sg.ScoreGui();
    }

    public static void addNewHighScore(HighScore userScore) {
        GameGui.userScore.addHighScore(GameGui.getPlayerName(), GameGui.getTimeKeep().getMinutes(),
                GameGui.getTimeKeep().getSeconds(), GameGui.getLevelNum());
    }

    public static void openFile() {
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(chooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            GameGui.getFileLoader().loadFile(chooser.getSelectedFile().getName());
            GameGui.getArchitect().setExit(GameGui.getFileLoader().ExitXCord(), GameGui.getFileLoader().ExitYCord());
        }
    }

    public void newGame() {

    }

}
