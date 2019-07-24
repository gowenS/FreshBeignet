package classes;

public class MoveCard {

    public static final int BOAR = 1;
    public static final int COBRA = 2;
    public static final int CRAB = 3;
    public static final int CRANE = 4;
    public static final int DRAGON = 5;
    public static final int EEL = 6;
    public static final int ELEPHANT = 7;
    public static final int FROG = 8;
    public static final int GOOSE = 9;
    public static final int HORSE = 10;
    public static final int MANTIS = 11;
    public static final int MONKEY  =12;
    public static final int OX = 13;
    public static final int RABBIT = 14;
    public static final int ROOSTER = 15;
    public static final int TIGER = 16;
    public static final int FOX = 17;
    public static final int DOG = 18;
    public static final int GIRAFFE = 19;
    public static final int PANDA = 20;
    public static final int BEAR = 21;
    public static final int KIRIN = 22;
    public static final int SEASNAKE = 23;
    public static final int VIPER = 24;
    public static final int PHOENIX = 25;
    public static final int MOUSE = 26;
    public static final int RAT = 27;
    public static final int TURTLE = 28;
    public static final int TANUKI = 29;
    public static final int IGUANA = 30;
    public static final int SABLE = 31;
    public static final int OTTER = 32;


    public static final int RED = 1;
    public static final int BLUE = 2;

    private int type = 0;
    private int color = 0;
    private int[] row;
    private int[] col;
    private int bgid;
    private int bginv;

    public MoveCard(int type) {
        this.type = type;
        setAttributes();
    }

    private void setAttributes() {

        switch (type) {
            case BOAR:  color = RED;
                        row = new int[]{0 , 1 , 0 , 0};
                        col = new int[]{-1 , 0, 1 , 0};
                        bgid = R.drawable.boar;
                        bginv = R.drawable.boarinv;
                        break;
            case COBRA: color = RED;
                        row = new int[]{0 , 1 , -1 , 0};
                        col = new int[]{-1 , 1 , 1 , 0};
                        bgid = R.drawable.cobra;
                        bginv = R.drawable.cobrainv;
                        break;
            case CRAB:  color = BLUE;
                        row = new int[]{0 , 1 , 0 , 0};
                        col = new int[]{-2 , 0 , 2, 0};
                        bgid = R.drawable.crab;
                        bginv = R.drawable.crabinv;
                        break;
            case CRANE: color = BLUE;
                        row = new int[]{-1 , 1 , -1, 0};
                        col = new int[]{-1 , 0 , 1 , 0};
                        bgid = R.drawable.crane;
                        bginv = R.drawable.craneinv;
                        break;
            case DRAGON:color = RED;
                        row = new int[]{1 , -1 , -1 , 1};
                        col = new int[]{-2 , -1 , 1 , 2};
                        bgid = R.drawable.dragon;
                        bginv = R.drawable.dragoninv;
                        break;
            case EEL:   color = BLUE;
                        row = new int[]{1 , -1 , 0 , 0};
                        col = new int[]{-1 , -1 , 1 , 0};
                        bgid = R.drawable.eel;
                        bginv = R.drawable.eelinv;
                        break;
            case ELEPHANT:  color = RED;
                            row = new int[]{1 , 0 , 0 , 1};
                            col = new int[]{-1 , -1 , 1, 1};
                            bgid = R.drawable.elephant;
                            bginv = R.drawable.elephantinv;
                            break;
            case FROG:  color = RED;
                        row = new int[]{0 , 1 , -1 , 0};
                        col = new int[]{-2 , -1 , 1 , 0};
                        bgid = R.drawable.frog;
                        bginv = R.drawable.froginv;
                        break;
            case GOOSE: color = BLUE;
                        row = new int[]{1 , 0 , 0 , -1};
                        col = new int[]{-1 , -1 , 1 , 1};
                        bgid = R.drawable.goose;
                        bginv = R.drawable.gooseinv;
                        break;
            case HORSE: color = RED;
                        row = new int[]{0 , 1 , -1 , 0};
                        col = new int[]{-1, 0 , 0 , 0};
                        bgid = R.drawable.horse;
                        bginv = R.drawable.horseinv;
                        break;
            case MANTIS:color = RED;
                        row = new int[]{1 , -1 , 1 , 0};
                        col = new int[]{-1 , 0 , 1 , 0};
                        bgid = R.drawable.mantis;
                        bginv = R.drawable.mantisinv;
                        break;
            case MONKEY:color = BLUE;
                        row = new int[]{1 , -1 , 1, -1};
                        col = new int[]{-1 , -1 , 1 , 1};
                        bgid = R.drawable.monkey;
                        bginv = R.drawable.monkeyinv;
                        break;
            case OX:    color = BLUE;
                        row = new int[]{1 , -1 , 0 , 0};
                        col = new int[]{0 , 0 , 1 , 0};
                        bgid = R.drawable.ox;
                        bginv = R.drawable.oxinv;
                        break;
            case RABBIT:color = BLUE;
                        row = new int[]{-1 , 1 , 0 , 0};
                        col = new int[]{-1 , 1 , 2 , 0};
                        bgid = R.drawable.rabbit;
                        bginv = R.drawable.rabbitinv;
                        break;
            case ROOSTER:   color = RED;
                            row = new int[]{-1 , 0 , 0 , 1};
                            col = new int[]{-1 , -1 , 1 , 1};
                            bgid = R.drawable.rooster;
                            bginv = R.drawable.roosterinv;
                            break;
            case TIGER: color = BLUE;
                        row = new int[]{2 , -1 , 0 , 0};
                        col = new int[]{0 , 0 , 0 , 0};
                        bgid = R.drawable.tiger;
                        bginv = R.drawable.tigerinv;
                        break;
            case FOX:   color = RED;
                        row = new int[]{1 , 0 , -1 , 0};
                        col = new int[]{1 , 1 , 1 , 0};
                        bgid = R.drawable.fox;
                        bginv = R.drawable.foxinv;
                        break;
            case DOG:   color = BLUE;
                        row = new int[]{1 , 0 , -1 , 0};
                        col = new int[]{-1 , -1 , -1 , 0};
                        bgid = R.drawable.dog;
                        bginv = R.drawable.doginv;
                        break;
            case GIRAFFE:   color = BLUE;
                            row = new int[]{1, -1 , 1 , 0};
                            col = new int[]{-2 , 0 , 2 , 0};
                            bgid = R.drawable.giraffe;
                            bginv = R.drawable.giraffeinv;
                            break;
            case PANDA: color = RED;
                        row = new int[]{-1 , 1 , 1 , 0};
                        col = new int[]{-1 , 0 , 1 , 0};
                        bgid = R.drawable.panda;
                        bginv = R.drawable.pandainv;
                        break;
            case BEAR:  color = BLUE;
                        row = new int[]{1 , 1 , -1 , 0};
                        col = new int[]{-1 , 0 , 1 , 0};
                        bgid = R.drawable.bear;
                        bginv = R.drawable.bearinv;
                        break;
            case KIRIN: color = RED;
                        row = new int[]{2 , -2 , 2 , 0};
                        col = new int[]{-1 , 0 , 1 , 0};
                        bgid = R.drawable.kirin;
                        bginv = R.drawable.kirininv;
                        break;
            case SEASNAKE:  color = BLUE;
                            row = new int[]{-1 , 1 , 0 , 0};
                            col = new int[]{-1 , 0 , 2 , 0};
                            bgid = R.drawable.seasnake;
                            bginv = R.drawable.seasnakeinv;
                            break;
            case VIPER: color = RED;
                        row = new int[]{0 , 1 , -1 , 0};
                        col = new int[]{-2 , 0 , 1 , 0};
                        bgid = R.drawable.viper;
                        bginv = R.drawable.viperinv;
                        break;
            case PHOENIX:   color = BLUE;
                            row = new int[]{0 , 1 , 1 , 0};
                            col = new int[]{-2 , -1 , 1 , 2};
                            bgid = R.drawable.phoenix;
                            bginv = R.drawable.phoenixinv;
                            break;
            case MOUSE: color = BLUE;
                        row = new int[]{-1 , 1 , 0 , 0};
                        col = new int[]{-1 , 0 , 1 , 0};
                        bgid = R.drawable.mouse;
                        bginv = R.drawable.mouseinv;
                        break;
            case RAT:   color = RED;
                        row = new int[]{0 , 1 , -1 , 0};
                        col = new int[]{-1 , 0 , 1 , 0};
                        bgid = R.drawable.rat;
                        bginv = R.drawable.ratinv;
                        break;
            case TURTLE:color = RED;
                        row = new int[]{0 , -1 , -1 , 0};
                        col = new int[]{-2 , -1 , 1 , 2};
                        bgid = R.drawable.turtle;
                        bginv = R.drawable.turtleinv;
                        break;
            case TANUKI:color = BLUE;
                        row = new int[]{-1, 1 , 1 , 0};
                        col = new int[]{-1 , 0 , 2 , 0};
                        bgid = R.drawable.tanuki;
                        bginv = R.drawable.tanukiinv;
                        break;
            case IGUANA:color = RED;
                        row = new int[]{1, 1 , -1 , 0};
                        col = new int[]{-2, 0 , 1 , 0};
                        bgid = R.drawable.iguana;
                        bginv = R.drawable.iguanainv;
                        break;
            case SABLE: color = BLUE;
                        row = new int[]{0 , -1 , 1 , 0};
                        col = new int[]{-2 , -1 , 1 , 0};
                        bgid = R.drawable.sable;
                        bginv = R.drawable.sableinv;
                        break;
            case OTTER: color = RED;
                        row = new int[]{1 , -1 , 0 , 0};
                        col = new int[]{-1 , 1 , 2 , 0};
                        bgid = R.drawable.otter;
                        bginv = R.drawable.otterinv;
                        break;

        }

    }

    public int[] getRow() {
        return row;
    }

    public int[] getCol() {
        return col;
    }

    public int whichColor() {
        return color;
    }

    public int getBG() {return bgid; }

    public int getBGinv() { return bginv; }

}
