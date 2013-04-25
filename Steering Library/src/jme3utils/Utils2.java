package jme3utils;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Sphere;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author shirkit
 */
public class Utils2 {

    public static final String unshaded = "Common/MatDefs/Misc/Unshaded.j3md";
    private static AssetManager assetManager;
    private static BitmapFont guiFont;
    private static Node rootNode, debugNode;

    public static void init(AssetManager am, Node rootnode, Node debugnode) {
        assetManager = am;
        rootNode = rootnode;
        debugNode = debugnode;
        guiFont = am.loadFont("Interface/Fonts/Default.fnt");
    }

    public static Material unshaded() {
        return new Material(assetManager, unshaded);
    }

    public static BitmapFont getGuiFont() {
        return guiFont;
    }

    public static Node getRootNode() {
        return rootNode;
    }

    public static Node getDebugNode() {
        return debugNode;
    }

    public static Vector3f randomVector() {
        float x = FastMath.nextRandomFloat();
        if (FastMath.nextRandomInt(0, 1) == 0)
            x = -x;
        float y = FastMath.nextRandomFloat();
        if (FastMath.nextRandomInt(0, 1) == 0)
            y = -y;
        float z = FastMath.nextRandomFloat();
        if (FastMath.nextRandomInt(0, 1) == 0)
            z = -z;
        return new Vector3f(x, y, z).normalizeLocal();
    }

    public static Vector3f randomVector(float mult) {
        return randomVector().multLocal(mult);
    }

    public static Vector3f randomVector(boolean zeroY) {
        Vector3f randomVector = randomVector();
        if (zeroY)
            randomVector.y = 0;
        return randomVector;
    }

    public static Vector3f randomVector(boolean zeroY, float mult) {
        return randomVector(zeroY).multLocal(mult);
    }

    public static Node createSphereAtPoint(Vector3f point, ColorRGBA color, String message) {
        Node n = new Node();
        n.setLocalTranslation(point);

        Geometry g = new Geometry(point.toString(), new Sphere(16, 16, 0.5f));
        g.setCullHint(Spatial.CullHint.Never);
        Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", color);
        g.setMaterial(m);
        n.attachChild(g);

        BitmapText text = new BitmapText(guiFont, false);
        text.setSize(0.6f);
        BillboardControl control = new BillboardControl();
        text.addControl(control);
        text.setName(message);
        text.move(0f, 0.5f + 0.5f, 0f);
        text.setColor(color);
        text.setText(message);
        n.attachChild(text);

        rootNode.attachChild(n);

        return n;
    }
}