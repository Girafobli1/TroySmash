/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import org.newdawn.slick.*;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class RectHB extends HitBox 
{

    private Rectangle r;
    
    public RectHB(int h, int k, int width, int length)
    {
        super(h,k, width, length);
        
        r = new Rectangle(l, w, x, y);
    }
    
    public Rectangle getR()
    {
        return r;
    }
    
    @Override
    public boolean intersects(HitBox h) 
    {
        if(h instanceof RectHB)
        {
            Rectangle r2 = ((RectHB) h).getR();
            return r.intersects(r2);
        }
        else if(h instanceof CircleHB)
        {
            return ((CircleHB)h).intersects(this);
        }
        return false;
    }


}//last curly
