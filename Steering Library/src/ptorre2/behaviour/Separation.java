/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2.behaviour;

import com.jme3.math.Vector3f;
import java.util.List;
import ptorre2.IObstacle;

/**
 * Separation steering behavior gives a character the ability to 
 * maintain a certain separation distance from others nearby. This 
 * can be used to prevent characters from crowding together.
 * 
 * For each nearby character, a repulsive force is computed by 
 * subtracting the positions of our character and the nearby character, 
 * normalizing, and then applying a 1/r weighting. (That is, the position 
 * offset vector is scaled by 1/r^2.) Note that 1/r is just a setting 
 * that has worked well, not a fundamental value. These repulsive forces 
 * for each nearby character are summed together to produce the overall 
 * steering force.
 * 
 * The supplied neighbours should only be the nearby neighbours in
 * the field of view of the character that is steering. It is good to
 * ignore anything behind the character.
 * 
 * @author Brent Owens
 */
public class Separation implements Behaviour {

    public Vector3f calculateForce(Vector3f location, List<IObstacle> neighbours) {

        Vector3f steering = new Vector3f();

        int count = 0;
        for (IObstacle o : neighbours) {
            Vector3f loc = o.getLocation().subtract(location).negate();
            float len2 = loc.lengthSquared();
            steering.x += loc.x / (len2 * len2 + 1);
            steering.y += loc.y / (len2 * len2 + 1);
            steering.z += loc.z / (len2 * len2 + 1);
            count++;
        }

        if (count == 0) {
            return new Vector3f();
        }

        return steering.divide(count);
    }
}
