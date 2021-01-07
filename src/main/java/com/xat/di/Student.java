package com.xat.di;

import java.util.*;

/**
 * @author xuantao
 */
public class Student {
    private String name;
    private int age;
    private String wife;
    private Address address;
    private Map<String, String> hobbyMap;
    private List<String> books;
    private Set<String> courses;
    private String[] games;
    private Properties information;


    public Properties getInformation() {
        return information;
    }

    public void setInformation(Properties information) {
        this.information = information;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getWife() {
        return wife;
    }

    public void setWife(String wife) {
        this.wife = wife;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Map<String, String> getHobbyMap() {
        return hobbyMap;
    }

    public void setHobbyMap(Map<String, String> hobbyMap) {
        this.hobbyMap = hobbyMap;
    }

    public List<String> getBooks() {
        return books;
    }

    public void setBooks(List<String> books) {
        this.books = books;
    }

    public Set<String> getCourses() {
        return courses;
    }

    public void setCourses(Set<String> courses) {
        this.courses = courses;
    }

    public String[] getGames() {
        return games;
    }

    public void setGames(String[] games) {
        this.games = games;
    }
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", wife='" + wife + '\'' +
                ", address=" + address +
                ", hobbyMap=" + hobbyMap +
                ", books=" + books +
                ", courses=" + courses +
                ", games=" + Arrays.toString(games) +
                ", information=" + information +
                '}';
    }

}
