package _3D_Container;

import java.awt.*;

public class tetrahedron {
    public triangle v1;
    public triangle v2;
    public triangle v3;
    Color color;
    public tetrahedron(triangle v1, triangle v2, triangle v3, Color color){
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = color;
    }
}
