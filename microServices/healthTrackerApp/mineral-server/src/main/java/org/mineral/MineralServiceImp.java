package org.mineral;

import java.util.List;

import org.mineral.model.entity.Mineral;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MineralServiceImp {

	private final MineralRepository mineralRepository;

	public List<Mineral> getAllViewMinerals() {

		return mineralRepository
				.getAllMinerals()
				.stream()
				.toList();
	}

	public Mineral getMineralViewByName(String name) throws MineralNotFoundException {

		return mineralRepository
				.getMineralByName(name)
				.orElseThrow(() -> new MineralNotFoundException(name));
	}

	public List<String> getAllMineralNames() {
		return mineralRepository.getAllMineralNames();
	}

}