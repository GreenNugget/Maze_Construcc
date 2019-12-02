
import java.io.*;

public class HighScore {

    public void addHighScore(String name, int min, int sec, int level) {
        try {
            String outData = "Player: " + name + " Total Time for Levels: " + min + ":" + sec
                    + "(Minutes:Seconds)" + "\nLevel Reached:*" + level + "\n";
            PrintWriter out = new PrintWriter(new FileOutputStream("scores.txt", true));
            out.println(outData);
            out.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }
}
