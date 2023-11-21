package org.mineral;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mineral.model.entity.Mineral;
import org.mineral.model.entity.Pair;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class MineralRepository {
    private final Map<String, Mineral> mineralMap = new LinkedHashMap<>();

    public List<Mineral> getAllMinerals() {
        return new LinkedList<>(mineralMap.values());
    }

    public Optional<Mineral> getMineralByName(String name) {
        return Optional.ofNullable(mineralMap.get(name));
    }

    public List<String> getAllMineralNames() {
        return new LinkedList<>(mineralMap.keySet());
    }

    @PostConstruct
    public void initData() {
        // Macrominerals
        Mineral calcium = Mineral.builder()
                .name("Calcium")
                .description(
                        "Calcium is a vital macromineral that plays a crucial role in the formation and maintenance of bones and teeth. It is also involved in various physiological functions, including muscle contraction, blood clotting, and nerve transmission.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Bone Health")
                                        .value("Calcium is a primary component of bone structure, providing strength and rigidity to the skeleton.")
                                        .build(),
                                Pair.builder()
                                        .key("Muscle Contraction")
                                        .value("Calcium is essential for muscle contraction, including the heart muscles.")
                                        .build(),
                                Pair.builder()
                                        .key("Blood Clotting")
                                        .value("Calcium is necessary for the blood clotting process, preventing excessive bleeding.")
                                        .build(),
                                Pair.builder()
                                        .key("Nerve Transmission")
                                        .value("Calcium ions play a role in transmitting nerve signals, facilitating communication between nerve cells.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Dairy Products")
                                        .value("milk, cheese, yogurt")
                                        .build(),
                                Pair.builder()
                                        .key("Leafy Greens")
                                        .value("kale, broccoli, bok choy")
                                        .build(),
                                Pair.builder()
                                        .key("Fish")
                                        .value("sardines, salmon")
                                        .build(),
                                Pair.builder()
                                        .key("Fortified Foods")
                                        .value("fortified plant-based milk, fortified cereals")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("Calcium absorption can be influenced by factors such as vitamin D levels, age, and certain medications.")
                                        .build()))
                .measure("milligrams (mg)")
                .maleLowerBoundIntake(new BigDecimal("1000"))
                .maleHigherBoundIntake(new BigDecimal("1300"))
                .femaleLowerBoundIntake(new BigDecimal("1000"))
                .femaleHigherBoundIntake(new BigDecimal("1300"))
                .build();

        mineralMap.put(calcium.getName(), calcium);

        Mineral phosphorus = Mineral.builder()
                .name("Phosphorus")
                .description(
                        "Phosphorus is a macromineral that works in conjunction with calcium to support bone and teeth health. It is involved in various physiological processes, including energy metabolism and DNA synthesis.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Bone and Teeth Health")
                                        .value("Phosphorus, along with calcium, contributes to the formation and maintenance of bone and teeth structure.")
                                        .build(),
                                Pair.builder()
                                        .key("Energy Metabolism")
                                        .value("Phosphorus is a key component of ATP, the primary energy currency of cells.")
                                        .build(),
                                Pair.builder()
                                        .key("DNA Synthesis")
                                        .value("Phosphorus is involved in the synthesis of DNA and RNA, essential for genetic processes.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Dairy Products")
                                        .value("milk, cheese, yogurt")
                                        .build(),
                                Pair.builder()
                                        .key("Meat")
                                        .value("beef, pork, poultry")
                                        .build(),
                                Pair.builder()
                                        .key("Seafood")
                                        .value("salmon, tuna")
                                        .build(),
                                Pair.builder()
                                        .key("Whole Grains")
                                        .value("whole wheat, oats, barley")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("Phosphorus levels are typically balanced by the kidneys. Individuals with kidney issues may require monitoring of phosphorus intake.")
                                        .build()))
                .measure("milligrams (mg)")
                .maleLowerBoundIntake(new BigDecimal("700"))
                .maleHigherBoundIntake(new BigDecimal("1250"))
                .femaleLowerBoundIntake(new BigDecimal("700"))
                .femaleHigherBoundIntake(new BigDecimal("1250"))
                .build();

        mineralMap.put(phosphorus.getName(), phosphorus);

        Mineral magnesium = Mineral.builder()
                .name("Magnesium")
                .description(
                        "Magnesium is a macromineral that plays a crucial role in various physiological functions, including muscle and nerve function, blood glucose control, and bone health.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Muscle and Nerve Function")
                                        .value("Magnesium is essential for muscle contraction and nerve transmission, contributing to proper muscle function and sensation.")
                                        .build(),
                                Pair.builder()
                                        .key("Blood Glucose Control")
                                        .value("Magnesium helps regulate blood glucose levels and is involved in insulin action.")
                                        .build(),
                                Pair.builder()
                                        .key("Bone Health")
                                        .value("Magnesium is a component of bone structure and contributes to bone health.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Nuts and Seeds")
                                        .value("almonds, cashews, pumpkin seeds")
                                        .build(),
                                Pair.builder()
                                        .key("Leafy Greens")
                                        .value("spinach, kale, Swiss chard")
                                        .build(),
                                Pair.builder()
                                        .key("Whole Grains")
                                        .value("brown rice, quinoa, whole wheat")
                                        .build(),
                                Pair.builder()
                                        .key("Legumes")
                                        .value("black beans, lentils, chickpeas")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("Maintaining an adequate level of magnesium is important for overall health. Magnesium deficiency can lead to muscle cramps, fatigue, and other health issues.")
                                        .build()))
                .measure("milligrams (mg)")
                .maleLowerBoundIntake(new BigDecimal("400"))
                .maleHigherBoundIntake(new BigDecimal("420"))
                .femaleLowerBoundIntake(new BigDecimal("310"))
                .femaleHigherBoundIntake(new BigDecimal("320"))
                .build();

        mineralMap.put(magnesium.getName(), magnesium);

        Mineral sodium = Mineral.builder()
                .name("Sodium")
                .description(
                        "Sodium is a macromineral that plays a critical role in maintaining various bodily functions. It is essential for fluid balance, nerve transmission, muscle function, and blood pressure regulation.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Fluid Balance")
                                        .value("Sodium is crucial for maintaining fluid balance within cells and their surroundings. It helps regulate osmotic pressure, determining the movement of water in and out of cells.")
                                        .build(),
                                Pair.builder()
                                        .key("Nerve Transmission")
                                        .value("Sodium is essential for generating electrical signals that enable nerve cells to communicate with each other and with muscles. This is vital for proper muscle function and sensation.")
                                        .build(),
                                Pair.builder()
                                        .key("Muscle Contraction")
                                        .value("Sodium plays a central role in muscle contraction. Adequate levels of sodium are necessary for muscles, including the heart, to contract and relax properly.")
                                        .build(),
                                Pair.builder()
                                        .key("Blood Pressure Regulation")
                                        .value("Sodium intake is closely linked to blood pressure regulation. It helps counteract the effects of potassium by promoting the excretion of potassium through urine, which can help regulate blood pressure.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Table Salt")
                                        .value("common salt used in cooking")
                                        .build(),
                                Pair.builder()
                                        .key("Processed Foods")
                                        .value("canned soups, snacks, processed meats")
                                        .build(),
                                Pair.builder()
                                        .key("Dairy Products")
                                        .value("milk, cheese")
                                        .build(),
                                Pair.builder()
                                        .key("Vegetables")
                                        .value("celery, beets, carrots")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("While sodium is essential for bodily functions, excessive intake can contribute to high blood pressure and other health issues. It's important to balance sodium intake, especially for individuals with hypertension.")
                                        .build()))
                .measure("milligrams (mg)")
                .maleLowerBoundIntake(new BigDecimal("1500"))
                .maleHigherBoundIntake(new BigDecimal("2300"))
                .femaleLowerBoundIntake(new BigDecimal("1500"))
                .femaleHigherBoundIntake(new BigDecimal("2300"))
                .build();

        mineralMap.put(sodium.getName(), sodium);

        Mineral potassium = Mineral.builder()
                .name("Potassium")
                .description(
                        "Potassium is an essential mineral and electrolyte that plays a critical role in maintaining various bodily functions. It works in tandem with sodium to help regulate fluid balance, nerve transmission, muscle function, and blood pressure. Potassium is abundant in many foods, and maintaining a proper balance of potassium in the body is crucial for overall health.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Fluid Balance")
                                        .value("Potassium is a key player in maintaining fluid balance within cells and their surroundings. It helps regulate osmotic pressure, which determines the movement of water in and out of cells.")
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
                                        .key("Dairy Products")
                                        .value("milk, yogurt")
                                        .build(),
                                Pair.builder()
                                        .key("Nuts and Seeds")
                                        .value("almonds, pistachios")
                                        .build(),
                                Pair.builder()
                                        .key("Whole Grains")
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
                .maleLowerBoundIntake(new BigDecimal("2000"))
                .maleHigherBoundIntake(new BigDecimal("3400"))
                .femaleLowerBoundIntake(new BigDecimal("2000"))
                .femaleHigherBoundIntake(new BigDecimal("3400"))
                .measure("milligrams (mg)")
                .build();

        mineralMap.put(potassium.getName(), potassium);

        Mineral chloride = Mineral.builder()
                .name("Chloride")
                .description(
                        "Chloride is a macromineral that, along with sodium, plays a crucial role in maintaining fluid balance, osmotic pressure, and the acidity of bodily fluids. It is essential for various physiological functions.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Fluid Balance")
                                        .value("Chloride, in the form of chloride ions, is vital for maintaining proper fluid balance in and around cells.")
                                        .build(),
                                Pair.builder()
                                        .key("Osmotic Pressure")
                                        .value("Chloride helps regulate osmotic pressure, influencing the movement of water across cell membranes.")
                                        .build(),
                                Pair.builder()
                                        .key("Acid-Base Balance")
                                        .value("Chloride contributes to the body's acid-base balance, helping maintain proper pH levels in bodily fluids.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Table Salt")
                                        .value("common salt used in cooking")
                                        .build(),
                                Pair.builder()
                                        .key("Processed Foods")
                                        .value("canned soups, pickles, processed meats")
                                        .build(),
                                Pair.builder()
                                        .key("Seaweed")
                                        .value("nori, kelp")
                                        .build(),
                                Pair.builder()
                                        .key("Dairy Products")
                                        .value("milk, cheese")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("Chloride is typically obtained through dietary sources, and deficiencies are rare. Excessive intake, primarily from salt, may contribute to health issues such as hypertension in sensitive individuals.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("2300"))
                .maleHigherBoundIntake(new BigDecimal("3600"))
                .femaleLowerBoundIntake(new BigDecimal("2300"))
                .femaleHigherBoundIntake(new BigDecimal("3600"))
                .measure("milligrams (mg)")
                .build();

        mineralMap.put(chloride.getName(), chloride);

        // MICROMINERALS

        Mineral iron = Mineral.builder()
                .name("Iron")
                .description(
                        "Iron is a micromineral that plays a crucial role in the formation of hemoglobin, the protein responsible for carrying oxygen in red blood cells. It is essential for oxygen transport and various metabolic processes.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Oxygen Transport")
                                        .value("Iron is a key component of hemoglobin, allowing red blood cells to transport oxygen from the lungs to the rest of the body.")
                                        .build(),
                                Pair.builder()
                                        .key("Energy Metabolism")
                                        .value("Iron is involved in the production of ATP, the energy currency of cells.")
                                        .build(),
                                Pair.builder()
                                        .key("Immune Function")
                                        .value("Iron supports the function of immune cells, helping the body defend against infections.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Red Meat")
                                        .value("beef, lamb, pork")
                                        .build(),
                                Pair.builder()
                                        .key("Poultry")
                                        .value("chicken, turkey")
                                        .build(),
                                Pair.builder()
                                        .key("Fish")
                                        .value("salmon, tuna")
                                        .build(),
                                Pair.builder()
                                        .key("Legumes")
                                        .value("beans, lentils")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("Iron deficiency can lead to anemia, characterized by fatigue, weakness, and reduced cognitive function. Excessive iron intake can be harmful; therefore, iron levels should be monitored.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("8"))
                .maleHigherBoundIntake(new BigDecimal("11"))
                .femaleLowerBoundIntake(new BigDecimal("8"))
                .femaleHigherBoundIntake(new BigDecimal("11"))
                .measure("milligrams (mg)")
                .build();

        mineralMap.put(iron.getName(), iron);

        Mineral zinc = Mineral.builder()
                .name("Zinc")
                .description(
                        "Zinc is a micromineral that plays a crucial role in various physiological processes, including immune function, wound healing, and DNA synthesis. It is essential for the proper functioning of numerous enzymes.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Immune Function")
                                        .value("Zinc is essential for the development and function of immune cells, helping the body fight infections.")
                                        .build(),
                                Pair.builder()
                                        .key("Wound Healing")
                                        .value("Zinc is involved in the synthesis of collagen, a protein crucial for wound healing and tissue repair.")
                                        .build(),
                                Pair.builder()
                                        .key("Enzyme Function")
                                        .value("Zinc acts as a cofactor for many enzymes, participating in various metabolic processes.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Meat")
                                        .value("beef, lamb, pork")
                                        .build(),
                                Pair.builder()
                                        .key("Shellfish")
                                        .value("oysters, crab, lobster")
                                        .build(),
                                Pair.builder()
                                        .key("Nuts and Seeds")
                                        .value("pumpkin seeds, cashews")
                                        .build(),
                                Pair.builder()
                                        .key("Legumes")
                                        .value("beans, lentils")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("While zinc is essential for health, excessive intake can lead to adverse effects. Adequate intake is important, especially for individuals at risk of zinc deficiency.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("11"))
                .maleHigherBoundIntake(new BigDecimal("11"))
                .femaleLowerBoundIntake(new BigDecimal("8"))
                .femaleHigherBoundIntake(new BigDecimal("8"))
                .measure("milligrams (mg)")
                .build();

        mineralMap.put(zinc.getName(), zinc);

        Mineral copper = Mineral.builder()
                .name("Copper")
                .description(
                        "Copper is a micromineral that plays a key role in various physiological processes, including the formation of red blood cells, maintenance of connective tissues, and the function of enzymes.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Red Blood Cell Formation")
                                        .value("Copper is essential for the production of hemoglobin and the formation of red blood cells.")
                                        .build(),
                                Pair.builder()
                                        .key("Connective Tissue Maintenance")
                                        .value("Copper is involved in the synthesis of collagen, a protein important for the maintenance of connective tissues, skin, and bones.")
                                        .build(),
                                Pair.builder()
                                        .key("Enzyme Function")
                                        .value("Copper acts as a cofactor for various enzymes, playing a role in numerous metabolic processes.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Shellfish")
                                        .value("oysters, crabs, lobster")
                                        .build(),
                                Pair.builder()
                                        .key("Nuts and Seeds")
                                        .value("cashews, sunflower seeds")
                                        .build(),
                                Pair.builder()
                                        .key("Organ Meats")
                                        .value("liver, kidney")
                                        .build(),
                                Pair.builder()
                                        .key("Legumes")
                                        .value("lentils, chickpeas")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("Copper is important for health, but excessive intake can lead to copper toxicity. Adequate intake is crucial, and it's often obtained through a balanced diet.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("0.9"))
                .maleHigherBoundIntake(new BigDecimal("2.3"))
                .femaleLowerBoundIntake(new BigDecimal("0.9"))
                .femaleHigherBoundIntake(new BigDecimal("2.3"))
                .measure("milligrams (mg)")
                .build();

        mineralMap.put(copper.getName(), copper);

        Mineral manganese = Mineral.builder()
                .name("Manganese")
                .description(
                        "Manganese is a micromineral that serves as a cofactor for various enzymes involved in bone formation, energy metabolism, and antioxidant defense.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Bone Formation")
                                        .value("Manganese plays a role in the formation and maintenance of bone structure, working with other minerals like calcium and phosphorus.")
                                        .build(),
                                Pair.builder()
                                        .key("Energy Metabolism")
                                        .value("Manganese is involved in the metabolism of carbohydrates, amino acids, and cholesterol, contributing to overall energy production.")
                                        .build(),
                                Pair.builder()
                                        .key("Antioxidant Defense")
                                        .value("Manganese participates in antioxidant processes, helping protect cells from oxidative stress and damage.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Nuts and Seeds")
                                        .value("almonds, pecans, sunflower seeds")
                                        .build(),
                                Pair.builder()
                                        .key("Whole Grains")
                                        .value("brown rice, oats, whole wheat")
                                        .build(),
                                Pair.builder()
                                        .key("Leafy Greens")
                                        .value("spinach, kale, collard greens")
                                        .build(),
                                Pair.builder()
                                        .key("Tea")
                                        .value("green tea, black tea")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("While manganese is essential for health, excessive intake from supplements can lead to toxicity. Adequate intake is usually achieved through a balanced diet.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("2.3"))
                .maleHigherBoundIntake(new BigDecimal("11"))
                .femaleLowerBoundIntake(new BigDecimal("1.8"))
                .femaleHigherBoundIntake(new BigDecimal("11"))
                .measure("milligrams (mg)")
                .build();

        mineralMap.put(manganese.getName(), manganese);

        Mineral iodine = Mineral.builder()
                .name("Iodine")
                .description(
                        "Iodine is a micromineral that is crucial for the synthesis of thyroid hormones, which regulate metabolism, growth, and development. Iodine deficiency can lead to thyroid-related disorders.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Thyroid Hormone Synthesis")
                                        .value("Iodine is a key component in the synthesis of thyroid hormones (thyroxine and triiodothyronine), which play a crucial role in regulating metabolism, growth, and development.")
                                        .build(),
                                Pair.builder()
                                        .key("Brain Development")
                                        .value("Adequate iodine is essential for proper brain development, particularly during pregnancy and infancy.")
                                        .build(),
                                Pair.builder()
                                        .key("Metabolic Regulation")
                                        .value("Thyroid hormones, influenced by iodine, help regulate the body's metabolic processes.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Seafood")
                                        .value("seaweed, fish, shrimp")
                                        .build(),
                                Pair.builder()
                                        .key("Dairy Products")
                                        .value("milk, cheese, yogurt")
                                        .build(),
                                Pair.builder()
                                        .key("Iodized Salt")
                                        .value("table salt fortified with iodine")
                                        .build(),
                                Pair.builder()
                                        .key("Eggs")
                                        .value("especially from chickens raised in areas with iodine-rich soil")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("Iodine deficiency can lead to thyroid-related disorders, including goiter and hypothyroidism. However, excessive iodine intake can also have adverse effects, and it's important to maintain a balanced iodine level.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("150"))
                .maleHigherBoundIntake(new BigDecimal("290"))
                .femaleLowerBoundIntake(new BigDecimal("150"))
                .femaleHigherBoundIntake(new BigDecimal("290"))
                .measure("micrograms (mcg)")
                .build();

        mineralMap.put(iodine.getName(), iodine);

        Mineral selenium = Mineral.builder()
                .name("Selenium")
                .description(
                        "Selenium is a micromineral that serves as a cofactor for antioxidant enzymes, helping protect cells from oxidative damage. It also plays a role in thyroid hormone metabolism.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Antioxidant Defense")
                                        .value("Selenium is a crucial component of antioxidant enzymes, such as glutathione peroxidase, which help neutralize harmful free radicals.")
                                        .build(),
                                Pair.builder()
                                        .key("Thyroid Hormone Metabolism")
                                        .value("Selenium is involved in the conversion of thyroid hormones, supporting proper thyroid function.")
                                        .build(),
                                Pair.builder()
                                        .key("Immune System Support")
                                        .value("Selenium plays a role in the proper functioning of the immune system.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Brazil Nuts")
                                        .value("a particularly rich source of selenium")
                                        .build(),
                                Pair.builder()
                                        .key("Seafood")
                                        .value("fish, shrimp, crab")
                                        .build(),
                                Pair.builder()
                                        .key("Poultry")
                                        .value("chicken, turkey")
                                        .build(),
                                Pair.builder()
                                        .key("Whole Grains")
                                        .value("brown rice, oats, barley")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("While selenium is essential for health, excessive intake can lead to toxicity. Adequate intake is generally achieved through a balanced diet, and selenium supplements should be used with caution.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("55"))
                .maleHigherBoundIntake(new BigDecimal("400"))
                .femaleLowerBoundIntake(new BigDecimal("55"))
                .femaleHigherBoundIntake(new BigDecimal("400"))
                .measure("micrograms (mcg)")
                .build();

        mineralMap.put(selenium.getName(), selenium);

        Mineral fluoride = Mineral.builder()
                .name("Fluoride")
                .description(
                        "Fluoride is a micromineral that plays a crucial role in dental health, particularly in the prevention of tooth decay. It strengthens tooth enamel and helps protect against cavities.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Dental Health")
                                        .value("Fluoride helps prevent tooth decay by strengthening tooth enamel. It inhibits the demineralization of enamel and promotes the remineralization process.")
                                        .build(),
                                Pair.builder()
                                        .key("Bone Health")
                                        .value("Fluoride is incorporated into bone structure, contributing to bone health.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Fluoridated Water")
                                        .value("drinking water treated with added fluoride")
                                        .build(),
                                Pair.builder()
                                        .key("Tea")
                                        .value("especially when prepared with fluoridated water")
                                        .build(),
                                Pair.builder()
                                        .key("Seafood")
                                        .value("fish, crab")
                                        .build(),
                                Pair.builder()
                                        .key("Toothpaste")
                                        .value("fluoride-containing toothpaste for oral hygiene")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("While fluoride is important for dental health, excessive intake can lead to dental fluorosis and other health issues. The optimal level of fluoride intake depends on factors such as age, dental health, and water fluoride levels.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("3"))
                .maleHigherBoundIntake(new BigDecimal("4"))
                .femaleLowerBoundIntake(new BigDecimal("3"))
                .femaleHigherBoundIntake(new BigDecimal("4"))
                .measure("milligrams (mg)")
                .build();

        mineralMap.put(fluoride.getName(), fluoride);

        Mineral chromium = Mineral.builder()
                .name("Chromium")
                .description(
                        "Chromium is a micromineral that plays a role in insulin action and glucose metabolism. It is involved in maintaining normal blood sugar levels.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Insulin Sensitivity")
                                        .value("Chromium enhances the action of insulin, a hormone that regulates blood sugar levels by facilitating the uptake of glucose into cells.")
                                        .build(),
                                Pair.builder()
                                        .key("Glucose Metabolism")
                                        .value("Chromium is involved in the metabolism of carbohydrates, contributing to the regulation of blood glucose.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Broccoli")
                                        .value("a good source of chromium")
                                        .build(),
                                Pair.builder()
                                        .key("Whole Grains")
                                        .value("whole wheat, oats, barley")
                                        .build(),
                                Pair.builder()
                                        .key("Lean Meats")
                                        .value("chicken, turkey")
                                        .build(),
                                Pair.builder()
                                        .key("Nuts and Seeds")
                                        .value("especially broccoli, sunflower seeds")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("While chromium is essential for health, its role in improving insulin sensitivity is still being studied. Adequate intake is generally achieved through a balanced diet, and supplementation is usually not necessary for most individuals.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("30"))
                .maleHigherBoundIntake(new BigDecimal("35"))
                .femaleLowerBoundIntake(new BigDecimal("20"))
                .femaleHigherBoundIntake(new BigDecimal("25"))
                .measure("micrograms (mcg)")
                .build();

        mineralMap.put(chromium.getName(), chromium);

        Mineral molybdenum = Mineral.builder()
                .name("Molybdenum")
                .description(
                        "Molybdenum is a micromineral that serves as a cofactor for enzymes involved in the metabolism of amino acids, purines, and sulfur-containing compounds.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Amino Acid Metabolism")
                                        .value("Molybdenum is essential for the metabolism of certain amino acids, including the conversion of sulfite to sulfate.")
                                        .build(),
                                Pair.builder()
                                        .key("Purine Metabolism")
                                        .value("Molybdenum is involved in the breakdown of purines, compounds that are part of DNA and RNA.")
                                        .build(),
                                Pair.builder()
                                        .key("Sulfur Metabolism")
                                        .value("Molybdenum plays a role in the metabolism of sulfur-containing compounds.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Legumes")
                                        .value("lentils, peas, beans")
                                        .build(),
                                Pair.builder()
                                        .key("Grains")
                                        .value("particularly in whole grains")
                                        .build(),
                                Pair.builder()
                                        .key("Nuts")
                                        .value("almonds, peanuts")
                                        .build(),
                                Pair.builder()
                                        .key("Dairy Products")
                                        .value("milk, cheese")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("Molybdenum is generally obtained through a balanced diet, and deficiencies are rare. Excessive intake from supplements is uncommon and may lead to adverse effects.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("45"))
                .maleHigherBoundIntake(new BigDecimal("2000"))
                .femaleLowerBoundIntake(new BigDecimal("45"))
                .femaleHigherBoundIntake(new BigDecimal("2000"))
                .measure("micrograms (mcg)")
                .build();

        mineralMap.put(molybdenum.getName(), molybdenum);

    }
}
