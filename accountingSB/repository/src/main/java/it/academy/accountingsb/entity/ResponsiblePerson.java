package it.academy.accountingsb.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "responsible_person",
        uniqueConstraints = @UniqueConstraint(columnNames = {"branch_id", "department_id"}))
public class ResponsiblePerson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @OneToMany(mappedBy = "responsiblePerson", cascade = CascadeType.MERGE)
    private Set<Equipment> equipments
            = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ResponsiblePerson person = (ResponsiblePerson) object;
        return Objects.equals(id, person.id)
                && Objects.equals(name, person.name)
                && Objects.equals(surname, person.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname);
    }

    @Override
    public String toString() {
        return "ResponsiblePerson{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
