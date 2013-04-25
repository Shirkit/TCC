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
public interface CombinedBehaviour {
    
    /**
     * Gets the local vehicle which this combined behaviour is being applied to.
     * 
     * @return 
     */
    public IVehicle getVehicle();
    
    public void setVehicle(IVehicle vehicle);
    
    public void update(float tpf);
}
