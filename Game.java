class Game {
  public static void main(String[] args) {
    Player[] player = new Player[3];
    player[Board.BLACK] = new HumanPlayer();
    player[Board.WHITE] = new HumanPlayer();
    Board bd = new Board();
    Console cs = new Console();
    cs.viewBoard(bd);
    while(true) {
      player[bd.getCurrentColor()].onTurn(bd);
      cs.viewBoard(bd);
      if (bd.isGameOver()) {
        System.out.println("Game Over");
        break;
      }
    }
  }
}
