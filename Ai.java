import java.util.*;

public class Ai {
  final long MAX_VALUE = (long)Math.pow(2, 30) - 1;
  final long MIN_VALUE = -((long)Math.pow(2, 30));
  final long PRESEARCH_DEPTH = 3;
  final long NORMAL_DEPTH = 6; //#15
  public static final long WINLOSE_DEPTH = 5; //#15
  final long PERFECT_DEPTH = 5;  // #13

  PerfectEvaluator perfectEvaluator;
  WinLoseEvaluator winLoseEvaluator;
  MidEvaluator midEvaluator;

  public Ai() {
    perfectEvaluator = new PerfectEvaluator();
    winLoseEvaluator = new WinLoseEvaluator();
    midEvaluator = new MidEvaluator();
  }

  public Move getMove(Board board) {
    long result = MIN_VALUE;

    HashSet<Disc> movables = board.getMovablePos();

    if (movables.isEmpty()) {
      board.pass();
      return null;
    }

    Object[] sortedMove = sort(board, movables, PRESEARCH_DEPTH);

    long limit;
    if (Board.MAX_TURNS - board.getTurn() <= WINLOSE_DEPTH) {
      limit = WINLOSE_DEPTH;
    } else {
      limit = NORMAL_DEPTH;
    }

    int i;
    Move selected = (Move)sortedMove[0];
    for (i=0; i < sortedMove.length; i++) {
      Move move = (Move)sortedMove[i];
      Disc disc = new Disc(move.x, move.y, board.getCurrentColor());
      board.put(disc.getPos(), disc.color);
      long eval = -(alphaBeta(board, limit - 1, MIN_VALUE, MAX_VALUE));
      board.undo();
      if (eval > result) {
        result = eval;
        selected = move;
        selected.eval = eval;
      }
    }
    return selected;
  }

  Object[] sort(Board board, HashSet<Disc> movables, long limit) {
    ArrayList<Move> al = new ArrayList<Move>(movables.size());
    Iterator<Disc> it = movables.iterator();
    while (it.hasNext()) {
      Disc disc = it.next();
      board.put(disc.getPos(), disc.color);
      long eval = -(alphaBeta(board, limit - 1, MIN_VALUE, MAX_VALUE));
      board.undo();
      al.add(new Move(disc.x, disc.y, eval));
    }
    Object[] oa = al.toArray();
    Arrays.sort(oa, new MoveComparator());
    return oa;
  }

  class MoveComparator implements Comparator<Object> {
    public int compare(Object o1, Object o2) {
      Move m1 = (Move)o1;
      Move m2 = (Move)o2;
      if (m2.eval - m1.eval > 0) { return 1;}
      if (m2.eval - m1.eval < 0) { return -1;}
      return 0;
    }
  }

  long alphaBeta(Board board, long limit, long alpha, long beta) {
    long result;

    if (limit == 0 || board.isGameOver()) {
      result = evaluate(board);
      return result;
    }

    HashSet<Disc> movables = board.getMovablePos();

    if (movables.isEmpty()) {
      board.pass();
      result = -(alphaBeta(board, limit, -(beta), -(alpha)));
      board.undo();
      return result;
    }

    Iterator<Disc> it = movables.iterator();
    while (it.hasNext()) {
      Disc disc = it.next();
      board.put(disc.getPos(), disc.color);
      long eval = -(alphaBeta(board, limit, -(beta), -(alpha)));
      board.undo();
      alpha = Math.max(alpha, eval);
      if (alpha >= beta) { return alpha;}
    }
    return alpha;
  }

  long evaluate(Board board) {
    int remain_turn = Board.MAX_TURNS - board.getTurn();
    if (remain_turn <= PERFECT_DEPTH) {
      return perfectEvaluator.evaluate(board);
    }
    if (remain_turn <= WINLOSE_DEPTH) {
      return winLoseEvaluator.evaluate(board);
    }
    return midEvaluator.evaluate(board);
  }
}
