package it.academy.accountingsb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceDto {

    private Integer id;
    @NotNull(message = "Поле не должно быть пустым")
    @Digits(message = "Введите цифру", integer = 9, fraction = 0)
    @Min(value = 1, message = "Поле должно быть больше 0")
    private Integer number;
    @NotNull(message = "Нужно ввести дату накладной")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotEmpty(message = "Поле не должно быть пустым")
    private String cause;
    private OrganizationDto receiver;
    private OrganizationDto supplier;
    private List<EquipmentDto> equipmentsInInvoice;
}
