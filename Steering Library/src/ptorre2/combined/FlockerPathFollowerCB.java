/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2.combined;

import com.jme3.math.Vector3f;
import java.util.List;
import ptorre2.IObstacle;
import ptorre2.NavigationProvider;
import ptorre2.SteerControl;
import ptorre2.behaviour.Alignment;
import ptorre2.behaviour.Cohesion;
import ptorre2.behaviour.ObstacleAvoid;
import ptorre2.behaviour.Seek;
import ptorre2.behaviour.Separation;
import ptorre2.behaviour.StayInsideMesh;

/**
 *
 * @author Shirkit
 */
public class FlockerPathFollowerCB extends AbstractCombinedBehaviour {

    private SteerControl outer;
    private Seek seek = new Seek();
    private Alignment alignment = new Alignment();
    private Cohesion cohesion = new Cohesion();
    private Separation separation = new Separation();
    private ObstacleAvoid avoid = new ObstacleAvoid();
    private StayInsideMesh stayInsideMesh = new StayInsideMesh();
    //
    private float radius = 10f;
    private float minDistanceToWaypoint = 5f;
    private NavigationProvider navMesh;
    private int pos = 2;


    public FlockerPathFollowerCB(NavigationProvider navmesh, SteerControl outer) {
        this.outer = outer;
        this.navMesh = navmesh;
    }

    @Override
    public void update(float tpf) {

        if (vehicle.getFlock().hasPath() && vehicle.getFlock().isPathFinished(pos)) {
            vehicle.getVelocity().zero();
        } else {

            List<IObstacle> neighbors = outer.neighboursNearby(vehicle, radius);
            List<IObstacle> obstacles = outer.obstacalsNearby(vehicle, radius);

            Vector3f seperationForce = this.separation.calculateForce(vehicle.getLocation(), neighbors);
            Vector3f avoidForce = this.avoid.calculateForce(vehicle.getLocation(), vehicle.getVelocity(), vehicle.getCollisionRadius(), vehicle.getSpeed(), vehicle.getMaximumTurnForce(), tpf, obstacles);
            Vector3f cohesionForce = this.cohesion.calculateForce(vehicle.getLocation(), neighbors);
            Vector3f alignmentForce = this.alignment.calculateForce(vehicle.getVelocity(), neighbors);
            Vector3f insideMeshForce = this.stayInsideMesh.calculateForce(vehicle.getLocation(), vehicle.getVelocity(), vehicle.getSpeed(), navMesh);
            Vector3f seekForce = seek.calculateForce(vehicle.getLocation(), vehicle.getVelocity(), vehicle.getSpeed(), vehicle.getFlock().getWaypointPosition(pos));
            Vector3f momentumForce = vehicle.getVelocity();

            alignmentForce.multLocal(1f);
            momentumForce.multLocal(1f);
            insideMeshForce.multLocal(100f);

            cohesionForce.multLocal(0.8f);
            seperationForce.multLocal(600f);
            avoidForce.multLocal(3f);
            seekForce.multLocal(1f);

            Vector3f steer = seperationForce.add(cohesionForce).addLocal(avoidForce).addLocal(alignmentForce).addLocal(momentumForce).addLocal(insideMeshForce).addLocal(seekForce);

            if (minDistanceToWaypoint >= vehicle.getFlock().getWaypointPosition(pos).distance(vehicle.getLocation()) + vehicle.getCollisionRadius()) {
                pos++;
            }

            vehicle.updateVelocity(steer, tpf);
        }

    }
}
