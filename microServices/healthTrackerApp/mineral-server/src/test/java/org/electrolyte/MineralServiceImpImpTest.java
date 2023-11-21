package org.electrolyte;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mineral.MineralNotFoundException;
import org.mineral.MineralRepository;
import org.mineral.MineralServiceImp;
import org.mineral.model.dtos.MineralView;
import org.mineral.model.dtos.PairView;
import org.mineral.model.entity.Mineral;
import org.mineral.model.entity.Pair;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MineralServiceImpImpTest {

    @Mock
    private MineralRepository mineralRepository;

    @InjectMocks
    private MineralServiceImp mineralServiceImp;

    private final List<Mineral> mineralList = new ArrayList<>();
    private final List<MineralView> mineralViews = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        mineralList.addAll(List.of(Mineral.builder()
                .name("Sodium")
                .description(
                        "Sodium is a vital mineral and electrolyte that plays a central role in maintaining various physiological functions in the body. It's essential for regulating fluid balance, nerve transmission, muscle function, and blood pressure. While sodium is important for health, excessive intake can lead to health issues like high blood pressure and increased risk of heart disease.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Fluid Balance")
                                        .value("Sodium helps regulate the balance of fluids inside and outside cells. It's a major contributor to osmotic pressure, which determines the movement of water between cells and their surroundings.")
                                        .build(),
                                Pair.builder()
                                        .key("Nerve Transmission")
                                        .value("Sodium is essential for generating electrical signals that allow nerve cells to communicate with each other and with muscles. This is critical for muscle contraction, sensation, and other nervous system functions.")
                                        .build(),
                                Pair.builder()
                                        .key("Muscle Contraction")
                                        .value("Sodium is involved in muscle contraction, including the contractions of skeletal muscles and the heart.")
                                        .build(),
                                Pair.builder()
                                        .key("Blood Pressure Regulation")
                                        .value("Sodium levels influence blood volume and blood pressure. Excess sodium intake can cause the body to retain more water, leading to increased blood volume and higher blood pressure.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Table salt")
                                        .value("sodium chloride")
                                        .build(),
                                Pair.builder()
                                        .key("Processed and packaged foods")
                                        .value("canned soups, snacks, sauces")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Fast food and restaurant meals")
                                        .build(),
                                Pair.builder()
                                        .key("Some natural foods")
                                        .value("dairy products, meat, seafood")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("High Blood Pressure")
                                        .value("Consuming too much sodium can lead to increased blood pressure, a major risk factor for heart disease and stroke.")
                                        .build(),
                                Pair.builder()
                                        .key("Cardiovascular Health")
                                        .value("High sodium intake is associated with an increased risk of heart disease and stroke.")
                                        .build(),
                                Pair.builder()
                                        .key("Fluid Retention")
                                        .value("Excess sodium can lead to water retention, causing swelling and bloating.")
                                        .build()))
                .femaleHigherBoundIntake(new BigDecimal("2300"))
                .femaleLowerBoundIntake(new BigDecimal("2300"))
                .maleHigherBoundIntake(new BigDecimal("2300"))
                .maleLowerBoundIntake(new BigDecimal("2300"))
                .measure("milligrams (mg)")
                .build(),

                Mineral.builder()
                        .name("Potassium")
                        .description(
                                "Potassium is an essential mineral and electrolyte that plays a critical role in maintaining various bodily functions. It works in tandem with sodium to help regulate fluid balance, nerve transmission, muscle function, and blood pressure. Potassium is abundant in many foods, and maintaining a proper balance of potassium in the body is crucial for overall health.")
                        .functions(
                                List.of(
                                        Pair.builder()
                                                .key("Fluid Balance")
                                                .value("Potassium is a key player in maintaining fluid balance within cells and their surroundings. It helps regulate osmotic pressure, which determines the movement of water in and out of cells")
                                                .build(),
                                        Pair.builder()
                                                .key("Nerve Transmission")
                                                .value("Potassium is essential for generating electrical signals that enable nerve cells to communicate with each other and with muscles. This is vital for proper muscle function and sensation.")
                                                .build(),
                                        Pair.builder()
                                                .key("Muscle Contraction")
                                                .value("Potassium plays a central role in muscle contraction. Adequate levels of potassium are necessary for muscles, including the heart, to contract and relax properly.")
                                                .build(),
                                        Pair.builder()
                                                .key("Blood Pressure Regulation")
                                                .value("Potassium intake is closely linked to blood pressure regulation. It helps counteract the effects of sodium by promoting the excretion of sodium through urine, which can help lower blood pressure.")
                                                .build()))

                        .sources(
                                List.of(
                                        Pair.builder()
                                                .key("Fruits")
                                                .value("bananas, oranges, melons, avocados")
                                                .build(),
                                        Pair.builder()
                                                .key("Vegetables")
                                                .value("spinach, potatoes, tomatoes")
                                                .build(),
                                        Pair.builder()
                                                .key("Legumes")
                                                .value("beans, lentils")
                                                .build(),
                                        Pair.builder()
                                                .key("Dairy products")
                                                .value("milk, yogurt")
                                                .build(),
                                        Pair.builder()
                                                .key("-")
                                                .value("Nuts and seeds")
                                                .build(),
                                        Pair.builder()
                                                .key("Whole grains")
                                                .value("brown rice, whole wheat")
                                                .build(),
                                        Pair.builder()
                                                .key("Fish")
                                                .value("salmon, tuna")
                                                .build()))
                        .healthConsiderations(
                                List.of(
                                        Pair.builder()
                                                .key("-")
                                                .value("Maintaining a proper balance of potassium is important for overall health, but excessive intake of potassium supplements can be harmful, especially for individuals with certain medical conditions like kidney problems. Kidneys play a critical role in regulating potassium levels in the body, and compromised kidney function can lead to potassium imbalances.")
                                                .build()))
                        .maleLowerBoundIntake(new BigDecimal("3500"))
                        .maleHigherBoundIntake(new BigDecimal("4700"))
                        .femaleLowerBoundIntake(new BigDecimal("3500"))
                        .femaleHigherBoundIntake(new BigDecimal("4700"))
                        .measure("milligrams (mg)")
                        .build()));
        mineralViews.add(MineralView.builder()
                .name(mineralList.get(0).getName())
                .description(mineralList.get(0).getDescription())
                .functions(mineralList.get(0).getFunctions().stream().map(PairView::new).toList())
                .sources(mineralList.get(0).getSources().stream().map(PairView::new).toList())
                .healthConsiderations(
                        mineralList.get(0).getHealthConsiderations().stream().map(PairView::new).toList())
                .maleHigherBoundIntake(mineralList.get(0).getMaleHigherBoundIntake())
                .maleLowerBoundIntake(mineralList.get(0).getMaleLowerBoundIntake())
                .femaleHigherBoundIntake(mineralList.get(0).getFemaleHigherBoundIntake())
                .femaleLowerBoundIntake(mineralList.get(0).getFemaleLowerBoundIntake())
                .measure(mineralList.get(0).getMeasure())
                .build());
        mineralViews.add(MineralView.builder()
                .name(mineralList.get(1).getName())
                .description(mineralList.get(1).getDescription())
                .functions(mineralList.get(1).getFunctions().stream().map(PairView::new).toList())
                .sources(mineralList.get(1).getSources().stream().map(PairView::new).toList())
                .healthConsiderations(
                        mineralList.get(1).getHealthConsiderations().stream().map(PairView::new).toList())
                .maleHigherBoundIntake(mineralList.get(1).getMaleHigherBoundIntake())
                .maleLowerBoundIntake(mineralList.get(1).getMaleLowerBoundIntake())
                .femaleHigherBoundIntake(mineralList.get(1).getFemaleHigherBoundIntake())
                .femaleLowerBoundIntake(mineralList.get(1).getFemaleLowerBoundIntake())
                .measure(mineralList.get(1).getMeasure())
                .build());

    }

//     @Test
//     public void getAllViewMins_ValidInput_ReturnsCorrectlyConvertedViews() {
//         when(mineralRepository.getAllMineralNames()).thenReturn(mineralViews);

//         List <MineralView> actual = mineralServiceImp.getAllViewMinerals();

//         assertEquals(mineralViews, actual);
//     }

    @Test
    public void getMineralViewByName_ValidInput_ReturnsCorrectlyConvertedView() throws MineralNotFoundException {
        when(mineralRepository.getMineralByName("Sodium"))
        .thenReturn(java.util.Optional.ofNullable(mineralList.get(0)));

        MineralView actual = mineralServiceImp.getMineralViewByName("Sodium");

        assertEquals(mineralViews.get(0), actual);
    }

    @Test
    public void getElectrolyteByName_InvalidInput_ThrowsElectrolyteNotFoundException(){
        when(mineralRepository.getMineralByName("Sodium"))
        .thenReturn(java.util.Optional.empty());

        assertThrows(MineralNotFoundException.class,
        () -> mineralServiceImp.getMineralViewByName("Sodium"));
    }

    @Test
    public void getAllElectrolytesNames_ValidInput_ReturnsCorrectlyConvertedNames() {
        when(mineralRepository.getAllMineralNames())
        .thenReturn(List.of("Sodium", "Potassium"));

        List <String> actual = mineralServiceImp.getAllMineralNames();

        assertEquals(List.of("Sodium", "Potassium"), actual);
    }
}
