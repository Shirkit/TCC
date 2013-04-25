/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2.combined;

import com.jme3.math.Vector3f;
import ptorre2.IVehicle;
import ptorre2.behaviour.Evade;

/**
 *
 * @author ptorre2
 */
public class EvasionCB extends AbstractCombinedBehaviour {

    private Evade evade = new Evade();
    private IVehicle target;

    public EvasionCB(IVehicle target) {
        this.target = target;
    }

    @Override
    public void update(float tpf) {
        // calculate the steering force from the Persuit routine
        Vector3f steering = evade.calculateForce(vehicle.getLocation(), vehicle.getVelocity(), vehicle.getSpeed(), target.getSpeed(), tpf, target.getVelocity(), target.getFuturePosition(tpf));
        // add the force to the velicity
        vehicle.updateVelocity(steering, tpf);
    }
}
