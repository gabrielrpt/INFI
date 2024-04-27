package Application;

public class Machine {

    private int id; //machine id
    private int p3;
    private int p4;
    private int p5;
    private int p6;
    private int p7;
    private int p8;
    private int p9;

    private int total;

    private int time;

    public Machine(int id, int p3, int p4, int p5, int p6, int p7, int p8, int p9, int total, int time) {
        this.id = id;
        this.p3 = p3;
        this.p4 = p4;
        this.p5 = p5;
        this.p6 = p6;
        this.p7 = p7;
        this.p8 = p8;
        this.p9 = p9;
        this.total = total;
        this.time = time;
    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setP3(int p3) {
        this.p3 = p3;
    }

    public void setP4(int p4) {
        this.p4 = p4;
    }

    public void setP5(int p5) {
        this.p5 = p5;
    }

    public void setP6(int p6) {
        this.p6 = p6;
    }

    public void setP7(int p7) {
        this.p7 = p7;
    }

    public void setP8(int p8) {
        this.p8 = p8;
    }

    public void setP9(int p9) {
        this.p9 = p9;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getP3() {
        return p3;
    }

    public int getP4() {
        return p4;
    }

    public int getP5() {
        return p5;
    }

    public int getP6() {
        return p6;
    }

    public int getP7() {
        return p7;
    }

    public int getP8() {
        return p8;
    }

    public int getP9() {
        return p9;
    }

    public int getId() {
        return id;
    }

    public int getTotal() {
        return total;
    }
}

