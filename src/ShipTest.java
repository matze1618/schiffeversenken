import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    @Test
    @DisplayName("Set periphery for horizontal ship in center of field")
    public void testCentralHorizontal(){
        Field field = new Field();
        Ship ship = new Ship(4, 4, true, 4, false, field);
        ship.setAllPositions();
        ship.setPeriphery(field);
        assertEquals(4, ship.getAllPositions().size());
        assertEquals(10, ship.getPeriphery().size());
    }

    @Test
    @DisplayName("Set periphery for vertical ship in center of field")
    public void testCentralVertical(){
        Field field = new Field();
        Ship ship = new Ship(4, 4, false, 4, false, field);
        ship.setAllPositions();
        ship.setPeriphery(field);
        assertEquals(4, ship.getAllPositions().size());
        assertEquals(10, ship.getPeriphery().size());
    }
}