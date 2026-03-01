package ru.cu.lecture.props;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Arrays;

// Лучше вешать над конфигом
@PropertySource("classpath:simple.properties")
@Data
@Component
public class SimpleProps {

    @Value("${not.really.existing.prop:DefaultValue}")
    private String notReallyExistingPropWithDefaultValue;

    @Value("${str.prop}")
    private String strProp;

    @Value("${int.prop}")
    private String intPropAsStr;

    @Value("${double.prop}")
    private String doublePropAsStr;

    @Value("${int.prop}")
    private int intProp;

    @Value("${double.prop}")
    private double doubleProp;

    @Value("${array.prop}")
    private String[] stringsArrProp;

    @Value("${array.prop}")
    private int[] integersArrProp;

    @Override
    public String toString() {
        return "SimpleProps{" +
                "\n\tnotReallyExistingPropWithDefaultValue='" + notReallyExistingPropWithDefaultValue + '\'' +
                ", \n\tstrProp='" + strProp + '\'' +
                ", \n\tintPropAsStr='" + intPropAsStr + '\'' +
                ", \n\tdoublePropAsStr=" + doublePropAsStr +
                ", \n\tintProp=" + intProp +
                ", \n\tdoubleProp=" + doubleProp +
                ", \n\tstringsArrProp=" + Arrays.toString(stringsArrProp) +
                ", \n\tintegersArrProp=" + Arrays.toString(integersArrProp) +
                "\n}";
    }
}
