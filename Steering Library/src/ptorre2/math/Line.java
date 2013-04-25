/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptorre2.math;


import com.jme3.math.Vector3f;

/**
 *
 * @author Shirkit
 */
public class Line extends com.jme3.math.Line {

    public Line() {
    }

    public Line(Vector3f origin, Vector3f direction) {
        super(origin, direction);
    }

    private Vector3f computeNormal(Vector3f direction) {
        return direction.cross(Vector3f.UNIT_Y);
    }

    /**
     * Determines the signed distance from a point to this line. Consider the line as
     * if you were standing on PointA of the line looking towards PointB. Posative distances
     * are to the right of the line, negative distances are to the left.
     */
    public float signedDistance(Vector3f point) {
        return point.subtract(getOrigin()).dot(computeNormal(getDirection()));
    }
}
