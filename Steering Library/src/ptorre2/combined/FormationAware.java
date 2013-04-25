/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2.combined;

import com.jme3.math.Vector3f;

/**
 *
 * @author ptorre2
 */
public interface FormationAware extends CombinedBehaviour {
    
    public void onFormationRegister(Vector3f location);
    
}
