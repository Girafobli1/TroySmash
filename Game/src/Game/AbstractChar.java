/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public abstract class AbstractChar {

    //coordinates of the character
    float x;
    float y;
    //starting stance of character
    Image idle;
    private SpriteSheet attack1;
    //1 indicates that he is facing left during the attack
    private SpriteSheet sideAttack1;
    private SpriteSheet upAttack1;
    private SpriteSheet upSpecial1;
    private SpriteSheet ult1;
    private SpriteSheet jump1;
    private SpriteSheet running1;
    private SpriteSheet idle1;
    private SpriteSheet ded;
    private SpriteSheet ded2;
    private SpriteSheet ded3;
    private SpriteSheet ded4;
    private SpriteSheet knock;
    public SpriteSheet [] s;

    public AbstractChar(float x1, float y1, Image i, SpriteSheet att, SpriteSheet sideatt, SpriteSheet upatt, SpriteSheet upspec, SpriteSheet ult, SpriteSheet jum, SpriteSheet run, SpriteSheet id, SpriteSheet knocked) throws SlickException {

        attack1 = att;
        sideAttack1 = sideatt;
        upAttack1 = upatt;
        upSpecial1 = upspec;
        ult1 = ult;
        jump1 = jum;
        running1 = run;
        idle1 = id;
        knock = knocked;
        ded = new SpriteSheet("res/KO1.png", 800, 800);
        ded2 = new SpriteSheet("res/KO2.png", 800, 800);
        ded3 = new SpriteSheet("res/KO3.png", 800, 800);
        ded4 = new SpriteSheet("res/KO4.png", 800, 800);
        s = new SpriteSheet[]{attack1, sideAttack1, upAttack1, upSpecial1, ult1, jump1, running1, idle1, ded, ded2, ded3, ded4};

        x = x1;
        y = y1;
        idle = i;
    }
}
