public class WinLoseEvaluator {
  
  public long evaluate(Board board) {
    final long WIN = 1;
    final long LOSE = -1;
    final long DRAW = 0;

    long diff = board.getAlternate(board.getCurrentColor()) *
         (board.getCountDiscs(board.BLACK) - board.getCountDiscs(board.WHITE));

    if (diff > 0) { return WIN; }
    if (diff < 0) { return LOSE; }

    return DRAW;
  }

}
