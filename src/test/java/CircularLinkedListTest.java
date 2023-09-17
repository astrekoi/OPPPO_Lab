import oppo.lab.first.common.CircularLinkedList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CircularLinkedListTest {
    @Test
    public void testAddAndRemove() {
        CircularLinkedList<Integer> list = new CircularLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        assertEquals(3, list.size());
        assertEquals(1, list.remove());
        assertEquals(2, list.size());
    }

    @Test
    public void testRemoveFromEmptyList() {
        CircularLinkedList<Integer> list = new CircularLinkedList<>();
        assertNull(list.remove());
    }
}
