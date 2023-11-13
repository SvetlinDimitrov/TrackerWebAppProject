package org.electrolyte;

import java.util.List;

import org.electrolyte.model.dtos.ElectrolyteView;
import org.electrolyte.model.dtos.PairView;
import org.electrolyte.model.entity.Electrolyte;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ElectrolyteServiceImp {

	private final ElectrolyteRepository electrolyteRepository;

	public List<ElectrolyteView> getAllViewElectrolytes() {

		return electrolyteRepository
				.getAllElectrolytes()
				.stream()
				.map(this::toElectrolyteView)
				.toList();
	}

	public ElectrolyteView getElectrolyteViewByName(String name) throws ElectrolyteNotFoundException {

		return electrolyteRepository
				.getElectrolyteByName(name)
				.map(this::toElectrolyteView)
				.orElseThrow(() -> new ElectrolyteNotFoundException(name));
	}

	public List<String> getAllElectrolytesNames() {
		return electrolyteRepository.getAllElectrolytesNames();
	}

	private ElectrolyteView toElectrolyteView(Electrolyte entity) {
		return ElectrolyteView.builder()
				.name(entity.getName())
				.description(entity.getDescription())
				.functions(entity.getFunctions().stream().map(PairView::new).toList())
				.sources(entity.getSources().stream().map(PairView::new).toList())
				.healthConsiderations(
						entity.getHealthConsiderations().stream().map(PairView::new).toList())
				.maleHigherBoundIntake(entity.getMaleHigherBoundIntake())
				.maleLowerBoundIntake(entity.getMaleLowerBoundIntake())
				.femaleHigherBoundIntake(entity.getFemaleHigherBoundIntake())
				.femaleLowerBoundIntake(entity.getFemaleLowerBoundIntake())
				.measure(entity.getMeasure())
				.build();
	}

}