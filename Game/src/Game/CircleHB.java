/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Game;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import java.awt.Rectangle;

public class CircleHB extends HitBox
{
    private int r;
    private Ellipse2D.Float e;
    
    public CircleHB(int h, int k, int width, int length)
    {
        super(h, k, width, length);
        e = new Ellipse2D.Float(x, y, w, l);
    }

    @Override
    public boolean intersects(HitBox h) 
    {
        if(h instanceof RectHB)
        {
            return e.intersects(h.getX(), h.getY(), h.getW(), h.getL());
        }
        return false;
    }
}//last curly
