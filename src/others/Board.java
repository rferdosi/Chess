package others;

import themes.BoardTheme;

public class Board {
    private Cell[][] cells = new Cell[8][8];
    private BoardTheme theme;

    public Cell[][] getCells() {
        return cells;
    }

    public BoardTheme getTheme() {
        return theme;
    }

    public void setTheme(BoardTheme theme) {
        this.theme = theme;
    }

    {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j] = new Cell();
                if ((i + j) % 2 == 0)
                    cells[i][j].setSide(Side.White);
                else
                    cells[i][j].setSide(Side.Black);
            }
        }
    }
}
