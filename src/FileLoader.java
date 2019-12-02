
import java.io.*;
import javax.swing.*;

public class FileLoader {

    public void loadFile(String fileName) {
        try {
            BufferedReader storageLines = new BufferedReader(new FileReader(fileName));
            String line;
            int lineNum = 0;
            while ((line = storageLines.readLine()) != null) {
                MatrixLoader(line, lineNum);
                lineNum++;
            }
        } catch (IOException e) {
            JFrame frame = new JFrame("Alert");
            JOptionPane.showMessageDialog(frame, "Ooops IOException error, i did it again!" + e.getMessage());
        }
    }

    public void MatrixLoader(String fileTextLine, int lineNum) throws gameFileError {
        int blankSpaces = 0;
        char textVar;
        if (lineNum == 0) {//if its the first line of the Matrix
            for (int i = 0; i < fileTextLine.length(); i++) {
                if (fileTextLine.charAt(i) == ' ') {
                    blankSpaces += 1;
                }
            }
            int locationOfSpace = fileTextLine.indexOf(" ");
            String fileColumn = fileTextLine.substring(0, locationOfSpace);
            String fileRow = fileTextLine.substring(locationOfSpace + blankSpaces, fileTextLine.length());
            column = Integer.parseInt(fileColumn);
            row = Integer.parseInt(fileRow);
            GameMatrix = new String[row][column];
        } else {
            for (int i = 0; i < fileTextLine.length(); i++) {
                textVar = fileTextLine.charAt(i);
                if (textVar == '.') {
                    textVar = 'N';
                }
                String textVar1 = "" + textVar;
                if (textVar == 'E') {

                    exitXCord = lineNum - 1;
                    exitYCord = i;
                    textVar1 = "" + textVar;
                }
                GameMatrix[lineNum - 1][i] = textVar1;
            }
        }

    }

    public String[][] getGameMatrix() {
        int exitCount = 0;
        int i1 = 0;
        int j1 = 0;
        int playerCount = 0;
        for (int i = 0; i < GameMatrix.length; i++) {
            for (int j = 0; j < GameMatrix[i].length; j++) {
                if (GameMatrix[i][j].equals("P")) {
                    playerCount += 1;

                } else if (GameMatrix[i][j].equals("E")) {
                    exitCount += 1;
                    i1 = i;
                    j1 = j;
                }
                System.out.println(playerCount + "playerCount");
                System.out.println(exitCount + "playerCount");
            }
        }
        if (playerCount > 1 || exitCount > 1) {
            throw new gameFileError();
        } else {
            GameMatrix[i1][j1] = "W";
        }

        return GameMatrix;
    }

    public int getMatrixSizeColumn() {
        return column;
    }

    public int getMatrixSizeRow() {
        return row;

    }

    public int ExitXCord() {
        return exitXCord;
    }

    public int ExitYCord() {
        return exitYCord;
    }

    public int totalOfDiamonds() {
        int totalDimonds = 0;
        for (int i = 0; i < GameMatrix.length; i++) {
            for (int j = 0; j < GameMatrix[i].length; j++) {
                if (GameMatrix[i][j].equals("D") || GameMatrix[i][j].equals("H")) {
                    totalDimonds += 1;
                }
            }
        }
        return totalDimonds;
    }

    private class gameFileError extends RuntimeException {

        public gameFileError() {
            JFrame frame = new JFrame("Alert");
            JOptionPane.showMessageDialog(frame, "Your maze file is damaged, there are maybe 2 players or 2 exits");
        }
    }

    private int exitXCord = 0;
    private int exitYCord = 0;
    ;
private String[][] GameMatrix;
    private int column;
    private int row;

}
