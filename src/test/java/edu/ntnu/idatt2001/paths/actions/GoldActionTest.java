package edu.ntnu.idatt2001.paths.actions;

import edu.ntnu.idatt2001.paths.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The class tests the GoldAction class
 *
 * @author  ...
 * @version JDK 17
 */
class GoldActionTest {

  GoldAction goldAction;
  Player player;

  @BeforeEach
  void setUp() {
    goldAction = new GoldAction(10);
    player = new Player("Name", 20, 30, 40);
  }

  @Test
  @DisplayName("Test constructor valid input")
  void testConstructorValidInput(){
    GoldAction testGoldActionConstructor = new GoldAction(10);
    int expected = 10;
    int actual = testGoldActionConstructor.getGold();
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Should get gold")
  void shouldGetGold(){
    int expectedGold = 40;
    int actualGold = player.getGold();
    assertEquals(expectedGold, actualGold);
  }

  @Test
  @DisplayName("Should execute and add gold")
  void shouldExecuteAndAddGold() {
    goldAction.execute(player);
    int expectedAmount = 50;
    int actualAmount = player.getGold();
    assertEquals(expectedAmount, actualAmount);
  }

  @Test
  @DisplayName("Should execute and remove gold")
  void shouldExecuteAndRemoveGold() {
    GoldAction negativeGoldAction = new GoldAction(-10);
    negativeGoldAction.execute(player);
    int expectedAmount = 30;
    int actualAmount = player.getGold();
    assertEquals(expectedAmount, actualAmount);
  }

  @Test
  @DisplayName("Should not execute throws NullPointerException")
  void shouldNotExecuteThrowsNullPointerException() {
    Player invalidPlayer = null;
    assertThrows(NullPointerException.class, () -> goldAction.execute(invalidPlayer));
  }
}