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
import ptorre2.behaviour.ObstacleAvoid;
import ptorre2.behaviour.Persuit;
import ptorre2.behaviour.Seek;
import ptorre2.behaviour.Separation;
import ptorre2.behaviour.StayInsideMesh;

/**
 *
 * @author Shirkit
 */
public class PathFollowerCB extends AbstractCombinedBehaviour implements FormationAware {

    private SteerControl outer;
    private Persuit persuit = new Persuit();
    private Seek seek = new Seek();
    private Alignment alignment = new Alignment();
    private ObstacleAvoid avoid = new ObstacleAvoid();
    private StayInsideMesh stayInsideMesh = new StayInsideMesh();
    private Separation separation = new Separation();
    //
    private float radius = 10f;
    private float minDistanceToWaypoint = 5f;
    private NavigationProvider navMesh;
    //
    private float time = 0f;
    private boolean insideMesh = false;

    public PathFollowerCB(NavigationProvider navmesh, SteerControl outer) {
        this.outer = outer;
        this.navMesh = navmesh;
    }

    @Override
    public void update(float tpf) {

        if (vehicle.getFlock().hasPath() && vehicle.getFlock().isPathFinished(vehicle.getFlock().pos)) {
            vehicle.getVelocity().zero();
        } else {

            List<IObstacle> neighbors = outer.neighboursNearby(vehicle, radius);
            List<IObstacle> obstacles = outer.obstacalsNearby(vehicle, radius);

            Vector3f avoidForce = this.avoid.calculateForce(vehicle.getLocation(), vehicle.getVelocity(), vehicle.getCollisionRadius(), vehicle.getSpeed(), vehicle.getMaximumTurnForce(), tpf, obstacles);
            Vector3f alignmentForce = this.alignment.calculateForce(vehicle.getVelocity(), neighbors);
            Vector3f insideMeshForce = this.stayInsideMesh.calculateForce(vehicle.getLocation(), vehicle.getVelocity(), vehicle.getSpeed(), navMesh);
            Vector3f persuitForce = persuit.calculateForce(vehicle.getLocation(), vehicle.getVelocity(), vehicle.getSpeed(), 0f, tpf, Vector3f.ZERO, vehicle.getFlock().getWaypointPosition(vehicle.getFlock().pos));
            Vector3f momentumForce = vehicle.getVelocity();

            // Always applied forces
            alignmentForce.multLocal(1f);
            momentumForce.multLocal(1f);
            insideMeshForce.multLocal(16f);
            avoidForce.multLocal(3f);
            persuitForce.multLocal(1f);

            insideMesh = insideMeshForce.lengthSquared() > 0 || insideMesh;

            Vector3f steer = avoidForce.add(alignmentForce).addLocal(momentumForce).addLocal(insideMeshForce).addLocal(persuitForce);

            if (minDistanceToWaypoint >= vehicle.getFlock().getWaypointPosition(vehicle.getFlock().pos).distance(vehicle.getLocation()) + vehicle.getCollisionRadius()) {
                vehicle.getFlock().pos++;
            }

            if (vehicle.getFlock().hasFormation()) {

                Vector3f formPos = vehicle.getFlock().getRelativePosition(vehicle).add(vehicle.getFlock().getCenter());
                float dist = vehicle.getLocation().distance(formPos);
                if (dist <= vehicle.getCollisionRadius())
                    dist = 0;
                else {
                    Vector3f seekFormationForce = this.seek.calculateForce(vehicle.getLocation(), vehicle.getVelocity(), vehicle.getSpeed(), vehicle.getFlock().getRelativePosition(vehicle).add(vehicle.getFlock().getCenter()));
                    steer.addLocal(seekFormationForce.multLocal(1.5f));
                    vehicle.getFlock().tempDirection.addLocal(seekFormationForce.multLocal(dist));
                    vehicle.getFlock().tempDistanceSum += dist;
                }

                if (vehicle.getFlock().distanceSum > 5 && dist == 0) {   
                    vehicle.setSpeed(vehicle.getMaximumSpeed()*0.8f);
                }
                else {
                    vehicle.setSpeed(vehicle.getMaximumSpeed());
                }

                if (vehicle.getLocation() == vehicle.getFlock().getCenter()) {
                    vehicle.getFlock().updateRotation(vehicle.getMaximumTurnForce() * tpf * 0.1f);
                    steer.addLocal(vehicle.getFlock().direction.negateLocal().normalizeLocal().multLocal(1f));
                    vehicle.getFlock().distanceSum = vehicle.getFlock().tempDistanceSum;
                    vehicle.getFlock().tempDistanceSum = 0f;
                    vehicle.getFlock().direction.set(vehicle.getFlock().tempDirection);
                    vehicle.getFlock().tempDirection.zero();
                }

            } else {
                Vector3f separationForce = separation.calculateForce(vehicle.getLocation(), neighbors);

                separationForce.multLocal(150f);

                steer.addLocal(separationForce);
            }

            time += tpf;

            if (time > 0.1f) {
                insideMesh = false;
                time = 0f;
            }

            vehicle.updateVelocity(steer, tpf);
        }

    }

    @Override
    public void onFormationRegister(Vector3f location) {
        minDistanceToWaypoint = vehicle.getCollisionRadius() * 10;
    }
}
