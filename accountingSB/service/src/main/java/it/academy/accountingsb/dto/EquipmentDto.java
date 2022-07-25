package it.academy.accountingsb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EquipmentDto {

    private Integer id;
    @NotNull(message = "Поле не должно быть пустым")
    @Digits(message = "Введите цифру", integer = 9, fraction = 0)
    @Min(value = 1, message = "Поле должно быть больше 0")
    private Integer accountNumber;
    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 5, max = 15, message = "Серийный номер должен содержать от 5 до 15 символов")
    private String serialNumber;
    private List<InvoiceDto> invoicesForEquipment;
    private Integer idInvoiceLast;
    private Integer invoiceNumberLast;
    private EquipmentDetailDto equipmentDetail;
    @NotEmpty(message = "Поле не должно быть пустым")
    private String status;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @NotNull(message = "Поле не должно быть пустым")
    @DecimalMin("1")
    private BigDecimal price;
    private ResponsiblePersonDto responsiblePerson;
}
