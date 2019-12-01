public class CoordinatesSystem {
    private final String[][] matrix;
    private final int xScale;
    private final int yScale;
    private final int playerCordX;
    private final int playerCordY;

    public CoordinatesSystem(String[][] matrix, int xScale, int yScale, int playerX, int playerY) {
        this.matrix = matrix;
        this.xScale = xScale;
        this.yScale = yScale;
        this.playerCordX = playerX;
        this.playerCordY = playerY;
    }

    public boolean isHiddenDimond() {
        return matrix[playerCordX + xScale][playerCordY + yScale].equals("H");
    }

    public boolean isDiamond() {
        return matrix[playerCordX + xScale][playerCordY + yScale].equals("D");
    }

    public void movePlayer() {
        matrix[playerCordX][playerCordY] = "N";
        matrix[playerCordX + xScale][playerCordY + yScale] = "P";
    }

    public boolean isMovableWall() {
        return matrix[playerCordX + xScale][playerCordY + yScale].equals("M")
                && matrix[playerCordX + (xScale * 2)][playerCordY + (yScale * 2)].equals("N");
    }

    public void moveMovableWall() {
        matrix[playerCordX + (xScale * 2)][playerCordY + (yScale * 2)] = "M";
    }

    public boolean isNothing() {
        return matrix[playerCordX + xScale][playerCordY + yScale].equals("N");
    }

    public boolean isExit() {
        return matrix[playerCordX + xScale][playerCordY + yScale].equals("E");
    }

    public String[][] getMatrix() {
        return matrix;
    }
}
