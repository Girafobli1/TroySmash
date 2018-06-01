/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import org.newdawn.slick.*;

public class KimBoi extends AbstractChar {
    //stores the animation

    public Animation anim;
    //dimension of the spriteSheet
    private final int dimX;
    private final int dimY;
    SpriteSheet[] s;
    //all spritesheet references
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
    //true if facing right, else false
    private boolean faceRight;
    //variables corresponding to the index of the spritesheet in the array
    //abbreviated. ex: attack1 = a1; sideAttack1 = sA1
    private final int a1 = 0;
    private final int sA1 = 1;
    private final int uA1 = 2;
    private final int uS1 = 3;
    private final int u1 = 4;
    private final int j1 = 5;
    private final int r1 = 6;
    private final int i1 = 7;
    private final int d1 = 8;
    private final int d2 = 9;
    private final int d3 = 10;
    private final int d4 = 11;

    private int temp = 0;

    private RectHB kimHB;

    public KimBoi(float x1, float y1, Image i) throws SlickException {
        super(x1, y1, i, new SpriteSheet("res/resized/Attack.png", 300, 200), new SpriteSheet("res/resized/SideAttack.png", 300, 200), new SpriteSheet("res/resized/UpAttack.png", 300, 200), new SpriteSheet("res/resized/UpSpecial.png", 300, 200), new SpriteSheet("res/resized/Ult.png", 300, 200), new SpriteSheet("res/resized/Jump.png", 300, 200), new SpriteSheet("res/resized/Running.png", 300, 200),  new SpriteSheet("res/resized/Idle.png", 300, 200), new SpriteSheet("res/resized/Knockback.png", 300, 200));

        s = super.s;
        kimHB = new RectHB((int) x + 120, (int) y + 75, 50, 100);
        dimX = 300;
        dimY = 200;
    }

    //gets coordinates of kim
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float a) {
        x = a;
    }

    public HitBox getHB() {
        return kimHB;
    }

    public void setY(float b) {
        y = b;
    }

    //initial state of kim animation
    public void start() {
        anim = new Animation();
        change(r1, 1, 8); //the 3 was originally 8
        anim.start();
    }

    //draws the animation
    public void draw() {
        anim.draw(x, y);
        //anim.getCurrentFrame().getFlippedCopy(faceRight, false).draw(x, y);
    }

    //starts the animation
    public void startAnim() {
        anim.start();
    }

    /*
     * changes to a different animation param i is the index of the animation in
     * the array param r is the number of rows in the spritesheet param c is the
     * number of columns
     */
    public void change(int i, int r, int c) {

        anim = new Animation();
        for (int j = 0; j < r; j++) {
            for (int k = 0; k < c; k++) {
                anim.addFrame(s[i].getSprite(j, k).getFlippedCopy(faceRight, false), 360); //method layout: Animation.addFrame(Image img.getFlippedCopy(flip horizon, flip vert) , duration); 
                //System.out.println(j + " " + k);
            }
        }
        anim.setSpeed(10);
    }

    //walk = run oops
    //arrow keys
    public void walk(float xShift) {
        x += 0.3 * xShift;
    }

    //t is regular attack
    //switches the animation to frames of attack
    public void changeAttack() {
        change(a1, 1, 9);
        anim.setLooping(false);
        anim.start();
    }

    public void changeLeft() {
        faceRight = false;
        change(r1, 1, 8); //the 3 was originally 8
        anim.setLooping(true);
        anim.start();
    }

    public void changeRight() {
        faceRight = true;
        change(r1, 1, 8); //the 3 was originally 8
        anim.setLooping(true);
        anim.start();
    }

    public void changeSideAttack() {
        change(sA1, 1, 11); //the 3 was originally 11
        anim.setLooping(false);
        anim.start();
    }

    public void changeUpAttack() {
        change(uA1, 1, 6);
        anim.setLooping(false);
        anim.start();
    }

    public void changeUpSpecial() {
        change(uS1, 1, 4);
        anim.setLooping(false);
        anim.start();
    }

    public void changeUlt() {
        faceRight = false;
        change(u1, 1, 4);
        anim.setLooping(true);
        anim.setPingPong(true);
        anim.start();
    }

    public void changeJump() throws InterruptedException {
        change(j1, 1, 4);
        anim.setLooping(false);
        anim.start();
    }

    public void knockedL() {
        change(9, 1, 1);
        anim.setLooping(true);
        anim.start();
        walk(-55);
    }

    public void knockedR() {
        change(9, 1, 1);
        anim.setLooping(true);
        anim.start();
        walk(55);
    }

    public void ded(int dir) {
        switch (dir)
        {
            case 0:
                change(d1, 1, 14);
                break;
            case 1: 
                change(d2, 1, 14);
                break;
            case 2:
                change(d3, 1, 14);
                break;
            case 3: 
                change(d4, 1, 14);
                break;
        }
        anim.setLooping(false);
        anim.start();
    }

    public void stop() {
        anim.stop();
    }

    public void idle() {
        anim.stop();
        change(i1, 1, 1);
        //anim = new Animation();
        //idle.draw(x, y);
    }

    public boolean detectHit(HitBox h, KimBoi k) {
        if (h.intersects(k.getHB())) {
            return true;
        }
        return false;
    }

}
