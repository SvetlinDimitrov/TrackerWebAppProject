package org.vitamin;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vitamin.model.entity.Pair;
import org.vitamin.model.entity.Vitamin;

@ExtendWith(MockitoExtension.class)
public class VitaminServiceImpTest {

    @Mock
    private VitaminRepository vitaminRepository;

    @InjectMocks
    private VitaminServiceImp vitaminService;

    private final List<Vitamin> vitamins = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        vitamins.addAll(List.of(
                Vitamin.builder()
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
                        .build(),
                Vitamin.builder()
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
                        .build()));
    }

    @Test
    public void getVitamins_ValidEntities_ReturnsCorrectly() {

        when(vitaminRepository.getVitamins()).thenReturn(vitamins);

        List<Vitamin> actual = 
        vitaminService.getVitamins();

        assertEquals(vitamins, actual);
    }

    @Test 
    public void getVitaminByName_ValidName_ReturnsCorrectly() throws VitaminNotFoundException {
        when(vitaminRepository.getVitaminByName("A"))
        .thenReturn(Optional.ofNullable(vitamins.get(0)));

        Vitamin actual = 
        vitaminService.getVitaminByName("A");

        assertEquals(vitamins.get(0), actual);
    }

    @Test
    public void getVitaminByName_InvalidName_ThrowsVitaminNotFoundException() {
        
        when(vitaminRepository.getVitaminByName("C"))
        .thenReturn(Optional.empty());

       assertThrows(VitaminNotFoundException.class,
        () -> vitaminService.getVitaminByName("C"));
    }

    @Test
    public void getAllVitaminNames_ValidEntities_ReturnsCorrectlyTransformedViews() {
        when(vitaminRepository.getAllVitaminsNames()).thenReturn(List.of("A", "D"));

        List<String> actual = 
        vitaminService.getAllVitaminNames();

        assertEquals(List.of("A", "D"), actual);
    }
}