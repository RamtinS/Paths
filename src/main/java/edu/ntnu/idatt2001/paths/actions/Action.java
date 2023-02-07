package edu.ntnu.idatt2001.paths.actions;

import edu.ntnu.idatt2001.paths.Player;

/**
 * The interface represents an action, which
 * represents a future change in the state of a player.
 *
 * @author ...
 * @version JDK 17
 */
public interface Action {

  /**
   * The method changes the state of the player.
   *
   * @param player the player which is affected by the action.
   */
  void execute(Player player);
}