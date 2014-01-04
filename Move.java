public class Move extends Point {
  public long eval;
  
  public Move(int x, int y, long eval) {
    super(x, y);
    this.eval = eval;
  }

}
