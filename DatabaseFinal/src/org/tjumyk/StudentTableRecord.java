/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tjumyk;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author E40-G8C
 */
// Generated by JFXTableClassBuilder
// Inputs:
// id,String
// name,String
// gender,String
// startYear,Integer
// startAge,Integer
// className,String
// End
public class StudentTableRecord {

    StringProperty id;
    StringProperty name;
    StringProperty gender;
    IntegerProperty startYear;
    IntegerProperty startAge;
    StringProperty className;

    public StudentTableRecord(String id, String name, String gender, Integer startYear, Integer startAge, String className) {
        setId(id);
        setName(name);
        setGender(gender);
        setStartYear(startYear);
        setStartAge(startAge);
        setClassName(className);
    }
    
    public StringProperty idProperty() {
        if (id == null) {
            id = new SimpleStringProperty();
        }
        return id;
    }

    public String getId() {
        return idProperty().get();
    }

    public void setId(String value) {
        idProperty().set(value);
    }

    public StringProperty nameProperty() {
        if (name == null) {
            name = new SimpleStringProperty();
        }
        return name;
    }

    public String getName() {
        return nameProperty().get();
    }

    public void setName(String value) {
        nameProperty().set(value);
    }

    public StringProperty genderProperty() {
        if (gender == null) {
            gender = new SimpleStringProperty();
        }
        return gender;
    }

    public String getGender() {
        return genderProperty().get();
    }

    public void setGender(String value) {
        genderProperty().set(value);
    }

    public IntegerProperty startYearProperty() {
        if (startYear == null) {
            startYear = new SimpleIntegerProperty();
        }
        return startYear;
    }

    public Integer getStartYear() {
        return startYearProperty().get();
    }

    public void setStartYear(Integer value) {
        startYearProperty().set(value);
    }

    public IntegerProperty startAgeProperty() {
        if (startAge == null) {
            startAge = new SimpleIntegerProperty();
        }
        return startAge;
    }

    public Integer getStartAge() {
        return startAgeProperty().get();
    }

    public void setStartAge(Integer value) {
        startAgeProperty().set(value);
    }

    public StringProperty classNameProperty() {
        if (className == null) {
            className = new SimpleStringProperty();
        }
        return className;
    }

    public String getClassName() {
        return classNameProperty().get();
    }

    public void setClassName(String value) {
        classNameProperty().set(value);
    }
// Code Example of building corresponding TableColumns:
//
// TableColumnBuilder.create().text("id").cellValueFactory(new PropertyValueFactory("id")).prefWidth(200).build(),
// TableColumnBuilder.create().text("name").cellValueFactory(new PropertyValueFactory("name")).prefWidth(200).build(),
// TableColumnBuilder.create().text("gender").cellValueFactory(new PropertyValueFactory("gender")).prefWidth(200).build(),
// TableColumnBuilder.create().text("startYear").cellValueFactory(new PropertyValueFactory("startYear")).prefWidth(200).build(),
// TableColumnBuilder.create().text("startAge").cellValueFactory(new PropertyValueFactory("startAge")).prefWidth(200).build(),
// TableColumnBuilder.create().text("className").cellValueFactory(new PropertyValueFactory("className")).prefWidth(200).build()
}
