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

    @Test
    public void testLoopFilteringLogic() throws InvalidCapacityException {
        java.util.List<Bogie> bogies = java.util.Arrays.asList(
            new Bogie("Sleeper", 72),
            new Bogie("General", 60),
            new Bogie("First Class", 24)
        );
        java.util.List<Bogie> filtered = TrainConsistManagementApp.loopBasedFiltering(bogies);
        assertEquals(1, filtered.size());
        assertEquals("Sleeper", filtered.get(0).name);
    }

    @Test
    public void testStreamFilteringLogic() throws InvalidCapacityException {
        java.util.List<Bogie> bogies = java.util.Arrays.asList(
            new Bogie("Sleeper", 72),
            new Bogie("General", 60),
            new Bogie("First Class", 24)
        );
        java.util.List<Bogie> filtered = TrainConsistManagementApp.streamBasedFiltering(bogies);
        assertEquals(1, filtered.size());
        assertEquals("Sleeper", filtered.get(0).name);
    }

    @Test
    public void testLoopAndStreamResultsMatch() throws InvalidCapacityException {
        java.util.List<Bogie> bogies = java.util.Arrays.asList(
            new Bogie("Sleeper", 72),
            new Bogie("General", 60),
            new Bogie("AC", 64),
            new Bogie("First Class", 24)
        );
        java.util.List<Bogie> loopResult = TrainConsistManagementApp.loopBasedFiltering(bogies);
        java.util.List<Bogie> streamResult = TrainConsistManagementApp.streamBasedFiltering(bogies);
        assertEquals(loopResult.size(), streamResult.size());
    }

    @Test
    public void testExecutionTimeMeasurement() throws InvalidCapacityException {
        java.util.List<Bogie> bogies = java.util.Arrays.asList(
            new Bogie("Sleeper", 72),
            new Bogie("General", 60)
        );
        long start = System.nanoTime();
        TrainConsistManagementApp.loopBasedFiltering(bogies);
        long end = System.nanoTime();
        long elapsed = end - start;
        assertTrue(elapsed > 0);
    }

    @Test
    public void testLargeDatasetProcessing() throws InvalidCapacityException {
        java.util.List<Bogie> bogies = new java.util.ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            bogies.add(new Bogie("Bogie" + i, (i % 100) + 1));
        }
        long start = System.nanoTime();
        java.util.List<Bogie> filtered = TrainConsistManagementApp.streamBasedFiltering(bogies);
        long end = System.nanoTime();
        assertTrue(filtered.size() > 0);
        assertTrue((end - start) > 0);
    }

    @Test
    public void testException_ValidCapacityCreation() throws InvalidCapacityException {
        Bogie bogie = new Bogie("Sleeper", 72);
        assertNotNull(bogie);
    }

    @Test
    public void testException_NegativeCapacityThrowsException() {
        assertThrows(InvalidCapacityException.class, () -> new Bogie("Sleeper", -10));
    }

    @Test
    public void testException_ZeroCapacityThrowsException() {
        assertThrows(InvalidCapacityException.class, () -> new Bogie("Sleeper", 0));
    }

    @Test
    public void testException_ExceptionMessageValidation() {
        InvalidCapacityException exception = assertThrows(InvalidCapacityException.class, () -> new Bogie("Sleeper", 0));
        assertEquals("Capacity must be greater than zero", exception.getMessage());
    }

    @Test
    public void testException_ObjectIntegrityAfterCreation() throws InvalidCapacityException {
        Bogie bogie = new Bogie("Sleeper", 72);
        assertEquals("Sleeper", bogie.name);
        assertEquals(72, bogie.capacity);
    }

    @Test
    public void testException_MultipleValidBogiesCreation() throws InvalidCapacityException {
        Bogie b1 = new Bogie("Sleeper", 72);
        Bogie b2 = new Bogie("General", 60);
        assertNotNull(b1);
        assertNotNull(b2);
    }
}
