import java.util.*;
import java.io.*;

public class Board {
  final int BOARD_SIZE = 8;
  final int WALL = -1;
  public static final int EMPTY = 0;
  final int BLACK = 1;
  final int WHITE = 2;

  int[] pattern;
  int[] diskNum;

  public Board () {
    pattern = new int[Pattern.ID_LAST];
    diskNum = new int[3];
  }

  public int getPattern(int id) {
    return pattern[id]; // return Pattern Number
  }

  public int getCountDisks(int color) {
    return diskNum[color];
  }
}
