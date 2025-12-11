package cat.itacademy.s04.s02.n01.fruit.mapper;

import cat.itacademy.s04.s02.n01.fruit.dto.FruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.dto.FruitResponseDTO;
import cat.itacademy.s04.s02.n01.fruit.model.Fruit;
import org.springframework.stereotype.Component;

@Component
public class FruitMapper {

    public Fruit toEntity(FruitRequestDTO dto) {
        Fruit fruit = new Fruit();
        fruit.setName(dto.getName());
        fruit.setWeightInKilos(dto.getWeightInKilos());
        return fruit;
    }

    public FruitResponseDTO toDTO(Fruit entity) {
        return new FruitResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getWeightInKilos()
        );
    }
}
