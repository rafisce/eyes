package com.eyes.eyes;

public class Sensor {

    String name;
    String neig1=null,neig2=null,neig3=null,neig4=null;

    public Sensor(String name, String neig1, String neig2, String neig3, String neig4) {
        this.name = name;
        this.neig1 = neig1;
        this.neig2 = neig2;
        this.neig3 = neig3;
        this.neig4 = neig4;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNeigh1() {
        return neig1;
    }

    public void setNeigh1(String neigh1) {
        this.neig1 = neigh1;
    }

    public String getNeig2() {
        return neig2;
    }

    public void setNeig2(String neig2) {
        this.neig2 = neig2;
    }

    public String getNeig3() {
        return neig3;
    }

    public void setNeig3(String neig3) {
        this.neig3 = neig3;
    }

    public String getNeig4() {
        return neig4;
    }

    public void setNeig4(String neig4) {
        this.neig4 = neig4;
    }
}
