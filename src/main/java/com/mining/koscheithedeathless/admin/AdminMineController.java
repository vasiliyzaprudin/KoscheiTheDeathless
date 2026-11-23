package com.mining.koscheithedeathless.admin;

import com.mining.koscheithedeathless.organization.Mine;
import com.mining.koscheithedeathless.organization.MineService;
import com.mining.koscheithedeathless.organization.dto.MineRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/mines")
@RequiredArgsConstructor
public class AdminMineController {

    private final MineService mineService;

    @GetMapping
    public String listMines(Model model) {
        model.addAttribute("mines", mineService.getAllMines());
        return "admin/mines/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("mineForm", new MineRequest());
        return "admin/mines/form";
    }

    @PostMapping
    public String createMine(@Valid @ModelAttribute("mineForm") MineRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/mines/form";
        }
        mineService.createMine(request);
        return "redirect:/admin/mines";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Mine mine = mineService.getMineById(id); // <-- Используем сервис

        MineRequest req = new MineRequest();
        req.setCode(mine.getCode());
        req.setName(mine.getName());
        req.setAddress(mine.getAddress());
        req.setDescription(mine.getDescription());

        model.addAttribute("mineForm", req);
        model.addAttribute("mineId", id);
        return "admin/mines/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateMine(@PathVariable Long id,
                             @Valid @ModelAttribute("mineForm") MineRequest request,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/mines/edit";
        }
        mineService.updateMine(id, request);
        return "redirect:/admin/mines";
    }

    // Замена удаления на блокировку
    @PostMapping("/{id}/toggle")
    public String toggleStatus(@PathVariable Long id) {
        mineService.toggleMineStatus(id);
        return "redirect:/admin/mines";
    }
}