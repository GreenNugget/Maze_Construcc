
import java.io.*;

public class HighScore {

    public void addHighScore(String name, int min, int sec, int level) {
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream("scores.txt", true));
            out.println("PlayerName: " + name
                    + "\nTotal Time for Levels:" + min + ":" + sec + "(Minutes:Seconds)"
                    + "\nLevel Reached:*" + level + "\n\n-----------------------------------------\n");
            out.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
