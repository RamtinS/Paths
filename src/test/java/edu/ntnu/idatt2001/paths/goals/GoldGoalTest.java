package edu.ntnu.idatt2001.paths.goals;

import edu.ntnu.idatt2001.paths.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The class tests the GoldGoal class.
 *
 * @author ...
 * @version JDK 17
 */
class GoldGoalTest {

  GoldGoal goldGoal;

  @BeforeEach
  void setUp() {
    goldGoal = new GoldGoal(10);
  }

  @Test
  @DisplayName("Test constructor valid input")
  void testConstructorValidInput() {
    int minimumGold = 5;
    GoldGoal testGoldGoalConstructor = new GoldGoal(minimumGold);
    assertEquals(minimumGold, testGoldGoalConstructor.getMinimumGold());
  }

  @Test
  @DisplayName("Test constructor invalid input throws IllegalArgumentException")
  void testConstructorInvalidInputThrowsIllegalArgumentException() {
    int invalidMinimumGold = -5;
    assertThrows(IllegalArgumentException.class, () -> new GoldGoal(invalidMinimumGold));
  }

  @Test
  @DisplayName("Goal is fulfilled")
  void goalIsFulfilled() {
    Player player = new Player("Test name", 100, 100, 15);
    assertTrue(goldGoal.isFulfilled(player));
  }

  @Test
  @DisplayName("Goal is not fulfilled")
  void goalIsNotFulfilled() {
    Player player = new Player("Test name", 100, 100, 5);
    assertFalse(goldGoal.isFulfilled(player));
  }

  @Test
  @DisplayName("Goal is not fulfilled throws NullPointerException")
  void goalIsNotFulfilledThrowsNullPointerException() {
    Player player = null;
    assertThrows(NullPointerException.class, () -> goldGoal.isFulfilled(player));
  }
}