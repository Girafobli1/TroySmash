package Game;
import java.awt.Font;
import org.newdawn.slick.TrueTypeFont;
import java.util.regex.*;


import java.io.IOException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Smash extends BasicGameState {

    private boolean dead = false;
    //id of state
    private int state;
    //Final D map
    private Image FD;
    //k1 starting stance
    private Image idle;
    //mr kim test
    private KimBoi k1;
    private boolean m;
    private Pattern xCheck;
    private Pattern yCheck;
    private Music music;
    private Sound sound;
    private Sound death;
    private int numPlayers;
    private int temp = 0;
    private boolean jumping = false;
    private boolean jumpings[] = {false};
    private float initY;
    private KimBoi[] kims;
    private boolean CorS;
    private boolean gen = true;
    private boolean left = true;
    //move sets based on controls
    private final int[] LEFTIE = new int[]{Input.KEY_A, Input.KEY_D, Input.KEY_W, Input.KEY_S};
    private final int[] DEFAULT = new int[]{Input.KEY_LEFT, Input.KEY_RIGHT, Input.KEY_UP, Input.KEY_DOWN};
    private final int LEFT = 0;
    private final int RIGHT = 1;
    private final int UP = 2;
    private final int DOWN = 3;
    private int[] controls;
    Conf conf;
    private Font font;
    private TrueTypeFont ttf;

    public Smash(int s) {
        state = s;
    }

    @Override
    public int getID() {
        conf = new Conf();
        conf.buildConf();
        if (conf.confMoves.equals("default")) {
            controls = DEFAULT;
        } else {
            controls = LEFTIE;
        }
        return state;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        idle = new Image("res/KimPick.png");
        FD = new Image("res/Final_ProcrastinationMap.png");
        //xCheck = Pattern.compile("x:\d+");
        music = new Music("sound/music/finaldest.ogg");
        //GHETTO PLAYLIST BELOW (paste the name in between the "/" and the ".ogg")
        //finaldest
        //cuban
        //troytrap

        sound = new Sound("sound/metal_block.ogg");
        death = new Sound("sound/ded.ogg");

        FD = new Image("res/Final_ProcrastinationMap.png");


        font = new Font("Showcard Gothic", Font.BOLD, 50);
        ttf = new TrueTypeFont(font, true);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        FD.draw(0, 0, .5f);
        k1.draw();
        try {
            for (int i = 0; i < kims.length; i++) {
                if (i > 0) {
                    break;
                }
                if (kims.length <= 1) {
                    kims[i].draw();
                }
            }
        } catch (NullPointerException x) {
            sbg.enterState(5);
        }
        if (CorS) {
            ttf.drawString(400, 700, "Lives: " + Client.numLives);
        } else {
            ttf.drawString(400, 700, "Lives: " + Server2.numLives);

        }
    }

    @Override
    public synchronized void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        if (!CorS) {
            if(Server2.numLives < 1) {
                sbg.enterState(4);
            }
        }
        System.out.println(numPlayers);
        if (gen) {
            init2();
        }
        if (!music.playing()) {
            music.play(1, Server2.getVolume());
        }
        if (numPlayers > 1) {

            Input input = gc.getInput();
            try {
                if (CorS) {
                    if (input.isKeyPressed(controls[LEFT])) {
                        k1.changeLeft();
                        left = true;
                        Client.send("turn left ", Client.id);
                        //k1.idle();
                    }
                    if (input.isKeyPressed(controls[RIGHT])) {
                        k1.changeRight();
                        left = false;
                        Client.send("turn right ", Client.id);
                        //k1.idle();
                    }
                    if (input.isKeyPressed(Input.KEY_T)) {
                        k1.changeAttack();
                        sound.play(1, 0.3f);
                        m = false;
                        Client.send("attack ", Client.id);
                        //System.out.println(m);
                        if (left) {
                            HitBox h = new CircleHB((int) k1.getX() + 60, (int) k1.getY() + 50, 135, 110);
                            if (k1.detectHit(h, kims[0])) {
                                Client.send("hit left", Client.id);
                                kims[0].changeLeft();
                                kims[0].walk(-25);
                            }
                        } else {
                            HitBox h = new CircleHB((int) k1.getX() + 120, (int) k1.getY() + 50, 135, 110);
                            if (k1.detectHit(h, kims[0])) {
                                Client.send("hit right", Client.id);
                                kims[0].changeRight();
                                kims[0].walk(25);
                            }
                        }
                    }
                    if (input.isKeyPressed(Input.KEY_V)) {
                        k1.changeUpAttack();

                        sound.play(1, 0.3f);
                        m = false;
                    }

                    if (input.isKeyPressed(Input.KEY_Y)) {
                        k1.changeSideAttack();
                        Client.send("side special ", Client.id);
                        if (left) {
                            HitBox h = new RectHB((int) k1.getX(), (int) k1.getY() + 110, 100, 15);
                            if (k1.detectHit(h, kims[0])) {
                                Client.send("hit left", Client.id);
                                kims[0].changeLeft();
                                kims[0].walk(-25);
                            }
                        } else {
                            HitBox h = new RectHB((int) k1.getX() + 190, (int) k1.getY() + 110, 100, 15);
                            if (k1.detectHit(h, kims[0])) {
                                Client.send("hit right", Client.id);
                                kims[0].changeRight();
                                kims[0].walk(25);
                            }
                        }
                        sound.play(1, 0.3f);
                        m = false;
                    }
                    if (input.isKeyPressed(Input.KEY_I)) {
                        k1.changeUpSpecial();
                        m = false;
                    }
                    if (input.isKeyPressed(Input.KEY_U)) {
                        k1.changeUlt();
                        Client.send("ult ", Client.id);
                        m = false;
                    }
                    if (input.isKeyPressed(controls[UP]) && !jumping) { //!jumping is so you can't jump mid jump
                        Client.send("jump ", Client.id);
                        jumping = true;
                        try {
                            initY = k1.getY(); //sets the initial y of Kim (this is only temporary)
                            k1.changeJump();
                            jump2(gc, k1); //using the updated "jump2" method
                        } catch (InterruptedException ex) {
                        }
                        m = false;
                    }

                    if (input.isKeyDown(controls[LEFT])) {
                        k1.walk(-2);
                        Client.send("left " + "x:" + k1.getX() + "y:"+k1.getY(), Client.id);
                        m = true;
                    } else if (input.isKeyDown(controls[RIGHT])) {
                        k1.walk(2);
                        Client.send("right " + "x:" + k1.getX() + "y:"+k1.getY(), Client.id);
                        m = true;
                    } else if (m) {
                        k1.idle();
                    }
                    if (!(input.isKeyDown(controls[LEFT]) || input.isKeyDown(controls[RIGHT]) || input.isKeyDown(Input.KEY_T) || input.isKeyDown(Input.KEY_V) || input.isKeyDown(Input.KEY_Y) || input.isKeyDown(Input.KEY_I) || input.isKeyDown(Input.KEY_U) || input.isKeyDown(controls[UP]))) {
                        Client.send("stop ", Client.id);
                        k1.idle();
                    }

                    if (Client.recievedData.contains("jump")) {
                        jumpings[0] = true;
                        try {
                            initY = kims[0].getY(); //sets the initial y of Kim (this is only temporary)
                            kims[0].changeJump();
                            jump2(gc, kims[0]); //using the updated "jump2" method
                            //kims[0].setX();
                            //kims[0].setY();
                        } catch (InterruptedException ex) {
                        }
                    }
                    if (Client.recievedData.contains("turn left")) {
                        kims[0].changeLeft();
                    }
                    if (Client.recievedData.contains("turn right")) {
                        kims[0].changeRight();
                    }
                    if (Client.recievedData.contains("left")) {

                        kims[0].walk(2f);

                    }

                    if (Client.recievedData.contains("hit left")) {
                        k1.walk(-25);
                    }

                    if (Client.recievedData.contains("hit right")) {
                        k1.walk(25);
                    }
                    if (Client.recievedData.contains("right")) {

                        kims[0].walk(2f);

                    }

                    if (Client.recievedData.contains("attack")) {
                        kims[0].changeAttack();
                        sound.play(1, 0.3f);
                    }
                    if (Client.recievedData.contains("side special")) {
                        kims[0].changeSideAttack();
                        sound.play(1, 0.3f);
                    }
                    if (Client.recievedData.contains("ult")) {
                        kims[0].changeUlt();
                    }
                    if (Client.recievedData.contains("stop")) {
                        kims[0].idle();
                    }
                } else {
                    if (input.isKeyPressed(controls[LEFT])) {
                        k1.changeLeft();
                        Server2.send("turn left ", 1);
                        //k1.idle();
                    }
                    if (input.isKeyPressed(controls[RIGHT])) {
                        k1.changeRight();
                        Server2.send("turn right ", 1);
                        //k1.idle();
                    }
                    if (input.isKeyPressed(Input.KEY_T)) {
                        k1.changeAttack();
                        Server2.send("attack ", 1);
                        if (left) {
                            HitBox h = new CircleHB((int) k1.getX() + 60, (int) k1.getY() + 50, 135, 110);
                            if (k1.detectHit(h, kims[0])) {
                                Server2.send("hit left", Client.id);
                                kims[0].changeLeft();
                                kims[0].walk(-55);
                            }
                        } else {
                            HitBox h = new CircleHB((int) k1.getX() + 120, (int) k1.getY() + 50, 135, 110);
                            if (k1.detectHit(h, kims[0])) {
                                Server2.send("hit right", Client.id);
                                kims[0].changeRight();
                                kims[0].walk(55);
                            }
                        }
                        sound.play(1, 0.3f);
                        m = false;
                        //System.out.println(m);

                        //k1.draw();
                    }
                    if (input.isKeyPressed(Input.KEY_V)) {
                        k1.changeUpAttack();
                        sound.play(1, 0.3f);
                        m = false;
                    }

                    if (input.isKeyPressed(Input.KEY_Y)) {
                        k1.changeSideAttack();
                        Server2.send("side special ", 1);
                        if (left) {
                            HitBox h = new RectHB((int) k1.getX(), (int) k1.getY() + 110, 100, 15);
                            if (k1.detectHit(h, kims[0])) {
                                Server2.send("hit left", Client.id);
                                kims[0].changeLeft();
                                kims[0].walk(-25);
                            }
                        } else {
                            HitBox h = new RectHB((int) k1.getX() + 190, (int) k1.getY() + 110, 100, 15);
                            if (k1.detectHit(h, kims[0])) {
                                Server2.send("hit right", Client.id);
                                kims[0].changeRight();
                                kims[0].walk(25);
                            }
                        }
                        sound.play(1, 0.3f);
                        m = false;
                    }
                    if (input.isKeyPressed(Input.KEY_I)) {
                        k1.changeUpSpecial();
                        m = false;
                    }
                    if (input.isKeyPressed(Input.KEY_U)) {
                        k1.changeUlt();
                        Server2.send("ult ", 1);
                        m = false;
                    }
                    if (input.isKeyPressed(controls[UP]) && !jumping) { //!jumping is so you can't jump mid jump
                        Server2.send("jump ", 1);
                        jumping = true;
                        try {
                            initY = k1.getY(); //sets the initial y of Kim (this is only temporary)
                            k1.changeJump();
                            jump2(gc, k1); //using the updated "jump2" method
                        } catch (InterruptedException ex) {
                        }
                        m = false;
                    }

                    if (input.isKeyDown(controls[LEFT])) {
                        k1.walk(-2);
                        Server2.send("left " + k1.getX(), 1);
                        m = true;
                    } else if (input.isKeyDown(controls[RIGHT])) {
                        k1.walk(2);
                        Server2.send("right " + k1.getX(), 1);
                        m = true;
                    } else if (m) {
                        System.out.println(m);
                        k1.idle();
                    }
                    if (!(input.isKeyDown(controls[LEFT]) || input.isKeyDown(controls[RIGHT]) || input.isKeyDown(Input.KEY_T) || input.isKeyDown(Input.KEY_V) || input.isKeyDown(Input.KEY_Y) || input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_U) || input.isKeyDown(controls[UP]))) {
                        Server2.send("stop", 1);
                        k1.idle();
                    }

                    if (Server2.recievedData.contains("turn left ")) {
                        kims[0].changeLeft();
                    }
                    if (Server2.recievedData.contains("turn right ")) {
                        kims[0].changeRight();
                    }
                    if (Server2.recievedData.contains("left ")) {

                        kims[0].walk(-2f);

                    }
                }
                if (Server2.recievedData.contains("hit left")) {
                    k1.walk(-25);
                }

                if (Server2.recievedData.contains("hit right")) {
                    k1.walk(25);
                }
                if (Server2.recievedData.contains("right ")) {

                    kims[0].walk(2f);

                }
                if (Server2.recievedData.contains("jump ")) {
                    jumpings[0] = true;
                    try {
                        initY = kims[0].getY(); //sets the initial y of Kim (this is only temporary)
                        kims[0].changeJump();
                        jump2(gc, kims[0]); //using the updated "jump2" method
                    } catch (InterruptedException ex) {
                    }
                }
                if (Server2.recievedData.contains("side special ")) {
                    kims[0].changeSideAttack();
                    sound.play(1, 0.3f);
                }
                if (Server2.recievedData.contains("attack ")) {
                    kims[0].changeAttack();
                    sound.play(1, 0.3f);
                }
                if (Server2.recievedData.contains("ult ")) {
                    kims[0].changeUlt();
                }
                if (Server2.recievedData.contains("stop ")) {
                    kims[0].idle();
                }
            } catch (IOException e) {
            }
            //x left bound is -300 x right bound is 900
            //
            //System.out.println("x: " + k1.getX() + " y: " + k1.getY());
            //System.out.println("jumping: " + jumping);
            //System.out.println("currentY: " + k1.getY());
            //System.out.println("initY: " + initY);

            if (k1.getX() < -24 || k1.getX() > 714 || k1.getY() > 400) { //if off platform fall
                try {
                    fall(gc);
                } catch (InterruptedException ex) {
                }
            }
            if (!CorS) {
                if (Server2.numLives < 1) {
                    Server2.send("selection", 1);
                    sbg.enterState(4);
                }
            }
            System.out.println(k1.getX() + " " + k1.getY());
            if (k1.getY() > 660 || k1.getX() < -160 || k1.getX() > 950) { //if out of arena play a sound and reset position //TO-DO subtract lives
                dead = true;
                if (k1.getY() > 660) {
                    k1.ded(0);
                } else if (k1.getX() < -160) {
                    k1.ded(1);
                } else if (k1.getX() > 950) {
                    k1.ded(3);
                }
                if (dead) {
                    k1.setX(60);
                    k1.setY(345);
                    if (CorS) {
                        Client.numLives--;
                        try {
                            Client.send("life", Client.id);
                        } catch (IOException ex) {
                        }
                    } else {
                        Server2.numLives--;
                        Server2.send("life", 1);
                    }
                }
                death.play();
            }
            if (CorS) {
                if (Client.numLives < 1) {
                    try {
                        Client.send("dead", Client.id);
                    } catch (IOException ex) {
                    }
                }
                if (Client.recievedData.contains("dead")) {
                    kims[0] = new KimBoi(1000000000, 100000000, idle);
                }
                if (Client.recievedData.contains("selection")) {
                    sbg.enterState(5);
                }
                if (Client.recievedData.contains("life")) {
                    if (kims[0].getY() > 660) {
                        kims[0].ded(0);
                    } else if (kims[0].getX() < -160) {
                        kims[0].ded(1);
                    } else if (kims[0].getX() > 950) {
                        kims[0].ded(2);
                    }
                }
            } else {
                if (Server2.recievedData.contains("dead")) {
                    Server2.numAlive--;
                }
                if (Server2.numLives < 1) {
                    Server2.send("dead", 1);
                }
                if (Server2.numAlive < 2 && Server2.getNumPlayers() > 1) {
                    Server2.send("selection", 1);
                    sbg.enterState(4);
                }
                if (Server2.recievedData.contains("life")) {
                    if (kims[0].getY() > 660) {
                        kims[0].ded(0);
                    } else if (kims[0].getX() < -160) {
                        kims[0].ded(1);
                    } else if (kims[0].getX() > 950) {
                        kims[0].ded(2);
                    }
                }
            }
        } else {

            Input input = gc.getInput();
            try {
                if (CorS && dead == false) {
                    if (input.isKeyPressed(controls[LEFT])) {
                        k1.changeLeft();
                        left = true;
                        Client.send("turn left ", Client.id);
                        //k1.idle();
                    }
                    if (input.isKeyPressed(controls[RIGHT])) {
                        k1.changeRight();
                        left = false;
                        Client.send("turn right ", Client.id);
                        //k1.idle();
                    }
                    if (input.isKeyPressed(Input.KEY_T)) {
                        k1.changeAttack();
                        sound.play(1, 0.3f);
                        m = false;
                        Client.send("attack ", Client.id);

                    }
                    if (input.isKeyPressed(Input.KEY_V)) {
                        k1.changeUpAttack();

                        sound.play(1, 0.3f);
                        m = false;
                    }

                    if (input.isKeyPressed(Input.KEY_Y)) {
                        k1.changeSideAttack();
                        Client.send("side special ", Client.id);

                        sound.play(1, 0.3f);
                        m = false;
                    }
                    if (input.isKeyPressed(Input.KEY_S)) {
                        k1.changeUpSpecial();
                        m = false;
                    }
                    if (input.isKeyPressed(Input.KEY_U)) {
                        k1.changeUlt();
                        Client.send("ult ", Client.id);
                        m = false;
                    }
                    if (input.isKeyPressed(controls[UP]) && !jumping) { //!jumping is so you can't jump mid jump
                        Client.send("jump ", Client.id);
                        jumping = true;
                        try {
                            initY = k1.getY(); //sets the initial y of Kim (this is only temporary)
                            k1.changeJump();
                            jump2(gc, k1); //using the updated "jump2" method
                        } catch (InterruptedException ex) {
                        }
                        m = false;
                    }

                    if (input.isKeyDown(controls[LEFT])) {
                        k1.walk(-2);
                        Client.send("left ", Client.id);
                        m = true;
                    } else if (input.isKeyDown(controls[RIGHT])) {
                        k1.walk(2);
                        Client.send("right ", Client.id);
                        m = true;
                    } else if (m) {
                        k1.idle();
                    }
                    if (!(input.isKeyDown(controls[LEFT]) || input.isKeyDown(controls[RIGHT]) || input.isKeyDown(Input.KEY_T) || input.isKeyDown(Input.KEY_V) || input.isKeyDown(Input.KEY_Y) || input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_U) || input.isKeyDown(controls[UP]))) {
                        Client.send("stop ", Client.id);
                        k1.idle();
                    }

                } else {
                    if (!dead) {
                        if (input.isKeyPressed(controls[LEFT])) {
                            k1.changeLeft();
                            Server2.send("turn left ", 1);
                            //k1.idle();
                        }
                        if (input.isKeyPressed(controls[RIGHT])) {
                            k1.changeRight();
                            Server2.send("turn right ", 1);
                            //k1.idle();
                        }
                        if (input.isKeyPressed(Input.KEY_T)) {
                            k1.changeAttack();
                            Server2.send("attack ", 1);
                            sound.play(1, 0.3f);
                            m = false;
                            //System.out.println(m);

                            //k1.draw();
                        }
                        if (input.isKeyPressed(Input.KEY_V)) {
                            k1.changeUpAttack();
                            sound.play(1, 0.3f);
                            m = false;
                        }

                        if (input.isKeyPressed(Input.KEY_Y)) {
                            k1.changeSideAttack();
                            Server2.send("side special ", 1);
                            sound.play(1, 0.3f);
                            m = false;
                        }
                        if (input.isKeyPressed(Input.KEY_I)) {
                            k1.changeUpSpecial();
                            m = false;
                        }
                        if (input.isKeyPressed(Input.KEY_U)) {
                            k1.changeUlt();
                            Server2.send("ult ", 1);
                            m = false;
                        }
                        if (input.isKeyPressed(controls[UP]) && !jumping) { //!jumping is so you can't jump mid jump
                            Server2.send("jump ", 1);
                            jumping = true;
                            try {
                                initY = k1.getY(); //sets the initial y of Kim (this is only temporary)
                                k1.changeJump();
                                jump2(gc, k1); //using the updated "jump2" method
                            } catch (InterruptedException ex) {
                            }
                            m = false;
                        }
                        if (k1.getX() < -24 || k1.getX() > 714 || k1.getY() > 400) { //if off platform fall
                            try {
                                fall(gc);
                            } catch (InterruptedException ex) {
                            }
                        }
                        if (input.isKeyDown(controls[LEFT])) {
                            k1.walk(-2);
                            Server2.send("left ", 1);
                            m = true;
                        } else if (input.isKeyDown(controls[RIGHT])) {
                            k1.walk(2);
                            Server2.send("right ", 1);
                            m = true;
                        } else if (m) {
                            System.out.println(m);
                            k1.idle();
                        }
                        if (!(input.isKeyDown(controls[LEFT]) || input.isKeyDown(controls[RIGHT]) || input.isKeyDown(Input.KEY_T) || input.isKeyDown(Input.KEY_V) || input.isKeyDown(Input.KEY_Y) || input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_U) || input.isKeyDown(controls[UP]))) {
                            Server2.send("stop", 1);
                            k1.idle();
                        }
                    }
                    if (k1.getY() > 660 || k1.getX() < -160 || k1.getX() > 950) { //if out of arena play a sound and reset position //TO-DO subtract lives
                        if (!dead) {
                            death.play();
                            if (k1.getY() > 660) {
                                k1.ded(0);
                            } else if (k1.getX() < -160) {
                                k1.ded(1);
                            } else if (k1.getX() > 950) {
                                k1.ded(3);
                            }
                        }
                        dead = true && !k1.anim.isStopped();
                        if (!dead) {
                            k1.setX(60);
                            k1.setY(345);
                            if (CorS) {
                                Client.numLives--;
                                try {
                                    Client.send("life", Client.id);
                                } catch (IOException ex) {
                                }
                            } else {
                                Server2.numLives--;
                                Server2.send("life", 1);
                            }
                        }

                    }

                }
            } catch (IOException e) {
            }
        }
    }

    /*
     * public void jump(GameContainer gc) throws InterruptedException { final
     * Input input = gc.getInput(); Thread a = new Thread(new Runnable() {
     * @Override public void run() { int i = 0; while (i < 35 &&
     * input.isKeyDown(controls[UP])) { k1.setY(k1.getY() - 3); try {
     * Thread.sleep(10); } catch (InterruptedException ex) { } i++; } while (i >
     * -1) { k1.setY(k1.getY() + 3); try { Thread.sleep(10); } catch
     * (InterruptedException ex) { } i--; } i = 0; } }); a.start(); }
     */
    public void jump2(GameContainer gc, final KimBoi k) throws InterruptedException {
        final Input input = gc.getInput();

        Thread a = new Thread(new Runnable() {

            @Override
            public void run() {
                int scale = 7;
                while (k.getY() > initY - 180 && input.isKeyDown(controls[UP])) {
                    k.setY(k.getY() - (3 + scale));//going up
                    scale = Math.max(0, scale - 1);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                    }
                }
                scale = 0;
                while (k.getY() < initY - 5) {
                    k.setY(k.getY() + (3 + scale));//going down
                    scale = Math.min(5, scale + 1);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                    }
                }
                k.setY(initY);
                jumping = false;
            }
        });
        a.start();
    }

    public void fall(GameContainer gc) throws InterruptedException {
        Thread a = new Thread(new Runnable() {

            @Override
            public void run() {
                //temp++;//BLOCK START
                //if (temp % 2 == 0) {
                //if (!dead) {
                k1.setY(k1.getY() + 1);//this block "slows" down the falling of Kim
                //}                //    temp = 0;
                //}//BLOCK END
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }
            }
        });
        a.start();
    }

    public void init2() throws SlickException {
        if (Client.inGame) {
            CorS = true;
        } else {
            CorS = false;
        }
        int first = 60;
        if (CorS) {
            first += 90;
        }
        k1 = new KimBoi(first, 345, idle);
        m = true;
        k1.start();
        numPlayers = Math.max(Client.getNumPlayers(), Server2.getNumPlayers());
        kims = new KimBoi[numPlayers - 1];
        int second = 150;
        for (int i = 0; i < numPlayers - 1; i++) {
            if (CorS) {
                second -= 90;
            }
            kims[i] = new KimBoi(second + 90 * i, 345, idle);
            kims[i].start();
            kims[i].idle();
        }
        System.out.println("CorS: " + CorS);
        gen = false;
    }
}