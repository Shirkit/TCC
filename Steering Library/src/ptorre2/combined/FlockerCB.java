package ptorre2.combined;

import com.jme3.math.Vector3f;
import java.util.List;
import ptorre2.IObstacle;
import ptorre2.SteerControl;
import ptorre2.behaviour.Alignment;
import ptorre2.behaviour.Cohesion;
import ptorre2.behaviour.ObstacleAvoid;
import ptorre2.behaviour.Separation;

/**
 *
 * @author Shirkit
 */
public class FlockerCB extends AbstractCombinedBehaviour {

    protected SteerControl outer;
    protected Alignment alignment = new Alignment();
    protected Cohesion cohesion = new Cohesion();
    protected Separation separation = new Separation();
    protected ObstacleAvoid avoidance = new ObstacleAvoid();
    protected float radius = 10f;
    private List<IObstacle> obstacles;

    public FlockerCB(SteerControl outer) {
        this.outer = outer;
    }

    // TODO: 
    /*@Override
     protected void onAttach() {
     obstacles = outer.getObstacals();
     }*/
    @Override
    public void update(float tpf) {

        List<IObstacle> neighbors = outer.neighboursNearby(vehicle, radius);
        obstacles = outer.obstacalsNearby(vehicle, radius);

        Vector3f seperationForce = this.separation.calculateForce(vehicle.getLocation(), neighbors);
        Vector3f avoidForce = this.avoidance.calculateForce(vehicle.getLocation(), vehicle.getVelocity(), vehicle.getCollisionRadius(), vehicle.getSpeed(), vehicle.getMaximumTurnForce(), tpf, obstacles);
        Vector3f cohesionForce = this.cohesion.calculateForce(vehicle.getLocation(), neighbors);
        Vector3f alignmentForce = this.alignment.calculateForce(vehicle.getVelocity(), neighbors);
        Vector3f momentumForce = vehicle.getVelocity();

        cohesionForce.multLocal(1.3f);
        alignmentForce.multLocal(1f);
        seperationForce.multLocal(800f);
        avoidForce.multLocal(4f);
        momentumForce.multLocal(1f);

        Vector3f last = seperationForce.add(cohesionForce).add(alignmentForce).add(momentumForce).add(avoidForce);

        vehicle.updateVelocity(last, tpf);
    }
}