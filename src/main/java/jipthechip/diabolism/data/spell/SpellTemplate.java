package jipthechip.diabolism.data.spell;

import java.io.Serializable;

public class SpellTemplate implements Serializable {

    private final SpellType spellType;

    public SpellTemplate(SpellType type){
        this.spellType = type;
    }

    public SpellType getSpellType() {
        return spellType;
    }
}
