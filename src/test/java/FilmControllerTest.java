import oppo.lab.first.common.CircularLinkedList;
import oppo.lab.first.controller.FilmController;
import oppo.lab.first.exceptions.InvalidAnimationTypeException;
import oppo.lab.first.model.Film;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class FilmControllerTest {
    @Test
    public void testMockProcessFile() throws IOException {
        CircularLinkedList<Film> mockFilms = mock(CircularLinkedList.class);
        BufferedReader mockReader = mock(BufferedReader.class);
        FilmController controller = new FilmController(mockFilms);

        String currentDir = System.getProperty("user.dir");
        BufferedReader realReader = new BufferedReader(new FileReader(currentDir + "\\src\\test\\java\\test.txt"));

        when(mockReader.readLine()).thenAnswer(invocation -> realReader.readLine());

        controller.setReader(mockReader);

        File directory = new File(currentDir + "\\src\\test\\java\\");
        if (!directory.exists()){
            directory.mkdirs();
        }

        controller.setWorkPath("\\src\\test\\java\\");
        controller.processFile("test.txt");

        verify(mockFilms, atLeastOnce()).add(any(Film.class));
    }

    @Test
    public void testIntegrationProcessFile() throws IOException {
        CircularLinkedList<Film> films = new CircularLinkedList<>();
        FilmController controller = new FilmController(films);

        String currentDir = System.getProperty("user.dir");
        BufferedReader reader = new BufferedReader(new FileReader(currentDir + "\\src\\test\\java\\test.txt"));
        controller.setReader(reader);

        controller.setWorkPath("\\src\\test\\java\\");
        controller.processFile("test.txt");

        String outputPath = currentDir + "\\src\\test\\java\\output\\test.txt";
        assertTrue(Files.exists(Paths.get(outputPath)), "Output file does not exist");

        String outputContent = new String(Files.readAllBytes(Paths.get(outputPath)));
        assertTrue(outputContent.contains("FeatureFilm{name='Тест Уыв', directorName='Артем Куров'}"), "Output file does not contain expected content");
        assertTrue(outputContent.contains("TVSeries{name='Богема', directorName='Контроль Вильсов', numberOfEpisodes=5}"), "Output file does not contain expected content");
    }

    @Test
    public void testAddFilm() throws InvalidAnimationTypeException {
        CircularLinkedList<Film> films = new CircularLinkedList<>();
        FilmController controller = new FilmController(films);

        controller.addFilm("Игровой фильм, Тест Уыв, Артем Куров");

        assertFalse(films.size() == 0, "Films list should not be empty after adding a film");
    }

    @Test
    public void testRemoveFilms() {
        CircularLinkedList<Film> films = new CircularLinkedList<>();
        FilmController controller = new FilmController(films);

        controller.addFilm("Игровой фильм, Тест Уыв, Артем Куров");
        controller.removeFilms("название has Уыв");

        assertTrue(films.size() == 0, "Films list should be empty after removing the film");
    }
}
