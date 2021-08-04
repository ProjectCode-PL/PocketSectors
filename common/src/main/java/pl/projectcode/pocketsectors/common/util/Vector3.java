package pl.projectcode.pocketsectors.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Vector3 implements Serializable {

    private final double x;
    private final double y;
    private final double z;

    @Override
    public String toString() {
        return "Vector3{" +
                "x=" + (int) x +
                ", y=" + (int) y +
                ", z=" + (int) z +
                '}';
    }
}
