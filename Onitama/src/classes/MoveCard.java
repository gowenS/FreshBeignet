package classes;

public class MoveCard {

    public static final int BLANK = 0;
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
    private String bgid;
    private String bginv;

    public MoveCard(int type) {
        this.type = type;
        setAttributes();
    }
        

    private void setAttributes() {

        switch (type) {
        	
            case BOAR:  color = RED;
                        row = new int[]{0 , 1 , 0 , 0};
                        col = new int[]{-1 , 0, 1 , 0};
                        bgid = "images/boar.png";
                        bginv = "images/boarinv.png";
                        break;
            case COBRA: color = RED;
                        row = new int[]{0 , 1 , -1 , 0};
                        col = new int[]{-1 , 1 , 1 , 0};
                        bgid = "images/cobra.png";
                        bginv = "images/cobrainv.png";
                        break;
            case CRAB:  color = BLUE;
                        row = new int[]{0 , 1 , 0 , 0};
                        col = new int[]{-2 , 0 , 2, 0};
                        bgid = "images/crab.png";
                        bginv = "images/crabinv.png";
                        break;
            case CRANE: color = BLUE;
                        row = new int[]{-1 , 1 , -1, 0};
                        col = new int[]{-1 , 0 , 1 , 0};
                        bgid = "images/crane.png";
                        bginv = "images/craneinv.png";
                        break;
            case DRAGON:color = RED;
                        row = new int[]{1 , -1 , -1 , 1};
                        col = new int[]{-2 , -1 , 1 , 2};
                        bgid = "images/dragon.png";
                        bginv = "images/dragoninv.png";
                        break;
            case EEL:   color = BLUE;
                        row = new int[]{1 , -1 , 0 , 0};
                        col = new int[]{-1 , -1 , 1 , 0};
                        bgid = "images/eel.png";
                        bginv = "images/eelinv.png";
                        break;
            case ELEPHANT:  color = RED;
                            row = new int[]{1 , 0 , 0 , 1};
                            col = new int[]{-1 , -1 , 1, 1};
                            bgid = "images/elephant.png";
                            bginv = "images/elephantinv.png";
                            break;
            case FROG:  color = RED;
                        row = new int[]{0 , 1 , -1 , 0};
                        col = new int[]{-2 , -1 , 1 , 0};
                        bgid = "images/frog.png";
                        bginv = "images/froginv.png";
                        break;
            case GOOSE: color = BLUE;
                        row = new int[]{1 , 0 , 0 , -1};
                        col = new int[]{-1 , -1 , 1 , 1};
                        bgid = "images/goose.png";
                        bginv = "images/gooseinv.png";
                        break;
            case HORSE: color = RED;
                        row = new int[]{0 , 1 , -1 , 0};
                        col = new int[]{-1, 0 , 0 , 0};
                        bgid = "images/horse.png";
                        bginv = "images/horseinv.png";
                        break;
            case MANTIS:color = RED;
                        row = new int[]{1 , -1 , 1 , 0};
                        col = new int[]{-1 , 0 , 1 , 0};
                        bgid = "images/mantis.png";
                        bginv = "images/mantisinv.png";
                        break;
            case MONKEY:color = BLUE;
                        row = new int[]{1 , -1 , 1, -1};
                        col = new int[]{-1 , -1 , 1 , 1};
                        bgid = "images/monkey.png";
                        bginv = "images/monkeyinv.png";
                        break;
            case OX:    color = BLUE;
                        row = new int[]{1 , -1 , 0 , 0};
                        col = new int[]{0 , 0 , 1 , 0};
                        bgid = "images/ox.png";
                        bginv = "images/oxinv.png";
                        break;
            case RABBIT:color = BLUE;
                        row = new int[]{-1 , 1 , 0 , 0};
                        col = new int[]{-1 , 1 , 2 , 0};
                        bgid = "images/rabbit.png";
                        bginv = "images/rabbitinv.png";
                        break;
            case ROOSTER:   color = RED;
                            row = new int[]{-1 , 0 , 0 , 1};
                            col = new int[]{-1 , -1 , 1 , 1};
                            bgid = "images/rooster.png";
                            bginv = "images/roosterinv.png";
                            break;
            case TIGER: color = BLUE;
                        row = new int[]{2 , -1 , 0 , 0};
                        col = new int[]{0 , 0 , 0 , 0};
                        bgid = "images/tiger.png";
                        bginv = "images/tigerinv.png";
                        break;
            case FOX:   color = RED;
                        row = new int[]{1 , 0 , -1 , 0};
                        col = new int[]{1 , 1 , 1 , 0};
                        bgid = "images/fox.png";
                        bginv = "images/foxinv.png";
                        break;
            case DOG:   color = BLUE;
                        row = new int[]{1 , 0 , -1 , 0};
                        col = new int[]{-1 , -1 , -1 , 0};
                        bgid = "images/dog.png";
                        bginv = "images/doginv.png";
                        break;
            case GIRAFFE:   color = BLUE;
                            row = new int[]{1, -1 , 1 , 0};
                            col = new int[]{-2 , 0 , 2 , 0};
                            bgid = "images/giraffe.png";
                            bginv = "images/giraffeinv.png";
                            break;
            case PANDA: color = RED;
                        row = new int[]{-1 , 1 , 1 , 0};
                        col = new int[]{-1 , 0 , 1 , 0};
                        bgid = "images/panda.png";
                        bginv = "images/pandainv.png";
                        break;
            case BEAR:  color = BLUE;
                        row = new int[]{1 , 1 , -1 , 0};
                        col = new int[]{-1 , 0 , 1 , 0};
                        bgid = "images/bear.png";
                        bginv = "images/bearinv.png";
                        break;
            case KIRIN: color = RED;
                        row = new int[]{2 , -2 , 2 , 0};
                        col = new int[]{-1 , 0 , 1 , 0};
                        bgid = "images/kirin.png";
                        bginv = "images/kirininv.png";
                        break;
            case SEASNAKE:  color = BLUE;
                            row = new int[]{-1 , 1 , 0 , 0};
                            col = new int[]{-1 , 0 , 2 , 0};
                            bgid = "images/seasnake.png";
                            bginv = "images/seasnakeinv.png";
                            break;
            case VIPER: color = RED;
                        row = new int[]{0 , 1 , -1 , 0};
                        col = new int[]{-2 , 0 , 1 , 0};
                        bgid = "images/viper.png";
                        bginv = "images/viperinv.png";
                        break;
            case PHOENIX:   color = BLUE;
                            row = new int[]{0 , 1 , 1 , 0};
                            col = new int[]{-2 , -1 , 1 , 2};
                            bgid = "images/phoenix.png";
                            bginv = "images/phoenixinv.png";
                            break;
            case MOUSE: color = BLUE;
                        row = new int[]{-1 , 1 , 0 , 0};
                        col = new int[]{-1 , 0 , 1 , 0};
                        bgid = "images/mouse.png";
                        bginv = "images/mouseinv.png";
                        break;
            case RAT:   color = RED;
                        row = new int[]{0 , 1 , -1 , 0};
                        col = new int[]{-1 , 0 , 1 , 0};
                        bgid = "images/rat.png";
                        bginv = "images/ratinv.png";
                        break;
            case TURTLE:color = RED;
                        row = new int[]{0 , -1 , -1 , 0};
                        col = new int[]{-2 , -1 , 1 , 2};
                        bgid = "images/turtle.png";
                        bginv = "images/turtleinv.png";
                        break;
            case TANUKI:color = BLUE;
                        row = new int[]{-1, 1 , 1 , 0};
                        col = new int[]{-1 , 0 , 2 , 0};
                        bgid = "images/tanuki.png";
                        bginv = "images/tanukiinv.png";
                        break;
            case IGUANA:color = RED;
                        row = new int[]{1, 1 , -1 , 0};
                        col = new int[]{-2, 0 , 1 , 0};
                        bgid = "images/iguana.png";
                        bginv = "images/iguanainv.png";
                        break;
            case SABLE: color = BLUE;
                        row = new int[]{0 , -1 , 1 , 0};
                        col = new int[]{-2 , -1 , 1 , 0};
                        bgid = "images/sable.png";
                        bginv = "images/sableinv.png";
                        break;
            case OTTER: color = RED;
                        row = new int[]{1 , -1 , 0 , 0};
                        col = new int[]{-1 , 1 , 2 , 0};
                        bgid = "images/otter.png";
                        bginv = "images/otterinv.png";
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

    public String getBG() {return bgid; }

    public String getBGinv() { return bginv; }

}
