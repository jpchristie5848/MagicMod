package jipthechip.diabolism.Utils;

public interface IMagicProperties {

    public int getMagicShield();

    public void setMagicShield(int entityId);

    public void toggleMagicShield();

    public boolean isAwakened();

    public void setAwakened(boolean awakened);

    public int getMagicka();

    public void setMagicka(int magicka);

    public int getMaxMagicka();

    public void setMaxMagicka(int maxMagicka);

    public float getMagickaRegenRate();

    public void setMagickaRegenRate(float regenRate);

    public void deinitialize();
}
