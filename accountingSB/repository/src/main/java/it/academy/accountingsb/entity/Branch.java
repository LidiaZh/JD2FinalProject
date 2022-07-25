package it.academy.accountingsb.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "branch")
public class Branch implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "contact")
    private String contact;

    @Column(name = "phone")
    private String phone;
    @OneToMany(mappedBy = "branch", cascade = CascadeType.MERGE)
    private Set<ResponsiblePerson> responsiblePerson = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "department_branch",
            joinColumns = {@JoinColumn(name = "id_branch")},
            inverseJoinColumns = {@JoinColumn(name = "id_department")}
    )
    @Builder.Default
    private Set<Department> department = new HashSet<>();

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return Objects.equals(id, branch.id)
                && Objects.equals(name, branch.name)
                && Objects.equals(address, branch.address)
                && Objects.equals(contact, branch.contact)
                && Objects.equals(phone, branch.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, contact, phone);
    }
}
