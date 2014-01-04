public class PerfectEvaluator {
  
  public long evaluate(Board board) {
    long result = board.getAlternate(board.getCurrentColor()) * 
         (board.getCountDiscs(board.BLACK) - board.getCountDiscs(board.WHITE));
    return result;
  }

}
