package Entities;

import jakarta.persistence.*;

@Entity
public class Human {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(nullable = false, unique = true)
    public String name;

    @Column(nullable = true)
    public String surname;

    @Column(nullable = true)
    public String fi;

    @Override
    public String toString() {
        return "Human{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
