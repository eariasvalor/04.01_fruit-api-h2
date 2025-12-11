package cat.itacademy.s04.s02.n01.fruit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FruitResponseDTO {
    private Long id;
    private String name;
    private int weightInKilos;
}