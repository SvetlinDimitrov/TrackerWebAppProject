package org.electrolyte;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.electrolyte.model.entity.Electrolyte;
import org.electrolyte.model.entity.Pair;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class ElectrolyteRepository {
    private final Map<String, Electrolyte> electrolyteEntityMap = new LinkedHashMap<>();

    public List<Electrolyte> getAllElectrolytes() {
        return new LinkedList<>(electrolyteEntityMap.values());
    }

    public Optional<Electrolyte> getElectrolyteByName(String name) {
        return Optional.ofNullable(electrolyteEntityMap.get(name));
    }

    public List<String> getAllElectrolytesNames() {
        return new LinkedList<>(electrolyteEntityMap.keySet());
    }

    @PostConstruct
    public void initData() {
        Electrolyte sodium = Electrolyte.builder()
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
                .build();

        electrolyteEntityMap.put(sodium.getName(), sodium);

        Electrolyte potassium = Electrolyte.builder()
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
                .build();

        electrolyteEntityMap.put(potassium.getName(), potassium);

        Electrolyte calcium = Electrolyte.builder()
                .name("Calcium")
                .description(
                        "Calcium is a vital mineral that plays a fundamental role in maintaining strong bones and teeth, supporting muscle and nerve function, and facilitating various cellular processes throughout the body. It's one of the most abundant minerals in the human body and is essential for overall health.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Bone and Teeth Health")
                                        .value("Calcium is a primary building block for bones and teeth. It provides strength and rigidity to the skeletal system and helps prevent conditions like osteoporosis, which is characterized by weakened bones.")
                                        .build(),
                                Pair.builder()
                                        .key("Muscle Contraction")
                                        .value("Calcium is crucial for muscle contraction, including both skeletal muscles and the heart. It helps regulate the contraction and relaxation of muscles.")
                                        .build(),
                                Pair.builder()
                                        .key("Nerve Function")
                                        .value("Calcium plays a role in nerve transmission by facilitating the release of neurotransmitters, which are chemicals that transmit signals between nerve cells.")
                                        .build(),
                                Pair.builder()
                                        .key("Blood Clotting")
                                        .value("Calcium is involved in the blood clotting process. It's necessary for the activation of certain proteins that help form blood clots to stop bleeding.")
                                        .build(),
                                Pair.builder()
                                        .key("Cellular Signaling")
                                        .value("Calcium ions serve as important signaling molecules within cells. They are involved in numerous cellular processes, including enzyme activation and gene expression.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Dairy products")
                                        .value("milk, yogurt, cheese")
                                        .build(),
                                Pair.builder()
                                        .key("Fortified plant-based milk alternatives")
                                        .value("soy milk, almond milk")
                                        .build(),
                                Pair.builder()
                                        .key("Leafy green vegetables")
                                        .value("collard greens, broccoli, spinach")
                                        .build(),
                                Pair.builder()
                                        .key("Nuts and seeds")
                                        .value("almonds, chia seeds")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Fortified cereals and breads")
                                        .build(),
                                Pair.builder()
                                        .key("Fish with soft, edible bones")
                                        .value("sardines, canned salmon")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("Calcium intake is essential, but other factors can influence its absorption and utilization in the body. For instance, vitamin D is crucial for calcium absorption in the intestines. Additionally, certain conditions like lactose intolerance or dairy allergies may require alternative sources of calcium.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("1000"))
                .maleHigherBoundIntake(new BigDecimal("1000"))
                .femaleLowerBoundIntake(new BigDecimal("1000"))
                .femaleHigherBoundIntake(new BigDecimal("1000"))
                .measure("milligrams (mg)")
                .build();
        electrolyteEntityMap.put(calcium.getName(), calcium);

        Electrolyte magnesium = Electrolyte.builder()
                .name("Magnesium")
                .description("\n" +
                        "Magnesium is a crucial mineral that is involved in a wide range of physiological processes within the body. It plays a vital role in supporting muscle and nerve function, maintaining a healthy immune system, regulating heart rhythm, and promoting bone health, among other functions. Magnesium is required for over 300 enzymatic reactions, making it essential for overall health and well-being.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Muscle and Nerve Function")
                                        .value("Magnesium is involved in the regulation of muscle contractions and nerve impulses. It helps muscles contract and relax properly and aids in the transmission of nerve signals.")
                                        .build(),
                                Pair.builder()
                                        .key("Energy Production")
                                        .value("Magnesium is a co-factor in many enzymatic reactions involved in energy metabolism. It's necessary for the conversion of food into energy at the cellular level.")
                                        .build(),
                                Pair.builder()
                                        .key("Bone Health")
                                        .value("Magnesium is essential for bone health as it plays a role in bone mineralization. It works alongside other minerals like calcium and vitamin D to support strong and healthy bones.")
                                        .build(),
                                Pair.builder()
                                        .key("Heart Health")
                                        .value("Magnesium helps regulate heart rhythm and supports the function of the cardiovascular system. It helps maintain a healthy heart rate and blood pressure.")
                                        .build(),
                                Pair.builder()
                                        .key("Protein Synthesis")
                                        .value("Magnesium is involved in the synthesis of proteins, which are essential for various bodily functions, including immune response, tissue repair, and hormone production.")
                                        .build(),
                                Pair.builder()
                                        .key("Nervous System Health")
                                        .value("Magnesium plays a role in maintaining the health of the nervous system. It helps reduce stress and anxiety and supports relaxation.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Nuts and seeds")
                                        .value("almonds, cashews, pumpkin seeds")
                                        .build(),
                                Pair.builder()
                                        .key("Leafy green vegetables")
                                        .value("spinach, kale")
                                        .build(),
                                Pair.builder()
                                        .key("Whole grains")
                                        .value("brown rice, whole wheat")
                                        .build(),
                                Pair.builder()
                                        .key("Legumes")
                                        .value("beans, lentils")
                                        .build(),
                                Pair.builder()
                                        .key("Fish")
                                        .value("salmon, mackerel")
                                        .build(),
                                Pair.builder()
                                        .key("Dairy products")
                                        .value("yogurt, milk")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Dark chocolate")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("Magnesium deficiency can lead to a range of health issues, including muscle cramps, fatigue, weakness, and abnormal heart rhythms. Certain medical conditions and medications can interfere with magnesium absorption or increase its excretion, leading to deficiency.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("400"))
                .maleHigherBoundIntake(new BigDecimal("420"))
                .femaleLowerBoundIntake(new BigDecimal("310"))
                .femaleHigherBoundIntake(new BigDecimal("320"))
                .measure("milligrams (mg)")
                .build();

        electrolyteEntityMap.put(magnesium.getName(), magnesium);

        Electrolyte chloride = Electrolyte.builder()
                .name("Chloride")
                .description(
                        "Chloride is an essential mineral that often works in tandem with other electrolytes, such as sodium and potassium, to maintain various bodily functions. It plays a crucial role in fluid balance, acid-base balance, and the function of digestive juices in the stomach.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Fluid Balance")
                                        .value("Chloride, along with sodium, helps regulate fluid balance within and outside cells. This balance is crucial for maintaining proper hydration, blood pressure, and overall cell function.")
                                        .build(),
                                Pair.builder()
                                        .key("Acid-Base Balance")
                                        .value("Chloride is a component of hydrochloric acid (HCl) in the stomach. This acid is essential for digestion and helps create an acidic environment that breaks down food and aids in the absorption of nutrients.")
                                        .build(),
                                Pair.builder()
                                        .key("Nerve Function")
                                        .value("Chloride ions play a role in nerve impulse transmission. They help maintain the electrical gradient necessary for nerve cells to transmit signals.")
                                        .build(),
                                Pair.builder()
                                        .key("Immune Response")
                                        .value("Chloride is involved in the immune response and the body's defense against pathogens.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Table salt")
                                        .value("sodium chloride")
                                        .build(),
                                Pair.builder()
                                        .key("Processed and packaged foods")
                                        .value("canned soups, snacks, condiments")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Salted meats and fish")
                                        .build(),
                                Pair.builder()
                                        .key("Certain vegetables")
                                        .value("celery, olives")
                                        .build()))
                .healthConsiderations(
                        List.of(
                                Pair.builder()
                                        .key("-")
                                        .value("Chloride imbalances are relatively rare and often associated with imbalances in other electrolytes, such as sodium or potassium. Excessive sodium consumption, often in the form of sodium chloride, can lead to high levels of sodium and chloride in the body, potentially contributing to issues like high blood pressure and fluid retention.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("2300"))
                .maleHigherBoundIntake(new BigDecimal("2300"))
                .femaleLowerBoundIntake(new BigDecimal("2300"))
                .femaleHigherBoundIntake(new BigDecimal("2300"))
                .measure("milligrams (mg)")
                .build();

        electrolyteEntityMap.put(chloride.getName(), chloride);
    }
}
