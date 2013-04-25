package ptorre2;

import com.jme3.math.Vector3f;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * An obstacle provides information about the properties of objects in the simulation.
 * 
 * @author ptorre2
 */
public interface IObstacle {
    
    /**
     * The world velocity of the obstacle
     */
    public Vector3f getVelocity();
    
    /**
     * The world location of the obstacle
     */
    public Vector3f getLocation();
    
    /**
     * The collision radius of the obstacle
     */
    public float getCollisionRadius();
    
}
