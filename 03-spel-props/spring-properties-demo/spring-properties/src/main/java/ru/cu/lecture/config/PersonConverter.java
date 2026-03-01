package ru.cu.lecture.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.cu.lecture.model.Person;

@Component
public class PersonConverter implements Converter<String, Person> {
    @Override
    public Person convert(String source) {
        var arr = source.split(";");
        return new Person(arr[0], arr[1]);
    }
}
