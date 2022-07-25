package it.academy.accountingsb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BranchDto {

    private Integer id;
    @NotEmpty(message = "Название не должно быть пустым")
    @Size(min = 2, max = 30, message = "Название должно содержать от 2 до 30 символов")
    private String name;
    @NotEmpty(message = "Адрес не должен быть пустым")
    @Size(min = 10, max = 40, message = "Адрес должен содержать от 10 до 40 символов")
    private String address;
    @NotEmpty(message = "Данные о контактном лице не должны быть пустыми")
    @Size(min = 5, max = 30, message = "Информация о контактном лице должна содержать от 5 до 40 символов")
    private String contact;
    @NotEmpty(message = "Следует ввести телефон")
    @Pattern(regexp = "\\d{7,10}|(?:\\d{3})\\d{2}-?\\d{5}|\\(\\d{3}\\)\\d{3}-?\\d{4}", message = "Введите телефон правильно")
    private String phone;
    private Set<DepartmentDto> department;

}
