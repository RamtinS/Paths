package edu.ntnu.idatt2001.paths.model.actions;

import edu.ntnu.idatt2001.paths.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The class tests the ScoreAction class.
 *
 * @author Ramtin Samavat and Tobias Oftedal.
 * @version 1.0
 * @since March 29, 2023.
 */
class ScoreActionTest {
  private ScoreAction scoreAction;
  private Player player;

  @BeforeEach
  void setUp() {
    scoreAction = new ScoreAction(10);
    player = new Player.PlayerBuilder("Name")
            .score(20)
            .build();
  }

  @Test
  @DisplayName("Test constructor valid input")
  void testConstructorValidInput() {
    ScoreAction scoreActionTest = new ScoreAction(10);
    int expected = 10;
    int actual = scoreActionTest.getPoints();
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Should get points")
  void shouldGetPoints() {
    int expectedPoints = 10;
    int actualPoints = scoreAction.getPoints();
    assertEquals(expectedPoints, actualPoints);
  }

  @Test
  @DisplayName("Should execute increase score")
  void shouldExecuteIncreaseScore() {
    scoreAction.execute(player);
    int expectedScore = 30;
    int actualScore = player.getScore();
    assertEquals(expectedScore, actualScore);
  }

  @Test
  @DisplayName("Should execute decrease score")
  void shouldExecuteDecreaseScore() {
    ScoreAction negativeScoreAction = new ScoreAction(-10);
    negativeScoreAction.execute(player);
    int expectedScore = 10;
    int actualScore = player.getScore();
    assertEquals(expectedScore, actualScore);
  }

  @Test
  @DisplayName("Should not execute throws NullPointerException")
  void shouldNotExecuteThrowsNullPointerException() {
    Player invalidPlayer = null;
    assertThrows(NullPointerException.class, () -> scoreAction.execute(invalidPlayer));
  }

  @Test
  @DisplayName("Test toString")
  void testToString() {
    String expected = "{Score:10}";
    String actual = scoreAction.toString();
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Test equals method")
  void testEqualsMethod() {
    ScoreAction scoreActionEqual = new ScoreAction(10);
    assertEquals(scoreAction, scoreActionEqual);
  }
}