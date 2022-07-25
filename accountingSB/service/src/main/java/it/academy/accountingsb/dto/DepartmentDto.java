package it.academy.accountingsb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DepartmentDto {

    private Integer id;
    @NotEmpty(message = "Название не должно быть пустым")
    @Size(min = 2, max = 30, message = "Название должно содержать от 2 до 30 символов")
    private String name;
}
