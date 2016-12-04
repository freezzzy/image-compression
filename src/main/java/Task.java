import Jama.Matrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Task {

    private int imageWidth;
    private int imageHeight;
    private int n;
    private int m;
    private int p;
    private double e;
    private BufferedImage inputImage = Main.DEFAULT_IMAGE;
    private List<ImagePart> partsList;
    private Matrix W;
    private Matrix W_;

    public Task(int n, int m, int p, double e ) {
        imageWidth = inputImage.getWidth();
        imageHeight = inputImage.getHeight();
        this.n = n;
        this.e = e;
        this.m = m;
        this.p = p;
    }

    public int decomposeImage() {
        partsList = new ArrayList<>();
        int x = 0;
        while (x < imageWidth) {
            int y = 0;
            while (y < imageHeight) {
                ImagePart vector = new ImagePart(x, y, n, m);
                for (int i = x; i < x + n; i++) {
                    for (int j = y; j < y + m; j++) {
                        if (i < imageWidth && j < imageHeight) {
                            Color colorPixel = new Color(inputImage.getRGB(i, j));
                            vector.addElement(setNewColor(colorPixel.getRed()));
                            vector.addElement(setNewColor(colorPixel.getGreen()));
                            vector.addElement(setNewColor(colorPixel.getBlue()));
                        } else {
                            vector.addElement(-1);
                            vector.addElement(-1);
                            vector.addElement(-1);
                        }
                    }
                }
                vector.createMatrixX();
                partsList.add(vector);
                y += m;
            }
            x += n;
        }
        return partsList.size();
    }

    private double setNewColor(double color) {
        return 2 * color / 255 - 1;
    }

    public void doAction() {
        createWeightMatrix();
        init();
    }

    private void createWeightMatrix() {
        double bufferW[][] = new double[n * m * Main.RGB_COLORS_COUNT][p];
        for (int i = 0; i < n * m * Main.RGB_COLORS_COUNT; i++) {
            for (int j = 0; j < p; j++) {
                bufferW[i][j] = Math.random() * 2 - 1;
            }
        }
        W = new Matrix(bufferW);
        W_ = W.transpose();
        normalize(W);
        normalize(W_);
    }

    private void init() {
        int iteration = 1;
        double E = Double.MAX_VALUE;
        while (E > e) {
            double alpha;
            double alpha_;
            E = 0;
            Matrix X;
            Matrix X_;
            for (ImagePart aPartsList : partsList) {
                X = aPartsList.getX();
                Matrix Y = X.times(W);
                X_ = Y.times(W_);
                Matrix deltaX = X_.minus(X);
                alpha = 1 / calculateVectorSum(X);
                alpha_ = 1 / calculateVectorSum(Y);
                W = W.minus(X.transpose().times(alpha).times(deltaX).times(W_.transpose()));
                W_ = W_.minus(Y.transpose().times(alpha_).times(deltaX));
                normalize(W);
                normalize(W_);
            }
            for (ImagePart aPartsList : partsList) {
                X = aPartsList.getX();
                Matrix Y = X.times(W);
                X_ = Y.times(W_);
                E += getError(X, X_);
            }
            System.out.println("Итерация: " + iteration + "; Ошибка: " + E);
            printMatrix(W);
            System.out.println('\n');
            printMatrix(W_);
            iteration++;
        }
    }

    private double getError(Matrix X, Matrix X_) {
        double e = 0;
        for (int i = 0; i < X.getColumnDimension(); i++) {
            e += (X_.get(0, i) - X.get(0, i))*(X_.get(0, i) - X.get(0, i));
        }
        return e;
    }

    private void normalize(Matrix matrix) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            double sum = 0;
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                sum += matrix.get(i, j) * matrix.get(i, j);
            }
            sum = Math.sqrt(sum);
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                matrix.set(i, j, matrix.get(i, j) / sum);
            }
        }
    }

    private void printMatrix(Matrix matrix) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                System.out.print(matrix.get(i, j) + " ");
            }
            System.out.println();
        }
    }

    public double calculateVectorSum(Matrix currentMatrix) {
        if (currentMatrix.getRowDimension()>1) {
            return 0;
        }
        double sum = 1000;
        for (int i = 0; i < currentMatrix.getColumnDimension(); i++) {
            sum += currentMatrix.get(0, i) * currentMatrix.get(0, i);
        }
        return sum;
    }

    public BufferedImage createOutputImage(){
        BufferedImage answer = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);
        for (ImagePart currVector : partsList){
            Matrix X = currVector.getX();
            Matrix Y = X.times(W);
            Matrix X_ = Y.times(W_);
            int xx = currVector.getStartX();
            int yy = currVector.getStartY();
            int l = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    int r = (int) convertOut(X_.get(0, l++));
                    int g = (int) convertOut(X_.get(0, l++));
                    int b = (int) convertOut(X_.get(0, l++));
                    Color currentColor = new Color(r,g,b);
                    if (xx + i < imageWidth && yy + j < imageHeight) {
                        answer.setRGB(xx+i, yy+j, currentColor.getRGB());
                    }
                }
            }
        }
        return answer;
    }

    private double convertOut(double rgb) {
        double ans = 255 * (rgb + 1) / 2;
        if (ans < 0) {
            ans = 0;
        } else if (ans > 255) {
            ans = 255;
        }
        return ans;
    }

    public void saveImage(BufferedImage image) {
        File outputFile = new File("img/output.jpg");
        try {
            ImageIO.write(image, "jpg", outputFile);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
