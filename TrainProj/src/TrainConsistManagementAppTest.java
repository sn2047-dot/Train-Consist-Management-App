import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrainConsistManagementAppTest {

    @Test
    public void testRegex_ValidTrainID() {
        assertTrue(TrainConsistManagementApp.isValidTrainID("TRN-1234"));
    }

    @Test
    public void testRegex_InvalidTrainIDFormat() {
        assertFalse(TrainConsistManagementApp.isValidTrainID("TRAIN12"));
        assertFalse(TrainConsistManagementApp.isValidTrainID("TRN12A"));
        assertFalse(TrainConsistManagementApp.isValidTrainID("1234-TRN"));
    }

    @Test
    public void testRegex_ValidCargoCode() {
        assertTrue(TrainConsistManagementApp.isValidCargoCode("PET-AB"));
    }

    @Test
    public void testRegex_InvalidCargoCodeFormat() {
        assertFalse(TrainConsistManagementApp.isValidCargoCode("PET-ab"));
        assertFalse(TrainConsistManagementApp.isValidCargoCode("PET123"));
        assertFalse(TrainConsistManagementApp.isValidCargoCode("AB-PET"));
    }

    @Test
    public void testRegex_TrainIDDigitLengthValidation() {
        assertFalse(TrainConsistManagementApp.isValidTrainID("TRN-123"));
        assertFalse(TrainConsistManagementApp.isValidTrainID("TRN-12345"));
    }

    @Test
    public void testRegex_CargoCodeUppercaseValidation() {
        assertFalse(TrainConsistManagementApp.isValidCargoCode("PET-Ab"));
    }

    @Test
    public void testSafety_AllBogiesValid() {
        java.util.List<TrainConsistManagementApp.GoodsBogie> bogies = java.util.Arrays.asList(
            new TrainConsistManagementApp.GoodsBogie("Cylindrical", "Petroleum"),
            new TrainConsistManagementApp.GoodsBogie("Cylindrical", "Petroleum")
        );
        assertTrue(TrainConsistManagementApp.checkSafetyCompliance(bogies));
    }

    @Test
    public void testSafety_CylindricalWithInvalidCargo() {
        java.util.List<TrainConsistManagementApp.GoodsBogie> bogies = java.util.Arrays.asList(
            new TrainConsistManagementApp.GoodsBogie("Cylindrical", "Coal")
        );
        assertFalse(TrainConsistManagementApp.checkSafetyCompliance(bogies));
    }

    @Test
    public void testSafety_NonCylindricalBogiesAllowed() {
        java.util.List<TrainConsistManagementApp.GoodsBogie> bogies = java.util.Arrays.asList(
            new TrainConsistManagementApp.GoodsBogie("Open", "Coal"),
            new TrainConsistManagementApp.GoodsBogie("Box", "Grain")
        );
        assertTrue(TrainConsistManagementApp.checkSafetyCompliance(bogies));
    }

    @Test
    public void testSafety_MixedBogiesWithViolation() {
        java.util.List<TrainConsistManagementApp.GoodsBogie> bogies = java.util.Arrays.asList(
            new TrainConsistManagementApp.GoodsBogie("Open", "Coal"),
            new TrainConsistManagementApp.GoodsBogie("Cylindrical", "Grain")
        );
        assertFalse(TrainConsistManagementApp.checkSafetyCompliance(bogies));
    }

    @Test
    public void testSafety_EmptyDataset() {
        java.util.List<TrainConsistManagementApp.GoodsBogie> bogies = new java.util.ArrayList<>();
        assertTrue(TrainConsistManagementApp.checkSafetyCompliance(bogies));
    }
}
