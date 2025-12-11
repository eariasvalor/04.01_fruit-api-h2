package cat.itacademy.s04.s02.n01.fruit.service;

import cat.itacademy.s04.s02.n01.fruit.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.dto.FruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.dto.FruitResponseDTO;
import cat.itacademy.s04.s02.n01.fruit.exceptions.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.mapper.FruitMapper;
import cat.itacademy.s04.s02.n01.fruit.model.Fruit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public List<FruitResponseDTO> getAllFruits() {
        return fruitRepository.findAll().stream()
                .map(fruitMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FruitResponseDTO getFruitById(Long id) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException(
                        "Fruit not found with id: " + id));

        return fruitMapper.toDTO(fruit);
    }
}
