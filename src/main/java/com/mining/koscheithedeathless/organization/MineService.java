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

    public List<Mine> getAllMines() {
        return mineRepository.findAll();
    }

    public List<Mine> getActiveMines() {
        return mineRepository.findAll().stream()
                .filter(Mine::isMineEnabled)
                .toList();
    }
    public Mine getMineById(Long mineId) {
        return mineRepository.findById(mineId)
                .orElseThrow(() -> new RuntimeException("Рудник не найден"));
    }

    //@Transactional - Всё, что происходит внутри этого метода (или класса), должно выполняться как одна транзакция
    @Transactional
    public Mine createMine(MineRequest request) {
        Mine mine = new Mine();
        mine.setMineCode(request.getMineCode());
        mine.setMineName(request.getMineName());
        mine.setMineAddress(request.getMineAddress());
        mine.setMineDescription(request.getMineDescription());
        return mineRepository.save(mine);
    }

    @Transactional
    public Mine updateMine(Long mineId, MineRequest request) {
        Mine mine = mineRepository.findById(mineId)
                .orElseThrow(() -> new RuntimeException("Рудник не найден"));

        mine.setMineCode(request.getMineCode());
        mine.setMineName(request.getMineName());
        mine.setMineAddress(request.getMineAddress());
        mine.setMineDescription(request.getMineDescription());
        return mineRepository.save(mine);
    }

    // Переключение статуса (вместо удаления)
    @Transactional
    public void toggleMineStatus(Long mineId) {
        Mine mine = mineRepository.findById(mineId)
                .orElseThrow(() -> new RuntimeException("Рудник не найден"));
        mine.setMineEnabled(!mine.isMineEnabled());
        mineRepository.save(mine);
    }
}