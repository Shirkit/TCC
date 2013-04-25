/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import ptorre2.combined.FormationAware;

/**
 * Stores all relevant information about a flock in the world. This is to make
 * easier to find the other vehicles that should be considered when computing
 * behaviours.
 *
 * This class can be broken apart in one that is formation aware, and another
 * one that is path aware. I'll leave this for later.
 *
 * @author ptorre2
 */
public class Flock<T extends IVehicle> {

    private T pivot;
    private NavigationProvider path;
    private ArrayList<T> members;
    private HashMap<T, Integer> positions;
    private boolean formation;
    public float tempDistanceSum = 0.0f;
    public float distanceSum = 0.0f;
    public Vector3f tempDirection = new Vector3f();
    public Vector3f direction = new Vector3f();
    public float speed = 0.0f;
    public int pos = 2;
    private Node finalPositions = new Node();
    private Quaternion initialRotation = new Quaternion();

    public Flock() {
        formation = false;
        members = new ArrayList<T>();
        positions = new HashMap<T, Integer>();
    }

    //Members related
    public boolean add(T e) {
        e.setFlock(this);
        return members.add(e);
    }

    public boolean has(T e) {
        return members.contains(e);
    }

    public boolean remove(T e) {
        return members.remove(e);
    }

    public Collection<T> listMembers() {
        return members;
    }

    public Vector3f getCenter() {
        return pivot.getLocation();
    }

    public T getPivot() {
        return pivot;
    }

    // Formation related
    public void registerFormation() {
        positions.clear();
        Vector3f center = new Vector3f();
        for (T e : members) {
            Vector3f position = e.getLocation();
            center.addLocal(position);
        }
        center.divideLocal(members.size());
        pivot = members.get(0);
        for (T e : members) {
            if (e.getLocation().distance(center) < pivot.getLocation().distance(center)) {
                pivot = e;
            }
        }

        int i = 0;
        initialRotation.set(pivot.getRotation());

        for (T e : members) {
            Vector3f v = e.getLocation().subtract(pivot.getLocation());
            positions.put(e, i++);
            Node n = new Node();
            n.setLocalTranslation(v);
            finalPositions.attachChild(n);
            if (e.getCombinedBehaviour() instanceof FormationAware)
                ((FormationAware) e.getCombinedBehaviour()).onFormationRegister(v);
        }

        this.speed = pivot.getSpeed();

        formation = true;
    }

    public void updateRotation(float scale) {
        Quaternion inverse = initialRotation.inverse();
        Quaternion mult = pivot.getRotation().mult(inverse);
        mult.multLocal(finalPositions.getLocalRotation());
        
        Quaternion clone = finalPositions.getLocalRotation().clone();
        clone.slerp(mult, scale);
        finalPositions.setLocalRotation(clone);
        //finalPositions.setLocalRotation(mult);
        
        initialRotation.slerp(pivot.getRotation(), scale);
        //initialRotation.set(pivot.getRotation());
        //System.out.println(Arrays.toString(mult.toAngles(null)));
    }

    public Vector3f getRelativePosition(T e) {
        return finalPositions.getChild(positions.get(e)).getWorldTranslation();
    }

    public boolean hasFormation() {
        return formation;
    }

    // Path related
    /*public void setPath(NavigationProvider p) {
     // Removes duplicated entries
     this.path = p;
     for (int i = 0; i < path.getWaypoints().size() - 1; i++) {
     if (path.getWaypoints().get(i) == path.getWaypoints().get(i + 1)) {
     path.getWaypoints().remove(i + 1);
     }
     }
     }*/
    public void setPath(NavigationProvider path) {
        this.path = path;
    }

    public boolean hasPath() {
        return path != null && path.hasPath();
    }

    public boolean isPathFinished(int currentWaypoint) {
        return path.isPathFinished(currentWaypoint);
    }

    public Vector3f getWaypointPosition(int i) {
        return path.getWaypointPosition(i);
    }
}
