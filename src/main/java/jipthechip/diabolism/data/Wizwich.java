package jipthechip.diabolism.data;

import java.io.Serializable;

public class Wizwich implements Serializable {

    String ingredients;

    public Wizwich(String ingredients){
        this.ingredients = ingredients;
    }

    public String getIngredients() {
        return ingredients;
    }
}
