package org.macronutrient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.macronutrient.model.dtos.MacronutrientView;
import org.macronutrient.model.dtos.PairView;
import org.macronutrient.model.entity.Macronutrient;
import org.macronutrient.model.entity.Pair;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MacronutrientServiceImpTest {

    @Mock
    private MacronutrientRepository macronutrientRepository;

    @InjectMocks
    private MacronutrientServiceImp macronutrientServiceImp;

    private final List<Macronutrient> macronutrientList = new ArrayList<>();
    private final List<MacronutrientView> macronutrientListViews = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        macronutrientList.addAll(List.of(
                Macronutrient.builder()
                        .name("Carbohydrates")
                        .description(
                                "The amount of carbohydrates you should consume depends on various factors, including your age, gender, activity level, health goals, and individual metabolic needs. Carbohydrate needs can vary widely from person to person.")
                        .types(
                                List.of(
                                        Pair.builder()
                                                .key("Simple Carbohydrates")
                                                .value("These are made up of one or two sugar molecules. Simple carbs are found in foods like fruits (fructose), milk (lactose), and table sugar (sucrose). They are quickly digested and can lead to rapid spikes in blood sugar levels.")
                                                .build(),
                                        Pair.builder()
                                                .key("Complex Carbohydrates")
                                                .value("These consist of longer chains of sugar molecules. They are found in foods like whole grains (e.g., brown rice, quinoa, oats), legumes (beans, lentils), and vegetables. Complex carbs are broken down more slowly, providing sustained energy and helping to stabilize blood sugar levels.")
                                                .build()))
                        .functions(
                                List.of(
                                        Pair.builder()
                                                .key("Energy Source")
                                                .value("Carbohydrates are the body's primary and most efficient source of energy. When you consume carbs, they are broken down into glucose, which is used by cells for fuel.")
                                                .build(),
                                        Pair.builder()
                                                .key("Brain Function")
                                                .value("The brain relies heavily on glucose for energy. Consuming adequate carbohydrates helps support cognitive function, memory, and mood.")
                                                .build(),
                                        Pair.builder()
                                                .key("Muscle Glycogen")
                                                .value("Carbohydrates are stored in the muscles and liver as glycogen. During exercise, muscle glycogen serves as a readily available source of energy.")
                                                .build(),
                                        Pair.builder()
                                                .key("Preventing Protein Breakdown")
                                                .value("Adequate carbohydrate intake spares protein from being used as an energy source. This is important for preserving muscle mass.")
                                                .build()))
                        .sources(
                                List.of(
                                        Pair.builder()
                                                .key("Grains")
                                                .value("Whole grains like brown rice, quinoa, whole wheat, and oats provide complex carbohydrates, fiber, and various nutrients.")
                                                .build(),
                                        Pair.builder()
                                                .key("Fruits")
                                                .value("Fruits contain natural sugars along with vitamins, minerals, and fiber. Berries, apples, citrus fruits, and bananas are popular choices.")
                                                .build(),
                                        Pair.builder()
                                                .key("Vegetables")
                                                .value("Starchy vegetables (potatoes, corn) and non-starchy vegetables (leafy greens, broccoli, peppers) are rich in carbs, fiber, vitamins, and minerals.")
                                                .build(),
                                        Pair.builder()
                                                .key("Legumes")
                                                .value("Beans, lentils, chickpeas, and other legumes offer both carbohydrates and protein.")
                                                .build(),
                                        Pair.builder()
                                                .key("Dairy")
                                                .value("Dairy products contain lactose, a natural sugar. Choose low-fat or non-fat options for reduced saturated fat.")
                                                .build()))
                        .dietaryConsiderations(
                                List.of(
                                        Pair.builder()
                                                .key("Fiber")
                                                .value("Many carbohydrate-rich foods are also high in dietary fiber, which aids digestion, supports a healthy gut, and helps control appetite.")
                                                .build(),
                                        Pair.builder()
                                                .key("Glycemic Index (GI)")
                                                .value("The GI measures how quickly a carbohydrate-containing food raises blood sugar levels. Foods with a low GI (complex carbs) lead to gradual increases in blood sugar, while high-GI foods (simple carbs) cause rapid spikes.")
                                                .build(),
                                        Pair.builder()
                                                .key("Added Sugars")
                                                .value("Be mindful of added sugars in processed foods and sugary beverages. Consuming too much added sugar can lead to health issues.")
                                                .build()))
                        .activeState(0.50)
                        .inactiveState(0.50)
                        .build(),

                Macronutrient.builder()
                        .name("Protein")
                        .description(
                                "Protein is one of the three macronutrients essential for human health, alongside carbohydrates and fats. Proteins are made up of amino acids, which are the building blocks of the body. These amino acids play crucial roles in various bodily functions, including muscle repair, immune function, enzyme production, and hormone regulation.")
                        .types(List.of())
                        .functions(
                                List.of(
                                        Pair.builder()
                                                .key("Muscle Health")
                                                .value("Protein is vital for building, repairing, and maintaining muscles. It's especially important for individuals engaged in physical activity, as exercise can cause muscle tissue breakdown that requires repair.")
                                                .build(),
                                        Pair.builder()
                                                .key("Cell Structure")
                                                .value("Proteins are the structural components of cells, tissues, and organs. They help maintain the integrity and functionality of these structures.")
                                                .build(),
                                        Pair.builder()
                                                .key("Enzymes")
                                                .value("Many enzymes are proteins that catalyze biochemical reactions in the body, allowing essential processes like digestion and metabolism to occur efficiently.")
                                                .build(),
                                        Pair.builder()
                                                .key("Hormones")
                                                .value("Some hormones, such as insulin and growth hormone, are proteins that regulate various physiological processes.")
                                                .build(),
                                        Pair.builder()
                                                .key("Immune Function")
                                                .value("Antibodies, which are proteins, play a critical role in the immune system by identifying and neutralizing foreign invaders like bacteria and viruses.")
                                                .build(),
                                        Pair.builder()
                                                .key("Transport")
                                                .value("Some proteins act as carriers, transporting nutrients, oxygen, and other molecules throughout the body.")
                                                .build()))
                        .sources(
                                List.of(
                                        Pair.builder()
                                                .key("Animal Sources")
                                                .value("Meat, poultry, fish, eggs, dairy products, and seafood are rich in high-quality protein. These sources provide all essential amino acids in appropriate proportions.")
                                                .build(),
                                        Pair.builder()
                                                .key("Plant Sources")
                                                .value("Legumes (beans, lentils, peas), nuts, seeds, tofu, tempeh, and whole grains are good sources of plant-based protein. Combining different plant protein sources can help ensure a complete range of amino acids.")
                                                .build()))
                        .dietaryConsiderations(
                                List.of(
                                        Pair.builder()
                                                .key("Quality of Protein Sources")
                                                .value("Choose a variety of protein sources to ensure a balance of essential amino acids. Animal-based sources (meat, poultry, fish, eggs, dairy) are often complete proteins, while plant-based sources (legumes, nuts, seeds, grains) may require combining different sources to ensure a complete amino acid profile.")
                                                .build(),
                                        Pair.builder()
                                                .key("Protein Intake")
                                                .value("Recommendations vary based on factors such as age, gender, activity level, and goals. For general health, a common guideline is about 0.8 grams of protein per kilogram of body weight per day. Athletes and those engaged in strength training may require more (1.2 to 2.0 grams per kilogram).")
                                                .build(),
                                        Pair.builder()
                                                .key("Protein and Weight Management")
                                                .value("Protein-rich foods can help control appetite and contribute to feelings of fullness. Including protein in meals and snacks can support weight management goals.")
                                                .build(),
                                        Pair.builder()
                                                .key("Individual Needs and Goals")
                                                .value("Dietary protein needs vary widely based on factors such as age, gender, activity level, metabolism, and health status. Consider working with a registered dietitian to determine your personalized protein requirements.")
                                                .build(),
                                        Pair.builder()
                                                .key("Limit Processed Meats")
                                                .value("Processed meats (such as sausages, bacon, and deli meats) are often high in salt, saturated fats, and additives. Limiting their consumption supports overall health.")
                                                .build(),
                                        Pair.builder()
                                                .key("Consider the Source")
                                                .value("When consuming animal-based proteins, opt for lean cuts of meat, poultry without the skin, and low-fat dairy to reduce saturated fat intake")
                                                .build(),
                                        Pair.builder()
                                                .key("Protein Powder Supplements")
                                                .value("Protein supplements can be convenient for individuals with increased protein needs, such as athletes or those with dietary restrictions. However, whole food sources are generally preferred due to their additional nutritional benefits.")
                                                .build()))
                        .activeState(0.25)
                        .inactiveState(0.15)
                        .build()));

        Macronutrient entity = macronutrientList.get(0);
        macronutrientListViews.add(
                MacronutrientView.builder()
                        .name(entity.getName())
                        .description(entity.getDescription())
                        .activeState(entity.getActiveState())
                        .inactiveState(entity.getInactiveState())
                        .functions(entity.getFunctions().stream().map(PairView::new).toList())
                        .sources(entity.getSources().stream().map(PairView::new).toList())
                        .types(entity.getTypes().stream().map(PairView::new).toList())
                        .dietaryConsiderations(
                                entity.getDietaryConsiderations().stream()
                                        .map(PairView::new).toList())
                        .build());
        Macronutrient entity2 = macronutrientList.get(1);
        macronutrientListViews.add(
                MacronutrientView.builder()
                        .name(entity2.getName())
                        .description(entity2.getDescription())
                        .activeState(entity2.getActiveState())
                        .inactiveState(entity2.getInactiveState())
                        .functions(entity2.getFunctions().stream().map(PairView::new).toList())
                        .sources(entity2.getSources().stream().map(PairView::new).toList())
                        .types(entity2.getTypes().stream().map(PairView::new).toList())
                        .dietaryConsiderations(
                                entity2.getDietaryConsiderations().stream()
                                        .map(PairView::new).toList())
                        .build());
    }

    @Test 
    public void getAllMacrosView_ValidInput_CorrectlyTransformToViews() {
        
        when(macronutrientRepository.getAllMacronutrient()).thenReturn(macronutrientList);
        
        List<MacronutrientView> result = macronutrientServiceImp.getAllMacrosView();

        assertEquals(macronutrientListViews, result);
    }

    @Test
    public void getMacronutrientByName_ValidInput_CorrectlyTransformToView() throws MacronutrientNotFoundException {
        Macronutrient entity = macronutrientList.get(0);
        MacronutrientView view = macronutrientListViews.get(0);

        when(macronutrientRepository.getMacronutrientByName(entity.getName())).thenReturn(Optional.of(entity));

        MacronutrientView result = macronutrientServiceImp.getMacroViewByName(entity.getName());

        assertEquals(view, result);
    }

    @Test
    public void getMacronutrientByName_InvalidInput_ThrowsException() {
        String invalidName = "invalid";
        when(macronutrientRepository.getMacronutrientByName(invalidName)).thenReturn(Optional.empty());

        assertThrows(MacronutrientNotFoundException.class,
                () -> macronutrientServiceImp.getMacroViewByName(invalidName));
    }

    @Test
    public void getAllMacrosNames_ValidInput_CorrectlyTransformToViews() {
        List<String> names = new ArrayList<>();
        names.add("Carbohydrates");
        names.add("Protein");

        when(macronutrientRepository.getAllMacronutrientNames()).thenReturn(names);

        List<String> result = macronutrientServiceImp.getAllMacrosNames();

        assertEquals(names, result);
    }
}
