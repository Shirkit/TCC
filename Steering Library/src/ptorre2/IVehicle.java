/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import ptorre2.combined.CombinedBehaviour;

/**
 *
 * @author ptorre2
 */
public interface IVehicle {

    /**
     * Used internally to set the flock. Do not use this.
     *
     * @param flock
     */
    void setFlock(Flock flock);

    public Flock<IVehicle> getFlock();

    public void setLocation(Vector3f location);

    public Vector3f getLocation();

    public void setRotation(Quaternion rotation);

    public Quaternion getRotation();

    public void setCombinedBehaviour(CombinedBehaviour behaviour);

    public CombinedBehaviour getCombinedBehaviour();

    /**
     * Take the steering influence and apply the vehicle's mass, max speed,
     * speed, and maxTurnForce to determine the new velocity.
     */
    public void updateVelocity(Vector3f steeringInfluence, float scale);

    public Vector3f getVelocity();

    public void setSpeed(float speed);

    public float getSpeed();

    public void setMaximumSpeed(float maxSpeed);

    public float getMaximumSpeed();

    /**
     * Gets the predicted position for this 'frame', taking into account current
     * position and velocity.
     *
     * @param tpf time per fram
     */
    public Vector3f getFuturePosition(float tpf);

    public float getCollisionRadius();

    public float getMaximumTurnForce();
}
