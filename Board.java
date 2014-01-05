import java.util.*;
import java.util.concurrent.*;

public class Board {
  public static final int MAX_TURNS = 60;
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

  CopyOnWriteArraySet<Disc>[] movablePos; //movable positions by turn

  HashSet<Integer> freePos;

  public Board () {
    contents = new int[NUM_DISK];
    discNum = new int[3];
    pattern = new int[Pattern.ID_LAST];
    //initialize movablePos
    movablePos = new CopyOnWriteArraySet[MAX_TURNS + 1];
    for (int i=0; i < MAX_TURNS + 1; i++) {
      movablePos[i] = new CopyOnWriteArraySet<Disc>();
    }
    //initialize freePos
    freePos = new HashSet<Integer>();
    for (int i=0; i < Pattern.allPos.length; i++) {
      freePos.add(Pattern.allPos[i]);
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
    if (turn > MAX_TURNS) { return; }
    int i, j, cf;
    movablePos[turn].clear();
    Iterator it = freePos.iterator();
    while (it.hasNext()) {
      int pos = (int)it.next();
      cf = getCountFlip(pos, currentColor);
      if (cf > 0) {
        Disc disc = new Disc(pos % (BOARD_SIZE + 1), pos / (BOARD_SIZE + 1), currentColor);
        if (Debug.state) {
          Debug.println("mp=(" + disc.x + "," + disc.y);
        }
        movablePos[turn].add(disc);
      }
    }
//    for (j=0; j < BOARD_SIZE; j++) {
//      for (i=0; i < BOARD_SIZE; i++) {
//        Disc disc = new Disc(i+1, j+1, currentColor);
//        cf = getCountFlip(disc.getPos(), disc.color);
//        if (cf > 0) {
//          movablePos[turn].add(disc);
//        }
//      }
//    }
  }

  //count flipable discs in all lines 
  public int getCountFlip(int pos, int color) {
    int altColor = getAltColor(color);
    int result = 0;
    if (contents[pos] != EMPTY) {return 0;} //cannot put disc at this position
    switch (pos) {
    case Pattern.C1:
    case Pattern.C2:
    case Pattern.D1:
    case Pattern.D2:
    case Pattern.E1:
    case Pattern.E2:
    case Pattern.F1:
    case Pattern.F2:
      result += getCountFlipLine(pos, color, altColor, LEFT);
      result += getCountFlipLine(pos, color, altColor, RIGHT);
      result += getCountFlipLine(pos, color, altColor, DOWN_LEFT);
      result += getCountFlipLine(pos, color, altColor, DOWN);
      result += getCountFlipLine(pos, color, altColor, DOWN_RIGHT);
      break;
    case Pattern.C7:
    case Pattern.C8:
    case Pattern.D7:
    case Pattern.D8:
    case Pattern.E7:
    case Pattern.E8:
    case Pattern.F7:
    case Pattern.F8:
      result += getCountFlipLine(pos, color, altColor, UP_LEFT);
      result += getCountFlipLine(pos, color, altColor, UP);
      result += getCountFlipLine(pos, color, altColor, UP_RIGHT);
      result += getCountFlipLine(pos, color, altColor, LEFT);
      result += getCountFlipLine(pos, color, altColor, RIGHT);
      break;
    case Pattern.A3:
    case Pattern.A4:
    case Pattern.A5:
    case Pattern.A6:
    case Pattern.B3:
    case Pattern.B4:
    case Pattern.B5:
    case Pattern.B6:
      result += getCountFlipLine(pos, color, altColor, UP);
      result += getCountFlipLine(pos, color, altColor, UP_RIGHT);
      result += getCountFlipLine(pos, color, altColor, RIGHT);
      result += getCountFlipLine(pos, color, altColor, DOWN);
      result += getCountFlipLine(pos, color, altColor, DOWN_RIGHT);
      break;
    case Pattern.H3:
    case Pattern.H4:
    case Pattern.H5:
    case Pattern.H6:
    case Pattern.G3:
    case Pattern.G4:
    case Pattern.G5:
    case Pattern.G6:
      result += getCountFlipLine(pos, color, altColor, UP_LEFT);
      result += getCountFlipLine(pos, color, altColor, UP);
      result += getCountFlipLine(pos, color, altColor, LEFT);
      result += getCountFlipLine(pos, color, altColor, DOWN_LEFT);
      result += getCountFlipLine(pos, color, altColor, DOWN);
      break;
    case Pattern.A1:
    case Pattern.A2:
    case Pattern.B1:
    case Pattern.B2:
      result += getCountFlipLine(pos, color, altColor, RIGHT);
      result += getCountFlipLine(pos, color, altColor, DOWN);
      result += getCountFlipLine(pos, color, altColor, DOWN_RIGHT);
      break;
    case Pattern.A8:
    case Pattern.A7:
    case Pattern.B8:
    case Pattern.B7:
      result += getCountFlipLine(pos, color, altColor, UP);
      result += getCountFlipLine(pos, color, altColor, UP_RIGHT);
      result += getCountFlipLine(pos, color, altColor, RIGHT);
      break;
    case Pattern.H1:
    case Pattern.H2:
    case Pattern.G1:
    case Pattern.G2:
      result += getCountFlipLine(pos, color, altColor, LEFT);
      result += getCountFlipLine(pos, color, altColor, DOWN_LEFT);
      result += getCountFlipLine(pos, color, altColor, DOWN);
      break;
    case Pattern.H8:
    case Pattern.H7:
    case Pattern.G8:
    case Pattern.G7:
      result += getCountFlipLine(pos, color, altColor, UP_LEFT);
      result += getCountFlipLine(pos, color, altColor, UP);
      result += getCountFlipLine(pos, color, altColor, LEFT);
      break;
    default:
      result += getCountFlipLine(pos, color, altColor, UP_LEFT);
      result += getCountFlipLine(pos, color, altColor, UP);
      result += getCountFlipLine(pos, color, altColor, UP_RIGHT);
      result += getCountFlipLine(pos, color, altColor, LEFT);
      result += getCountFlipLine(pos, color, altColor, RIGHT);
      result += getCountFlipLine(pos, color, altColor, DOWN_LEFT);
      result += getCountFlipLine(pos, color, altColor, DOWN);
      result += getCountFlipLine(pos, color, altColor, DOWN_RIGHT);
      break;
    }
    return result;
  }

  int getAltColor(int color) {
    return color + getAlternate(color);
  }

  public int getAlternate(int color) {
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

  public CopyOnWriteArraySet<Disc> getMovablePos() {
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
    switch (pos) {
    case Pattern.C1:
    case Pattern.C2:
    case Pattern.D1:
    case Pattern.D2:
    case Pattern.E1:
    case Pattern.E2:
    case Pattern.F1:
    case Pattern.F2:
      result += flipLine(update, pos, color, altColor, LEFT);
      result += flipLine(update, pos, color, altColor, RIGHT);
      result += flipLine(update, pos, color, altColor, DOWN_LEFT);
      result += flipLine(update, pos, color, altColor, DOWN);
      result += flipLine(update, pos, color, altColor, DOWN_RIGHT);
      break;
    case Pattern.C7:
    case Pattern.C8:
    case Pattern.D7:
    case Pattern.D8:
    case Pattern.E7:
    case Pattern.E8:
    case Pattern.F7:
    case Pattern.F8:
      result += flipLine(update, pos, color, altColor, UP_LEFT);
      result += flipLine(update, pos, color, altColor, UP);
      result += flipLine(update, pos, color, altColor, UP_RIGHT);
      result += flipLine(update, pos, color, altColor, LEFT);
      result += flipLine(update, pos, color, altColor, RIGHT);
      break;
    case Pattern.A3:
    case Pattern.A4:
    case Pattern.A5:
    case Pattern.A6:
    case Pattern.B3:
    case Pattern.B4:
    case Pattern.B5:
    case Pattern.B6:
      result += flipLine(update, pos, color, altColor, UP);
      result += flipLine(update, pos, color, altColor, UP_RIGHT);
      result += flipLine(update, pos, color, altColor, RIGHT);
      result += flipLine(update, pos, color, altColor, DOWN);
      result += flipLine(update, pos, color, altColor, DOWN_RIGHT);
      break;
    case Pattern.H3:
    case Pattern.H4:
    case Pattern.H5:
    case Pattern.H6:
    case Pattern.G3:
    case Pattern.G4:
    case Pattern.G5:
    case Pattern.G6:
      result += flipLine(update, pos, color, altColor, UP_LEFT);
      result += flipLine(update, pos, color, altColor, UP);
      result += flipLine(update, pos, color, altColor, LEFT);
      result += flipLine(update, pos, color, altColor, DOWN_LEFT);
      result += flipLine(update, pos, color, altColor, DOWN);
      break;
    case Pattern.A1:
    case Pattern.A2:
    case Pattern.B1:
    case Pattern.B2:
      result += flipLine(update, pos, color, altColor, RIGHT);
      result += flipLine(update, pos, color, altColor, DOWN);
      result += flipLine(update, pos, color, altColor, DOWN_RIGHT);
      break;
    case Pattern.A8:
    case Pattern.A7:
    case Pattern.B8:
    case Pattern.B7:
      result += flipLine(update, pos, color, altColor, UP);
      result += flipLine(update, pos, color, altColor, UP_RIGHT);
      result += flipLine(update, pos, color, altColor, RIGHT);
      break;
    case Pattern.H1:
    case Pattern.H2:
    case Pattern.G1:
    case Pattern.G2:
      result += flipLine(update, pos, color, altColor, LEFT);
      result += flipLine(update, pos, color, altColor, DOWN_LEFT);
      result += flipLine(update, pos, color, altColor, DOWN);
      break;
    case Pattern.H8:
    case Pattern.H7:
    case Pattern.G8:
    case Pattern.G7:
      result += flipLine(update, pos, color, altColor, UP_LEFT);
      result += flipLine(update, pos, color, altColor, UP);
      result += flipLine(update, pos, color, altColor, LEFT);
      break;
    default:
      result += flipLine(update, pos, color, altColor, UP_LEFT);
      result += flipLine(update, pos, color, altColor, UP);
      result += flipLine(update, pos, color, altColor, UP_RIGHT);
      result += flipLine(update, pos, color, altColor, LEFT);
      result += flipLine(update, pos, color, altColor, RIGHT);
      result += flipLine(update, pos, color, altColor, DOWN_LEFT);
      result += flipLine(update, pos, color, altColor, DOWN);
      result += flipLine(update, pos, color, altColor, DOWN_RIGHT);
      break;
    }
    if (result > 0) {
      update.push(pos);      //save position of putted disc
    }
    updateLog.push(update);
  }

  public void putPos(int pos, int color) {
    contents[pos] = color;
    discNum[color]++;
    discNum[EMPTY]--;
    changePattern(pos, color);  //change index of pattern (include pos)
    freePos.remove(pos);
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
        update.push(pos);  //save position of flipped disc
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

  public boolean undo() {
    if (turn == 0 || updateLog.isEmpty()) { return false;}
    currentColor = getAltColor(currentColor);
    LinkedList<Integer> update = updateLog.pop();
    if (!update.isEmpty()) {
      int pos = update.pop(); //get position of put disc in previous action
      freePos.add(pos);
      changePattern(pos, -(contents[pos])); //change index of pattern (include pos)
      discNum[contents[pos]]--;
      discNum[EMPTY]++;
      contents[pos] = EMPTY;

      //restore flipped discs
      while (!update.isEmpty()) {
       pos = update.pop();
       flipPos(pos);
      }
      turn--;
    }
    setMovablePos();
    return true;
  }

  public boolean pass() {
    if (!movablePos[turn].isEmpty()) { return false; }
    if (isGameOver()) { return false; }
    currentColor = getAltColor(currentColor);
    LinkedList<Integer> update = new LinkedList<Integer>();
    updateLog.push(update);
    setMovablePos();
    return true;
  }

  public boolean isGameOver() {
    if (turn >= MAX_TURNS) { return true; }
    if (!movablePos[turn].isEmpty()) { return false; }
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
