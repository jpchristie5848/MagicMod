package jipthechip.diabolism.data.brewing;

import jipthechip.diabolism.data.MagickaConsumerData;
import jipthechip.diabolism.data.MagickaFluid;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;

public class ExtendedMagickaConsumerData<T> extends MagickaConsumerData implements Serializable {

    T extendedData;

    public ExtendedMagickaConsumerData(List<MagickaFluid> fluids, int progress, T extendedData){
        super(fluids, progress);
        this.extendedData = extendedData;
    }

    public ExtendedMagickaConsumerData(@Nullable T extendedData){
        super();
        this.extendedData = extendedData;
    }

    public T getExtendedData(){
        return extendedData;
    }

    public void setExtendedData(T extendedData){
        this.extendedData = extendedData;
    }
}
