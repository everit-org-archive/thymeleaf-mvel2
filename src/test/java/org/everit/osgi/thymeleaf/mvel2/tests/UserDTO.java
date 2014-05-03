package org.everit.osgi.thymeleaf.mvel2.tests;

public class UserDTO {

    private String name;

    private long age;

    public UserDTO(String name, long age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public long getAge() {
        return age;
    }

}
