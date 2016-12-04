public class Parameters {

    private int w;
    private int h;
    private int p;
    private int n;
    private double z;
    private int l;
    private double error;
    private Task task;

    public void update() {
        task = new Task(w, h, p, error);
        n = w * h * Main.RGB_COLORS_COUNT;
        l = task.decomposeImage();
        z = (n * l) / ((n + l) * p + 2.0);
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setP(int p) {
        this.p = p;
    }

    public void setError(double error) {
        this.error = error;
    }

    public Task getTask() {
        return task;
    }

    public void printParameters() {
        System.out.println("n = " + this.w);
        System.out.println("m = " + this.h);
        System.out.println("p = " + this.p);
        System.out.println("N = " + this.n);
        System.out.println("Z = " + this.z);
        System.out.println("L = " + this.l);
    }

}
