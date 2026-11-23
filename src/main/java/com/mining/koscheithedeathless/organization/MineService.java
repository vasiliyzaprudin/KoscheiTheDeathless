package com.mining.koscheithedeathless.organization;

import com.mining.koscheithedeathless.organization.dto.MineRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MineService {
    private final MineRepository mineRepository;

    // Получаем ВСЕ рудники для админки (включая заблокированные)
    public List<Mine> getAllMines() {
        return mineRepository.findAll();
    }

    // Получаем только АКТИВНЫЕ рудники (для выпадающих списков)
    public List<Mine> getActiveMines() {
        return mineRepository.findAll().stream()
                .filter(Mine::isEnabled)
                .toList();
    }
    public Mine getMineById(Long id) {
        return mineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Рудник не найден"));
    }

    @Transactional
    public Mine createMine(MineRequest request) {
        Mine mine = new Mine();
        mine.setCode(request.getCode());
        mine.setName(request.getName());
        mine.setAddress(request.getAddress());
        mine.setDescription(request.getDescription());
        return mineRepository.save(mine);
    }

    @Transactional
    public Mine updateMine(Long id, MineRequest request) {
        Mine mine = mineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Рудник не найден"));

        mine.setCode(request.getCode());
        mine.setName(request.getName());
        mine.setAddress(request.getAddress());
        mine.setDescription(request.getDescription());
        return mineRepository.save(mine);
    }

    // Переключение статуса (вместо удаления)
    @Transactional
    public void toggleMineStatus(Long id) {
        Mine mine = mineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Рудник не найден"));
        mine.setEnabled(!mine.isEnabled());
        mineRepository.save(mine);
    }
}