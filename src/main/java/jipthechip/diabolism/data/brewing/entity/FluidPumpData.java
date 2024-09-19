package jipthechip.diabolism.data.brewing.entity;

import java.io.Serializable;

public class FluidPumpData implements Serializable {

    private boolean isActive;

    public FluidPumpData(){
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void toggleActive(){
        isActive = !isActive;
    }
}
