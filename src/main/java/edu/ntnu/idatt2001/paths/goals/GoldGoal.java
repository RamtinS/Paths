package edu.ntnu.idatt2001.paths.goals;

import edu.ntnu.idatt2001.paths.Player;

import java.util.Objects;

/**
 * The class represents an expected minimum gold holding.
 *
 * @author ...
 * @version JDK 17
 */
public class GoldGoal implements Goal {
  private final int minimumGold;

  /**
   * Constructor to create an object of the type GoldGoal.
   *
   * @param minimumGold a minimum gold holding.
   * @throws IllegalArgumentException if minimumGold is less than zero.
   */
  public GoldGoal(int minimumGold) throws IllegalArgumentException {
    if (minimumGold < 0) throw new IllegalArgumentException("\nMinimum gold cannot be less than zero.");
    this.minimumGold = minimumGold;
  }

  /**
   * The method checks if the minimum gold goal is achieved.
   *
   * @param player the player assigned to the goal.
   * @return true or false depending on whether the goal is achieved.
   * @throws NullPointerException if the player is null.
   */
  @Override
  public boolean isFulfilled(Player player) throws NullPointerException {
    Objects.requireNonNull(player, "\nPlayer cannot be null.");
    return player.getGold() > this.minimumGold;
  }
}