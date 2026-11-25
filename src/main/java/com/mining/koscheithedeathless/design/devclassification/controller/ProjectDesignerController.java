package com.mining.koscheithedeathless.design.devclassification.controller;

import com.mining.koscheithedeathless.design.devclassification.GeologicalStabilityClassMine;
import com.mining.koscheithedeathless.design.devclassification.PropertyStability;
import com.mining.koscheithedeathless.design.devclassification.RangeValuePropertyStabilityName;
import com.mining.koscheithedeathless.design.devclassification.RangeValuePropertyStabilityNameMine;
import com.mining.koscheithedeathless.design.devclassification.service.ProjectDesignerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/designer")
@RequiredArgsConstructor
public class ProjectDesignerController {

    private final ProjectDesignerService designerService;

    // Страница выбора рудника
    @GetMapping
    public String listMinesForDesign(Model model) {
        model.addAttribute("mines", designerService.getAllMines());
        return "designer/mines";
    }

    // Страница настройки классов конкретного рудника
    @GetMapping("/{mineId}/classes")
    public String showMineClasses(@PathVariable Long mineId, Model model) {
        model.addAttribute("mineId", mineId);
        model.addAttribute("classes", designerService.getClassesForMine(mineId));
        model.addAttribute("globalClasses", designerService.getAllGlobalClasses());

        return "designer/classes";
    }

    // Обработка добавления нового класса
    @PostMapping("/{mineId}/classes")
    public String addClassToMine(@PathVariable Long mineId,
                                 @RequestParam Long globalClassId,
                                 @RequestParam String localCode) {
        designerService.addClassToMine(mineId, globalClassId, localCode);
        return "redirect:/designer/" + mineId + "/classes";
    }
    // 4. Страница настройки свойств для конкретного класса
    @GetMapping("/classes/{classMineId}/properties")
    public String showClassProperties(@PathVariable Long classMineId, Model model) {
        // Находим сам класс
        GeologicalStabilityClassMine classMine = designerService.getClassMineById(classMineId);

        // Получаем глобальные свойства для этого класса
        Long globalClassId = classMine.getGeologicalStabilityClass().getGeologicalStabilityClassId();
        List<PropertyStability> properties = designerService.getPropertiesForClass(globalClassId);

        // Получаем сохраненные значения (чтобы подставить их в форму при редактировании)
        List<RangeValuePropertyStabilityNameMine> savedValues = designerService.getSavedValuesForClassMine(classMineId);

        model.addAttribute("classMine", classMine);
        model.addAttribute("properties", properties);
        model.addAttribute("savedValues", savedValues);

        // Для каждого свойства подтягиваем доступные диапазоны
        Map<Long, List<RangeValuePropertyStabilityName>> availableValues = properties.stream()
                .collect(Collectors.toMap(
                        PropertyStability::getPropertyStabilityId,
                        prop -> designerService.getValuesForProperty(prop.getPropertyStabilityId()),
                        (existing, replacement) -> existing // Защита от дубликатов
                ));
        model.addAttribute("availableValues", availableValues);

        return "designer/properties";
    }

    // 5. Сохранение значений
    @PostMapping("/classes/{classMineId}/properties")
    public String saveClassProperties(@PathVariable Long classMineId,
                                      @RequestParam(value = "propertyIds", required = false) List<Long> propertyIds,
                                      @RequestParam(value = "valueIds", required = false) List<Long> valueIds) {
        if (propertyIds != null && valueIds != null) {
            designerService.saveValuesForClassMine(classMineId, propertyIds, valueIds);
        }
        return "redirect:/designer/" + designerService.getClassMineById(classMineId).getMine().getId() + "/classes";
    }
    // Удаление класса
    @PostMapping("/{mineId}/classes/{classId}/delete")
    public String deleteClass(@PathVariable Long mineId, @PathVariable Long classId) {
        designerService.deleteClassMine(classId);
        return "redirect:/designer/" + mineId + "/classes";
    }

}