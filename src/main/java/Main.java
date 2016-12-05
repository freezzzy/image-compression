import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    final static int RGB_COLORS_COUNT = 3;
    static BufferedImage DEFAULT_IMAGE;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in, "UTF-8");
        Parameters parameters = new Parameters();

        try {
            DEFAULT_IMAGE = ImageIO.read(new File("img/input.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Введите ширину прямоугольника:");
        parameters.setW(sc.nextInt());

        System.out.println("Введите высоту прямоугольника:");
        parameters.setH(sc.nextInt());

        System.out.println("Введите число нейронов на втором слое:");
        parameters.setP(sc.nextInt());

        System.out.println("Введите допустимое значение среднеквадратичной ошибки:");
        parameters.setError(Double.valueOf(sc.next()));

        parameters.update();

        Task task = parameters.getTask();
        task.doAction();

        BufferedImage image = task.createOutputImage();

        task.saveImage(image);

        System.out.println("Изображение сохранено");
        parameters.printParameters();
    }

}

