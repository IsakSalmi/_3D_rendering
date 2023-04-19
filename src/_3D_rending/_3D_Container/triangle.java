package _3D_rending._3D_Container;

import java.awt.*;

public class triangle {
    public Dot v1;
    public Dot v2;
    public Dot v3;
    public Color color;
    public triangle(Dot v1, Dot v2, Dot v3, Color color){
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = color;
    }
}
