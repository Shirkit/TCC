/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2.combined;

import com.jme3.math.Vector3f;
import ptorre2.IVehicle;
import ptorre2.behaviour.Flee;

/**
 *
 * @author Shirkit
 */
public class FleeCB extends AbstractCombinedBehaviour {

    private Flee flee = new Flee();
    private IVehicle target;

    public FleeCB(IVehicle target) {
        this.target = target;
    }

    @Override
    public void update(float tpf) {
        Vector3f steering = flee.calculateForce(vehicle.getLocation(), vehicle.getVelocity(), vehicle.getSpeed(), target.getLocation());
        // add the force to the velicity
        vehicle.updateVelocity(steering, tpf);
    }
}
