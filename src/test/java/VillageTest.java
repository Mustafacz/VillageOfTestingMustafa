import org.example.Village;
import org.example.objects.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class VillageTest {
    private Village village;

    @BeforeEach
    public void setUp() {
        village = new Village();
    }

    @Test
    void villageConstructor() {
        assertAll("villageConstructor",
                () -> assertFalse(village.isGameOver(), "gameOver should be false"),
                () -> assertEquals(4, village.getOccupationHashMap().size(), "Incorrect number of occupations"),
                () -> assertEquals(5, village.getPossibleProjects().size(), "Incorrect number of possible projects"),
                () -> assertEquals(3, village.getBuildings().size(), "Incorrect number of buildings"),
                () -> assertEquals(6, village.getMaxWorkers(), "Incorrect number of max workers"),
                () -> assertEquals(10, village.getFood(), "Incorrect amount of food")
        );
    }


    //Test Day()

    @Test
    void DayTest_OneDayHasPassedWithOneWorker() {

        village.AddWorker("Musse", "miner");
        village.Day();

        assertEquals(1, village.getDaysGone());
        assertEquals(9, village.getFood());
    }

    @Test
    void DayTest_OneDayHasPassedWithTwoWorkers() {

        village.AddWorker("Musse", "miner");
        village.AddWorker("Fille", "miner");
        village.Day();

        assertEquals(1, village.getDaysGone());
        assertEquals(8, village.getFood());
    }

    @Test
    void DayTest_OneDayHasPassedWithTwoWorkersAnd0Food() {

        village.AddWorker("Musse", "miner");
        village.AddWorker("Fille", "miner");
        village.setFood(0);
        village.Day();

        assertEquals(1, village.getDaysGone());
        assertEquals(0, village.getFood());
    }

    @ParameterizedTest(name = "So many days passed={0}, expected food left={1}")
    @CsvSource(value = {"1, 8", "2, 6", "3, 4", "4, 2", "5, 0", "6, 0", "7, 0"})
    void DayTest_NoFoodLeftAfter5Days(int days, int expectedFoodLeft) {

        village.AddWorker("Musse", "miner");
        village.AddWorker("Fille", "miner");
        for (int i = 0; i < days; i++) {
            village.Day();
        }

        assertEquals(expectedFoodLeft, village.getFood());
    }

    @ParameterizedTest(name = "So many days passed={0}, expected food left={1}")
    @CsvSource(value = {"10, 0"})
    void DayTest_AfterStarvingFiveDays(int days, int expectedFoodLeft) {

        village.AddWorker("Musse", "miner");
        village.AddWorker("Fille", "miner");
        for (int i = 0; i < days; i++) {
            village.Day();
        }

        assertEquals(expectedFoodLeft, village.getFood());
        assertFalse(village.getWorkers().get(0).isAlive()); // Ahmad
        assertFalse(village.getWorkers().get(1).isAlive()); // Niklas
    }



    //Test GameOver()
    @Test
    void gameOverTest(){

        village.GameOver();
        assertTrue(village.isGameOver());
    }
    @Test
    void gameOverTestWithoutCallingGameOver(){

        assertFalse(village.isGameOver());
    }
    //Test isFull()
    @Test
    void isFullTestEmptyWorkersList(){

        village.setWorkers(new ArrayList<Worker>());
        assertFalse(village.isFull());
    }



    @Test
    void testAddWorker() {

        Village village = new Village();
        village.AddWorker("Musse", "farmer");

        assertEquals("farmer", village.getWorkers().get(0).getOccupation());
        assertEquals("Musse", village.getWorkers().get(0).getName());
    }


    @Test
    void testAddWorkerUntilFull(){

        for (int i = 0; i < 7; i++){
            village.AddWorker("Musse", "farmer");
        }

        assertEquals(6, village.getWorkers().size());
    }

    //Test AddProject()
    @ParameterizedTest(name = "Wood amount={0}, Metal amount={1}, Wood left={2}, Metal left={3}, Name of project={4}")
    @CsvSource(value = {
            "51, 0, 46, 0, House",
            "5, 5, 5, 5, Hello",
            "4, 5, 4, 5, House",
            "5, 1, 0, 0, Woodmill",
            "6, 2, 1, 1, Woodmill",
            "20, 2, 15, 1, Woodmill",
            "3, 5, 0, 0, Quarry",
            "4, 5, 1, 0, Quarry",
            "2, 5, 2, 5, Quarry",
            "5, 2, 0, 0, Farm",
            "6, 2, 1, 0, Farm",
            "6, 3, 1, 1, Farm",
            "50, 50, 0, 0, Castle",
            "50, 49, 50, 49, Castle",
            "51, 51, 1, 1, Castle"
    })
    void AddProjectTest(int woodAmount, int metalAmount, int expectedWoodLeft, int expectedMetalLeft, String nameOfProject) {
        Village village = new Village();
        village.setWood(woodAmount);
        village.setMetal(metalAmount);

        village.AddProject(nameOfProject);

        assertEquals(expectedWoodLeft, village.getWood());
        assertEquals(expectedMetalLeft, village.getMetal());
    }


    //Testa AddFood()
    @ParameterizedTest(name = "days passed={0}, expected food amount={1}")
    @CsvSource(value = {"1 ,14", "2, 18", "3, 22", "4, 26"})
    void AddFoodTestOneFarmer(int daysPassed, int expectedFoodLeft){
        village.AddWorker("Musse", "farmer");
        for (int i = 0; i < daysPassed; i++){
            village.Day();
        }

        int actualFoodLeft = village.getFood();
        assertEquals(expectedFoodLeft, actualFoodLeft);
    }
    @ParameterizedTest(name = "days passed={0}, expected food amount={1}")
    @CsvSource(value = {"1 ,13", "2, 16", "3, 19", "4, 22"})
    void AddFoodTestOneFarmerOneMiner(int daysPassed, int expectedFoodLeft){
        village.AddWorker("Musse", "farmer");
        village.AddWorker("Fille", "miner");
        for (int i = 0; i < daysPassed; i++){
            village.Day();
        }

        int actualFoodLeft = village.getFood();
        assertEquals(expectedFoodLeft, actualFoodLeft);
    }

    @Test
    void AddFoodTest(){

        village.AddFood("Musse");
        assertEquals(15, village.getFood());
    }
    //Test AddMetal()
    @Test
    void AddOneMetalTest(){

        village.AddMetal("Musse");
        assertEquals(1, village.getMetal());
    }
    //Test AddWood()
    @Test
    void AddOneWoodTest(){

        village.AddWood("Musse");
        assertEquals(1, village.getWood());
    }

    //Test Build()
    @ParameterizedTest
    @CsvSource(value = {"Musse, House, 2", "Fille, Woodmill, 4", "Sandra, Quarry, 6", "Oskar, Farm, 4", "Tindra, Castle, 49"})
    void BuildTestAllAvalibleProjects(String name, String project, int expectedDaysLeft) {

        village.setWood(100);
        village.setMetal(100);
        village.AddProject(project);
        village.Build(name);
        int actualDaysLeft = village.getProjects().get(0).getDaysLeft();

        assertEquals(expectedDaysLeft, actualDaysLeft);
    }

    @ParameterizedTest
    @CsvSource(value = {"Musse, House", "Fille, Woodmill"})
    void BuildTestWithNoMaterials(String name, String project) {

        village.AddProject(project);
        village.Build(name);
        boolean actualProjectExisting = village.getProjects().isEmpty();

        assertTrue(actualProjectExisting);
    }

    @Test
    void winTheGame() {

        village.AddWorker("Musse", "farmer");
        village.AddWorker("Fille", "builder");
        village.AddWorker("Alle", "lumberjack");
        village.AddWorker("Kalle", "miner");

        for (int i = 0; i < 100; i++) {
            village.Day();
        }
        village.AddProject("Castle");
        for (int i = 0; i < 100; i++) {
            village.Day();
        }

        assertTrue(village.isGameOver());
    }



}

