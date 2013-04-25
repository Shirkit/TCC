/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2.behaviour;

import com.jme3.math.Vector3f;
import java.util.List;
import ptorre2.IObstacle;

/**
 * Cohesion steering behavior gives an character the ability to cohere with
 * (approach and form a group with) other nearby characters.
 * 
 * @author Shirkit
 */
public class Cohesion implements Behaviour {

    public Vector3f calculateForce(Vector3f location, List<IObstacle> neighbors) {

        if (neighbors.isEmpty()) {
            return Vector3f.ZERO;
        }

        Vector3f cohesion = new Vector3f();

        int count = 0;
        for (IObstacle v : neighbors) {
            if (!v.getVelocity().equals(Vector3f.ZERO)) {
                count++;
                cohesion.addLocal(location.subtract(v.getLocation()));
            }
        }

        if (count > 0) {
            cohesion.divideLocal(count);
        }

        return cohesion.negate();
    }
}
