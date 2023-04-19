package _3D_rending;

import _3D_rending._3D_Container.Dot;
import _3D_rending._3D_Container.triangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class _3D_rendering{
    JFrame frame = new JFrame();
    Container pane = frame.getContentPane();
    int[] x = new int[1];
    int[] y = new int[1];
    Matrix3D Transform;




    List<triangle> Create_tetrahedron(List<triangle> tris){
        tris.add(new triangle(new Dot(100, 100, 100),
                new Dot(-100, -100, 100),
                new Dot(-100, 100, -100),
                Color.WHITE));
        tris.add(new triangle(new Dot(100, 100, 100),
                new Dot(-100, -100, 100),
                new Dot(100, -100, -100),
                Color.RED));
        tris.add(new triangle(new Dot(-100, 100, -100),
                new Dot(100, -100, -100),
                new Dot(100, 100, 100),
                Color.GREEN));
        tris.add(new triangle(new Dot(-100, 100, -100),
                new Dot(100, -100, -100),
                new Dot(-100, -100, 100),
                Color.BLUE));
        return tris;
    }

    static boolean sameSide(Dot A, Dot B, Dot C, Dot p){
        Dot V1V2 = new Dot(B.x - A.x,B.y - A.y,B.z - A.z);
        Dot V1V3 = new Dot(C.x - A.x,C.y - A.y,C.z - A.z);
        Dot V1P = new Dot(p.x - A.x,p.y - A.y,p.z - A.z);
        double V1V2CrossV1V3 = V1V2.x * V1V3.y - V1V3.x * V1V2.y;
        double V1V2CrossP = V1V2.x * V1P.y - V1P.x * V1V2.y;
        return V1V2CrossV1V3 * V1V2CrossP >= 0;
    }
    private void Render(){
        JPanel renderPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());


                List<triangle> tris = new ArrayList<>();
                tris = Create_tetrahedron(tris);

                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

                double heading = Math.toRadians(x[0]);
                Matrix3D headingTransform = new Matrix3D(new double[]{
                        Math.cos(heading), 0, -Math.sin(heading),
                        0, 1, 0,
                        Math.sin(heading), 0, Math.cos(heading)
                });
                double pitch = Math.toRadians(y[0]);
                Matrix3D pitchTransform = new Matrix3D(new double[]{
                        1, 0, 0,
                        0, Math.cos(pitch), Math.sin(pitch),
                        0, -Math.sin(pitch), Math.cos(pitch)
                });
                Transform = pitchTransform.multiply(headingTransform);
                double[] zBuffer = new double[img.getWidth() * img.getHeight()];
                Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY);

                g2.translate(getWidth() / 2, getHeight() / 2);
                g2.setColor(Color.WHITE);
                for (triangle t : tris) {
                        Dot v1 = Transform.transform(t.v1);
                        Dot v2 = Transform.transform(t.v2);
                        Dot v3 = Transform.transform(t.v3);
                        v1.x += getWidth() / 2.0;
                        v1.y += getHeight() / 2.0;
                        v2.x += getWidth() / 2.0;
                        v2.y += getHeight() / 2.0;
                        v3.x += getWidth() / 2.0;
                        v3.y += getHeight() / 2.0;
                        int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
                        int maxX = (int) Math.min(img.getWidth() - 1,
                                Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
                        int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
                        int maxY = (int) Math.min(img.getHeight() - 1,
                                Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));

                        for (int y = minY; y <= maxY; y++) {
                            for (int x = minX; x <= maxX; x++) {
                                Dot p = new Dot(x,y,0);
                                boolean V1 = sameSide(v1,v2,v3,p);
                                boolean V2 = sameSide(v2,v3,v1,p);
                                boolean V3 = sameSide(v3,v1,v2,p);
                                if (V3 && V2 && V1) {
                                    double depth = v1.z + v2.z + v3.z;
                                    int zIndex = y * img.getWidth() + x;
                                    if (zBuffer[zIndex] < depth) {
                                        img.setRGB(x, y, t.color.getRGB());
                                        zBuffer[zIndex] = depth;
                                    }
                                }
                            }
                        }
                    }

                    g2.drawImage(img, -(getWidth()/2), -(getHeight()/2), null);
                }

        };
        renderPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double yi = 180.0 / renderPanel.getHeight();
                double xi = 180.0 / renderPanel.getWidth();

                x[0] = (int) (e.getX() * xi);
                y[0] = -1 * (int) (e.getY() * yi);
                renderPanel.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        pane.add(renderPanel, BorderLayout.CENTER);

    }

    public _3D_rendering(){

        pane.setLayout(new BorderLayout());

        Render();

        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);



    }
}
