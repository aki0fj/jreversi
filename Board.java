import java.util.*;

public class Board {
  final int MAX_TURNS = 60;

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

  //number of disc position
  final int NUM_DISK = ((BOARD_SIZE+1)*(BOARD_SIZE+2)+1);
  //max occurs for pattern by position
  final int NUM_PATTERN_DIFF = 6;

  int turn;           //current turn no.
  int currentColor;

  int[] contents; //Current color of contentss by position of board
  int[] discNum;  //Current number of contentss by color
  int[] pattern;  //Current variation no.(index) by all patterns
  
  //difference of index when the color of contents has changed
  int[][] patternId;  //pattern no. by position (by occurs)
  int[][] patternDiff;  //pow(3, column in pattern) by position (by occurs)

  ArrayDeque<LinkedList<Integer>> updateLog; //Stack of updated discs by turn

  HashSet<Disc>[] movablePos; //movable positions by turn

  public Board () {
    contents = new int[NUM_DISK];
    discNum = new int[3];
    pattern = new int[Pattern.ID_LAST];
    movablePos = new HashSet[MAX_TURNS];
    for (int i=0; i < MAX_TURNS; i++) {
      movablePos[i] = new HashSet<Disc>();
    }
    InitializePatternDiff();  //set constant for update index by pattern
    Clear();  //initial board setting
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
      contents[i] = WALL;   //fill all boards with WALL
    }
    for (j=0; j < BOARD_SIZE; j++) {
      for (i=0; i < BOARD_SIZE; i++) {
        disc = new Disc(i+1, j+1, 0);
        contents[disc.getPos()] = EMPTY;  //fill puttable position with EMPTY
      }
    }

    disc = new Disc(4, 4, WHITE); putPos(disc.getPos(), disc.color);
    disc = new Disc(5, 5, WHITE); putPos(disc.getPos(), disc.color);
    disc = new Disc(4, 5, BLACK); putPos(disc.getPos(), disc.color);
    disc = new Disc(5, 4, BLACK); putPos(disc.getPos(), disc.color);

    turn = 0;
    currentColor = BLACK;
    discNum[EMPTY] = MAX_TURNS;
    updateLog = new ArrayDeque<LinkedList<Integer>>(MAX_TURNS);
    setMovablePos();

    //if debug -> print image of board
    if (Debug.state) {
      Debug.println("d_num=" + movablePos[turn].size());
      Iterator it = movablePos[turn].iterator();
      while(it.hasNext()) {
        Disc pt = (Disc)it.next();
        Debug.println("mov(" + pt.x + "," + pt.y + ")");
      }
    }
  }

  void setMovablePos() {
    int i, j, cf;
    movablePos[turn].clear();
    for (j=0; j < BOARD_SIZE; j++) {
      for (i=0; i < BOARD_SIZE; i++) {
        Disc disc = new Disc(i+1, j+1, currentColor);
        cf = getCountFlip(disc.getPos(), disc.color);
        if (cf > 0) {
          movablePos[turn].add(disc);
        }
      }
    }
  }

  //count flipable discs in all lines 
  public int getCountFlip(int pos, int color) {
    int altColor = getAltColor(color);
    int result = 0;
    if (contents[pos] != EMPTY) {return 0;} //cannot put disc at this position
    result += getCountFlipLine(pos, color, altColor, UP_LEFT);
    result += getCountFlipLine(pos, color, altColor, UP);
    result += getCountFlipLine(pos, color, altColor, UP_RIGHT);
    result += getCountFlipLine(pos, color, altColor, LEFT);
    result += getCountFlipLine(pos, color, altColor, RIGHT);
    result += getCountFlipLine(pos, color, altColor, DOWN_LEFT);
    result += getCountFlipLine(pos, color, altColor, DOWN);
    result += getCountFlipLine(pos, color, altColor, DOWN_RIGHT);
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

  //count flipable discs in given line
  int getCountFlipLine(int inPos, int inColor, int altColor, int dir) {
    int result = 0;
    int pos;
    for (pos = inPos + dir; contents[pos] == altColor; pos += dir) {
      result++;
    }
    if (contents[pos] != inColor) {return 0;}  //color of end-pos not match
    return result;
  }

  public int getPattern(int id) {
    return pattern[id]; // return Pattern Number
  }

  public int[] getContents() {
    return contents;
  }

  public int getCountDiscs(int color) {
    return discNum[color];
  }

  public int getTurn() {
    return turn;
  }

  public int getCurrentColor() {
    return currentColor;
  }

  public HashSet<Disc> getMovablePos() {
    return movablePos[turn];  //all movable pos info at current turn
  }

  public boolean put(int pos, int color) {
    if (contents[pos] != EMPTY || getCountFlip(pos, color) == 0) {
      return false;
    }
    flip(pos, color);   //flip all discs
    turn++;
    currentColor = getAltColor(currentColor);
    setMovablePos();
    return true;
  }

  public void flip(int pos, int color) {
    putPos(pos, color);    //put new disc
    int result = 0;   //count of flipped discs
    LinkedList<Integer> update = new LinkedList<Integer>();
    //flip discs to all directions
    int altColor = getAltColor(color);
    result += flipLine(update, pos, color, altColor, UP_LEFT);
    result += flipLine(update, pos, color, altColor, UP);
    result += flipLine(update, pos, color, altColor, UP_RIGHT);
    result += flipLine(update, pos, color, altColor, LEFT);
    result += flipLine(update, pos, color, altColor, RIGHT);
    result += flipLine(update, pos, color, altColor, DOWN_LEFT);
    result += flipLine(update, pos, color, altColor, DOWN);
    result += flipLine(update, pos, color, altColor, DOWN_RIGHT);
    if (result > 0) {
      update.add(pos);      //save position of putted disc
      update.add(color);    //save color of putted disc
      update.add(result);   //save count of flipped discs
    }
    updateLog.add(update);
  }

  public void putPos(int pos, int color) {
    contents[pos] = color;
    discNum[color]++;
    discNum[EMPTY]--;
    changePattern(pos, color);
  }

  //flip discs in given line
  int flipLine(LinkedList update, int inPos, int inColor, int altColor,
                int dir) {
    int result, pos, alt;
    result = 0;
    //search end of alternate color
    for (pos = inPos + dir; contents[pos] == altColor; pos += dir) {}
    if (contents[pos] == inColor) {  //color of end-pos match
      //flip discs (go back to inPos)
      for (pos -= dir; contents[pos] == altColor; pos -= dir) {
        flipPos(pos);     //flip 1 disc
        update.add(pos);  //save position of flipped disc
        result++;
      }
    }
    return result;
  }

  public void flipPos(int pos) {
    int alt = getAlternate(contents[pos]);
    contents[pos] += alt;
    discNum[BLACK] -= alt;
    discNum[WHITE] += alt;
    changePattern(pos, alt);  //change index of pattern (include pos)
  }

  void changePattern(int pos, int alt) {
    for (int j=0; j < NUM_PATTERN_DIFF && patternDiff[pos][j] > 0; j++) {
      pattern[patternId[pos][j]] += patternDiff[pos][j] * alt;
      if (Debug.state) {
        Debug.println("Chg(" + pos + "," + j + ")=" + patternId[pos][j] + "," + pattern[patternId[pos][j]]);
      }
    }
  }

  public boolean pass() {
    if (!movablePos[turn].isEmpty()) { return false; }
    if (isGameOver()) { return false; }
    currentColor = getAltColor(currentColor);
    LinkedList<Integer> update = new LinkedList<Integer>();
    updateLog.add(update);
    setMovablePos();
    return true;
  }

  public boolean isGameOver() {
    if (!movablePos[turn].isEmpty()) { return false; }
    if (turn >= MAX_TURNS) { return true; }
    Disc disc = new Disc(0,0,0);
    int x, y, pos;
    int altColor = getAltColor(currentColor);
    for (y=1; y < BOARD_SIZE+1 ; y++) {
      for (x=1; x < BOARD_SIZE+1; x++) {
        disc.x = x; disc.y = y; pos = disc.getPos();
        if (getCountFlip(pos, altColor) > 0) {return false;}
      }
    }
    return true;
  }

}
