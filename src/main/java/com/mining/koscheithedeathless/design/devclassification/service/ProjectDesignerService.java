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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectDesignerService {

    private final MineRepository mineRepository;
    private final GeologicalStabilityClassMineRepository classMineRepository;
    private final GeologicalStabilityClassRepository globalClassRepository;
    private final ClassPropertyStabilityRepository classPropertyRepository;
    private final PropertyStabilityRepository propertyRepository;
    private final RangeValuePropertyStabilityNameRepository valueRepository;
    private final RangeValuePropertyStabilityNameMineRepository mineValueRepository;

    // === Рудники ===
    public List<Mine> getAllMines() {
        return mineRepository.findAll();
    }

    // === Классы рудника ===
    public List<GeologicalStabilityClassMine> getClassesForMine(Long mineId) {
        List<GeologicalStabilityClassMine> classes = classMineRepository.findByMine_MineId(mineId);
        return classes != null ? classes : new ArrayList<>();
    }

    public List<GeologicalStabilityClass> getAllGlobalClasses() {
        return globalClassRepository.findAll();
    }

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

    public GeologicalStabilityClassMine getClassMineById(Long id) {
        return classMineRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void deleteClassMine(Long classMineId) {
        GeologicalStabilityClassMine classMine = classMineRepository.findById(classMineId)
                .orElseThrow(() -> new RuntimeException("Класс не найден"));

        // Сначала удаляем все значения свойств, привязанные к этому классу
        List<RangeValuePropertyStabilityNameMine> values =
                mineValueRepository.findByGeologicalStabilityClassMine_GeologicalStabilityClassMineId(classMineId);
        if (!values.isEmpty()) {
            mineValueRepository.deleteAll(values);
        }

        // Затем удаляем сам класс
        classMineRepository.delete(classMine);
    }

    // === Свойства класса ===
    /** Возвращает связи класса и свойств (с флагом isRequired). */
    public List<ClassPropertyStability> getPropertyLinksForClass(Long globalClassId) {
        return classPropertyRepository.findByGeologicalStabilityClass_GeologicalStabilityClassId(globalClassId);
    }

    // Для обратной совместимости (если где-то используется)
    public List<PropertyStability> getPropertiesForClass(Long globalClassId) {
        return getPropertyLinksForClass(globalClassId).stream()
                .map(ClassPropertyStability::getPropertyStability)
                .collect(Collectors.toList());
    }

    /** Все доступные диапазоны для свойства. */
    public List<RangeValuePropertyStabilityName> getValuesForProperty(Long propertyId) {
        return valueRepository.findByPropertyStability_PropertyStabilityId(propertyId);
    }

    /** Уже сохранённые значения для класса рудника. */
    public List<RangeValuePropertyStabilityNameMine> getSavedValuesForClassMine(Long classMineId) {
        return mineValueRepository.findByGeologicalStabilityClassMine_GeologicalStabilityClassMineId(classMineId);
    }

    /**
     * Сохраняет выбранные значения. Если valueId == null — удаляет старую запись (очистка выбора).
     */
    @Transactional
    public void saveValuesForClassMine(Long classMineId, List<Long> propertyIds, List<Long> valueIds) {
        GeologicalStabilityClassMine classMine = classMineRepository.findById(classMineId).orElseThrow();

        for (int i = 0; i < propertyIds.size(); i++) {
            Long propId = propertyIds.get(i);
            Long valId = valueIds.get(i);

            Optional<RangeValuePropertyStabilityNameMine> existingOpt =
                    mineValueRepository
                            .findByGeologicalStabilityClassMine_GeologicalStabilityClassMineIdAndPropertyStability_PropertyStabilityId(
                                    classMineId, propId);

            if (valId == null) {
                // Проектировщик очистил выбор — удаляем старую запись, если была
                existingOpt.ifPresent(mineValueRepository::delete);
                continue;
            }

            PropertyStability property = propertyRepository.findById(propId).orElseThrow();
            RangeValuePropertyStabilityName value = valueRepository.findById(valId).orElseThrow();

            if (existingOpt.isPresent()) {
                existingOpt.get().setValuePropertyStability(value);
                mineValueRepository.save(existingOpt.get());
            } else {
                RangeValuePropertyStabilityNameMine newValue = new RangeValuePropertyStabilityNameMine();
                newValue.setGeologicalStabilityClassMine(classMine);
                newValue.setPropertyStability(property);
                newValue.setValuePropertyStability(value);
                mineValueRepository.save(newValue);
            }
        }
    }
}