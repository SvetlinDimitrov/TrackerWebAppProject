package org.macronutrient;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.macronutrient.model.entity.Macronutrient;
import org.macronutrient.model.entity.MacronutrientType;
import org.macronutrient.model.entity.Pair;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class MacronutrientRepository {

    private final Map<String, Macronutrient> macronutrientMap = new LinkedHashMap<>();
    private final Map<String, MacronutrientType> macronutrientTypesMap = new LinkedHashMap<>();

    public List<Macronutrient> getAllMacronutrient() {
        return new LinkedList<>(macronutrientMap.values());
    }

    public List<MacronutrientType> getAllMacronutrientTypes() {
        return new LinkedList<>(macronutrientTypesMap.values());
    }

    public Optional<Macronutrient> getMacronutrientByName(String name) {
        return Optional.ofNullable(macronutrientMap.get(name));
    }

    public Optional<MacronutrientType> getMacronutrientTypeByName(String name) {
        return Optional.ofNullable(macronutrientTypesMap.get(name));
    }

    public List<String> getAllMacronutrientNames() {
        return new LinkedList<>(macronutrientMap.keySet());
    }

    public List<String> getAllMacronutrientTypesNames() {
        return new LinkedList<>(macronutrientTypesMap.keySet());
    }

    @PostConstruct
    public void initData() {
        Macronutrient carbohydrates = Macronutrient.builder()
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

        macronutrientMap.put(carbohydrates.getName(), carbohydrates);

        Macronutrient protein = Macronutrient.builder()
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
                .build();

        macronutrientMap.put(protein.getName(), protein);

        Macronutrient fat = Macronutrient.builder()
                .name("Fat")
                .description("")
                .types(
                        List.of(
                                Pair.builder()
                                        .key("Saturated Fats")
                                        .value("These fats are found in animal products like meat, poultry, and dairy, as well as in some plant oils like coconut and palm oil. Consuming high amounts of saturated fats can raise cholesterol levels and increase the risk of heart disease.")
                                        .build(),
                                Pair.builder()
                                        .key("Monounsaturated Fats")
                                        .value("These fats are found in olive oil, avocados, nuts, and seeds. They are considered heart-healthy and can help lower bad (LDL) cholesterol levels while maintaining or increasing good (HDL) cholesterol levels.")
                                        .build(),
                                Pair.builder()
                                        .key("Polyunsaturated Fats")
                                        .value("These fats include omega-3 and omega-6 fatty acids. Omega-3s are found in fatty fish (salmon, mackerel, sardines), flaxseeds, chia seeds, and walnuts. Omega-6s are present in vegetable oils (corn, soybean, sunflower) and nuts. These fats are important for overall health and must be obtained from the diet, as the body can't produce them.")
                                        .build(),
                                Pair.builder()
                                        .key("Trans Fats")
                                        .value("Trans fats are primarily found in partially hydrogenated oils, which are used in some processed foods to improve shelf life and texture. Trans fats are known to raise bad cholesterol levels and increase the risk of heart disease. Many countries have implemented regulations to limit their use in foods.")
                                        .build()))
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Energy Source")
                                        .value("Fats are a concentrated source of energy, providing more than twice the calories per gram compared to carbohydrates and protein. They serve as a stored energy reserve in the body.")
                                        .build(),
                                Pair.builder()
                                        .key("Nutrient Absorption")
                                        .value("Fats are necessary for the absorption of fat-soluble vitamins (A, D, E, K) and certain antioxidants. These vitamins are crucial for various bodily functions, including vision, bone health, and immune function.")
                                        .build(),
                                Pair.builder()
                                        .key("Cell Structure")
                                        .value("Fats are essential components of cell membranes, helping maintain their integrity and allowing cells to function properly.")
                                        .build(),
                                Pair.builder()
                                        .key("Insulation and Protection")
                                        .value("Fats serve as insulation to regulate body temperature and protect vital organs by acting as a cushion.")
                                        .build(),
                                Pair.builder()
                                        .key("Hormone Production")
                                        .value("Fats are involved in the production of hormones, including sex hormones and hormones related to stress and inflammation.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Plant-Based Sources")
                                        .value("Nuts,Seeds,Avocado,Oils")
                                        .build(),
                                Pair.builder()
                                        .key("Animal-Based Sources")
                                        .value("Fatty Fish,Meat and Poultry,Full-fat dairy products like cheese, yogurt, and milk contain saturated fats. Opt for reduced-fat or low-fat options to reduce saturated fat intake")
                                        .build(),
                                Pair.builder()
                                        .key("Processed and Packaged Foods")
                                        .value("Snack Foods,Baked Goods,Processed Meats")
                                        .build(),
                                Pair.builder()
                                        .key("Nut Butters")
                                        .value("Peanut Butter,Almond Butter, Cashew Butter")
                                        .build()))
                .dietaryConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("Healthy Fat Choices")
                                        .value("Focus on sources of healthy fats, such as nuts, seeds, avocados, olive oil, and fatty fish, while limiting sources of saturated and trans fats.")
                                        .build(),
                                Pair.builder()
                                        .key("Balancing Omega-3 and Omega-6")
                                        .value("Aim for a balance between omega-3 and omega-6 fatty acids. The modern Western diet tends to be higher in omega-6s, so including more sources of omega-3s is beneficial.")
                                        .build(),
                                Pair.builder()
                                        .key("Portion Control")
                                        .value("While fats are essential, they are calorie-dense. Be mindful of portion sizes to avoid excessive calorie intake.")
                                        .build(),
                                Pair.builder()
                                        .key("Cooking Oils")
                                        .value("Choose cooking oils with a high smoke point for high-temperature cooking (e.g., avocado oil, coconut oil), and use oils like olive oil for low-heat cooking or as dressings.")
                                        .build(),
                                Pair.builder()
                                        .key("Hydrogenated and Trans Fats")
                                        .value("Minimize or avoid foods containing partially hydrogenated oils or trans fats, as they are detrimental to heart health.")
                                        .build()))
                .activeState(0.20)
                .inactiveState(0.35)
                .build();

        macronutrientMap.put(fat.getName(), fat);
    }

    @PostConstruct
    public void initData2() {
        MacronutrientType sugar = MacronutrientType.builder()
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
        macronutrientTypesMap.put(sugar.getName(), sugar);

        MacronutrientType fiber = MacronutrientType.builder()
                .name("Fiber")
                .description(
                        "Dietary fiber is a type of carbohydrate that the body cannot digest. It plays a crucial role in maintaining digestive health, regulating blood sugar levels, and supporting heart health.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Digestive Health")
                                        .value("Fiber adds bulk to the stool, promoting regular bowel movements and preventing constipation.")
                                        .build(),
                                Pair.builder()
                                        .key("Blood Sugar Regulation")
                                        .value("Soluble fiber helps slow the absorption of sugar, contributing to stable blood sugar levels.")
                                        .build(),
                                Pair.builder()
                                        .key("Heart Health")
                                        .value("Fiber-rich diets are associated with a reduced risk of heart disease. It helps lower cholesterol levels and improve overall cardiovascular health.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Whole Grains")
                                        .value("brown rice, oats, quinoa")
                                        .build(),
                                Pair.builder()
                                        .key("Legumes")
                                        .value("beans, lentils")
                                        .build(),
                                Pair.builder()
                                        .key("Fruits and Vegetables")
                                        .value("apples, broccoli, carrots")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("Adequate Intake")
                                        .value("Aim for a daily intake of fiber to support digestive health and overall well-being.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("25"))
                .maleHigherBoundIntake(new BigDecimal("38"))
                .femaleLowerBoundIntake(new BigDecimal("25"))
                .femaleHigherBoundIntake(new BigDecimal("38"))
                .measure("grams")
                .build();
        macronutrientTypesMap.put(fiber.getName(), fiber);

        MacronutrientType saturatedFat = MacronutrientType.builder()
                .name("Saturated Fat")
                .description(
                        "Saturated fat is a type of fat that is solid at room temperature. High intake of saturated fat is associated with an increased risk of heart disease and elevated cholesterol levels.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Energy Storage")
                                        .value("Saturated fat serves as a concentrated source of energy and is stored in adipose tissue for future energy needs.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Animal Products")
                                        .value("meat, poultry, dairy products, eggs")
                                        .build(),
                                Pair.builder()
                                        .key("Plant Oils")
                                        .value("coconut oil, palm oil")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("Heart Health")
                                        .value("Limiting saturated fat intake is recommended to maintain heart health and reduce the risk of cardiovascular diseases.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("0"))
                .maleHigherBoundIntake(new BigDecimal("22"))
                .femaleLowerBoundIntake(new BigDecimal("0"))
                .femaleHigherBoundIntake(new BigDecimal("22"))
                .measure("grams")
                .build();
        macronutrientTypesMap.put(saturatedFat.getName(), saturatedFat);

        MacronutrientType polyunsaturatedFat = MacronutrientType.builder()
                .name("Polyunsaturated Fat")
                .description(
                        "Polyunsaturated fat is a type of fat that remains liquid at room temperature. It includes essential fatty acids, such as omega-3 and omega-6, which are important for various bodily functions.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Essential Fatty Acids")
                                        .value("Polyunsaturated fats contain essential fatty acids, including omega-3 and omega-6, which the body cannot produce and must be obtained through diet.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Fatty Fish")
                                        .value("salmon, mackerel, sardines")
                                        .build(),
                                Pair.builder()
                                        .key("Nuts and Seeds")
                                        .value("flaxseeds, walnuts, chia seeds")
                                        .build(),
                                Pair.builder()
                                        .key("Plant Oils")
                                        .value("soybean oil, sunflower oil")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("Heart Health")
                                        .value("Including polyunsaturated fats in the diet can have positive effects on heart health, including lowering cholesterol levels.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("0"))
                .maleHigherBoundIntake(new BigDecimal("27"))
                .femaleLowerBoundIntake(new BigDecimal("0"))
                .femaleHigherBoundIntake(new BigDecimal("27"))
                .measure("grams")
                .build();
        macronutrientTypesMap.put(polyunsaturatedFat.getName(), polyunsaturatedFat);

        MacronutrientType monounsaturatedFat = MacronutrientType.builder()
                .name("Monounsaturated Fat")
                .description(
                        "Monounsaturated fat is a type of fat that remains liquid at room temperature. It is considered a healthy fat and is associated with various health benefits, including heart health.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Heart Health")
                                        .value("Monounsaturated fats are known to have a positive impact on heart health by reducing bad cholesterol levels and improving overall cardiovascular health.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Olive Oil")
                                        .value("extra virgin olive oil")
                                        .build(),
                                Pair.builder()
                                        .key("Avocados")
                                        .value("avocado")
                                        .build(),
                                Pair.builder()
                                        .key("Nuts")
                                        .value("almonds, peanuts")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("Moderation")
                                        .value("While monounsaturated fats are considered healthy, moderation is still advised to maintain a balanced diet.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("0"))
                .maleHigherBoundIntake(new BigDecimal("33"))
                .femaleLowerBoundIntake(new BigDecimal("0"))
                .femaleHigherBoundIntake(new BigDecimal("33"))
                .measure("grams")
                .build();
        macronutrientTypesMap.put(monounsaturatedFat.getName(), monounsaturatedFat);

        MacronutrientType transFat = MacronutrientType.builder()
                .name("Trans Fat")
                .description(
                        "Trans fat is a type of unsaturated fat that has undergone hydrogenation, making it solid at room temperature. High intake of trans fat is associated with an increased risk of heart disease.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Food Processing")
                                        .value("Trans fat is often found in processed and commercially baked goods, contributing to their texture and shelf life.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Partially Hydrogenated Oils")
                                        .value("margarine, shortening")
                                        .build(),
                                Pair.builder()
                                        .key("Processed Foods")
                                        .value("cakes, cookies, fried foods")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("Health Risks")
                                        .value("High consumption of trans fat is linked to an increased risk of heart disease, elevated cholesterol levels, and other cardiovascular issues.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("0"))
                .maleHigherBoundIntake(new BigDecimal("2"))
                .femaleLowerBoundIntake(new BigDecimal("0"))
                .femaleHigherBoundIntake(new BigDecimal("2"))
                .measure("grams")
                .build();

        macronutrientTypesMap.put(transFat.getName(), transFat);

    }
}
