package cat.itacademy.s04.s02.n01.fruit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FruitRequestDTO {

    @NotBlank(message = "The name cannot be empty.")
    private String name;

    @Positive(message = "The weight must be greater than 0.")
    private int weightInKilos;
}
