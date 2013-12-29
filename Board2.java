import java.util.*;

public class Board {
  final int MAX_TURNS = 0;

  public static final int BOARD_SIZE = 8;

  //colors of discs
  final int WALL = -1;
  public static final int EMPTY = 0;
  public static final int BLACK = 1;
  public static final int WHITE = 2;

  //directions
  final int UP_LEFT = -BOARD_SIZE -2;
  final int UP = -BOARD_SIZE -1;
  final int UP_RIGHT = -BOARD_SIZE;
  final int LEFT = -1;
  final int RIGHT = 1;
  final int DOWN_LEFT = BOARD_SIZE;
  final int DOWN = BOARD_SIZE + 1;
  final int DOWN_RIGHT = BOARD_SIZE + 2;

  final int NUM_DISK = ((BOARD_SIZE+1)*(BOARD_SIZE+2)+1);
  final int NUM_PATTERN_DIFF = 6;

  int turn;
  int current_color;

  int[] disk;     //Current color of disks by position of board
  int[] diskNum;  //Current number of disks by color
  int[] pattern;  //Current variation no.(index) by all patterns
  
  //difference of index when the color of disk has changed
  int[][] patternId;  //pattern no. by position (by occurs)
  int[][] patternDiff;  //pow(3, column in pattern) by position (by occurs)

  ArrayDeque<LinkedList<Integer>> updateLog; //Stack of updated discs by turn

  HashMap<Point, Integer>[] movableDir;

  public Board () {
    disk = new int[NUM_DISK];
    diskNum = new int[3];
    pattern = new int[Pattern.ID_LAST];
    InitializePatternDiff();
    Clear();
  }

  void InitializePatternDiff() {
    patternId = new int[NUM_DISK][NUM_PATTERN_DIFF];
    patternDiff = new int[NUM_DISK][NUM_PATTERN_DIFF];

    int i, j, k;
    for (i=0; i < NUM_DISK; i++) {
      for (j=0; j < NUM_PATTERN_DIFF; j++) {
        patternId[i][j] = 0;
        patternDiff[i][j] = 0;
      }
    }

    for (i = 0; Pattern.list[i][0] >= 0; i++) {
      int n = 1;
      for (j = 0; Pattern.list[i][j] >= 0; j++) {
        for (k=0; patternDiff[Pattern.list[i][j]][k] != 0; k++){}
        patternId[Pattern.list[i][j]][k] = i;
        patternDiff[Pattern.list[i][j]][k] = n;
        n *= 3;
      }
    }

    if (Debug.state) {
      for (i=0; i < NUM_DISK; i++) {
        for (j=0; j < NUM_PATTERN_DIFF && patternDiff[i][j] > 0; j++) {
          Debug.println("Diff(" + i + "," + j + ")=" + patternId[i][j] + "," + patternDiff[i][j]); 
        }
      }
    }
  }

  void Clear() {
    int i, j;
    Disc disc;
    for (i=0; i < NUM_DISK; i++) {
      disk[i] = WALL;   //fill all boards with WALL
    }
    for (j=0; j < BOARD_SIZE; j++) {
      for (i=0; i < BOARD_SIZE; i++) {
        disc = new Disc(i+1, j+1, 0);
        disk[disc.getPos()] = EMPTY;  //fill puttable position with EMPTY
      }
    }

    putDisc(new Disc(4, 4, WHITE));
    putDisc(new Disc(5, 5, WHITE));
    putDisc(new Disc(4, 5, BLACK));
    putDisc(new Disc(5, 4, BLACK));

    turn = 0;
    current_color = BLACK;
    updateLog = new ArrayDeque<LinkedList<Integer>>(MAX_TURNS);
    setMovableDir();

    //if debug -> print image of board
    if (Debug.state) {
      String row;
      for (j=0; j < BOARD_SIZE+2; j++) {
        row = "";
        for (i=0; i < BOARD_SIZE+1; i++) {
          disc = new Disc(i, j, 0);
          switch (disk[disc.getPos()]) {
          case WALL: row += "#"; break;
          case BLACK: row += "x"; break;
          case WHITE: row += "o"; break;
          case EMPTY: row += "."; break;
          }
        }
        Debug.println(row);
      }
      disc = new Disc(i, j-1, 0);
      Debug.println("" + disc.x + "," + disc.y + disk[disc.getPos()]);
    }
  }

  void setMovableDir() {
  }

  public int getPattern(int id) {
    return pattern[id]; // return Pattern Number
  }

  public int getCountDisks(int color) {
    return diskNum[color];
  }

  public int getTurn() {
    return turn;
  }

  public int getCurrentColor() {
    return current_color;
  }

  public HashMap<Point, Integer> getMovableDir() {
    return movableDir[turn];  //all movable pos info at current turn
  }

  public void putDisc(Disc disc) {
    int pos = disc.getPos();
    disk[pos] = disc.color;
    diskNum[disc.color]++;
    diskNum[EMPTY]--;
    changePattern(pos, disc.color);
  }

  public void flip(Disc disc) {
    int flipCount = 0;  //count of flipped discs
    LinkedList<Integer> update = new LinkedList<Integer>();
    //flip discs to all directions
    if (flipCount > 0) {
      update.add(disc.getPos());  //save position of putted disc
      update.add(disc.color);     //save color of putted disc
      update.add(flipCount);      //save count of flipped discs
    }
    updateLog.add(update);
  }

  //count flipable discs in all lines 
  public int getCountFlip(Disc disc) {
    int altColor = getAltColor(disc.color);
    int inPos = disc.getPos();
    int result = 0;
    if (disk[inPos] == EMPTY) {return 0;} //cannot put disc at this position
    result += getCountFlipLine(inPos, disc.color, altColor, UP_LEFT);
    result += getCountFlipLine(inPos, disc.color, altColor, UP);
    result += getCountFlipLine(inPos, disc.color, altColor, UP_RIGHT);
    result += getCountFlipLine(inPos, disc.color, altColor, LEFT);
    result += getCountFlipLine(inPos, disc.color, altColor, RIGHT);
    result += getCountFlipLine(inPos, disc.color, altColor, DOWN_LEFT);
    result += getCountFlipLine(inPos, disc.color, altColor, DOWN);
    result += getCountFlipLine(inPos, disc.color, altColor, DOWN_RIGHT);
    return result;
  }

  //count flipable discs in given line
  int getCountFlipLine(int inPos, int inColor, int altColor, int dir) {
    int result = 0;
    int pos;
    for (pos = inPos + dir; disk[pos] == altColor; pos += dir) {
      result++;
    }
    if (disk[pos] != inColor) {return 0;}  //color of end-pos not match
    return result;
  }

  int getAltColor(int color) {
    return color + getAlternate(color);
  }

  int getAlternate(int color) {
    int alt;
    if (color == BLACK) {
      alt = 1;
    } else {
      alt = -1;
    }
    return alt;
  }

  void flipDisc(Disc disc) {
    int alt = getAlternate(disc.color);
    int pos = disc.getPos();
    disk[pos] += alt;
    diskNum[BLACK] += alt;
    diskNum[WHITE] -= alt;
    changePattern(pos, alt);
  }

  void changePattern(int pos, int alt) {
    for (int j=0; j < NUM_PATTERN_DIFF && patternDiff[pos][j] > 0; j++) {
      pattern[patternId[pos][j]] += patternDiff[pos][j] * alt;
      if (Debug.state) {
        Debug.println("Chg(" + pos + "," + j + ")=" + patternId[pos][j] + "," + pattern[patternId[pos][j]]);
      }
    }
  }

}
