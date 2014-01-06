public class Move extends Point {
  public long eval;
  public int flipFlg;
  
  public Move(int x, int y, long eval) {
    super(x, y);
    this.eval = eval;
    this.flipFlg = 0;
  }

  public Move(int x, int y, long eval, int flipFlg) {
    super(x, y);
    this.eval = eval;
    this.flipFlg = flipFlg;
  }

}
