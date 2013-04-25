/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2.behaviour;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

/**
 * Wander is a type of random steering. One easy implementation would be to generate
 * a random steering force each frame, but this produces rather uninteresting motion.
 * It is “twitchy” and produces no sustained turns. A more interesting approach is to
 * retain steering direction state and make small random displacements to it each frame.
 * Thus at one frame the character may be turning up and to the right, and on the next
 * frame will still be turning in almost the same direction. The steering force takes a
 * “random walk” from one direction to another.
 * 
 * @author Shirkit
 */
public class Wander implements Behaviour {

    private Vector3f direction = new Vector3f();
    private Vector3f steering = new Vector3f();
    
    public Vector3f calculateForce() {
        return calculateForce(direction, steering, 0.03f);
    }
    
    public Vector3f calculateForce(float scale) {
        return calculateForce(direction, steering, scale);
    }

    private Vector3f calculateForce(Vector3f direction, Vector3f steering, float scale) {
        Vector3f jiggle = new Vector3f();

        do {
            jiggle.x = (FastMath.nextRandomFloat() * 2.0F - 1.0F);
            jiggle.z = (FastMath.nextRandomFloat() * 2.0F - 1.0F);
        } while (jiggle.lengthSquared() > 1.0F);

        jiggle.multLocal(scale);
        direction.addLocal(jiggle);
        direction.normalizeLocal();

        steering.addLocal(direction).normalizeLocal();

        return steering;
    }
}