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

    @GetMapping("/{mine_id}/edit")
    public String showEditForm(@PathVariable Long mine_id, Model model) {
        Mine mine = mineService.getMineById(mine_id);

        MineRequest req = new MineRequest();
        req.setMine_code(mine.getMine_code());
        req.setMine_name(mine.getMine_name());
        req.setMine_address(mine.getMine_address());
        req.setMine_description(mine.getMine_description());

        model.addAttribute("mineForm", req);
        model.addAttribute("mineId", mine_id);
        return "admin/mines/edit";
    }

    @PostMapping("/{mine_id}/edit")
    public String updateMine(@PathVariable Long mine_id,
                             @Valid @ModelAttribute("mineForm") MineRequest request,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/mines/edit";
        }
        mineService.updateMine(mine_id, request);
        return "redirect:/admin/mines";
    }

    // Замена удаления на блокировку
    @PostMapping("/{mine_id}/toggle")
    public String toggleStatus(@PathVariable Long mine_id) {
        mineService.toggleMineStatus(mine_id);
        return "redirect:/admin/mines";
    }
}