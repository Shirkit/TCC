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
import ptorre2.behaviour.Separation;
import ptorre2.behaviour.StayInsideMesh;
import ptorre2.behaviour.Wander;

/**
 *
 * @author ptorre2
 */
public class BirdCB extends AbstractCombinedBehaviour {
    
    private Separation separation = new Separation();
    private Alignment alignment = new Alignment();
    private Cohesion cohesion = new Cohesion();
    private ObstacleAvoid avoidance = new ObstacleAvoid();
    private StayInsideMesh insideMesh = new StayInsideMesh();
    private Wander wander = new Wander();
    private SteerControl outer;
    private float radius = 10f;
    private NavigationProvider provider;

    public BirdCB(SteerControl outer, NavigationProvider provider) {
        this.outer = outer;
        this.provider = provider;
    }

    
    
    @Override
    public void update(float tpf) {
        
        List<IObstacle> neighbors = outer.neighboursNearby(vehicle, radius);
        List<IObstacle> obstacles = outer.obstacalsNearby(vehicle, radius);

        Vector3f seperationForce = this.separation.calculateForce(vehicle.getLocation(), neighbors);
        Vector3f avoidForce = this.avoidance.calculateForce(vehicle.getLocation(), vehicle.getVelocity(), vehicle.getCollisionRadius(), vehicle.getSpeed(), vehicle.getMaximumTurnForce(), tpf, obstacles);
        Vector3f cohesionForce = this.cohesion.calculateForce(vehicle.getLocation(), neighbors);
        Vector3f alignmentForce = this.alignment.calculateForce(vehicle.getVelocity(), neighbors);
        Vector3f insideMeshForce = this.insideMesh.calculateForce(vehicle.getLocation(), vehicle.getVelocity(), vehicle.getSpeed(), provider);
        Vector3f wanderForce = this.wander.calculateForce(0.1f);
        Vector3f momentumForce = vehicle.getVelocity();

        cohesionForce.multLocal(1.3f);
        alignmentForce.multLocal(1f);
        seperationForce.multLocal(2000f);
        avoidForce.multLocal(8f);
        momentumForce.multLocal(1.2f);
        insideMeshForce.multLocal(16f);
        wanderForce.multLocal(2f);
        
        Vector3f steer = cohesionForce.add(alignmentForce).addLocal(seperationForce).addLocal(avoidForce).addLocal(momentumForce).addLocal(insideMeshForce).addLocal(wanderForce);
        vehicle.updateVelocity(steer, tpf);
    }
    
}
