package Game;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;


public class Game extends StateBasedGame {

    private final int menu = 0;
    private final int smash = 2;
    private final int selection = 4;
    private final int selection2 = 5;
    
    public Game(String name) {
        super(name);
        this.addState(new MainMenu(menu));
        this.addState(new Smash(smash));
        this.addState(new SelectionMenu(selection));
        this.addState(new CharSelection(selection2));
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        this.getState(menu).init(gc, this);
        this.getState(smash).init(gc, this);
        this.getState(selection).init(gc, this);
        this.getState(selection2).init(gc,this);
        this.enterState(menu);
    }

    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Game("Troy Smash 1.7"));
        app.setDisplayMode(1000, 800, false);
        app.setTargetFrameRate(60);
        app.start();
    }

}
