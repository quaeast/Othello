import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class BlackAndWhite {
    // 0 empty
    // 1 white
    // -1 black
    int[][] board = new int[8][8];


    // black first
    public void reset() {
        board[3][3] = 1;
        board[4][4] = 1;
        board[3][4] = -1;
        board[4][3] = -1;
    }

    public void showBoard() {
        for (int i=0; i<9;i++){
            System.out.printf( "%2d ", i-1);
        }
        System.out.println();
        for (int i = 0; i < 8; i++) {
            System.out.printf( "%2d ", i);
            for (int j = 0; j < 8; j++) {
                System.out.printf( "%2d", board[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    // get the position list of black/white token
    public List<int[]> getPositionsOfSpecifiedColor(int color) {
        List<int[]> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == color) {
                    int[] position = {i, j};
                    result.add(position);
                }
            }
        }
        return result;
    }

    // original method of is valid
    private boolean isValid0(int color, int x, int y, int toX, int toY) {
        boolean hasReverseColor = false;
        for (int i = x + toX, j = y + toY; i < 8 && i >= 0 && j < 8 && j >= 0; i += toX, j += toY) {
            if (board[i][j] == 0) {
                return false;
            }
            if (board[i][j] == -color) {
                hasReverseColor = true;
            }
            if (board[i][j] == color) {
                return hasReverseColor;
            }
        }
        return false;
    }

    // determine whether a specify position is valid to place the token
    public boolean isValid(int color, int x, int y) {
        List<int[]> positionList = getPositionsOfSpecifiedColor(color);
        if (board[x][y] != 0) {
            return false;
        }
        boolean isValidResult = isValid0(color, x, y, 1, 0) ||
                isValid0(color, x, y, -1, 0) ||
                isValid0(color, x, y, 0, 1) ||
                isValid0(color, x, y, 0, -1) ||
                isValid0(color, x, y, 1, 1) ||
                isValid0(color, x, y, -1, -1) ||
                isValid0(color, x, y, -1, 1) ||
                isValid0(color, x, y, 1, -1);
        return isValidResult;
    }

    // get the valid position list
    public List<int[]> getValidPositions(int color) {
        List<int[]> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValid(color, i, j)) {
                    int[] position = {i, j};
                    result.add(position);
                }
            }
        }
        return result;
    }

    // whether there is of valid position in the whole board
    public boolean hasPlaceToPut(int color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValid(color, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int[][] copyBoard() {
        int[][] newBoard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, 8);
        }
        return newBoard;
    }

    // original function of the renew board
    private void renewBoard0(int color, int x, int y, int toX, int toY) {
        for (int i = x + toX, j = y + toY; i < 8 && i >= 0 && j < 8 && j >= 0; i += toX, j += toY) {
            if (board[i][j] == 0) {
                break;
            }
            if (board[i][j] == color) {
                for (int iv = i, jv = j; iv != x || jv != y; iv -= toX, jv -= toY) {
                    board[iv][jv] = color;
                }
                break;
            }
        }
    }

    // place a token and renew the board
    // return the original board if the the position is valid
    public int[][] renewBoard(int color, int x, int y) {
        int[][] originalBoard = copyBoard();
        if (!isValid(color, x, y)) {
            return null;
        }
        board[x][y] = color;
        renewBoard0(color, x, y, 1, 0);
        renewBoard0(color, x, y, -1, 0);
        renewBoard0(color, x, y, 0, 1);
        renewBoard0(color, x, y, 0, -1);
        renewBoard0(color, x, y, 1, 1);
        renewBoard0(color, x, y, -1, -1);
        renewBoard0(color, x, y, -1, 1);
        renewBoard0(color, x, y, 1, -1);
        return originalBoard;
    }

    // recursive judge if this step can lead to final success
    // time complexity is to high, can't be run
    public boolean canTrump(int color, int x, int y) {
        if (!isValid(color, x, y)) {
            return false;
        }
        renewBoard(color, x, y);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (canTrump(-color, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    // ai: find where to put the token
    // time complexity is to high, can't be run
    // return {-1, -1}: computer can't win
    public int[] getNextStep(int color, int x, int y) {
        int[] result = {-1, -1};
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (canTrump(color, i, j)) {
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HashMap<Integer, String> colorMapping = new HashMap<>();
        colorMapping.put(1, "white");
        colorMapping.put(-1, "black");
        BlackAndWhite blackAndWhite = new BlackAndWhite();
        blackAndWhite.reset();
        int color = -1;

        while (blackAndWhite.hasPlaceToPut(color)) {
            blackAndWhite.showBoard();
            System.out.println(colorMapping.get(color) + "'s turn: ");
            int[] position = {scanner.nextInt(), scanner.nextInt()};
            if (blackAndWhite.renewBoard(color, position[0], position[1]) != null) {
                color = -color;
            }
        }
        System.out.println(colorMapping.get(color) + " lose");
    }
}

