package com.mining.koscheithedeathless.design.devclassification.service;

import com.mining.koscheithedeathless.design.devclassification.*;
import com.mining.koscheithedeathless.design.devclassification.repository.*;
import com.mining.koscheithedeathless.organization.Mine;
import com.mining.koscheithedeathless.organization.MineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectDesignerService {

    private final MineRepository mineRepository;
    private final GeologicalStabilityClassMineRepository classMineRepository;
    private final GeologicalStabilityClassRepository globalClassRepository;
    private final ClassPropertyStabilityRepository classPropertyRepository; // Связь классов и свойств
    private final PropertyStabilityRepository propertyRepository;
    private final RangeValuePropertyStabilityNameRepository valueRepository; // Глобальные значения
    private final RangeValuePropertyStabilityNameMineRepository mineValueRepository; // Значения на руднике

    // Получить все рудники для выбора
    public List<Mine> getAllMines() {
        return mineRepository.findAll();
    }

    // Получить настройки классов для конкретного рудника
    public List<GeologicalStabilityClassMine> getClassesForMine(Long mineId) {
        List<GeologicalStabilityClassMine> classes = classMineRepository.findByMineId(mineId);
        return classes != null ? classes : new ArrayList<>(); // <-- Защита от null
    }

    // Получить все глобальные классы (1b, 2b и т.д. для выпадающего списка)
    public List<GeologicalStabilityClass> getAllGlobalClasses() {
        return globalClassRepository.findAll();
    }

    // Добавить класс на рудник
    @Transactional
    public void addClassToMine(Long mineId, Long globalClassId, String localCode) {
        Mine mine = mineRepository.findById(mineId).orElseThrow();
        GeologicalStabilityClass globalClass = globalClassRepository.findById(globalClassId).orElseThrow();

        GeologicalStabilityClassMine newClassMine = new GeologicalStabilityClassMine();
        newClassMine.setMine(mine);
        newClassMine.setGeologicalStabilityClass(globalClass);
        newClassMine.setGeologicalStabilityClassMineCode(localCode);

        classMineRepository.save(newClassMine);
    }
    // 1. Получить свойства, привязанные к конкретному классу (из таблицы class_property_stability)
    public List<PropertyStability> getPropertiesForClass(Long globalClassId) {
        List<ClassPropertyStability> links = classPropertyRepository.findAll();
        return links.stream()
                .filter(link -> link.getGeologicalStabilityClass().getGeologicalStabilityClassId().equals(globalClassId))
                .map(ClassPropertyStability::getPropertyStability)
                .collect(Collectors.toList());
    }

    // 2. Получить все возможные значения (диапазоны) для конкретного свойства
    public List<RangeValuePropertyStabilityName> getValuesForProperty(Long propertyId) {
        return valueRepository.findAll().stream()
                .filter(v -> v.getPropertyStability().getPropertyStabilityId().equals(propertyId))
                .collect(Collectors.toList());
    }

    // 3. Получить уже сохраненные значения для класса на руднике
    public List<RangeValuePropertyStabilityNameMine> getSavedValuesForClassMine(Long classMineId) {
        return mineValueRepository.findByGeologicalStabilityClassMineGeologicalStabilityClassMineId(classMineId);
    }

    // 4. Сохранить выбранные значения
    @Transactional
    public void saveValuesForClassMine(Long classMineId, List<Long> propertyIds, List<Long> valueIds) {
        GeologicalStabilityClassMine classMine = classMineRepository.findById(classMineId).orElseThrow();

        for (int i = 0; i < propertyIds.size(); i++) {
            Long propId = propertyIds.get(i);
            Long valId = valueIds.get(i);

            if (valId == null) continue; // Пропускаем, если ничего не выбрано

            PropertyStability property = propertyRepository.findById(propId).orElseThrow();
            RangeValuePropertyStabilityName value = valueRepository.findById(valId).orElseThrow();

            // Проверяем, есть ли уже такая запись, если есть - обновляем, если нет - создаем
            RangeValuePropertyStabilityNameMine existing = mineValueRepository.findAll().stream()
                    .filter(v -> v.getGeologicalStabilityClassMine().getGeologicalStabilityClassMineId().equals(classMineId)
                            && v.getPropertyStability().getPropertyStabilityId().equals(propId))
                    .findFirst().orElse(null);

            if (existing != null) {
                existing.setValuePropertyStability(value);
                mineValueRepository.save(existing);
            } else {
                RangeValuePropertyStabilityNameMine newValue = new RangeValuePropertyStabilityNameMine();
                newValue.setGeologicalStabilityClassMine(classMine);
                newValue.setPropertyStability(property);
                newValue.setValuePropertyStability(value);
                mineValueRepository.save(newValue);
            }
        }
    }
    public GeologicalStabilityClassMine getClassMineById(Long id) {
        return classMineRepository.findById(id).orElseThrow();
    }
    @Transactional
    public void deleteClassMine(Long classMineId) {
        // Находим класс и удаляем его.
        // Благодаря связям в БД, настройки свойств (range_value_property_stability_name_mines) удалятся автоматически.
        GeologicalStabilityClassMine classMine = classMineRepository.findById(classMineId)
                .orElseThrow(() -> new RuntimeException("Класс не найден"));
        classMineRepository.delete(classMine);
    }
}
