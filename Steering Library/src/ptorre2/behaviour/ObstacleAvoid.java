/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2.behaviour;

import com.jme3.math.Vector3f;
import java.util.List;
import ptorre2.IObstacle;
import ptorre2.math.Line;

/**
 * Obstacle avoidance behavior gives a character the ability to maneuver 
 * in a cluttered environment by dodging around obstacles. There is an 
 * important distinction between obstacle avoidance and flee behavior. 
 * Flee will always cause a character to steer away from a given location, 
 * whereas obstacle avoidance takes action only when a nearby obstacle 
 * lies directly in front of the character.
 * 
 * The goal of the behavior is to keep an imaginary cylinder of free space 
 * in front of the character. The cylinder lies along the character’s forward 
 * axis, has a diameter equal to the character’s bounding sphere, and extends 
 * from the character’s center for a distance based on the character’s speed 
 * and agility. An obstacle further than this distance away is not an immediate 
 * threat.
 * 
 * @author Brent Owens
 */
public class ObstacleAvoid implements Behaviour {

    public Vector3f calculateForce(Vector3f location, Vector3f velocity, float collisionRadius, float speed, float turnSpeed, float tpf, List<IObstacle> obstacles) {

        float cautionRange = speed / turnSpeed + speed + collisionRadius;
        Line line = new Line(location, velocity.normalize());
        Vector3f closest = Vector3f.POSITIVE_INFINITY;

        for (IObstacle obs : obstacles) {
            Vector3f target = obs.getLocation();

            // If the obstacle isn't ahead of him, just ignore it
            if (line.getDirection().dot(target.subtract(location)) <= 0)
                continue;

            // Check if the target is inside the check radius
            if (location.distance(target) <= (cautionRange + obs.getCollisionRadius()))
                // Check if the target will collide with the source
                if (line.distance(target) <= (collisionRadius + obs.getCollisionRadius()))
                    // Store the closest target
                    if (target.distance(location) < closest.distance(location))
                        closest = obs.getLocation();
        }

        // If any target was found
        if (!closest.equals(Vector3f.POSITIVE_INFINITY)) {

            // The steering force will try to steer for the sides away from the target, instead of negating it's location
            // That way, the source just turn the minimum away needed to avoid the collision
            Vector3f steeringForce = velocity.normalize().cross(Vector3f.UNIT_Y);

            if (line.signedDistance(closest) > 0) // He's on the left side, just negate the steering force
                steeringForce.negateLocal();

            return steeringForce;
        } else
            return Vector3f.ZERO;
    }
}
