package it.academy.accountingsb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EquipmentDetailDto {
    private Integer id;
    @NotEmpty(message = "Название не должно быть пустым")
    @Size(min = 2, max = 30, message = "Название должно содержать от 2 до 30 символов")
    private String name;
    @NotEmpty(message = "Описание не должно быть пустым")
    private String desc;
    @NotNull(message = "Поле не должно быть пустым")
    @Digits(message = "Введите цифру", integer = 2, fraction = 0)
    @Min(value = 1, message = "Поле должно быть больше 0")
    @Max(value = 20, message = "Поле не должно быть больше 20")
    private Integer termOfUse;
    @NotEmpty(message = "Необходимо ввести производтеля оборудования")
    private String producer;
}
