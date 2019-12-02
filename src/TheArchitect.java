
import javax.swing.*;

public class TheArchitect extends JFrame {

    public void setExit(int x, int y) {
        WallXCord = x;
        WallYCord = y;
    }

    public void showWall() {
        updatedMatrix[WallXCord][WallYCord] = "E";
    }

    public void playerMove(int xScale, int yScale, String[][] currentMatrix, int totalDimonds) throws StupidAssMove {
        globalTotalDimonds = totalDimonds;
        nextLevel(false);
        getPlayerPosition(currentMatrix);
        CoordinatesSystem coordinates = new CoordinatesSystem(currentMatrix, xScale, yScale, playerCordX, playerCordY);
        if (coordinates.isHiddenDimond() || coordinates.isDiamond()) {
            collected += 1;
        } else if (coordinates.isMovableWall()) {
            coordinates.moveMovableWall();
        } else if (coordinates.isExit()) {
            nextLevel(true);
        } else if (!coordinates.isNothing()) {
            throw new StupidAssMove("Ass Hole hit wall!");
        }
        coordinates.movePlayer();

        if (collected == totalDimonds) {
            showWall();
        }

        updatedMatrix = coordinates.getMatrix();
    }

    public void nextLevel(boolean goOrNot) {
        level = goOrNot;
    }

    public boolean getLevel() {
        return level;
    }

    public int getDimondsLeft() {
        return globalTotalDimonds - collected;
    }

    public String[][] getUpdatedMatrix() {
        return updatedMatrix;
    }

    private class StupidAssMove extends RuntimeException {

        public StupidAssMove(String event) {
            JFrame frame = new JFrame("Warning");
            JOptionPane.showMessageDialog(frame, "Te chocaste contra algo, give it another try :c");
        }
    }

    private void getPlayerPosition(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j].equals("P")) {
                    playerCordX = i;
                    playerCordY = j;
                    break;
                }
            }
        }
    }

    String[][] updatedMatrix;
    int WallXCord;
    int WallYCord;
    int collected = 0;
    boolean level;
    int globalTotalDimonds = 0;
    private int playerCordX;
    private int playerCordY;
}
