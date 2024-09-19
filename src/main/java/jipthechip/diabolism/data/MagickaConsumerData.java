package jipthechip.diabolism.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MagickaConsumerData implements Serializable {

    protected List<MagickaFluid> fluids;

    protected int progress;

    public MagickaConsumerData(List<MagickaFluid> fluids, int progress) {
        this.fluids = fluids;
        this.progress = progress;
    }

    public MagickaConsumerData(){
        this.fluids = new ArrayList<>();
        this.progress = 0;
    }

    public List<MagickaFluid> getFluids() {
        return fluids;
    }

    public void addFluid(MagickaFluid fluid){
        fluids.add(fluid);
    }

    public MagickaFluid getFluid(int index){
        return fluids.get(index);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
