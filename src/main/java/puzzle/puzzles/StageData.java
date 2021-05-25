package puzzle.puzzles;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import savestate.selectscreen.GridCardSelectScreenState;
import savestate.selectscreen.HandSelectScreenState;

import java.util.ArrayList;

public class StageData {
    public String NAME;
    public String[] AUTHOR;
    public String CLASS;
    public int FLOOR;
    public boolean MY_TURN;
    public int TURN;
    public String SCREEN;
    public boolean SCREEN_UP;
    public boolean SCREEN_PRE;
    public String LIST;
    public String PLAYER;
    public String RNG;
    public String NODE;
    public int ASCENSION;
    public ArrayList<Integer> GRID_SELECT;
    public ArrayList<Integer> CARD_TURN;
    public ArrayList<Integer> CARD_BAK;
    public ArrayList<Integer> CARD_DRAWN;
}
