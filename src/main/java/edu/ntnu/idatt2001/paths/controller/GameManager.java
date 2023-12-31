package edu.ntnu.idatt2001.paths.controller;

import com.google.gson.JsonSyntaxException;
import edu.ntnu.idatt2001.paths.model.filehandling.FileGameHandler;
import edu.ntnu.idatt2001.paths.model.filehandling.FilePathValidator;
import edu.ntnu.idatt2001.paths.model.goals.Goal;
import edu.ntnu.idatt2001.paths.model.Game;
import edu.ntnu.idatt2001.paths.model.Passage;
import edu.ntnu.idatt2001.paths.model.Player;
import edu.ntnu.idatt2001.paths.model.Story;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The GameManager class is responsible for managing the creation, deletion, and saving of Game
 * objects.
 *
 * @author Ramtin Samavat
 * @author Tobias Oftedal
 * @version 1.0
 * @since April 26, 2023.
 */
public class GameManager {

  private static GameManager instance = null;
  private final String pathOfFile;
  private final List<Game> games;

  /**
   * Constructor for the GameManager class.
   *
   * @param pathOfFile the path to the file for reading and writing Game objects.
   * @throws NullPointerException     if the pathOfFile or file extension is null.
   * @throws IllegalArgumentException if the pathOfFile is blank or has an incorrect file
   *                                  extension.
   * @throws IOException              if there is an error reading the list of games form the file.
   * @throws JsonSyntaxException      if the file does not have the correct JSON syntax.
   */
  private GameManager(String pathOfFile)
      throws NullPointerException, IllegalArgumentException, IOException, JsonSyntaxException {
    FilePathValidator.validatePathOfFile(pathOfFile, FileGameHandler.getFileExtension());
    this.pathOfFile = pathOfFile;
    this.games = new ArrayList<>();
    this.games.addAll(FileGameHandler.parseGamesFromFile(pathOfFile));
  }

  /**
   * The method initializes the GameManager with the given path of file. This method can only be
   * called once to ensure that GameManager is a singleton instance.
   *
   * @param pathOfFile the path to the file for reading and writing Game objects.
   * @return the initialized GameManager instance.
   * @throws NullPointerException     if the pathOfFile or FILE_EXTENSION is null.
   * @throws IllegalArgumentException if the pathOfFile is blank or has an incorrect file
   *                                  extension.
   * @throws IOException              if there is an error reading the list of games from the file.
   * @throws IllegalStateException    if the GameManager has already been initialized.
   */
  public static GameManager initialize(String pathOfFile)
      throws NullPointerException, IllegalArgumentException, IOException, IllegalStateException {
    if (instance != null) {
      throw new IllegalStateException("GameManager has already been initialized.");
    }
    instance = new GameManager(pathOfFile);
    return instance;
  }

  /**
   * Returns an instance of the GameManager class. The GameManager must be initialized using the
   * initialize() method before calling this method.
   *
   * @return the instance of the GameManager.
   * @throws IllegalStateException if the GameManager has not been initialized.
   */
  public static GameManager getInstance() throws IllegalStateException {
    if (instance == null) {
      throw new IllegalStateException("GameManager has not been initialized.");
    }
    return instance;
  }

  /**
   * The method creates a new Game object with the given player, story, and goals.
   *
   * @param player the player of the new game.
   * @param story  the story for the new game.
   * @param goals  the goals for the new game.
   * @return the new created game.
   * @throws IllegalStateException    if the maximum number of games is reached.
   * @throws IllegalArgumentException if a game with the same ID already exists.
   * @throws NullPointerException     if the gameId, player, story, or goals is null.
   */
  public Game createGame(String gameId, Player player, Story story, List<Goal> goals)
      throws IllegalArgumentException, NullPointerException {
    if (gameId == null) {
      throw new NullPointerException("Game ID cannot be null.");
    }
    if (games.stream().anyMatch(game -> game.getGameId().equals(gameId.trim()))) {
      throw new IllegalArgumentException("A game with the same ID already exists.");
    }
    if (player == null) {
      throw new NullPointerException("Player cannot be null.");
    }
    if (story == null) {
      throw new NullPointerException("Story cannot be null.");
    }
    if (goals == null) {
      throw new NullPointerException("Goals cannot be null.");
    }
    return new Game(gameId, player, story, goals);
  }

  /**
   * The method deletes the given game from the list of games and writes the updated list to the
   * file.
   *
   * @param game the game to delete.
   * @throws NullPointerException     if the game or pathOfFile is null.
   * @throws IllegalArgumentException if the pathOfFile is blank or has an incorrect file
   *                                  extension.
   * @throws IOException              if there is an error writing list of games to file.
   */
  public void deleteGame(Game game)
      throws NullPointerException, IllegalArgumentException, IOException {
    validateGame(game);
    if (games.remove(game)) {
      FileGameHandler.writeGamesToFile(games, pathOfFile);
    }
  }

  /**
   * The method saves the given game to the list of games and writes the updated list to the file.
   *
   * @param game the game to save.
   * @throws NullPointerException     if game, currentPassage, or pathOfFile is null.
   * @throws IllegalArgumentException if the pathOfFile is blank or has an incorrect file
   *                                  extension.
   * @throws IOException              if there is an error writing list of games to file.
   */
  public void saveGame(Game game, Passage currentPassage)
      throws NullPointerException, IOException, IllegalArgumentException {
    validateGame(game);
    if (currentPassage == null) {
      throw new NullPointerException("Current passage cannot be null.");
    }
    game.getStory().setCurrentPassage(currentPassage);
    if (games.contains(game)) {
      int index = games.indexOf(game);
      games.set(index, game);
    } else {
      games.add(game);
    }
    FileGameHandler.writeGamesToFile(games, pathOfFile);
  }

  /**
   * The method retrieves the list of games.
   *
   * @return the list og games.
   */
  public List<Game> getGames() {
    return games;
  }

  /**
   * The method validates the given game object.
   *
   * @throws NullPointerException if the game is null.
   */
  private void validateGame(Game game) throws NullPointerException {
    if (game == null) {
      throw new NullPointerException("Game cannot be null.");
    }
  }
}
