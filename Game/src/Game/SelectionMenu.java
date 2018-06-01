package Game;

import Game.Client;
import Game.Server2;
import java.awt.Font;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.gui.*;

/**
 *
 * server
 */
public class SelectionMenu extends BasicGameState {
    private MouseOverArea backMouse;
    private MouseOverArea noMouse;
    private MouseOverArea yesMouse;
    private MouseOverArea kimMouse;
    private MouseOverArea startMouse;
    String mouse = "";
    private Sound selectSound;
    private Sound noSound;
    private Music music;
    private final float backSize;
    int numPlayers = 1;
    Image[] players;
    //state of the SelectionMenu screen
    private int state;
    //font
    private Font font;
    private Font ipfont;
    private TrueTypeFont ttf;
    private TrueTypeFont iptext;
    //images
    private Image BackButton1, BackButton2;
    private Image Background;
    private Image[] options;
    private Image confirmLeave, confirmLeave2;
    private Image Kim, Kim2, KimPick;
    private Image p1, p2, p3, p4;
    private Image notConnect;
    private Image[] selectedCharacter;
    private Image startGame1, startGame2;
    //button choice variables
    private int playerChoice;
    private int numChoices;
    private boolean leaveState = false; //check if leave prompt is active. true means leavePrompt is displayed.
    private boolean playSound;

    public SelectionMenu(int s) {
        state = s;
        backSize = 0.4f;
    }

    @Override
    public int getID() {

        return state;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        font = new Font("Showcard Gothic", Font.BOLD, 51);
        ipfont = new Font("Showcard Gothic", Font.BOLD, 25);
        ttf = new TrueTypeFont(font, true);
        iptext = new TrueTypeFont(ipfont, true);

        BackButton1 = new Image("res/BackButton1.png");
        BackButton2 = new Image("res/BackButton2.png");
        Background = new Image("res/Menu_Background.png");
        confirmLeave = new Image("res/LeavePrompt.png");
        confirmLeave2 = new Image("res/LeavePrompt2.png");
        startGame1 = new Image("res/startGame1.png");
        startGame2 = new Image("res/startGame2.png");

        Kim = new Image("res/Kim_head.png");
        Kim2 = new Image("res/Kim_head2.png");
        KimPick = new Image("res/KimPick.png");

        p1 = new Image("res/Player1Connect.png");
        p2 = new Image("res/Player2Connect.png");
        p3 = new Image("res/Player3Connect.png");
        p4 = new Image("res/Player4Connect.png");
        notConnect = new Image("res/NotConnected.png");

        players = new Image[]{p1, p2, p3, p4, notConnect};
        options = new Image[]{BackButton1, BackButton2, Kim, Kim2, startGame1, startGame2};
        selectedCharacter = new Image[4];

        playerChoice = 1;
        numChoices = 2;
        music = new Music("sound/music/character_selection.ogg");
        selectSound = new Sound("sound/select.ogg");
        noSound = new Sound("sound/no.ogg");
        backMouse = new MouseOverArea(gc, BackButton1, 26, 23, 164, 75);
        yesMouse = new MouseOverArea(gc, BackButton1, 260, 430, 225, 115);
        noMouse = new MouseOverArea(gc, BackButton1, 510, 430, 225, 115);
        kimMouse = new MouseOverArea(gc, BackButton1, 214, 192, 171, 176);
        startMouse = new MouseOverArea(gc, BackButton1, 1, 463, 998, 123);

        playSound = true;
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        Background.draw(0, 0, .5f);
        renderOptions();
        renderPlayers();
        g.drawString(mouse, 50, 50);
        ttf.drawString(100, 120, "Character Select");
        //ttf.drawString(100, 100, Mouse.getX() + "   " + Mouse.getY()); //260, 430,225,115
        try {
            iptext.drawString(700, 120, InetAddress.getLocalHost().getHostAddress()); //insert the ip variable
        } catch (UnknownHostException ex) {
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        if (Server2.recievedData.contains("Kim")) {
            System.out.println("kimboi");
            selectedCharacter[Integer.parseInt(Server2.recievedData.substring(Server2.recievedData.length() - 1)) - 1] = KimPick;
            Server2.recievedData="";
        }
        if (!music.playing()) {
            System.out.println(Server2.getVolume());
            music.play(1, Server2.getVolume());
        }
        if (Server2.getNumPlayers() == 0) {
            numPlayers = Client.getNumPlayers();
        } else {
            numPlayers = Server2.getNumPlayers();
        }

        Input input = gc.getInput();
        mouse = Mouse.getX() + " " + (800 - Mouse.getY());

        if (playerChoice > -1) {
            if (backMouse.isMouseOver()) {
                playerChoice = 0;
            }
            if (kimMouse.isMouseOver()) {
                playerChoice = 1;
            }
            if (startMouse.isMouseOver()) {
                playerChoice = 2;
            }
        } else if (playerChoice < 0) {
            if (noMouse.isMouseOver()) {
                playerChoice = -1;
            } else if (yesMouse.isMouseOver()) {
                playerChoice = -2;
            }
        }

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            if (leaveState) {
                changeLeave(false);
            }
        }

        if (input.isKeyPressed(Input.KEY_UP)) {
            if (playerChoice != 0 && playerChoice != -2) {
                playerChoice--;
            }
        }
        if (input.isKeyPressed(Input.KEY_DOWN)) {
            if ((playerChoice != numChoices - 1 && playerChoice >= 0) || playerChoice == -2) {
                playerChoice++;
            }
        }
        if (input.isKeyPressed(Input.KEY_ENTER)
                || (playerChoice == 0 && (backMouse.isMouseOver() && Mouse.isButtonDown(0) && playerChoice > -1))
                || (playerChoice < 0 && Mouse.isButtonDown(0) && (noMouse.isMouseOver() || yesMouse.isMouseOver()))
                || (playerChoice == 1 && (Mouse.isButtonDown(0) && kimMouse.isMouseOver() && playerChoice > 0))
                || (playerChoice == 2 && (Mouse.isButtonDown(0) && startMouse.isMouseOver()))) {
            //if ("Enter" detected) OR (back button pressed when not in confirmation screen) OR (mouse pressed on No or Yes in confirmation screen) OR (kim pressed)
            if (playerChoice != 0) {
                input.clearKeyPressedRecord();
            }
            if (leaveState && playerChoice == -1) { //leaving the game
                input.clearKeyPressedRecord();
                music.stop();

                changeLeave(false);
                try {
                    Server2.reboot();
                } catch (IOException ex) {
                }
                sbg.enterState(0);
                return;
            }
            if (leaveState && playerChoice == -2) {//staying in the game
                input.clearKeyPressedRecord();
                changeLeave(false);
                return;
            }
            if (playerChoice == 1) {
                input.clearKeyPressedRecord();
                selectedCharacter[0] = KimPick; //switch 0 to whatever ID you are on the server
                Server2.send("Kim", 1);
            }
            if (playerChoice == 0) {//back button
                input.clearKeyPressedRecord();
                noSound.play(1, Client.getSoundVolume());
                changeLeave(true);
                //sbg.enterState(0);
            }
            if (playerChoice == 2 && selectedCharacter[0]==KimPick) {
                Server2.inGame = true;
                Server2.Alive = true;
                Server2.numAlive = numPlayers;
                Server2.numLives = 4;
                Server2.send("game started", 1);
                sbg.enterState(2);
            }
            if (playSound) {
                selectSound.play(1, Client.getSoundVolume());
                playSound = false;
            }
            if (!Mouse.isButtonDown(0)) {
                playSound = true;
            }
        }
    }

    public void renderOptions() {
        numChoices = 3;
        for (int i = 0; i < numChoices; i++) {
            if (playerChoice == i) {
                if (i == 0) {
                    options[1].draw(10, 10, backSize); //back button
                } else if (i == 1) {
                    options[3].drawCentered(300, 280); //kim
                } else if (i == 2) {
                    options[5].drawCentered(500, 550);
                }
            } else {
                if (i == 0) {
                    options[0].draw(10, 10, backSize); //back button
                } else if (i == 1) {
                    options[2].drawCentered(300, 280); //kim
                } else if (i == 2) {
                    options[4].drawCentered(500, 550);
                }
            }
        }
        if (leaveState && playerChoice == -2) {
            confirmLeave2.drawCentered(500, 400);
        } else if (leaveState && playerChoice == -1) {
            confirmLeave.drawCentered(500, 400);
        }
    }

    public void renderPlayers() {
        for (int i = 0; i < 4; i++) {
            if (i < Server2.getNumPlayers()) {
                players[i].draw(90 + 200 * i, 600, .5f);
                if (selectedCharacter[i] != null) {
                    selectedCharacter[i].drawCentered(195 + 200 * i, 715);
                }
            } else {
                players[4].draw(90 + 200 * i, 600, .5f);
            }
        }
    }

    public void changeLeave(boolean to) //changes leave state and sets playerChoice
    {
        leaveState = to;
        if (to) {
            playerChoice = -1;
        } else {
            playerChoice = 0;
        }
    }
}
