import java.util.*;
import java.util.concurrent.*;

public class Ai {
  final long MAX_VALUE = (long)Math.pow(2, 30) - 1;
  public static final long MIN_VALUE = -((long)Math.pow(2, 30));
  final long PRESEARCH_DEPTH = 3;
  final long SORT_DEPTH = 8;
  final long NORMAL_DEPTH = 6; //#15
  public static final long WINLOSE_DEPTH = 15; //#15
  final long PERFECT_DEPTH = 15;  // #13

  PerfectEvaluator perfectEvaluator;
  WinLoseEvaluator winLoseEvaluator;
  MidEvaluator midEvaluator;

  public Ai() {
    perfectEvaluator = new PerfectEvaluator();
    winLoseEvaluator = new WinLoseEvaluator();
    midEvaluator = new MidEvaluator();
  }

  public CopyOnWriteArraySet<Move> getMove(Board board) {
    long result = MIN_VALUE;

    CopyOnWriteArraySet<Disc> movables = board.getMovablePos();

    if (movables.isEmpty()) {
      board.pass();
      return null;
    }

    long limit;
    if (Board.MAX_TURNS - board.getTurn() <= WINLOSE_DEPTH) {
      limit = WINLOSE_DEPTH;
    } else {
      limit = NORMAL_DEPTH;
    }

    Object[] sortedMove = sort(board, movables, PRESEARCH_DEPTH);

    CopyOnWriteArraySet<Move> selected = new CopyOnWriteArraySet<Move>();
    selected.add((Move)sortedMove[0]);
    int i;
    System.out.print("getMove t=" + board.getTurn() + "c=" + board.getCurrentColor() + "l=" + sortedMove.length);
    for (i=0; i < sortedMove.length; i++) {
      Move move = (Move)sortedMove[i];
      System.out.print("m=" + (move.x * 10 + move.y) + "e=" + move.eval);
      Disc disc = new Disc(move.x, move.y, board.getCurrentColor());
      board.put(disc.getPos(), disc.color);
      long eval = -(alphaBeta(board, limit - 1, MIN_VALUE, MAX_VALUE));
      board.undo();
      System.out.print("e=" + eval);
      if (eval > result) {
        result = eval;
        move.eval = eval;
        selected.clear();
        selected.add(move);
      } else if (eval == result) {
        move.eval = eval;
        selected.add(move);
      }
    }
    return selected;
  }

  Object[] sort(Board board, CopyOnWriteArraySet<Disc> movables, long limit) {
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
      if (m2.eval > m1.eval) { return 1;}
      if (m2.eval < m1.eval) { return -1;}
      return 0;
    }
  }

  long alphaBeta(Board board, long limit, long alpha, long beta) {
    long result;

    if (limit == 0 || board.isGameOver()) {
      result = evaluate(board);
      return result;
    }

    CopyOnWriteArraySet<Disc> movables = board.getMovablePos();

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
    long result;
    int remain_turn = Board.MAX_TURNS - board.getTurn();
    if (remain_turn <= PERFECT_DEPTH) {
      result = perfectEvaluator.evaluate(board);
      return result;
    }
    if (remain_turn <= WINLOSE_DEPTH) {
      return winLoseEvaluator.evaluate(board);
    }
    return midEvaluator.evaluate(board);
  }
}
