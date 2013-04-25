package ptorre2;

import java.util.List;

/**
 *
 * @author Shirkit
 */
public interface SteerControl {

    /**
     * Retrives all obstacals in the current scene.
     * 
     * @return a list of obstacals.
     */
    public List<IObstacle> getObstacals();

    /**
     * Retrieves all the obstacles nearby a target, given a radius.
     * 
     * @param radius the radius of how close the obstacals should be.
     * @param source the source position that we are looking from.
     * 
     * @return a list of the nearby obstacals.
     */
    public List<IObstacle> obstacalsNearby(IVehicle source, float radius);

    /**
     * Retrieves all the neighbours nearby a target, given a radius.
     * 
     * @param radius the radius of how close the neighbours should be.
     * @param source the source position that we are looking from.
     * 
     * @return a list of the nearby neighbours.
     */
    public List<IObstacle> neighboursNearby(IVehicle source, float radius);
}
