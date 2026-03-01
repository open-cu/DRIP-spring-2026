package ru.cu.lecture.props;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import ru.cu.lecture.model.Person;

import java.util.List;

// Лучше вешать над конфигом
@PropertySource("classpath:custom.properties")
@Data
//@Component
public class CustomProps {
    @Value("${list.prop}")
    private List<String> stringsListProp;
    @Value("${list.prop}")
    private List<Integer> integersListProp;
    @Value("${person.prop}")
    private Person personProp;

    @Value("${person.list.prop}")
    private List<Person> personsListProp;
    @Override
    public String toString() {
        return "CustomProps{" +
                "\n\tstringsListProp=" + stringsListProp +
                ", \n\tintegersListProp=" + integersListProp +
                ", \n\tpersonProp=" + personProp +
                ", \n\tpersonsListProp=" + personsListProp +
                "\n}";
    }
}
