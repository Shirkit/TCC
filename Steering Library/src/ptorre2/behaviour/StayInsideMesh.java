/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2.behaviour;

import com.jme3.math.Vector3f;
import ptorre2.NavigationProvider;

/**
 *
 * @author Shirkit
 */
public class StayInsideMesh implements Behaviour {


    public StayInsideMesh() {
    }

    
    public Vector3f calculateForce(Vector3f location, Vector3f velocity, float speed, NavigationProvider navmesh) {

        // Test if the future position is inside a cell of the navigation mesh
        Vector3f futurePosition = location.add(velocity.mult(speed));
        if (navmesh.isInsideMesh(futurePosition)) {
            // Nothing has changed, so it's inside a cell, end here
            return Vector3f.ZERO;
        } else {
            Vector3f normal = navmesh.getNormal(location, futurePosition);
            if (normal != null)
                return normal;
            else {
                    Vector3f cross = velocity.normalize().cross(Vector3f.UNIT_Y);
                    Vector3f p = location.add(cross.mult(speed));
                    if (navmesh.isInsideMesh(p))
                        return cross;
                    else
                        return cross.negateLocal();
            }
            
        }
    }
}
