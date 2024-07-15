package org.example.hw3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Program {




    // Создание таблицы Person
    private static void createPersonTable(Connection connection) throws SQLException {
        String createTableSQL = """
  CREATE TABLE Person (
  id BIGINT PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  department_id BIGINT,
  FOREIGN KEY (department_id) REFERENCES Department(id)
  )
""";
        try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
            statement.execute();
        }
    }

    // Создание таблицы Department
    private static void createDepartmentTable(Connection connection) throws SQLException {
        String createTableSQL = """
  CREATE TABLE Department (
  id BIGINT PRIMARY KEY,
  name VARCHAR(128) NOT NULL
  )
""";
        try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
            statement.execute();
        }
    }

    // Метод для получения имени департамента по идентификатору персоны
    private static String getPersonDepartmentName(Connection connection, long personId) throws SQLException {
        String query = """
  SELECT d.name
  FROM Person p
  JOIN Department d ON p.department_id = d.id
  WHERE p.id = ?
""";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, personId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            } else {
                return null;
            }
        }
    }

    // Метод для получения маппинга person.name -> department.name
    private static Map<String, String> getPersonDepartments(Connection connection) throws SQLException {
        String query = """
  SELECT p.name AS person_name, d.name AS department_name
  FROM Person p
  JOIN Department d ON p.department_id = d.id
""";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            Map<String, String> personDepartments = new HashMap<>();
            while (resultSet.next()) {
                personDepartments.put(resultSet.getString("person_name"), resultSet.getString("department_name"));
            }
            return personDepartments;
        }
    }

    // Метод для получения маппинга department.name -> <person.name>
    private static Map<String, List<String>> getDepartmentPersons(Connection connection) throws SQLException {
        String query = """
  SELECT d.name AS department_name, p.name AS person_name
  FROM Person p
  JOIN Department d ON p.department_id = d.id
""";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            Map<String, List<String>> departmentPersons = new HashMap<>();
            while (resultSet.next()) {
                String departmentName = resultSet.getString("department_name");
                String personName = resultSet.getString("person_name");
                departmentPersons.computeIfAbsent(departmentName, k -> new ArrayList<>()).add(personName);
            }
            return departmentPersons;
        }
    }
}

