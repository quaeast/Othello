import java.util.ArrayList;
import java.util.List;

public class BlackAndWhite {
    // 0 empty
    // 1 white
    // -1 black
    int[][] board = new int[8][8];

    // black first
    public void reset() {
        board[3][3] = 1;
        board[4][4] = 1;
        board[3][4] = 2;
        board[4][3] = 2;
    }


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

    public boolean isValid(int color, int x, int y) {
        List<int[]> positionList = getPositionsOfSpecifiedColor(color);
        if (board[x][y] != 0) {
            return false;
        }
        for (int[] position : positionList) {
            if (position[0] == x || position[1] == y
                    || position[0] - position[1] == x - y
                    || position[0] + position[1] == x + y) {
                return true;
            }
        }
        return false;
    }

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

//    private void renewBoardSpecificDirection(int color, int x, int y, int left){}

    // 下一步新棋，更新棋盘，这一步还没有把新子写入
    public void renewBoard(int color, int x, int y) {
        // right
        for (int i = x + 1; i < 8; i++) {
            if (board[i][y] == 0) {
                break;
            }
            if (board[i][y] == color) {
                for (int iv = i; iv > x; iv--) {
                    board[iv][y] = color;
                }
                break;
            }
        }
        // left
        for (int i = x - 1; i >= 0; i--) {
            if (board[i][y] == 0) {
                break;
            }
            if (board[i][y] == color) {
                for (int iv = i; iv < x; iv++) {
                    board[iv][y] = color;
                }
                break;
            }
        }
        // down
        for (int j = y + 1; j < 8; j++) {
            if (board[x][j] == 0) {
                break;
            }
            if (board[x][j] == color) {
                for (int jv = j; jv > y; j--) {
                    board[x][jv] = color;
                }
                break;
            }
        }
        // up....
    }

    public boolean canTrump(int color, int x, int y) {
        if (!isValid(color, x, y)) {
            return false;
        }
        renewBoard(color, x, y);
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (canTrump(-color, i, j)){
                    return true;
                }
            }
        }
        return false;
    }

    //{-1, -1}: 电脑输了
    public int[] getNextStep(int color, int x, int y){
        int[]result = {-1, -1};
        for (int i=0; i<8;i++){
            for (int j=0;j<8; j++){
                if (canTrump(color, i, j)){
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        BlackAndWhite blackAndWhite = new BlackAndWhite();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.println(blackAndWhite.board[i][j]);
            }
        }
        int[] a = new int[10];
    }
}

