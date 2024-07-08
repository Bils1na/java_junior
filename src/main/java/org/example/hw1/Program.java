package org.example.hw1;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Program {

    private static class Department {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Department{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    private static class Person {
        private String name;
        private int age;
        private double salary;
        private Department depart;

        public Person(String name, int age, double salary, Department depart) {
            this.name = name;
            this.age = age;
            this.salary = salary;
            this.depart = depart;
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

        public double getSalary() {
            return salary;
        }

        public void setSalary(double salary) {
            this.salary = salary;
        }

        public Department getDepart() {
            return depart;
        }

        public void setDepart(Department depart) {
            this.depart = depart;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", salary=" + salary +
                    ", depart=" + depart +
                    '}';
        }
    }

    /**
     * Найти самого молодого сотрудника
     */
    static Optional<Person> findMostYoungestPerson(List<Person> people) {
        return people.stream()
                .min(Comparator.comparingInt(Person::getAge));
    }

    /**
     * Найти департамент, в котором работает сотрудник с самой большой зарплатой
     */
    static Optional<Department> findMostExpensiveDepartment(List<Person> people) {
        return people.stream()
                .max(Comparator.comparingDouble(Person::getSalary))
                .map(Person::getDepart);
    }

    /**
     * Сгруппировать сотрудников по департаментам
     */
    static Map<Department, List<Person>> groupByDepartment(List<Person> people) {
        return people.stream()
                .collect(Collectors.groupingBy(Person::getDepart));
    }

    /**
     * Сгруппировать сотрудников по названиям департаментов
     */
    static Map<String, List<Person>> groupByDepartmentName(List<Person> people) {
        return people.stream()
                .collect(Collectors.groupingBy(person -> person.getDepart().getName()));
    }

    /**
     * В каждом департаменте найти самого старшего сотрудника
     */
    static Map<String, Person> getDepartmentOldestPerson(List<Person> people) {
        return people.stream()
                .collect(Collectors.groupingBy(person -> person.getDepart().getName(),
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(Person::getAge)),
                                Optional::get
                        )
                ));
    }

    /**
     * *Найти сотрудников с минимальными зарплатами в своем отделе
     * (прим. можно реализовать в два запроса)
     */
    static List<Person> cheapPersonsInDepartment(List<Person> people) {
        return people.stream()
                .collect(Collectors.groupingBy(Person::getDepart))
                .values()
                .stream()
                .flatMap(deptPeople -> deptPeople.stream()
                        .min(Comparator.comparingDouble(Person::getSalary))
                        .map(Stream::of)
                        .orElseGet(Stream::empty))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        Department department1 = new Department();
        Department department2 = new Department();
        department1.setName("dept1");
        department2.setName("dept2");

        Person person1 = new Person("Ivan", 26, 1500, department1);
        Person person2 = new Person("Irina", 28, 1000, department1);
        Person person3 = new Person("Egor", 33, 3000, department1);
        Person person4 = new Person("Nikolay", 27, 2000, department2);

        List<Person> people = new ArrayList<>();
        people.add(person1);
        people.add(person2);
        people.add(person3);
        people.add(person4);

        System.out.println(findMostYoungestPerson(people));
        System.out.println(findMostExpensiveDepartment(people));
        System.out.println(cheapPersonsInDepartment(people));
    }

}
