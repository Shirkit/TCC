/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2.behaviour;

import com.jme3.math.Vector3f;

/**
 * Evasion is analogous to pursuit, except that flee is used to steer away 
 * from the predicted future position of the target character.
 * 
 * @author Brent Owens
 */
public class Evade {

    public Vector3f calculateForce(Vector3f location, Vector3f velocity, float speed, float targetSpeed, float tpf, Vector3f targetVelocity, Vector3f targetLocation) {

        // calculate speed difference to see how far ahead we need to leed
        float speedDiff = targetSpeed - speed;
        float desiredSpeed = (targetSpeed + speedDiff) * tpf;
        Vector3f projectedLocation = targetLocation.add(targetVelocity.mult(desiredSpeed));
        Vector3f desierdVel = projectedLocation.subtract(location).normalize().mult(speed);
        Vector3f steering = desierdVel.subtract(velocity).negate(); // negate the direction

        return steering;
    }
}
