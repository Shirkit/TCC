/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2;

import com.jme3.math.Vector3f;

/**
 *
 * @author ptorre2
 */
public interface NavigationProvider {
    
    public boolean isInsideMesh(Vector3f point);
    
    public Vector3f getNormal(Vector3f initial, Vector3f destination);
    
    public boolean hasPath();
    public boolean isPathFinished(int waypoint);
    public Vector3f getWaypointPosition(int waypoint);
    
}
