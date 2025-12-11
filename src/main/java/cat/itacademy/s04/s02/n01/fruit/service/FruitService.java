package cat.itacademy.s04.s02.n01.fruit.service;

import cat.itacademy.s04.s02.n01.fruit.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.dto.FruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.dto.FruitResponseDTO;
import cat.itacademy.s04.s02.n01.fruit.mapper.FruitMapper;
import cat.itacademy.s04.s02.n01.fruit.model.Fruit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FruitService {

    private final FruitRepository fruitRepository;
    private final FruitMapper fruitMapper;

    @Transactional
    public FruitResponseDTO createFruit(FruitRequestDTO request) {
        Fruit fruit = fruitMapper.toEntity(request);
        Fruit savedFruit = fruitRepository.save(fruit);
        return fruitMapper.toDTO(savedFruit);
    }
}
