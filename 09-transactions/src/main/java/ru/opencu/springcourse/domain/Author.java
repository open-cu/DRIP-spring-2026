package ru.opencu.springcourse.domain;

public class Author {

    private Long id;
    private String name;
    private String email;

    public Author() {}

    public Author(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Long getId()                 { return id; }
    public void setId(Long id)          { this.id = id; }

    public String getName()             { return name; }
    public void setName(String name)    { this.name = name; }

    public String getEmail()            { return email; }
    public void setEmail(String email)  { this.email = email; }

    @Override
    public String toString() {
        return "Author{id=%d, name='%s'}".formatted(id, name);
    }
}
