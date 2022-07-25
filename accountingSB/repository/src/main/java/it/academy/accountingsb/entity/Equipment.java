package it.academy.accountingsb.entity;

import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "equipment")
public class Equipment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "account_number")
    private Integer accountNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @ColumnTransformer(write = "UPPER(?)")
    @Column(name = "serial_number", unique = true)
    private String serialNumber;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "equip_detail_id")
    private EquipmentDetail equipmentDetail;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "equipment_invoice",
            joinColumns = {@JoinColumn(name = "equipment_id")},
            inverseJoinColumns = {@JoinColumn(name = "invoice_id")}
    )
    @ToString.Exclude
    @Builder.Default
    private List<Invoice> invoices = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "res_person_id")
    private ResponsiblePerson responsiblePerson;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Equipment equipment = (Equipment) object;
        return accountNumber == equipment.accountNumber
                && Objects.equals(id, equipment.id)
                && Objects.equals(status, equipment.status)
                && Objects.equals(startDate, equipment.startDate)
                && Objects.equals(price, equipment.price)
                && Objects.equals(serialNumber, equipment.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber,
                status, startDate, price, serialNumber);
    }

}