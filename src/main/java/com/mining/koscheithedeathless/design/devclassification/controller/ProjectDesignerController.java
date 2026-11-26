package com.mining.koscheithedeathless.design.devclassification.controller;

import com.mining.koscheithedeathless.design.devclassification.*;
import com.mining.koscheithedeathless.design.devclassification.dto.*;
import com.mining.koscheithedeathless.design.devclassification.service.ProjectDesignerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/designer")
@RequiredArgsConstructor
public class ProjectDesignerController {

    private final ProjectDesignerService designerService;

    // 1. Список всех рудников
    @GetMapping("/mines")
    public List<MineDto> getMines() {
        return designerService.getAllMines().stream().map(m -> {
            MineDto dto = new MineDto();
            dto.setMineId(m.getMineId());
            dto.setMineName(m.getMineName());
            return dto;
        }).toList();
    }

    // 2. Список всех глобальных классов (для выпадающего списка)
    @GetMapping("/global-classes")
    public List<GlobalClassDto> getGlobalClasses() {
        return designerService.getAllGlobalClasses().stream().map(gc -> {
            GlobalClassDto dto = new GlobalClassDto();
            dto.setGeologicalStabilityClassId(gc.getGeologicalStabilityClassId());
            dto.setGeologicalStabilityClassCode(gc.getGeologicalStabilityClassCode());
            return dto;
        }).toList();
    }

    // 3. Классы, добавленные на конкретный рудник
    @GetMapping("/mines/{mineId}/classes")
    public List<ClassMineDto> getClassesForMine(@PathVariable Long mineId) {
        return designerService.getClassesForMine(mineId).stream().map(cm -> {
            ClassMineDto dto = new ClassMineDto();
            dto.setGeologicalStabilityClassMineId(cm.getGeologicalStabilityClassMineId());
            dto.setGeologicalStabilityClassMineCode(cm.getGeologicalStabilityClassMineCode());
            dto.setGlobalClassId(cm.getGeologicalStabilityClass().getGeologicalStabilityClassId());
            dto.setGlobalClassCode(cm.getGeologicalStabilityClass().getGeologicalStabilityClassCode());
            dto.setMineId(cm.getMine().getMineId());
            dto.setMineName(cm.getMine().getMineName());
            return dto;
        }).toList();
    }

    // 4. Добавить класс на рудник
    @PostMapping("/mines/{mineId}/classes")
    public ResponseEntity<Void> addClassToMine(@PathVariable Long mineId,
                                               @RequestParam Long globalClassId,
                                               @RequestParam String localCode) {
        designerService.addClassToMine(mineId, globalClassId, localCode);
        return ResponseEntity.ok().build();
    }

    // 5. Удалить класс
    @DeleteMapping("/classes/{classMineId}")
    public ResponseEntity<Void> deleteClass(@PathVariable Long classMineId) {
        designerService.deleteClassMine(classMineId);
        return ResponseEntity.noContent().build();
    }

    // 6. Данные для страницы настройки свойств
    @GetMapping("/classes/{classMineId}/properties")
    public ClassPropertiesResponse getClassProperties(@PathVariable Long classMineId) {
        GeologicalStabilityClassMine classMine = designerService.getClassMineById(classMineId);
        Long globalClassId = classMine.getGeologicalStabilityClass().getGeologicalStabilityClassId();

        // Забираем связи класса и свойств (там есть isRequired)
        List<ClassPropertyStability> links = designerService.getPropertyLinksForClass(globalClassId);

        List<PropertyStability> properties = links.stream()
                .map(ClassPropertyStability::getPropertyStability)
                .toList();

        List<RangeValuePropertyStabilityNameMine> saved = designerService.getSavedValuesForClassMine(classMineId);

        // Доступные значения
        Map<Long, List<RangeValueDto>> availableValues = properties.stream()
                .collect(Collectors.toMap(
                        PropertyStability::getPropertyStabilityId,
                        prop -> designerService.getValuesForProperty(prop.getPropertyStabilityId())
                                .stream().map(v -> {
                                    RangeValueDto dto = new RangeValueDto();
                                    dto.setValuePropertyStabilityId(v.getValuePropertyStabilityId());
                                    dto.setRangeValuePropertyStabilityNameRu(v.getRangeValuePropertyStabilityNameRu());
                                    return dto;
                                }).toList()
                ));

        ClassPropertiesResponse response = new ClassPropertiesResponse();
        response.setGeologicalStabilityClassMineId(classMine.getGeologicalStabilityClassMineId());
        response.setGeologicalStabilityClassMineCode(classMine.getGeologicalStabilityClassMineCode());
        response.setGlobalClassCode(classMine.getGeologicalStabilityClass().getGeologicalStabilityClassCode());
        response.setMineId(classMine.getMine().getMineId());

        // Собираем PropertyDto с isRequired из связи
        response.setProperties(links.stream().map(link -> {
            PropertyStability p = link.getPropertyStability();
            PropertyDto dto = new PropertyDto();
            dto.setPropertyStabilityId(p.getPropertyStabilityId());
            dto.setPropertyStabilityNameRu(p.getPropertyStabilityNameRu());
            dto.setUnit(p.getUnit());
            dto.setRequired(link.isRequired());   // ← флаг обязательности
            return dto;
        }).toList());

        response.setAvailableValues(availableValues);

        response.setSavedValues(saved.stream().map(s -> {
            SavedValueDto dto = new SavedValueDto();
            dto.setPropertyStabilityId(s.getPropertyStability().getPropertyStabilityId());
            dto.setValuePropertyStabilityId(s.getValuePropertyStability() != null ?
                    s.getValuePropertyStability().getValuePropertyStabilityId() : null);
            return dto;
        }).toList());

        return response;
    }

    // 7. Сохранить значения свойств
    @PostMapping("/classes/{classMineId}/properties")
    public ResponseEntity<Void> saveClassProperties(@PathVariable Long classMineId,
                                                    @RequestBody Map<String, List<Long>> payload) {
        List<Long> propertyIds = payload.get("propertyIds");
        List<Long> valueIds = payload.get("valueIds");
        if (propertyIds != null && valueIds != null) {
            designerService.saveValuesForClassMine(classMineId, propertyIds, valueIds);
        }
        return ResponseEntity.ok().build();
    }
}