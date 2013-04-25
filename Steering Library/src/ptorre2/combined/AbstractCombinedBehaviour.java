/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2.combined;

import ptorre2.IVehicle;

/**
 *
 * @author ptorre2
 */
public abstract class AbstractCombinedBehaviour implements CombinedBehaviour {
    
    protected IVehicle vehicle;

    @Override
    public void setVehicle(IVehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public IVehicle getVehicle() {
        return vehicle;
    }
    
}
