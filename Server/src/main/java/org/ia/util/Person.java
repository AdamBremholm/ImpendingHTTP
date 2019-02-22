package org.ia.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Person implements Serializable {
    String name, address, dateOfBirth;

    public Person(){}

    public Person(String name, String address, String dateOfBirth) {
        this.name = name;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name +
                "\",\"address\":\"" + address +
                ",\"dateOfBirth\":\"" + dateOfBirth +
                "\"" +
                "}";
    }

    public String getAddress() {
        return address;
    }

    public void setAdress(String adress) {
        this.address = adress;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
