/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2.behaviour;

import com.jme3.math.Vector3f;
import java.util.List;
import ptorre2.IObstacle;

/**
 * Alignment steering behavior gives an character the ability to align itself with
 * (that is, head in the same direction and/or speed as) other nearby characters.
 * 
 * @author Shirkit
 */
public class Alignment implements Behaviour {

    public Vector3f calculateForce(Vector3f velocity, List<IObstacle> neighbors) {

        if (neighbors.isEmpty()) {
            return Vector3f.ZERO;
        }

        Vector3f alignment = new Vector3f();

        int count = 0;
        for (IObstacle v : neighbors) {
            if (!v.getVelocity().equals(Vector3f.ZERO)) {
                alignment.addLocal(v.getVelocity());
                count++;
            }
        }

        if (count > 0) {
            alignment.divideLocal(neighbors.size());
        }

        return alignment.subtract(velocity);
    }
}
