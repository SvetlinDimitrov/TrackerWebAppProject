package org.vitamin;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.vitamin.model.entity.Pair;
import org.vitamin.model.entity.Vitamin;

import jakarta.annotation.PostConstruct;

@Repository
public class VitaminRepository {
    private final Map<String, Vitamin> vitaminMap = new LinkedHashMap<>();

    public Optional<Vitamin> getVitaminByName(String name) {
        return Optional.ofNullable(vitaminMap.get(name));
    }

    public List<Vitamin> getVitamins() {
        return new LinkedList<>(vitaminMap.values());
    }

    public List<String> getAllVitaminsNames() {
        return new LinkedList<>(vitaminMap.keySet());
    }

    @PostConstruct
    public void initVitamins() {
        Vitamin a = Vitamin.builder()
                .name("A")
                .description(
                        "Vitamin A is a fat-soluble vitamin that plays a critical role in various bodily functions, including vision, immune system support, and maintaining healthy skin. It exists in several forms, including retinol, retinal, and retinoic acid, which have distinct functions in the body.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Vision")
                                        .value(" Vitamin A is essential for maintaining good vision. It is a component of rhodopsin, a protein in the retina that allows the eyes to adjust to changes in light and facilitates vision in low-light conditions. A deficiency in vitamin A can lead to night blindness and other visual impairments.")
                                        .build(),
                                Pair.builder()
                                        .key("Immune System Support")
                                        .value("Vitamin A helps maintain the integrity of the mucosal surfaces, such as the linings of the respiratory, digestive, and urinary tracts. This is important for preventing infections and supporting immune responses.")
                                        .build(),
                                Pair.builder()
                                        .key("Skin Health")
                                        .value("Vitamin A plays a role in skin health by promoting the production of skin cells and supporting the health of mucous membranes. It's often used in skincare products for its potential to improve skin texture and reduce signs of aging.")
                                        .build(),
                                Pair.builder()
                                        .key("Cell Differentiation")
                                        .value("Vitamin A is involved in the process of cell differentiation, which is crucial for normal growth and development, especially during embryonic development.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Preformed Vitamin A (Retinoids)")
                                        .value("Found in animal products such as liver, fish, eggs, and dairy. These foods contain retinol, which is a form of preformed vitamin A.")
                                        .build(),
                                Pair.builder()
                                        .key("Provitamin A Carotenoids")
                                        .value(" Found in plant-based foods, primarily in the form of beta-carotene. Beta-carotene is a pigment that gives fruits and vegetables their orange, red, and yellow colors. The body can convert provitamin A carotenoids into active vitamin A as needed. Foods rich in beta-carotene include carrots, sweet potatoes, spinach, kale, and butternut squash.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("700"))
                .maleHigherBoundIntake(new BigDecimal("900"))
                .femaleLowerBoundIntake(new BigDecimal("600"))
                .femaleHigherBoundIntake(new BigDecimal("700"))
                .measure("micrograms of retinol activity equivalents (RAE)")
                .build();

        vitaminMap.put(a.getName(), a);

        Vitamin d = Vitamin.builder()
                .name("D")
                .description(
                        "Vitamin D is a fat-soluble vitamin that is essential for several important functions in the body. Often referred to as the \"sunshine vitamin,\" it can be synthesized by the skin when exposed to sunlight. Vitamin D also comes from dietary sources and supplements. Here's more information about vitamin D")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Bone Health")
                                        .value("One of the primary functions of vitamin D is to regulate calcium and phosphorus absorption in the intestines. This is crucial for maintaining strong and healthy bones. Vitamin D helps ensure that calcium is properly deposited in bones, which is important for preventing conditions like osteoporosis and fractures.")
                                        .build(),
                                Pair.builder()
                                        .key("Immune System Support")
                                        .value("Vitamin D plays a role in modulating the immune system. It helps regulate immune responses and supports the body's defense mechanisms against infections.")
                                        .build(),
                                Pair.builder()
                                        .key("Cell Growth and Differentiation")
                                        .value("Vitamin D is involved in cell growth, replication, and differentiation. It is important for the normal development of cells and tissues.")
                                        .build(),
                                Pair.builder()
                                        .key("Hormone Regulation")
                                        .value("Vitamin D acts as a hormone in the body, influencing various physiological processes. It has been linked to the regulation of insulin secretion, blood pressure, and the functioning of the cardiovascular system.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Sunlight")
                                        .value("The skin can produce vitamin D when exposed to ultraviolet B (UVB) sunlight. However, factors like skin color, geographical location, time of day, and sunscreen use can affect the body's ability to synthesize vitamin D from sunlight.")
                                        .build(),
                                Pair.builder()
                                        .key("Dietary Sources")
                                        .value("While there are fewer natural dietary sources of vitamin D, some foods are fortified with it. Dietary sources include fatty fish (salmon, mackerel, sardines), cod liver oil, egg yolks, and fortified foods like milk, orange juice, and cereal.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("600"))
                .maleHigherBoundIntake(new BigDecimal("800"))
                .femaleLowerBoundIntake(new BigDecimal("600"))
                .femaleHigherBoundIntake(new BigDecimal("800"))
                .measure("international units (IU)")
                .build();

        vitaminMap.put(d.getName(), d);

        Vitamin e = Vitamin.builder()
                .name("E")
                .description(
                        "Vitamin E is a fat-soluble vitamin with antioxidant properties, meaning it helps protect cells from damage caused by harmful molecules called free radicals. It exists in eight different forms, with alpha-tocopherol being the most common and biologically active form in the human body.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Antioxidant Protection")
                                        .value("Vitamin E's primary role is as an antioxidant. It helps neutralize free radicals, which are unstable molecules that can damage cells, DNA, and tissues. By reducing oxidative stress, vitamin E contributes to overall cellular health and may help prevent chronic diseases.")
                                        .build(),
                                Pair.builder()
                                        .key("Immune Support")
                                        .value("Vitamin E plays a role in supporting the immune system's response to infections and diseases. It helps regulate immune cell functions and enhances the body's defense mechanisms.")
                                        .build(),
                                Pair.builder()
                                        .key("Skin Health")
                                        .value("Vitamin E is often associated with skin health. It can help moisturize and protect the skin from damage caused by UV radiation and environmental pollutants.")
                                        .build(),
                                Pair.builder()
                                        .key("Cardiovascular Health")
                                        .value("Some research suggests that vitamin E might contribute to heart health by preventing the oxidation of low-density lipoprotein (LDL) cholesterol, often referred to as \"bad\" cholesterol. However, the role of vitamin E in heart health is still a topic of ongoing research.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Nuts and Seeds")
                                        .value("Almonds, sunflower seeds, hazelnuts, and pine nuts are rich sources of vitamin E.")
                                        .build(),
                                Pair.builder()
                                        .key("Vegetable Oils")
                                        .value("Sunflower oil, safflower oil, and wheat germ oil are high in vitamin E.")
                                        .build(),
                                Pair.builder()
                                        .key("Leafy Greens")
                                        .value("Spinach, Swiss chard, and kale provide smaller amounts of vitamin E")
                                        .build(),
                                Pair.builder()
                                        .key("Fortified Foods")
                                        .value("Some breakfast cereals and other packaged foods are fortified with vitamin E.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("15"))
                .maleHigherBoundIntake(new BigDecimal("15"))
                .femaleLowerBoundIntake(new BigDecimal("15"))
                .femaleHigherBoundIntake(new BigDecimal("15"))
                .measure("milligrams (mg) of alpha-tocophero")
                .build();

        vitaminMap.put(e.getName(), e);

        Vitamin k = Vitamin.builder()
                .name("K")
                .description(
                        "Vitamin K is a fat-soluble vitamin that plays a crucial role in blood clotting, bone health, and other important bodily functions. There are two main forms of vitamin K: vitamin K1 (phylloquinone) and vitamin K2 (menaquinone)")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Blood Clotting")
                                        .value("Vitamin K is essential for the synthesis of certain proteins that are involved in the blood clotting process. These proteins help stop bleeding by promoting the formation of blood clots when there is an injury.")
                                        .build(),
                                Pair.builder()
                                        .key("Bone Health")
                                        .value("Vitamin K plays a role in bone health by assisting in the regulation of calcium. It helps ensure that calcium is properly deposited in bones and teeth, contributing to bone density and strength.")
                                        .build(),
                                Pair.builder()
                                        .key("Heart Health")
                                        .value("Some research suggests that vitamin K might have a role in cardiovascular health by helping prevent the calcification of arteries and promoting overall heart health. However, more research is needed to fully understand this relationship.")
                                        .build(),
                                Pair.builder()
                                        .key("Cell Growth and Regulation")
                                        .value("Vitamin K is involved in cellular processes related to growth, regulation, and apoptosis (programmed cell death).")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Vitamin K1 (Phylloquinone)")
                                        .value(" Found primarily in green leafy vegetables, such as spinach, kale, broccoli, and Brussels sprouts.")
                                        .build(),
                                Pair.builder()
                                        .key("Vitamin K2 (Menaquinone)")
                                        .value("Produced by bacteria in the gut and also found in certain animal-based foods like dairy products, eggs, and meat. Fermented foods, such as natto (a type of fermented soybean), are particularly rich in vitamin K2.")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("120"))
                .maleHigherBoundIntake(new BigDecimal("120"))
                .femaleLowerBoundIntake(new BigDecimal("90"))
                .femaleHigherBoundIntake(new BigDecimal("90"))
                .measure("micrograms (mcg)")
                .build();

        vitaminMap.put(k.getName(), k);

        Vitamin C = Vitamin.builder()
                .name("C")
                .description(
                        "Vitamin C, also known as ascorbic acid, is a water-soluble vitamin that is essential for a wide range of bodily functions. It's well-known for its antioxidant properties and its role in supporting the immune system.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Antioxidant Protection")
                                        .value("One of the primary roles of vitamin C is its antioxidant function. It helps protect cells from damage caused by harmful molecules called free radicals. Antioxidants like vitamin C play a key role in maintaining overall cellular health and reducing oxidative stress.")
                                        .build(),
                                Pair.builder()
                                        .key("Immune System Support")
                                        .value("Vitamin C supports the immune system by promoting the production and function of white blood cells, which are vital for fighting infections. It may also help shorten the duration and severity of common colds.")
                                        .build(),
                                Pair.builder()
                                        .key("Collagen Synthesis")
                                        .value("Vitamin C is essential for the synthesis of collagen, a protein that is important for the structure of skin, blood vessels, tendons, ligaments, and other connective tissues. It's crucial for wound healing and maintaining healthy skin.")
                                        .build(),
                                Pair.builder()
                                        .key("Iron Absorption")
                                        .value("Vitamin C enhances the absorption of non-heme iron (the type of iron found in plant-based foods) from the digestive tract. This can be particularly important for individuals who follow vegetarian or vegan diets.")
                                        .build(),
                                Pair.builder()
                                        .key("Neurotransmitter Production")
                                        .value("Vitamin C is involved in the synthesis of certain neurotransmitters, such as serotonin and norepinephrine, which play a role in mood regulation and the nervous system.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Citrus Fruits")
                                        .value("Oranges,Grapefruits,Lemons,Limes,Tangerines")
                                        .build(),
                                Pair.builder()
                                        .key("Berries")
                                        .value("Strawberries,Blueberries,Raspberries,Blackberries")
                                        .build(),
                                Pair.builder()
                                        .key("")
                                        .value("Kiwi")
                                        .build(),
                                Pair.builder()
                                        .key("Bell Peppers")
                                        .value("Red bell peppers,Green bell peppers,Yellow bell peppers,Orange bell peppers")
                                        .build(),
                                Pair.builder()
                                        .key("Tropical Fruits")
                                        .value("Pineapple,Mango,Papaya")
                                        .build(),
                                Pair.builder()
                                        .key("Melons")
                                        .value("Cantaloupe,Watermelon")
                                        .build(),
                                Pair.builder()
                                        .key("Leafy Greens")
                                        .value("Spinach,Kale,Swiss chard")
                                        .build(),
                                Pair.builder()
                                        .key("Others")
                                        .value("Broccoli,Brussels Sprouts,Tomatoes,Cabbage,Cauliflower,Potatoes,Guava,Acerola Cherry,Mango,Papaya")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("90"))
                .maleHigherBoundIntake(new BigDecimal("90"))
                .femaleLowerBoundIntake(new BigDecimal("75"))
                .femaleHigherBoundIntake(new BigDecimal("75"))
                .measure("milligrams (mg)")
                .build();

        vitaminMap.put(C.getName(), C);

        Vitamin B1 = Vitamin.builder()
                .name("B1")
                .description(
                        "Vitamin B1, also known as thiamin, is a water-soluble B-vitamin that plays a vital role in energy metabolism and maintaining the health of the nervous system.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Energy Metabolism")
                                        .value("Thiamin is essential for converting carbohydrates into energy. It plays a key role in the metabolism of glucose, which is the primary source of energy for the body's cells.")
                                        .build(),
                                Pair.builder()
                                        .key("Nervous System Health")
                                        .value("Thiamin is crucial for the proper functioning of the nervous system. It helps maintain the health of nerve cells and supports nerve impulse transmission, which is essential for muscle contractions and sensory functions.")
                                        .build(),
                                Pair.builder()
                                        .key("Role in Coenzymes")
                                        .value("Thiamin is a precursor to thiamine pyrophosphate (TPP), a coenzyme involved in several biochemical reactions. TPP is necessary for reactions that generate energy from carbohydrates and certain amino acids.")
                                        .build(),
                                Pair.builder()
                                        .key("Cardiovascular Health")
                                        .value("Thiamin is involved in the production of neurotransmitters and certain hormones, which can impact cardiovascular function and overall well-being.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Whole grains")
                                        .value("brown rice, whole wheat, oats")
                                        .build(),
                                Pair.builder()
                                        .key("Legumes")
                                        .value("beans, lentils")
                                        .build(),
                                Pair.builder()
                                        .key("Nuts")
                                        .value("especially sunflower seeds")
                                        .build(),
                                Pair.builder()
                                        .key("Lean meats")
                                        .value("pork, beef, poultry")
                                        .build(),
                                Pair.builder()
                                        .key("Fish")
                                        .value("trout, mackerel")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Eggs")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Dairy products")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("1.2"))
                .maleHigherBoundIntake(new BigDecimal("1.2"))
                .femaleLowerBoundIntake(new BigDecimal("1.1"))
                .femaleHigherBoundIntake(new BigDecimal("1.1"))
                .measure("milligrams (mg)")
                .build();

        vitaminMap.put(B1.getName(), B1);

        Vitamin B2 = Vitamin.builder()
                .name("B2")
                .description(
                        "Vitamin B2, also known as riboflavin, is a water-soluble B-vitamin that plays a critical role in various cellular processes and energy production. It's involved in converting food into energy and is important for maintaining healthy skin, eyes, and red blood cells.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Energy Metabolism")
                                        .value("Riboflavin is an essential component of two coenzymes, flavin mononucleotide (FMN) and flavin adenine dinucleotide (FAD). These coenzymes are involved in numerous reactions that help convert carbohydrates, fats, and proteins from food into energy.")
                                        .build(),
                                Pair.builder()
                                        .key("Antioxidant Protection")
                                        .value("Riboflavin is part of the antioxidant enzyme glutathione reductase, which helps maintain the body's antioxidant defenses. It assists in protecting cells from oxidative stress and damage caused by free radicals.")
                                        .build(),
                                Pair.builder()
                                        .key("Skin and Eye Health")
                                        .value("Riboflavin contributes to maintaining healthy skin, eyes, and mucous membranes. It's essential for skin health and helps prevent conditions like cracked lips and skin lesions. It's also important for promoting good vision.")
                                        .build(),
                                Pair.builder()
                                        .key("Red Blood Cell Formation")
                                        .value("Riboflavin plays a role in the synthesis of red blood cells, which are responsible for transporting oxygen throughout the body.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Dairy products")
                                        .value("milk, yogurt, cheese")
                                        .build(),
                                Pair.builder()
                                        .key("Lean meats")
                                        .value("chicken, turkey, beef")
                                        .build(),
                                Pair.builder()
                                        .key("Fish")
                                        .value("salmon, trout")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Eggs")
                                        .build(),
                                Pair.builder()
                                        .key("Leafy green vegetables")
                                        .value("spinach, broccoli")
                                        .build(),
                                Pair.builder()
                                        .key("Whole grains")
                                        .value("enriched cereals, whole wheat")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Nuts and seeds")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("1.3"))
                .maleHigherBoundIntake(new BigDecimal("1.3"))
                .femaleLowerBoundIntake(new BigDecimal("1.1"))
                .femaleHigherBoundIntake(new BigDecimal("1.1"))
                .measure("milligrams (mg)")
                .build();

        vitaminMap.put(B2.getName(), B2);

        Vitamin B3 = Vitamin.builder()
                .name("B3")
                .description(
                        "Vitamin B3, also known as niacin, is a water-soluble B-vitamin that plays a crucial role in energy metabolism, nervous system function, and maintaining healthy skin and digestive system. Niacin has two primary forms: nicotinic acid and nicotinamide (niacinamide), both of which are converted into coenzymes that participate in numerous biochemical reactions in the body.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Energy Metabolism")
                                        .value("Niacin is a component of the coenzymes nicotinamide adenine dinucleotide (NAD) and nicotinamide adenine dinucleotide phosphate (NADP), which are involved in energy production by assisting in the breakdown of carbohydrates, fats, and proteins.")
                                        .build(),
                                Pair.builder()
                                        .key("Cell Signaling")
                                        .value("Niacin and its coenzymes play a role in cellular communication, including the transmission of signals within cells and between cells.")
                                        .build(),
                                Pair.builder()
                                        .key("Skin Health")
                                        .value("Niacinamide (nicotinamide) has been shown to have benefits for skin health. It may help improve the appearance of skin by reducing inflammation, supporting collagen production, and maintaining the skin's natural barrier function.")
                                        .build(),
                                Pair.builder()
                                        .key("Cardiovascular Health")
                                        .value("Niacin can help lower levels of LDL (\"bad\") cholesterol and triglycerides, while increasing levels of HDL (\"good\") cholesterol. However, the use of niacin supplements for this purpose is less common nowadays due to potential side effects.")
                                        .build(),
                                Pair.builder()
                                        .key("Nervous System Support")
                                        .value("Niacin is essential for the health of the nervous system. It supports the function of nerve cells and contributes to the synthesis of neurotransmitters.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Lean meats")
                                        .value("poultry, turkey, beef")
                                        .build(),
                                Pair.builder()
                                        .key("Fish")
                                        .value("tuna, salmon")
                                        .build(),
                                Pair.builder()
                                        .key("Dairy products")
                                        .value("milk, cheese")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Eggs")
                                        .build(),
                                Pair.builder()
                                        .key("Legumes")
                                        .value("peanuts, lentils")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Nuts and seeds")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Fortified cereals and breads")
                                        .build(),
                                Pair.builder()
                                        .key("Whole grains")
                                        .value("brown rice, whole wheat")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("16"))
                .maleHigherBoundIntake(new BigDecimal("16"))
                .femaleLowerBoundIntake(new BigDecimal("14"))
                .femaleHigherBoundIntake(new BigDecimal("14"))
                .measure("milligrams (mg)")
                .build();

        vitaminMap.put(B3.getName(), B3);

        Vitamin B5 = Vitamin.builder()
                .name("B5")
                .description(
                        "Vitamin B5, also known as pantothenic acid, is a water-soluble B-vitamin that plays a crucial role in various metabolic processes in the body. It's involved in energy production, the synthesis of important molecules, and maintaining overall health.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Energy Metabolism")
                                        .value("Pantothenic acid is a component of coenzyme A (CoA), which is essential for the breakdown of carbohydrates, fats, and proteins to produce energy. CoA is a key player in the citric acid cycle (Krebs cycle), which is a fundamental part of energy production in cells.")
                                        .build(),
                                Pair.builder()
                                        .key("Synthesis of Important Molecules")
                                        .value("CoA, derived from pantothenic acid, is also necessary for the synthesis of various molecules in the body. This includes the production of fatty acids, cholesterol, and acetylcholine (a neurotransmitter).")
                                        .build(),
                                Pair.builder()
                                        .key("Skin Health")
                                        .value("Pantothenic acid is often associated with skin health. It's used in various skincare products and supplements due to its potential benefits in reducing acne and promoting healthy skin.")
                                        .build(),
                                Pair.builder()
                                        .key("Nervous System Function")
                                        .value("Pantothenic acid is involved in the production of acetylcholine, a neurotransmitter that is important for proper nerve transmission and muscle function.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Meat")
                                        .value("chicken, beef, pork")
                                        .build(),
                                Pair.builder()
                                        .key("Fish")
                                        .value("salmon, tuna")
                                        .build(),
                                Pair.builder()
                                        .key("Dairy products")
                                        .value("milk, yogurt, cheese")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Eggs")
                                        .build(),
                                Pair.builder()
                                        .key("Legumes")
                                        .value("lentils, chickpeas")
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
                                        .key("Vegetables")
                                        .value("broccoli, sweet potatoes")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("5"))
                .maleHigherBoundIntake(new BigDecimal("5"))
                .femaleLowerBoundIntake(new BigDecimal("5"))
                .femaleHigherBoundIntake(new BigDecimal("5"))
                .measure("milligrams (mg)")
                .build();

        vitaminMap.put(B5.getName(), B5);

        Vitamin B6 = Vitamin.builder()
                .name("B6")
                .description(
                        "Vitamin B6, also known as pyridoxine, is a water-soluble B-vitamin that plays a vital role in various metabolic processes in the body. It's involved in energy production, neurotransmitter synthesis, and the metabolism of amino acids and fatty acids.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Amino Acid Metabolism")
                                        .value("Vitamin B6 is necessary for the metabolism of amino acids, the building blocks of proteins. It helps convert amino acids into usable forms, which are crucial for the synthesis of proteins and other important molecules.")
                                        .build(),
                                Pair.builder()
                                        .key("Neurotransmitter Production")
                                        .value("Vitamin B6 is involved in the synthesis of neurotransmitters such as serotonin, dopamine, and gamma-aminobutyric acid (GABA). These neurotransmitters play key roles in mood regulation, sleep, and overall brain function.")
                                        .build(),
                                Pair.builder()
                                        .key("Hemoglobin Formation")
                                        .value("Vitamin B6 is required for the synthesis of hemoglobin, the protein in red blood cells that carries oxygen to cells and tissues throughout the body.")
                                        .build(),
                                Pair.builder()
                                        .key("Immune System Function")
                                        .value("Vitamin B6 supports the immune system by influencing the production and activity of immune cells and antibodies.")
                                        .build(),
                                Pair.builder()
                                        .key("Nerve Function")
                                        .value("Vitamin B6 is essential for proper nerve function and transmission of nerve signals.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Meat")
                                        .value("chicken, turkey, beef")
                                        .build(),
                                Pair.builder()
                                        .key("Fish")
                                        .value("salmon, tuna")
                                        .build(),
                                Pair.builder()
                                        .key("Dairy products")
                                        .value("milk, cheese")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Eggs")
                                        .build(),
                                Pair.builder()
                                        .key("Legumes")
                                        .value("lentils, beans")
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
                                        .key("-")
                                        .value("Fortified cereals")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("1.3"))
                .maleHigherBoundIntake(new BigDecimal("1.7"))
                .femaleLowerBoundIntake(new BigDecimal("1.3"))
                .femaleHigherBoundIntake(new BigDecimal("1.5"))
                .measure("milligrams (mg)")
                .build();

        vitaminMap.put(B6.getName(), B6);

        Vitamin B7 = Vitamin.builder()
                .name("B7")
                .description(
                        "Vitamin B7, also known as biotin, is a water-soluble B-vitamin that plays a crucial role in various metabolic processes in the body. It's involved in the metabolism of carbohydrates, fats, and amino acids, and it supports healthy skin, hair, and nails. Biotin is also essential for maintaining healthy nerve function and assisting in the synthesis of certain enzymes.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Carbohydrate, Fat, and Protein Metabolism")
                                        .value("Biotin is a coenzyme that plays a key role in the metabolism of carbohydrates, fats, and amino acids. It helps convert these nutrients into energy that the body can use.")
                                        .build(),
                                Pair.builder()
                                        .key("Healthy Skin, Hair, and Nails")
                                        .value("Biotin is often associated with promoting healthy skin, hair, and nails. It's a common ingredient in beauty supplements due to its potential role in maintaining the health and appearance of these tissues.")
                                        .build(),
                                Pair.builder()
                                        .key("Nervous System Health")
                                        .value("Biotin is important for maintaining healthy nerve function and promoting the proper functioning of the nervous system.")
                                        .build(),
                                Pair.builder()
                                        .key("Enzyme Synthesis")
                                        .value("Biotin is required for the synthesis of certain enzymes that are involved in various biochemical reactions in the body. These enzymes are crucial for processes such as fatty acid synthesis and gluconeogenesis.")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Organ meats")
                                        .value("liver, kidney")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Eggs")
                                        .build(),
                                Pair.builder()
                                        .key("Dairy products")
                                        .value("milk, cheese")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Nuts and seeds")
                                        .build(),
                                Pair.builder()
                                        .key("Legumes")
                                        .value("soybeans, peanuts")
                                        .build(),
                                Pair.builder()
                                        .key("Whole grains")
                                        .value("oats, barley")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Leafy green vegetables")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Fish")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("30"))
                .maleHigherBoundIntake(new BigDecimal("30"))
                .femaleLowerBoundIntake(new BigDecimal("30"))
                .femaleHigherBoundIntake(new BigDecimal("30"))
                .measure("micrograms (mg)")
                .build();

        vitaminMap.put(B7.getName(), B7);

        Vitamin B9 = Vitamin.builder()
                .name("B9")
                .description(
                        "Vitamin B9, also known as folate (or folic acid when in synthetic form), is a water-soluble B-vitamin that plays a crucial role in various bodily functions, including cell division, DNA synthesis, and the formation of red blood cells. It's particularly important during periods of rapid growth and development, such as pregnancy.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("DNA Synthesis and Cell Division")
                                        .value("Folate is essential for the synthesis of DNA and RNA, the genetic material of cells. It plays a vital role in cell division and growth, making it particularly important during periods of rapid tissue development and growth.")
                                        .build(),
                                Pair.builder()
                                        .key("Red Blood Cell Formation")
                                        .value("Folate is necessary for the formation of red blood cells. Adequate folate levels are crucial for preventing certain types of anemia, such as megaloblastic anemia.")
                                        .build(),
                                Pair.builder()
                                        .key("Neural Tube Formation")
                                        .value("Folate is critical during early pregnancy for the proper formation of the neural tube in the developing fetus. Adequate folate intake can help prevent neural tube defects in newborns.")
                                        .build(),
                                Pair.builder()
                                        .key("Homocysteine Regulation")
                                        .value("Folate, along with vitamins B6 and B12, helps regulate homocysteine levels in the blood. Elevated homocysteine levels are associated with an increased risk of cardiovascular disease.")
                                        .build(),
                                Pair.builder()
                                        .key("")
                                        .value("")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Leafy green vegetables")
                                        .value("spinach, kale, lettuce")
                                        .build(),
                                Pair.builder()
                                        .key("Legumes")
                                        .value("beans, lentils")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Fortified cereals and breads")
                                        .build(),
                                Pair.builder()
                                        .key("Citrus fruits")
                                        .value("oranges, lemons")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Avocado")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Eggs")
                                        .build(),
                                Pair.builder()
                                        .key("Liver")
                                        .value("organ meat")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("400"))
                .maleHigherBoundIntake(new BigDecimal("400"))
                .femaleLowerBoundIntake(new BigDecimal("400"))
                .femaleHigherBoundIntake(new BigDecimal("400"))
                .measure("micrograms (mcg)")
                .build();

        vitaminMap.put(B9.getName(), B9);

        Vitamin B12 = Vitamin.builder()
                .name("B12")
                .description(
                        "Vitamin B12, also known as cobalamin, is a water-soluble B-vitamin that plays a critical role in various bodily functions, including red blood cell formation, nervous system health, and DNA synthesis. It's unique among the B-vitamins because it's not found in significant amounts in most plant-based foods and is often associated with animal products.")
                .functions(
                        List.of(
                                Pair.builder()
                                        .key("Red Blood Cell Formation")
                                        .value("Vitamin B12 is essential for the synthesis of red blood cells. It works in conjunction with folate to help prevent a type of anemia called megaloblastic anemia, where red blood cells are larger than normal.")
                                        .build(),
                                Pair.builder()
                                        .key("Nervous System Health")
                                        .value("Vitamin B12 is crucial for maintaining the health of the nervous system. It helps protect nerve cells and supports nerve impulse transmission, which is vital for muscle function and sensory perception.")
                                        .build(),
                                Pair.builder()
                                        .key("DNA Synthesis")
                                        .value("Vitamin B12 is involved in the synthesis of DNA, the genetic material of cells. It's particularly important during periods of rapid cell division, such as growth and development.")
                                        .build(),
                                Pair.builder()
                                        .key("Homocysteine Regulation")
                                        .value("Vitamin B12, along with folate and vitamin B6, helps regulate homocysteine levels in the blood. Elevated homocysteine levels are associated with an increased risk of cardiovascular disease")
                                        .build()))
                .sources(
                        List.of(
                                Pair.builder()
                                        .key("Meat")
                                        .value("beef, pork, poultry")
                                        .build(),
                                Pair.builder()
                                        .key("Fish")
                                        .value("salmon, trout, tuna")
                                        .build(),
                                Pair.builder()
                                        .key("Shellfish")
                                        .value("clams, mussels, crab")
                                        .build(),
                                Pair.builder()
                                        .key("Dairy products")
                                        .value("milk, cheese, yogurt")
                                        .build(),
                                Pair.builder()
                                        .key("-")
                                        .value("Eggs")
                                        .build()))
                .maleLowerBoundIntake(new BigDecimal("2.4"))
                .maleHigherBoundIntake(new BigDecimal("2.4"))
                .femaleLowerBoundIntake(new BigDecimal("2.4"))
                .femaleHigherBoundIntake(new BigDecimal("2.4"))
                .measure("micrograms (mcg)")
                .build();

        vitaminMap.put(B12.getName(), B12);
    }
}
