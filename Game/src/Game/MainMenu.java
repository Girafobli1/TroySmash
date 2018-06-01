package Game;

import java.awt.Font;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.gui.*;
import org.lwjgl.input.Mouse;
import java.net.UnknownHostException;
import java.io.*;
import java.util.regex.Pattern;

/**
 * The MainMenu class is built as a state for the main menu page. It can branch
 * off to different states.
 *
 */
public class MainMenu extends BasicGameState {

    //mouse inputs
    MouseOverArea joinServerMouse;
    MouseOverArea howMouse;
    MouseOverArea settingMouse;
    MouseOverArea exitMouse;
    MouseOverArea backMouse;
    MouseOverArea joinMouse;
    MouseOverArea createMouse;
    MouseOverArea playAttackMouse;
    MouseOverArea playFinalMouse;
    MouseOverArea ipMouse;
    MouseOverArea volumeMouse;
    MouseOverArea soundMouse;
    MouseOverArea defaultMouse;
    MouseOverArea leftieMouse;
    String mouse = "";
    //state of MainMenu
    private int state;
    //audio variables
    private boolean playSound;
    private Music music;
    private Sound sound;
    private Sound no;
    private Music opening;
    private float volume;
    private float soundVolume;
    private boolean introPlayed = false;
    //visual variables
    private final float backSize;
    private Image BackButton1;
    private Image BackButton2;
    private Image JoinButton1;
    private Image JoinButton2;
    private Image SettingsButton1;
    private Image SettingsButton2;
    private Image HowToPlayButton1;
    private Image HowToPlayButton2;
    private Image ExitButton1;
    private Image ExitButton2;
    private Image logo;
    private Image Background;
    private Image[] optionsMAIN;
    private Image[][] menuOptions;
    //how to play vars
    private int whichAnim;
    private Image instructions;
    private Image playAttack;
    private Image playFinal;
    private boolean clicked;
    private Animation[] anims;
    private SpriteSheet KimAttack;
    private Animation KimAttack1;
    private SpriteSheet KimUlt;
    private Animation KimUlt1;
    private Image[] optionsHOW;
    //join vars
    private Image Join1;
    private Image Join2;
    private Image Create1;
    private Image Create2;
    private TextField typeIP;
    private Image[] optionsJOIN;
    //options vars
    private Image volumeSlider1;
    private Image volumeSlider2;
    private Image slider1;
    private Image slider2;
    private float sliderX;
    private Image soundSlider1;
    private Image soundSlider2;
    private Image slider1s;
    private Image slider2s;
    private float sliderXs;
    private Image[] optionsOPTIONS;
    private Image WASD;
    private Image ARROWS;
    private Image leftie1;
    private Image leftie2;
    private Image default1;
    private Image default2;
    private String moves;
    //text variable
    private Font font;
    private Font smallfont;
    private TrueTypeFont ttf;
    private TrueTypeFont subfont;
    //choice variables
    private int[] playerChoices = new int[]{0, 1, 0, 0}; //beginning player choices
    private int menuStatus;
    private final int[] numChoices = new int[]{4, 4, 3, 5}; //num of choices in each menu
    //offset (spacing) variables
    private final int betweenMainButtons;
    private final int topMainButtonSpacing;
    //width and height variables
    private final int width = 1000;
    private final int height = 800;

    Conf conf;
    boolean connect;

    /**
     * Initializes the MainMenu class. The state number and the spacings are set
     * up.
     *
     * @param s
     */
    public MainMenu(int s) {
        //super(s);
        state = s;
        betweenMainButtons = 150;
        topMainButtonSpacing = 140;
        backSize = 0.4f;
    }

    /**
     * Returns the ID of this state. It is necessary to complete the realization
     * of the interface GameState.
     *
     * @return state
     */
    @Override
    public int getID() {
        return state;
    }

    /**
     * Initializes this state. It is necessary to complete the realization of
     * the interface GameState. Audio, visual, text, and choice variables are
     * initialized.
     *
     * @param gc
     * @param sbg
     * @throws org.newdawn.slick.SlickException
     */
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        conf = new Conf();
        conf.buildConf();
        opening = new Music("sound/music/main_intro.ogg");
        opening.play(1, volume);
        font = new Font("Showcard Gothic", Font.BOLD, 50);
        smallfont = new Font("Showcard Gothic", Font.BOLD, 30);
        ttf = new TrueTypeFont(font, true);
        subfont = new TrueTypeFont(smallfont, true);
        //main menu vars---------------------------------------------------
        JoinButton1 = new Image("res/JoinServerButton1.png");
        JoinButton2 = new Image("res/JoinServerButton2.png");
        HowToPlayButton1 = new Image("res/HowToPlayButton1.png");
        HowToPlayButton2 = new Image("res/HowToPlayButton2.png");
        SettingsButton1 = new Image("res/SettingsButton1.png");
        SettingsButton2 = new Image("res/SettingsButton2.png");
        ExitButton1 = new Image("res/ExitButton1.png");
        ExitButton2 = new Image("res/ExitButton2.png");
        optionsMAIN = new Image[]{JoinButton1, JoinButton2, HowToPlayButton1, HowToPlayButton2, SettingsButton1, SettingsButton2, ExitButton1, ExitButton2};
        //mouse fields
        joinServerMouse = new MouseOverArea(gc, JoinButton1, 80, 155, 195, 90);
        howMouse = new MouseOverArea(gc, JoinButton1, 80, 305, 195, 90);
        settingMouse = new MouseOverArea(gc, JoinButton1, 80, 455, 195, 90);
        exitMouse = new MouseOverArea(gc, JoinButton1, 80, 605, 195, 90);
        backMouse = new MouseOverArea(gc, BackButton1, 26, 23, 164, 75);
        joinMouse = new MouseOverArea(gc, JoinButton1, 757, 370, 110, 58);
        createMouse = new MouseOverArea(gc, Create1, 415, 530, 124, 56);
        ipMouse = new MouseOverArea(gc, Create1, 200, 372, 548, 56);
        volumeMouse = new MouseOverArea(gc, volumeSlider1, 20, 254, 460, 81);
        soundMouse = new MouseOverArea(gc, volumeSlider1, 20, 384, 460, 81);
        defaultMouse = new MouseOverArea(gc, volumeSlider1, 590, 261, 199, 100);
        leftieMouse = new MouseOverArea(gc, volumeSlider1, 790, 261, 199, 100);
        int animOffset = 125;
        playAttackMouse = new MouseOverArea(gc, playAttack, 480, 260, 100, 100); //change this X/Y to affect the rest
        playFinalMouse = new MouseOverArea(gc, playAttack, playAttackMouse.getX(), playAttackMouse.getY() + animOffset, 100, 100);
        //-----------------------------------------------------------------
        //join server vars-------------------------------------------------
        Join1 = new Image("res/JoinButton1.png");
        Join2 = new Image("res/JoinButton2.png");
        Create1 = new Image("res/CreateButton1.png");
        Create2 = new Image("res/CreateButton2.png");
        typeIP = new TextField(gc, ttf, 200, 370, 550, 60);
        optionsJOIN = new Image[]{BackButton1, BackButton2, Create1, Create2, Join1, Join2};
        //-----------------------------------------------------------------
        //how to play vars-------------------------------------------------
        instructions = new Image("res/Instructions.png");
        playAttack = new Image("res/AttackPlayAnimation.png");
        playFinal = new Image("res/FinalPlayAnimation.png");
        KimAttack = new SpriteSheet("res/resized/Attack.png", 300, 200);
        KimAttack1 = new Animation();
        KimUlt = new SpriteSheet("res/resized/Ult.png", 300, 200);
        KimUlt1 = new Animation();
        addFramesToAnims();
        KimUlt1.setPingPong(true);
        optionsHOW = new Image[]{BackButton1, BackButton2};
        anims = new Animation[]{KimAttack1, KimUlt1};
        whichAnim = 0;
        //-----------------------------------------------------------------
        //options vars
        volumeSlider1 = new Image("res/VolumeSlider1.png");
        volumeSlider2 = new Image("res/VolumeSlider2.png");
        slider1 = new Image("res/Slider1.png");
        slider2 = new Image("res/Slider2.png");
        sliderX = Math.round(conf.confVolume * 365); //185 is middle
        soundSlider1 = new Image("res/VolumeSlider1.png");
        soundSlider2 = new Image("res/VolumeSlider2.png");
        slider1s = new Image("res/Slider1.png");
        slider2s = new Image("res/Slider2.png");
        sliderXs = Math.round(conf.confSound * 365);//185 is middle
        WASD = new Image("res/WASD.png");
        ARROWS = new Image("res/ARROWS.png");
        default1 = new Image("res/DefaultMove1.png");
        default2 = new Image("res/DefaultMove2.png");
        leftie1 = new Image("res/LeftieMove1.png");
        leftie2 = new Image("res/LeftieMove2.png");
        moves = conf.confMoves;
        optionsOPTIONS = new Image[]{BackButton1, BackButton2, volumeSlider1, volumeSlider2, slider1, slider2, soundSlider1, soundSlider2, slider1s, slider2s, default1, default2, leftie1, leftie2, ARROWS, WASD};
        //-----------------------------------------------------------------
        //generic vars-----------------------------------------------------
        Background = new Image("res/Menu_Background.png");
        logo = new Image("res/logo.png");
        music = new Music("sound/music/main_body.ogg");
        sound = new Sound("sound/yes.ogg");
        no = new Sound("sound/no.ogg");
        BackButton1 = new Image("res/BackButton1.png");
        BackButton2 = new Image("res/BackButton2.png");
        volume = conf.confVolume;
        soundVolume = conf.confSound;
        playSound = true;
        //-----------------------------------------------------------------

    }

    /**
     * Renders this state to the game's graphics context. It is necessary to
     * complete the realization of the interface GameState. The background and
     * logo are drawn. This method also takes care of changing images when
     * "selected" by calling the renderOptions() method.
     *
     * @param gc
     * @param sbg
     * @param g
     * @throws org.newdawn.slick.SlickException
     */
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        //ttf.drawString(100, 100, "TROY SMASH", c2);
        //logo.draw(15, 0, .42f);
        Background.draw(0, 0, .5f);
        renderOptions();
        g.drawString(mouse, 50, 50);
        switch (menuStatus) {
            case 0:
                logo.draw(230, 150, .9f);
                break;
            case 1:
                ttf.drawString(100, 120, "Join Server");
                typeIP.render(gc, g);
                break;
            case 2:
                instructions.draw(30, 220, .7f);
                ttf.drawString(100, 120, "Instructions");
                playAttack.draw(playAttackMouse.getX(), playAttackMouse.getY());
                playFinal.draw(playFinalMouse.getX(), playFinalMouse.getY());
                switch (whichAnim) {
                    case 0:
                        KimAttack1.draw(570, 270);
                        break;
                    case 1:
                        KimUlt1.draw(570, 270);
                        break;
                }
                break;
            case 3:
                ttf.drawString(100, 120, "Settings");
                subfont.drawString(130, 210, "Music Volume");
                subfont.drawString(500, 275, ": " + (int) (100 * (sliderX / 365)));
                subfont.drawString(130, 340, "Sound Volume");
                subfont.drawString(500, 405, ": " + (int) (100 * (sliderXs / 365)));
                subfont.drawString(670, 210, "In-Game Controls");
                subfont.drawString(715, 390, "Movement");
        }//end of switch
    }

    /**
     * Updates the state's logic, can be based on the amount of time that has
     * passed. It is necessary to complete the realization of the interface
     * GameState. If the up arrow is pressed, the selection box will move up, if
     * possible. If the down arrow is pressed, the selection box will move down,
     * if possible. The state will enter whatever state the selection box tells
     * it to go to when the enter button is pressed. If the selection box is the
     * last one, the exit button, the game will exit instead. If the state
     * detects that the intro music has already ended, it will call for the
     * looping for the main song.
     *
     * @param gc
     * @param sbg
     * @param delta
     * @throws org.newdawn.slick.SlickException
     */
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        if (introPlayed && !music.playing()) {
            music.loop(1, volume);
        } else if (!introPlayed && !opening.playing()) {
            introPlayed = true;
        }

        Input input = gc.getInput();
        mouse = Mouse.getX() + " " + (800 - Mouse.getY());

        if (menuStatus == 0) {
            if (joinServerMouse.isMouseOver()) {
                playerChoices[0] = 0;
            } else if (howMouse.isMouseOver()) {
                playerChoices[0] = 1;
            } else if (settingMouse.isMouseOver()) {
                playerChoices[0] = 2;
            } else if (exitMouse.isMouseOver()) {
                playerChoices[0] = 3;
            }

        } else {
            if (backMouse.isMouseOver()) {
                playerChoices[menuStatus] = 0;
            }
            if (menuStatus == 1) {
                if (ipMouse.isMouseOver() && Mouse.isButtonDown(0)) {
                    playerChoices[1] = 1;
                } else if (joinMouse.isMouseOver()) {
                    playerChoices[1] = 2;
                } else if (createMouse.isMouseOver()) {
                    playerChoices[1] = 3;
                }
            } else if (menuStatus == 2) {
                if (playAttackMouse.isMouseOver()) {
                    playerChoices[2] = 1;
                } else if (playFinalMouse.isMouseOver()) {
                    playerChoices[2] = 2;
                }
            } else if (menuStatus == 3) {
                if (volumeMouse.isMouseOver()) {
                    playerChoices[3] = 1;
                }
                if (soundMouse.isMouseOver()) {
                    playerChoices[3] = 2;
                }
                if (defaultMouse.isMouseOver()) {
                    playerChoices[3] = 3;
                }
                if (leftieMouse.isMouseOver()) {
                    playerChoices[3] = 4;
                }
            }
        }
        if (playerChoices[3] == 1) {
            if (input.isKeyDown(Input.KEY_LEFT)) {
                if (sliderX > 0) {
                    sliderX -= .365;
                }
            } else if (input.isKeyDown(Input.KEY_RIGHT)) {
                if (sliderX < 365) {
                    sliderX += .365;
                }
            }
        }
        if (playerChoices[3] == 2) {
            if (input.isKeyDown(Input.KEY_LEFT)) {
                if (sliderXs > 0) {
                    sliderXs -= .365;
                }
            } else if (input.isKeyDown(Input.KEY_RIGHT)) {
                if (sliderXs < 365) {
                    sliderXs += .365;
                }
            }
        }
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            no.play();
            menuStatus = 0;
        }
        if (input.isKeyPressed(Input.KEY_UP)) {
            if (playerChoices[menuStatus] != 0) {
                playerChoices[menuStatus]--;
            }
            //System.out.println(playerChoices[menuStatus]);
        }
        if (input.isKeyPressed(Input.KEY_DOWN)) {
            if (playerChoices[menuStatus] < numChoices[menuStatus] - 1) {
                playerChoices[menuStatus]++;
            }
            //System.out.println(playerChoices[menuStatus]);
        }
        if (input.isKeyPressed(Input.KEY_ENTER)
                || (Mouse.isButtonDown(0)
                && ((menuStatus == 0 && (joinServerMouse.isMouseOver() || howMouse.isMouseOver() || settingMouse.isMouseOver() || exitMouse.isMouseOver()))
                || (menuStatus != 0 && backMouse.isMouseOver()) || (menuStatus == 1 && (joinMouse.isMouseOver() || createMouse.isMouseOver()))
                || (menuStatus == 3 && (volumeMouse.isMouseOver() || soundMouse.isMouseOver() || defaultMouse.isMouseOver() || leftieMouse.isMouseOver())) || (menuStatus == 2 && (playAttackMouse.isMouseOver() || playFinalMouse.isMouseOver()))))) {
            if ((menuStatus == 0 && playerChoices[menuStatus] < numChoices[menuStatus] - 1) || (playerChoices[menuStatus] < numChoices[menuStatus] && menuStatus != 0)) {

                //System.out.println(menuStatus);
                if (menuStatus != 0 && playerChoices[menuStatus] == 0) {
                    menuStatus = 0;
                    return;
                }
                switch (menuStatus) {
                    case 0: //main menu
                        if (playerChoices[menuStatus] == 0) {
                            menuStatus = 1;
                        } else if (playerChoices[menuStatus] == 1) {
                            menuStatus = 2;
                        } else if (playerChoices[menuStatus] == 2) {
                            menuStatus = 3;
                        }
                        break;
                    case 1: //join server menu
                        if ((playerChoices[menuStatus] == 1) || playerChoices[menuStatus] == 2) { //join
                            input.clearMousePressedRecord();
                            input.clearKeyPressedRecord();
                            playSound = false;
                            if (Pattern.matches("\\d*\\.\\d*\\.\\d*\\.\\d*", typeIP.getText())) {
                                System.out.println("Attempting to connect...");
                                try {
                                    Client.main(typeIP.getText(), gc);
                                    Client.setVolume(volume);
                                    Client.setSoundVolume(soundVolume);
                                    music.stop();
                                    sbg.enterState(5);
                                } catch (UnknownHostException e) {
                                } catch (IOException e) {
                                }
                            } else {
                                System.out.println("Invalid IP address.");
                            }
                        } else if (playerChoices[menuStatus] == 3) { //create
                            music.stop();
                            input.clearMousePressedRecord();
                            input.clearKeyPressedRecord();
                            Server2.main(new String[]{}, gc);
                            Server2.setVolume(volume);
                            Server2.setSoundVolume(soundVolume);
                            sbg.enterState(4);
                        }
                        break;
                    case 2: //how to play menu
                        if (playerChoices[2] == 1) {
                            playSound = false;
                            whichAnim = 0;
                        } else if (playerChoices[2] == 2) {
                            playSound = false;
                            whichAnim = 1;
                        }
                        //System.out.println(whichAnim);
                        break;
                    case 3: //settings menu
                        if (playerChoices[3] == 1) {
                            playSound = false;
                            sliderX = Mouse.getX() - 60;
                            if (sliderX < 0) {
                                sliderX = 0;
                            } else if (sliderX > 365) {
                                sliderX = 365;
                            }
                        } else if (playerChoices[3] == 2) {
                            playSound = false;
                            sliderXs = Mouse.getX() - 60;
                            if (sliderXs < 0) {
                                sliderXs = 0;
                            } else if (sliderXs > 365) {
                                sliderXs = 365;
                            }
                        } else if (playerChoices[3] == 3) {
                            playSound = false;
                            moves = "default";
                            conf.setControls("default");
                        } else if (playerChoices[3] == 4) {
                            playSound = false;
                            moves = "leftie";
                            conf.setControls("leftie");

                        }
                }
            } else {
                gc.exit();
            }
            if (playSound) {
                sound.play(1, soundVolume);
            } else {
                playSound = true;
            }
            input.clearMousePressedRecord();
            input.clearKeyPressedRecord();
        }
        //System.out.println(sliderXs);
        //System.out.println(playerChoices[menuStatus]);
    }

    /**
     * Flips between the different images for each state.
     */
    public void renderOptions() {
        switch (menuStatus) {
            case 0: //main menu rendering
                for (int i = 0; i < numChoices[0]; i++) {
                    if (playerChoices[0] == i) {
                        optionsMAIN[i * 2 + 1].draw(60, i * betweenMainButtons + topMainButtonSpacing, .48f);
                    } else {
                        optionsMAIN[i * 2].draw(60, i * betweenMainButtons + topMainButtonSpacing, .48f);
                    }
                }
                break;
            case 1: //join menu rendering
                for (int i = 0; i < numChoices[1]; i++) {

                    if (playerChoices[1] == i) {
                        if (i == 0) {
                            optionsJOIN[i * 2 + 1].draw(10, 10, backSize); //back button
                        } else if (i == 1) {
                            typeIP.setFocus(true); //text field
                        } else if (i == 2) {
                            optionsJOIN[i * 2 + 1].draw(692, 340, .48f); //join button

                        } else {
                            optionsJOIN[3].draw(350, 500, .48f); //create button
                        }
                    } else {
                        if (i == 0) {
                            optionsJOIN[i * 2].draw(10, 10, backSize); //back button
                        } else if (i == 1) {
                            typeIP.setFocus(false); //text field
                        } else if (i == 2) {
                            optionsJOIN[i * 2].draw(692, 340, .48f); //join button
                        } else {
                            optionsJOIN[2].draw(350, 500, .48f); //create button
                        }
                    }

                }
                break;
            case 2: //how to rendering
                for (int i = 0; i < numChoices[2]; i++) {
                    if (playerChoices[2] == i) {
                        if (i == 0) {
                            optionsHOW[1].draw(10, 10, backSize);
                        }
                    } else {
                        if (i == 0) {
                            optionsHOW[0].draw(10, 10, backSize);
                        }
                    }
                }
                break;
            case 3: //options rendering
                for (int i = 0; i < numChoices[3]; i++) {
                    if (playerChoices[3] == i) {
                        if (i == 0) {
                            optionsOPTIONS[i * 2 + 1].draw(10, 10, backSize);
                        } else if (i == 1) {
                            optionsOPTIONS[3].drawCentered(250, 290);
                            optionsOPTIONS[5].draw(sliderX, 249, .5f);
                            volume = sliderX / 365;
                            music.setVolume(volume);
                            opening.setVolume(volume);
                            conf.setVolume(volume);
                        } else if (i == 2) {
                            optionsOPTIONS[7].drawCentered(250, 420);
                            optionsOPTIONS[9].draw(sliderXs, 379, .5f);
                            soundVolume = sliderXs / 365;
                            conf.setSound(soundVolume);
                        } else if (i == 3) {
                            optionsOPTIONS[11].drawCentered(690, 310);
                        } else if (i == 4) {
                            optionsOPTIONS[13].drawCentered(890, 310);
                        }
                    } else {
                        if (i == 0) {
                            optionsOPTIONS[i * 2].draw(10, 10, backSize);
                        } else if (i == 1) {
                            optionsOPTIONS[2].drawCentered(250, 290);
                            optionsOPTIONS[4].draw(sliderX, 249, .5f);
                        } else if (i == 2) {
                            optionsOPTIONS[6].drawCentered(250, 420);
                            optionsOPTIONS[8].draw(sliderXs, 379, .5f);
                        } else if (i == 3) {
                            optionsOPTIONS[10].drawCentered(690, 310);
                        } else if (i == 4) {
                            optionsOPTIONS[12].drawCentered(890, 310);
                        }
                    }
                }
                if (moves.equals("default")) {
                    optionsOPTIONS[14].drawCentered(790, 500);
                } else {
                    optionsOPTIONS[15].drawCentered(790, 500);
                }
        }//end of switch statement
    }

    public void addFramesToAnims() {
        for (int i = 0; i < 9; i++) {
            for (int x = 0; x < 1; x++) {
                KimAttack1.addFrame(KimAttack.getSprite(x, i), 90);
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < 1; x++) {
                KimUlt1.addFrame(KimUlt.getSprite(x, i), 90);
            }
        }
    }
} //last curly

