package org.macronutrient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.macronutrient.model.entity.Macronutrient;
import org.macronutrient.model.entity.MacronutrientType;
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
    private final List<MacronutrientType> macronutrientTypeList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        macronutrientList.addAll(List.of(getMacro()));
        macronutrientTypeList.addAll(List.of(getMacroType()));
    }

    @Test 
    public void testGetAllMacros_CorrectlyReturnsMacros() {
        
        when(macronutrientRepository.getAllMacronutrient()).thenReturn(macronutrientList);
        
        List<Macronutrient> result = macronutrientServiceImp.getAllMacros();

        assertEquals(macronutrientList, result);
    }

    @Test
    public void testGetAllMacrosTypes_CorrectlyReturnsMacrosTypes() {
        when(macronutrientRepository.getAllMacronutrientTypes()).thenReturn(macronutrientTypeList);

        List<MacronutrientType> result = macronutrientServiceImp.getAllMacroTypes();

        assertEquals(macronutrientTypeList, result);
    }

    @Test
    public void testGetMacronutrientByName_ValidInput_CorrectlyReturnsMacros() throws MacronutrientNotFoundException {
        Macronutrient entity = macronutrientList.get(0);

        when(macronutrientRepository.getMacronutrientByName(entity.getName())).thenReturn(Optional.of(entity));

        Macronutrient result = macronutrientServiceImp.getMacroByName(entity.getName());

        assertEquals(macronutrientList.get(0), result);
    }

    @Test
    public void getMacronutrientByName_InvalidInput_ThrowsException() {
        String invalidName = "invalid";
        when(macronutrientRepository.getMacronutrientByName(invalidName)).thenReturn(Optional.empty());

        assertThrows(MacronutrientNotFoundException.class,
                () -> macronutrientServiceImp.getMacroByName(invalidName));
    }

    @Test
    public void getMacronutrientTypeByName_ValidInput_CorrectlyReturnsMacrosType()
            throws MacronutrientTypeNotFoundException {
        MacronutrientType entity = macronutrientTypeList.get(0);

        when(macronutrientRepository.getMacronutrientTypeByName(entity.getName())).thenReturn(Optional.of(entity));

        MacronutrientType result = macronutrientServiceImp.getMacronutrientTypeByName(entity.getName());

        assertEquals(macronutrientTypeList.get(0), result);
    }

    @Test
    public void getMacronutrientTypeByName_InvalidInput_ThrowsException() {
        String invalidName = "invalid";
        when(macronutrientRepository.getMacronutrientTypeByName(invalidName)).thenReturn(Optional.empty());

        assertThrows(MacronutrientTypeNotFoundException.class,
                () -> macronutrientServiceImp.getMacronutrientTypeByName(invalidName));
    }

    @Test
    public void getAllMacrosNames_ValidInput_CorrectlyTransformToViews() {

        List<String> names = macronutrientList.stream().map(Macronutrient::getName).toList();

        when(macronutrientRepository.getAllMacronutrientNames()).thenReturn(names);

        List<String> result = macronutrientServiceImp.getAllMacrosNames();

        assertEquals(names, result);
    }

    @Test
    public void getAllMacrosTypesNames_ValidInput_CorrectlyTransformToViews() {

        List<String> names = macronutrientTypeList.stream().map(MacronutrientType::getName).toList();

        when(macronutrientRepository.getAllMacronutrientTypesNames()).thenReturn(names);

        List<String> result = macronutrientServiceImp.getAllMacrosTypesNames();

        assertEquals(names, result);
    }

    private Macronutrient getMacro() {
        return Macronutrient.builder()
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
                .build();
    }

    private MacronutrientType getMacroType() {
        return MacronutrientType.builder()
                .name("Sugar")
                .description(
                        "Sugar is a type of carbohydrate that provides a quick source of energy for the body. However, excessive consumption of added sugars can contribute to health problems such as weight gain, tooth decay, and an increased risk of chronic diseases.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Energy Source")
                                        .value("Sugar is a simple carbohydrate that the body quickly converts into glucose, providing a rapid source of energy.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Natural Sources")
                                        .value("fruits, vegetables, and dairy products")
                                        .build(),
                                Pair.builder()
                                        .key("Added Sources")
                                        .value("sweets, sugary beverages, processed foods")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("Excessive Intake")
                                        .value("High consumption of added sugars is associated with various health risks, including obesity, type 2 diabetes, and cardiovascular disease.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("0"))
                .maleHigherBoundIntake(new BigDecimal("50"))
                .femaleLowerBoundIntake(new BigDecimal("0"))
                .femaleHigherBoundIntake(new BigDecimal("50"))
                .measure("grams")
                .build();
    }
}
