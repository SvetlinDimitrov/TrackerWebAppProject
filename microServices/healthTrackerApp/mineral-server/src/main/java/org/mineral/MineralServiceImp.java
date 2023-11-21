package org.mineral;

import java.util.List;

import org.mineral.model.dtos.MineralView;
import org.mineral.model.dtos.PairView;
import org.mineral.model.entity.Mineral;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MineralServiceImp {

	private final MineralRepository mineralRepository;

	public List<MineralView> getAllViewMinerals() {

		return mineralRepository
				.getAllMinerals()
				.stream()
				.map(this::toMineralView)
				.toList();
	}

	public MineralView getMineralViewByName(String name) throws MineralNotFoundException {

		return mineralRepository
				.getMineralByName(name)
				.map(this::toMineralView)
				.orElseThrow(() -> new MineralNotFoundException(name));
	}

	public List<String> getAllMineralNames() {
		return mineralRepository.getAllMineralNames();
	}

	private MineralView toMineralView(Mineral entity) {
		return MineralView.builder()
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