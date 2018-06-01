/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

public abstract class HitBox 
{
    public int x;
    public int y;
    public int w;
    public int l;
    
    public HitBox(int h, int k, int width, int length)
    {
        x = h; 
        y = k;
        w = width;
        l = length;
    }
    
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public int getW()
    {
        return w;
    }
    public int getL()
    {
        return l;
    }
    
    public abstract boolean intersects(HitBox h);
    
}//last curly
