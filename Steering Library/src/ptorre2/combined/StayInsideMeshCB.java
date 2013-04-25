/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2.combined;

import com.jme3.math.Vector3f;
import ptorre2.NavigationProvider;
import ptorre2.behaviour.StayInsideMesh;

/**
 *
 * @author Shirkit
 */
public class StayInsideMeshCB extends AbstractCombinedBehaviour {

    private NavigationProvider navmesh;
    private StayInsideMesh stayInsideMesh = new StayInsideMesh();

    public StayInsideMeshCB(NavigationProvider navmesh) {
        this.navmesh = navmesh;
    }

    @Override
    public void update(float tpf) {
        Vector3f force = stayInsideMesh.calculateForce(vehicle.getLocation(), vehicle.getVelocity(), vehicle.getSpeed(), navmesh);

        force.multLocal(10f);

        vehicle.updateVelocity(force, tpf);
    }    
}
