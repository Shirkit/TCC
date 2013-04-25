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
public abstract class Vehicle implements IVehicle {

    public float speed = 1.5f; // worldUnits/second
    public float maxSpeed = 1.5f; // worldUnits/second
    public float maxTurnForce = 2f; // max steering force per second (perpendicular to velocity)
    // if speed is 1 and turn force is 1, then it will turn 45 degrees in a second
    public float mass = 1.0f; // the higher, the slower it turns
    public float collisionRadius = 0.2f;
    public Vector3f velocity = new Vector3f();
    public Flock flock;
    public CombinedBehaviour behaviour;

    @Override
    public void updateVelocity(Vector3f steeringInfluence, float scale) {
       Vector3f steeringForce = truncate(steeringInfluence, maxTurnForce * scale);
        Vector3f acceleration = steeringForce.divide(mass);
        Vector3f vel = truncate(velocity.add(acceleration), speed);
        velocity = vel;

        setLocation(getLocation().add(velocity.mult(scale).multLocal(speed)));

        // rotate to face
        Quaternion rotTo = getRotation().clone();
        rotTo.lookAt(velocity.normalize(), Vector3f.UNIT_Y);

        setRotation(rotTo);
    }

    /**
     * truncate the length of the vector to the given limit
     */
    private Vector3f truncate(Vector3f source, float limit) {
        if (source.lengthSquared() <= limit * limit) {
            return source;
        } else {
            return source.normalize().scaleAdd(limit, Vector3f.ZERO);
        }
    }

    /**
     * Gets the predicted position for this 'frame', taking into account current
     * position and velocity.
     *
     * @param tpf time per fram
     */
    @Override
    public Vector3f getFuturePosition(float tpf) {
        return getLocation().add(velocity);
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = Math.min(speed, maxSpeed);
    }

    @Override
    public void setMaximumSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @Override
    public void setFlock(Flock f) {
        this.flock = f;
    }

    @Override
    public Flock getFlock() {
        return flock;
    }

    @Override
    public CombinedBehaviour getCombinedBehaviour() {
        return behaviour;
    }

    @Override
    public void setCombinedBehaviour(CombinedBehaviour behaviour) {
        this.behaviour = behaviour;
        if (behaviour != null)
            this.behaviour.setVehicle(this);
    }

    @Override
    public Vector3f getVelocity() {
        return velocity;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getMaximumSpeed() {
        return maxSpeed;
    }

    @Override
    public float getCollisionRadius() {
        return collisionRadius;
    }

    @Override
    public float getMaximumTurnForce() {
        return maxTurnForce;
    }
}
