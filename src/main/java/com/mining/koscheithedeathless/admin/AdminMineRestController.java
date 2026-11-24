package com.mining.koscheithedeathless.admin;

import com.mining.koscheithedeathless.organization.Mine;
import com.mining.koscheithedeathless.organization.MineService;
import com.mining.koscheithedeathless.organization.dto.MineRequest;
import com.mining.koscheithedeathless.organization.dto.MineResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/mines")
@RequiredArgsConstructor
public class AdminMineRestController {

    private final MineService mineService;

    @GetMapping
    public List<MineResponse> listMines() {
        return mineService.getAllMines().stream()
                .map(MineResponse::from)
                .toList();
    }

    @GetMapping("/{mine_id}")
    public ResponseEntity<?> getMine(@PathVariable Long mine_id) {
        try {
            Mine mine = mineService.getMineById(mine_id);
            return ResponseEntity.ok(MineResponse.from(mine));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createMine(@Valid @RequestBody MineRequest request,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationError(bindingResult);
        }
        Mine created = mineService.createMine(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(MineResponse.from(created));
    }

    @PutMapping("/{mine_id}")
    public ResponseEntity<?> updateMine(@PathVariable Long mine_id,
                                        @Valid @RequestBody MineRequest request,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationError(bindingResult);
        }
        try {
            Mine updated = mineService.updateMine(mine_id, request);
            return ResponseEntity.ok(MineResponse.from(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{mine_id}/toggle")
    public ResponseEntity<?> toggleStatus(@PathVariable Long mine_id) {
        try {
            mineService.toggleMineStatus(mine_id);
            Mine mine = mineService.getMineById(mine_id);
            return ResponseEntity.ok(MineResponse.from(mine));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    private ResponseEntity<?> validationError(BindingResult bindingResult) {
        Map<String, String> errors = new java.util.HashMap<>();
        for (FieldError fe : bindingResult.getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(Map.of("errors", errors));
    }
}