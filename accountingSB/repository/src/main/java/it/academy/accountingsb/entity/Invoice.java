package it.academy.accountingsb.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "invoice",
        uniqueConstraints = @UniqueConstraint(columnNames = {"number", "date"}))
public class Invoice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "number")
    private int number;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "cause")
    private String cause;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id")
    private Organization receiver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id")
    private Organization supplier;

    @ManyToMany(mappedBy = "invoices", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Equipment> equipments = new ArrayList<>();

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Invoice invoice = (Invoice) object;
        return id == invoice.id
                && number == invoice.number
                && Objects.equals(date, invoice.date)
                && Objects.equals(cause, invoice.cause);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, date, cause);
    }
}
