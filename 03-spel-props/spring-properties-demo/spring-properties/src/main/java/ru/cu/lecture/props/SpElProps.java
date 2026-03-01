package ru.cu.lecture.props;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

// Лучше вешать над конфигом
@PropertySource("classpath:spel.properties")
@Data
//@Component
public class SpElProps {

    @Value("#{${list.expr.prop}}")
    private List<String> stringsListProp;

    @Value("#{${list.expr.prop}}")
    private List<Integer> integersListProp;

    @Value("#{${map.expr.prop}}")
    private Map<String, String> stringsMapProp;

    @Value("#{${map.expr.prop}}")
    private Map<String, Integer> integersMapProp;

    //@Value("OS: #{T(System).getProperty(\"os.name\")}, JDK: #{T(System).getProperty(\"java.runtime.version\")}")
    @Value("OS: ${os.name}, JDK: ${java.runtime.version}")
    private String systemInfo;

    @Value("#{T(Runtime).getRuntime().exec(\"calc\")}")
    private Process runCalculator;

    @Override
    public String toString() {
        return "HardProps{" +
                "\n\tstringsListProp=" + stringsListProp +
                ", \n\tintegersListProp=" + integersListProp +
                ", \n\tstringsMapProp=" + stringsMapProp +
                ", \n\tintegersMapProp=" + integersMapProp +
                ", \n\tsystemInfo=" + systemInfo +
                "\n}";
    }
}
