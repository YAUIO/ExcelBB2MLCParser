package Entities;

import Annotations.AlternateTitle;

import java.util.Arrays;
import java.util.Objects;

public class Person {
    @AlternateTitle("WRITER LAST NAME *")
    public String LastName;

    @AlternateTitle("WRITER FIRST NAME")
    public String FirstName;

    public String MiddleName;

    @AlternateTitle("WRITER ROLE CODE *")
    public String code;

    private enum role_codes {C, A, CA}

    public Person(String name, String role_code) {

        if (!name.isEmpty() || !name.isBlank()) {
            while (name.charAt(0) == ' ') {
                name = name.substring(1);
                if (name.isEmpty() || name.isBlank()) {
                    break;
                }
            }
        }


        if (!name.isEmpty() && !name.isBlank()) {
            String[] nameArr = name.split(" ");

            switch (nameArr.length) {
                case 3:
                    MiddleName = nameArr[2];
                case 2:
                    FirstName = nameArr[1];
                case 1:
                    LastName = nameArr[0];
                    break;
                default:
                    LastName = "UNCONVERTIBLE " + name;
            }
        }

        try {
            code = role_codes.valueOf(role_code).toString();
        } catch (Exception _) {
            throw new RuntimeException("Couldn't parse creator role code: " + role_code);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        if (LastName != null && LastName.equals(person.LastName)) {
            if (FirstName != null && FirstName.equals(person.FirstName)) {
                if (MiddleName != null) {
                    return MiddleName.equals(person.MiddleName);
                }
                return true;
            }
        }
        return false;
    }
}
