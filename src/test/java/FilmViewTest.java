import oppo.lab.first.controller.FilmController;
import oppo.lab.first.view.FilmView;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.mockito.Mockito.*;

public class FilmViewTest {
    @Test
    public void testRun() {
        FilmController mockController = mock(FilmController.class);
        Scanner mockScanner = mock(Scanner.class);

        when(mockScanner.nextInt()).thenReturn(1);
        when(mockScanner.next()).thenReturn("д");

        FilmView view = new FilmView(mockController, mockScanner);

        view.run();

        verify(mockController, times(1)).processFile(anyString());
    }
}
