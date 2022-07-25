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
@Table(name = "equipment_detail")
public class EquipmentDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String desc;

    @Column(name = "term_of_use")
    private Integer termOfUse;

    @Column(name = "producer")
    private String producer;

    @OneToMany(mappedBy = "equipmentDetail", cascade = CascadeType.MERGE)
    @ToString.Exclude
    private Set<Equipment> equipment = new HashSet<>();

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        EquipmentDetail that = (EquipmentDetail) object;
        return termOfUse == that.termOfUse && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(desc, that.desc) && Objects.equals(producer, that.producer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, desc, termOfUse, producer);
    }
}
