package edu.ntnu.idatt2001.paths.goals;

import edu.ntnu.idatt2001.paths.Player;

import java.util.Objects;

/**
 * The class represents an expected minimum health value.
 *
 * @author ...
 * @version JDK 17
 */
public class HealthGoal implements Goal {
  private final int minimumHealth;

  /**
   * Constructor to create an object of the type HealthGoal.
   *
   * @param minimumHealth minimum health value.
   * @throws IllegalArgumentException if minimumHealth is less than zero.
   */
  public HealthGoal(int minimumHealth) throws IllegalArgumentException{
    if (minimumHealth < 0) throw new IllegalArgumentException("\nMinimum health cannot be less than zero.");
    this.minimumHealth = minimumHealth;
  }

  /**
   * The method checks if the minimum health value is achieved.
   *
   * @param player the player assigned to the goal.
   * @return true or false depending on whether the goal is achieved.
   * @throws NullPointerException if the player is null.
   */
  @Override
  public boolean isFulfilled(Player player) throws NullPointerException {
    Objects.requireNonNull(player, "\nPlayer cannot be null.");
    return player.getHealth() > this.minimumHealth;
  }
}