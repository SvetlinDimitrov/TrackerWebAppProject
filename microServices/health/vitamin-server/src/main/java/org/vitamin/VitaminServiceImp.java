package org.vitamin;

import java.util.List;

import org.springframework.stereotype.Service;
import org.vitamin.model.dtos.PairView;
import org.vitamin.model.dtos.VitaminView;
import org.vitamin.model.entity.Vitamin;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VitaminServiceImp {

	private final VitaminRepository vitaminRepository;

	public List<VitaminView> getVitamins() {
		return vitaminRepository
				.getVitamins()
				.stream()
				.map(this::toVitaminView)
				.toList();
	}

	public VitaminView getVitaminViewByName(String name) throws VitaminNotFoundException {
		return vitaminRepository
				.getVitaminByName(name)
				.map(this::toVitaminView)
				.orElseThrow(() -> new VitaminNotFoundException(name));
	}

	public List<String> getAllVitaminsNames() {
		return vitaminRepository.getAllVitaminsNames();
	}

	private VitaminView toVitaminView(Vitamin entity) {
		return VitaminView.builder()
				.name(entity.getName())
				.description(entity.getDescription())
				.functions(entity.getFunctions().stream().map(PairView::new).toList())
				.sources(entity.getSources().stream().map(PairView::new).toList())
				.maleHigherBoundIntake(entity.getMaleHigherBoundIntake())
				.maleLowerBoundIntake(entity.getMaleLowerBoundIntake())
				.femaleHigherBoundIntake(entity.getFemaleHigherBoundIntake())
				.femaleLowerBoundIntake(entity.getFemaleLowerBoundIntake())
				.measure(entity.getMeasure())
				.build();
	}

}
