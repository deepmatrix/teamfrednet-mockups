package org.teamfrednet.mockups;


import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import java.awt.AWTEvent;
import java.awt.GraphicsConfigTemplate;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Node;
import javax.media.j3d.Locale;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import org.jdesktop.j3d.loaders.vrml97.VrmlLoader;


public class PicoRoverApplet
    extends java.applet.Applet
{
    public final static GraphicsConfiguration PreferredConfiguration(){
        GraphicsConfigTemplate3D gc3D = new GraphicsConfigTemplate3D();
        gc3D.setSceneAntialiasing(GraphicsConfigTemplate.PREFERRED);
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getBestConfiguration(gc3D);
    }

    private SimpleUniverse universe;
    private Canvas3D canvas;


    public PicoRoverApplet(){
        super();
    }


    public void init(){
        try {
            BranchGroup root = new BranchGroup();
            BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);

            {
                TransformGroup scale = new TransformGroup();
                Transform3D t3d = new Transform3D();
                t3d.setScale(8.0);
                scale.setTransform(t3d);
                root.addChild(scale);

                TransformGroup xlate = new TransformGroup();
                xlate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                xlate.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                scale.addChild(xlate);

                {
                    MouseRotate mouse = new MouseRotate();
                    mouse.setTransformGroup(xlate);
                    xlate.addChild(mouse);
                    mouse.setSchedulingBounds(bounds);
                }
                {
                    VrmlLoader loader = new VrmlLoader();
                    URL model = this.getClass().getResource("/PicoRover.wrl");
                    Scene scene = loader.load(model);
                    xlate.addChild(scene.getSceneGroup());
                }

                Color3f bgColor = new Color3f(0.01f, 0.01f, 0.08f);
                Background bgNode = new Background(bgColor);
                bgNode.setApplicationBounds(bounds);

                root.addChild(bgNode);
            }
            this.canvas = new Canvas3D(PreferredConfiguration());
            this.universe = new SimpleUniverse(this.canvas);
            ViewingPlatform viewingPlatform = this.universe.getViewingPlatform();
            {
                PlatformGeometry pg = new PlatformGeometry();

                Color3f ambientColor = new Color3f(0.1f, 0.1f, 0.1f);
                AmbientLight ambientLightNode = new AmbientLight(ambientColor);
                ambientLightNode.setInfluencingBounds(bounds);
                pg.addChild(ambientLightNode);

                Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
                Vector3f light1Direction  = new Vector3f(1.0f, 1.0f, 1.0f);
                Color3f light2Color = new Color3f(1.0f, 1.0f, 1.0f);
                Vector3f light2Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);

                DirectionalLight light1
                    = new DirectionalLight(light1Color, light1Direction);
                light1.setInfluencingBounds(bounds);
                pg.addChild(light1);

                DirectionalLight light2
                    = new DirectionalLight(light2Color, light2Direction);
                light2.setInfluencingBounds(bounds);
                pg.addChild(light2);

                viewingPlatform.setPlatformGeometry( pg );
            }
            viewingPlatform.setNominalViewingTransform();
            this.add(this.canvas);
            this.doLayout();
            this.universe.addBranchGraph(root);
        }
        catch (IOException exc){
            exc.printStackTrace();
            System.exit(1);
        }
    }
}
