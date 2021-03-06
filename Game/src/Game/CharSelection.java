package Game;

import Game.Client;
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
 * client
 */
public class CharSelection extends BasicGameState {

    private MouseOverArea backMouse;
    private MouseOverArea noMouse;
    private MouseOverArea yesMouse;
    private MouseOverArea kimMouse;
    private Sound sound;
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
    //button choice variables
    private int playerChoice;
    private int numChoices;
    private boolean leaveState = false; //check if leave prompt is active. true means leavePrompt is displayed.

    public CharSelection(int s) {
        state = s;
        backSize = 0.4f;
    }

    @Override
    public int getID() {
        return state;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        Client.recievedData = "";
        font = new Font("Showcard Gothic", Font.BOLD, 51);
        ipfont = new Font("Showcard Gothic", Font.BOLD, 25);
        ttf = new TrueTypeFont(font, true);
        iptext = new TrueTypeFont(ipfont, true);

        BackButton1 = new Image("res/BackButton1.png");
        BackButton2 = new Image("res/BackButton2.png");
        Background = new Image("res/Menu_Background.png");
        confirmLeave = new Image("res/LeavePrompt.png");
        confirmLeave2 = new Image("res/LeavePrompt2.png");

        Kim = new Image("res/Kim_head.png");
        Kim2 = new Image("res/Kim_head2.png");
        KimPick = new Image("res/KimPick.png");

        p1 = new Image("res/Player1Connect.png");
        p2 = new Image("res/Player2Connect.png");
        p3 = new Image("res/Player3Connect.png");
        p4 = new Image("res/Player4Connect.png");
        notConnect = new Image("res/NotConnected.png");
        music = new Music("sound/music/character_selection.ogg");
        players = new Image[]{p1, p2, p3, p4, notConnect};
        options = new Image[]{BackButton1, BackButton2, Kim, Kim2};
        selectedCharacter = new Image[4];

        playerChoice = 1;
        numChoices = options.length / 2;
        backMouse = new MouseOverArea(gc, BackButton1, 26, 23, 164, 75);
        yesMouse = new MouseOverArea(gc, BackButton1, 260, 470, 225, 115);
        noMouse = new MouseOverArea(gc, BackButton1, 510, 470, 225, 115);
        kimMouse = new MouseOverArea(gc, BackButton1, 214, 192, 171, 176);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        Background.draw(0, 0, .5f);
        renderOptions();
        renderPlayers();
        ttf.drawString(100, 120, "Character Select");
        try {
            iptext.drawString(700, 120, InetAddress.getLocalHost().getHostAddress()); //insert the ip variable
        } catch (UnknownHostException ex) {
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        if (!music.playing()) {
            music.play(1, Client.getVolume());
        }
        numPlayers = Client.getNumPlayers();
        Input input = gc.getInput();
        if (backMouse.isMouseOver() && playerChoice > -1) {
            playerChoice = 0;
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
                || (playerChoice == 1 && (Mouse.isButtonDown(0) && kimMouse.isMouseOver() && playerChoice > 0))) {
            //if ("Enter" detected) OR (back button pressed when not in confirmation screen) OR (mouse pressed on No or Yes in confirmation screen) OR (kim pressed)
            if (playerChoice != 0) {
                input.clearKeyPressedRecord();
                sound = new Sound("sound/select.ogg");
                sound.play(1, Client.getSoundVolume());
            }
            if (leaveState && playerChoice == -1) { //leaving the game
                input.clearKeyPressedRecord();
                music.stop();
                changeLeave(false);
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
                selectedCharacter[(Client.getID() - 1)] = KimPick; //switch 0 to whatever ID you are on the server
                try {
                    Client.send("Kim", Client.getID());
                    System.out.println("Kim has been picked");
                } catch (IOException noOneCares) {
                }
            }
            if (playerChoice == 0) {//back button
                input.clearKeyPressedRecord();
                sound = new Sound("sound/no.ogg");
                sound.play(1, Client.getSoundVolume());
                changeLeave(true);
                //sbg.enterState(0);
            }
        }
        if (Client.recievedData.contains("Kim")) {
            selectedCharacter[Integer.parseInt(Client.recievedData.substring(Client.recievedData.length() - 1)) - 1] = KimPick; //switch 0 to whatever ID you are on the server
        }
        if (Client.recievedData.contains("game started")) {
            Client.numLives = 4;
            Client.inGame = true;
            Client.Alive = true;
            sbg.enterState(2);
        }
    }

    public void renderOptions() {

        for (int i = 0; i < numChoices; i++) {

            if (playerChoice == i) {
                if (i == 0) {
                    options[i * 2 + 1].draw(10, 10, backSize); //back button
                } else if (i == 1) {
                    options[i * 2 + 1].drawCentered(300, 280); //kim
                }
            } else {
                if (i == 0) {
                    options[i * 2].draw(10, 10, backSize); //back button
                } else if (i == 1) {
                    options[i * 2].drawCentered(300, 280); //kim
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
            if (i < numPlayers) {
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
