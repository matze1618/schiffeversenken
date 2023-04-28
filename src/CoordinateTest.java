import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {

    @Test
    @DisplayName("Valid Coordinate")
    void testValid() {
        Coordinate coordinate = new Coordinate( 4, 4);
        assertTrue(coordinate.isValid(10));
    }

    @Test
    @DisplayName("Valid Coordinate top")
    void testValidTop() {
        Coordinate coordinate = new Coordinate( 4, 0);
        assertTrue(coordinate.isValid(10));
    }

    @Test
    @DisplayName("Valid Coordinate right")
    void testValidRight() {
        Coordinate coordinate = new Coordinate( 9, 2);
        assertTrue(coordinate.isValid(10));
    }

    @Test
    @DisplayName("Valid Coordinate bottom")
    void testValidBottom() {
        Coordinate coordinate = new Coordinate( 3, 9);
        assertTrue(coordinate.isValid(10));
    }

    @Test
    @DisplayName("Valid Coordinate left")
    void testValidLeft() {
        Coordinate coordinate = new Coordinate( 0, 6);
        assertTrue(coordinate.isValid(10));
    }

    @Test
    @DisplayName("Valid Coordinate corner top left")
    void testValidTopLeft() {
        Coordinate coordinate = new Coordinate( 0, 0);
        assertTrue(coordinate.isValid(10));
    }

    @Test
    @DisplayName("Valid Coordinate corner top right")
    void testValidTopRight() {
        Coordinate coordinate = new Coordinate( 9, 0);
        assertTrue(coordinate.isValid(10));
    }

    @Test
    @DisplayName("Valid Coordinate corner bottom left")
    void testValidBottomLeft() {
        Coordinate coordinate = new Coordinate( 0, 9);
        assertTrue(coordinate.isValid(10));
    }

    @Test
    @DisplayName("Valid Coordinate corner bottom right")
    void testValidBottomRight() {
        Coordinate coordinate = new Coordinate( 9, 9);
        assertTrue(coordinate.isValid(10));
    }
}