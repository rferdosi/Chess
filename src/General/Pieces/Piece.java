package General.Pieces;

import General.Board.Cell;
import General.Board.Move;
import General.Board.Side;
import javafx.scene.image.ImageView;

abstract public class Piece {
    protected ImageView imageView;
    protected Cell cell;
    protected Side side;
    protected int row;

    public void setRowAndColumn() {
        row = this.cell.getRow();
        column = this.cell.getColumn();
    }

    protected int column;

    public Piece(Side side) {
        this.side = side;
    }

    public void move(Cell destination) {
        if (!destination.isEmpty() && destination.getPiece().side != this.side) {
            this.cell.getBoard().getPieces().remove(destination.getPiece());
        }
        /*this.cell.setPiece(null);
       // this.cell.setGraphic(null);
        this.cell = destination;
        this.cell.setPiece(this);
      //  this.cell.setGraphic(this.getImageView());
        this.row = destination.getRow();
        this.column = destination.getColumn();*/
        Move move = new Move(this, cell, destination);
        cell.getBoard().getGame().getMoves().add(move);
        cell.setPiece(null);
        cell = destination;
        cell.setPiece(this);
        row = destination.getRow();
        column = destination.getColumn();
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }


    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Side getSide() {
        return side;
    }

    public abstract void setLabels();

    @Override
    public String toString() {
        return side.toString() + " ";
    }
}
