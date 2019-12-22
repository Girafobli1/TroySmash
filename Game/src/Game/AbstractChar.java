/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import org.newdawn.slick.*;

public abstract class AbstractChar 
{
    
    //coordinates of the character
    float x;
    float y;
    
    //starting stance of character
    Image idle;
    
    public AbstractChar(float x1, float y1, Image i)
    {
        x = x1; 
        y = y1;
        idle = i;
    }
}
