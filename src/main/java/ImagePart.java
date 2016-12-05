import Jama.Matrix;

import java.util.ArrayList;
import java.util.List;

public class ImagePart {

    private int startX;
    private int startY;
    private List<Double> vectorX;
    private Matrix X;

    public ImagePart(int startX, int startY) {
        vectorX = new ArrayList<>();
        this.startX = startX;
        this.startY = startY;
    }

    public void createMatrixX() {
        double bufferX[][] = new double[1][vectorX.size()];
        for (int i = 0; i < vectorX.size(); i++) {
            bufferX[0][i] = vectorX.get(i);
        }
        X = new Matrix(bufferX);
    }

    public void addElement(double newElement) {
        vectorX.add(newElement);
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public Matrix getX() {
        return X;
    }

}
