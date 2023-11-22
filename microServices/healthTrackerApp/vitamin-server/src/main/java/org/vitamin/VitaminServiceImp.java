package org.vitamin;

import java.util.List;

import org.springframework.stereotype.Service;
import org.vitamin.model.entity.Vitamin;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VitaminServiceImp {

	private final VitaminRepository vitaminRepository;

	public List<Vitamin> getVitamins() {
		return vitaminRepository
				.getVitamins()
				.stream()
				.toList();
	}

	public Vitamin getVitaminByName(String name) throws VitaminNotFoundException {
		return vitaminRepository
				.getVitaminByName(name)
				.orElseThrow(() -> new VitaminNotFoundException(name));
	}

	public List<String> getAllVitaminNames() {
		return vitaminRepository.getAllVitaminsNames();
	}
}
