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
                .filter(Mine::isMine_enabled)
                .toList();
    }
    public Mine getMineById(Long mine_id) {
        return mineRepository.findById(mine_id)
                .orElseThrow(() -> new RuntimeException("Рудник не найден"));
    }

    //@Transactional - Всё, что происходит внутри этого метода (или класса), должно выполняться как одна транзакция
    @Transactional
    public Mine createMine(MineRequest request) {
        Mine mine = new Mine();
        mine.setMine_code(request.getMine_code());
        mine.setMine_name(request.getMine_name());
        mine.setMine_address(request.getMine_address());
        mine.setMine_description(request.getMine_description());
        return mineRepository.save(mine);
    }

    @Transactional
    public Mine updateMine(Long mine_id, MineRequest request) {
        Mine mine = mineRepository.findById(mine_id)
                .orElseThrow(() -> new RuntimeException("Рудник не найден"));

        mine.setMine_code(request.getMine_code());
        mine.setMine_name(request.getMine_name());
        mine.setMine_address(request.getMine_address());
        mine.setMine_description(request.getMine_description());
        return mineRepository.save(mine);
    }

    // Переключение статуса (вместо удаления)
    @Transactional
    public void toggleMineStatus(Long id) {
        Mine mine = mineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Рудник не найден"));
        mine.setMine_enabled(!mine.isMine_enabled());
        mineRepository.save(mine);
    }
}