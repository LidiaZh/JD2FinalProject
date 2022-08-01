package it.academy.accountingsb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

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
    @PastOrPresent(message = "Введите дату корректно")
    private LocalDate date;
    @NotEmpty(message = "Поле не должно быть пустым")
    private String cause;
    private OrganizationDto receiver;
    private OrganizationDto supplier;
}
