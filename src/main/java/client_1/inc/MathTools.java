package client_1.inc;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.value.ObservableValue;

import java.text.NumberFormat;

public class MathTools {
    public static NumberFormat instance = NumberFormat.getInstance();

    public static void init(){
        instance.setMaximumFractionDigits(2);
    }

    public static float toPrice(float f){
        return Float.parseFloat(instance.format(f).replace(",",""));
    }
    public static String toPriceAsString(float f){
        return instance.format(f);
    }

    public static StringExpression toStringExpression(FloatProperty fp){
        return Bindings.format("%.2f", fp);
    }
    public static StringExpression toStringExpression(DoubleProperty dp){
        return Bindings.format("%.2f", dp);
    }

    public static ObservableValue<String> toStringExpression(NumberBinding subtract) {
        return Bindings.format("%.2f", subtract);
    }
}
