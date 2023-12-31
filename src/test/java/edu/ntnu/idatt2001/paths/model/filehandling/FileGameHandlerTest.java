package edu.ntnu.idatt2001.paths.model.filehandling;

import com.google.gson.JsonSyntaxException;
import edu.ntnu.idatt2001.paths.model.Game;
import edu.ntnu.idatt2001.paths.model.Link;
import edu.ntnu.idatt2001.paths.model.Passage;
import edu.ntnu.idatt2001.paths.model.Player;
import edu.ntnu.idatt2001.paths.model.Story;
import edu.ntnu.idatt2001.paths.model.actions.Action;
import edu.ntnu.idatt2001.paths.model.actions.GoldAction;
import edu.ntnu.idatt2001.paths.model.actions.HealthAction;
import edu.ntnu.idatt2001.paths.model.actions.InventoryAction;
import edu.ntnu.idatt2001.paths.model.actions.ScoreAction;
import edu.ntnu.idatt2001.paths.model.goals.Goal;
import edu.ntnu.idatt2001.paths.model.goals.GoldGoal;
import edu.ntnu.idatt2001.paths.model.goals.HealthGoal;
import edu.ntnu.idatt2001.paths.model.goals.InventoryGoal;
import edu.ntnu.idatt2001.paths.model.goals.ScoreGoal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The class tests the FileGameHandler class.
 *
 * @author Ramtin Samavat and Tobias Oftedal.
 * @version 1.0
 * @since May 6, 2023.
 */
class FileGameHandlerTest {

  private static final Logger logger = Logger.getLogger(FileGameHandlerTest.class.getName());
  private String pathOfFile;
  private File gamesFile;
  private List<Game> games;
  private Game game1;
  private Game game2;

  @BeforeEach
  void setUp() {
    pathOfFile = "src/test/resources/games/game_objects.json";
    gamesFile = new File(pathOfFile);

    String gameId1 = "Test ID 1";
    String gameId2 = "Test ID 2";

    Player player1 = new Player.PlayerBuilder("Player1")
            .health(50)
            .score(100)
            .gold(50)
            .build();

    Player player2 = new Player.PlayerBuilder("Player2")
            .health(60)
            .score(100)
            .gold(40)
            .build();

    Action actionOpeningPassage = new InventoryAction("Sword");
    Link linkOpeningPassage = new Link("Try to open the door", "Another room");
    Passage openingPassage = new Passage("Beginnings", "There is a door in front of you.");
    linkOpeningPassage.addAction(actionOpeningPassage);
    openingPassage.addLink(linkOpeningPassage);

    Action action1Link1Passage1 = new ScoreAction(10);
    Action action2Link1Passage1 = new HealthAction(10);
    Action action1Link2Passage1 = new ScoreAction(-10);
    Action action2Link2Passage1 = new HealthAction(-10);
    Link link1Passage1 = new Link("Open the book", "The book of spells");
    Link link2Passage1 = new Link("Go back", "Beginnings");
    Passage passage1 = new Passage("Another room",
            "The door opens to another room. You see a desk with a large, dusty book.");
    link1Passage1.addAction(action1Link1Passage1);
    link1Passage1.addAction(action2Link1Passage1);
    link2Passage1.addAction(action1Link2Passage1);
    link2Passage1.addAction(action2Link2Passage1);
    passage1.addLink(link1Passage1);
    passage1.addLink(link2Passage1);

    Action action1Link1Passage2 = new GoldAction(10);
    Action action2Link2Passage2 = new GoldAction(-10);
    Link link1Passage2 = new Link("Cast the spell", "Forest");
    Link link2Passage2 = new Link("Go back", "Another room");
    Passage passage2 = new Passage("The book of spells",
            "You open the book and find the spell of teleportation.");
    link1Passage2.addAction(action1Link1Passage2);
    link2Passage2.addAction(action2Link2Passage2);
    passage2.addLink(link1Passage2);
    passage2.addLink(link2Passage2);

    Story story = new Story("Haunted House", openingPassage);
    story.addPassage(passage1);
    story.addPassage(passage2);

    List<Goal> goals1 = new ArrayList<>();
    goals1.add(new ScoreGoal(10));
    goals1.add(new HealthGoal(70));
    goals1.add(new GoldGoal(100));
    List<String> inventoryGoal1 = new ArrayList<>();
    inventoryGoal1.add("Sword");
    goals1.add(new InventoryGoal(inventoryGoal1));

    List<Goal> goals2 = new ArrayList<>();
    goals2.add(new ScoreGoal(100));
    goals2.add(new HealthGoal(50));
    goals2.add(new GoldGoal(10));
    List<String> inventoryGoal2 = new ArrayList<>();
    inventoryGoal2.add("Sword");
    goals2.add(new InventoryGoal(inventoryGoal2));

    game1 = new Game(gameId1, player1, story, goals1);
    game2 = new Game(gameId2, player2, story, goals2);

    games = new ArrayList<>();
    games.add(game1);
    games.add(game2);
  }

  @AfterEach
  void tearDown() {
    Path pathGames = Paths.get(gamesFile.getPath());
    try {
      Files.deleteIfExists(pathGames);
    } catch (IOException e) {
      logger.log(Level.WARNING, "Error deleting file.", e);
    }
  }

  @Nested
  @DisplayName("Positive tests file handling")
  class PositiveTestsFileHandling {
    @Test
    @DisplayName("Should write games to file")
    void shouldWriteGamesToFile() {
      try {
        FileGameHandler.writeGamesToFile(games, pathOfFile);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      try (BufferedReader reader = new BufferedReader(new FileReader(pathOfFile))) {
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          content.append(line).append("\n");
        }

        String expected = """
                [
                  {
                    "game ID": "Test ID 1",
                    "player": {
                      "name": "Player1",
                      "startHealth": 50,
                      "startScore": 100,
                      "startGold": 50,
                      "health": 50,
                      "score": 100,
                      "gold": 50,
                      "inventory": []
                    },
                    "story title": "Haunted House",
                    "story opening passage": {
                      "title": "Beginnings",
                      "content": "There is a door in front of you.",
                      "links": [
                        {
                          "text": "Try to open the door",
                          "reference": "Another room",
                          "actions": [
                            {
                              "item": "Sword"
                            }
                          ]
                        }
                      ]
                    },
                    "story current passage": {
                      "title": "Beginnings",
                      "content": "There is a door in front of you.",
                      "links": [
                        {
                          "text": "Try to open the door",
                          "reference": "Another room",
                          "actions": [
                            {
                              "item": "Sword"
                            }
                          ]
                        }
                      ]
                    },
                    "story passages": [
                      {
                        "title": "Another room",
                        "content": "The door opens to another room. You see a desk with a large, dusty book.",
                        "links": [
                          {
                            "text": "Open the book",
                            "reference": "The book of spells",
                            "actions": [
                              {
                                "points": 10
                              },
                              {
                                "health": 10
                              }
                            ]
                          },
                          {
                            "text": "Go back",
                            "reference": "Beginnings",
                            "actions": [
                              {
                                "points": -10
                              },
                              {
                                "health": -10
                              }
                            ]
                          }
                        ]
                      },
                      {
                        "title": "The book of spells",
                        "content": "You open the book and find the spell of teleportation.",
                        "links": [
                          {
                            "text": "Cast the spell",
                            "reference": "Forest",
                            "actions": [
                              {
                                "gold": 10
                              }
                            ]
                          },
                          {
                            "text": "Go back",
                            "reference": "Another room",
                            "actions": [
                              {
                                "gold": -10
                              }
                            ]
                          }
                        ]
                      }
                    ],
                    "goals": [
                      {
                        "minimumPoints": 10
                      },
                      {
                        "minimumHealth": 70
                      },
                      {
                        "minimumGold": 100
                      },
                      {
                        "mandatoryItems": [
                          "Sword"
                        ]
                      }
                    ]
                  },
                  {
                    "game ID": "Test ID 2",
                    "player": {
                      "name": "Player2",
                      "startHealth": 60,
                      "startScore": 100,
                      "startGold": 40,
                      "health": 60,
                      "score": 100,
                      "gold": 40,
                      "inventory": []
                    },
                    "story title": "Haunted House",
                    "story opening passage": {
                      "title": "Beginnings",
                      "content": "There is a door in front of you.",
                      "links": [
                        {
                          "text": "Try to open the door",
                          "reference": "Another room",
                          "actions": [
                            {
                              "item": "Sword"
                            }
                          ]
                        }
                      ]
                    },
                    "story current passage": {
                      "title": "Beginnings",
                      "content": "There is a door in front of you.",
                      "links": [
                        {
                          "text": "Try to open the door",
                          "reference": "Another room",
                          "actions": [
                            {
                              "item": "Sword"
                            }
                          ]
                        }
                      ]
                    },
                    "story passages": [
                      {
                        "title": "Another room",
                        "content": "The door opens to another room. You see a desk with a large, dusty book.",
                        "links": [
                          {
                            "text": "Open the book",
                            "reference": "The book of spells",
                            "actions": [
                              {
                                "points": 10
                              },
                              {
                                "health": 10
                              }
                            ]
                          },
                          {
                            "text": "Go back",
                            "reference": "Beginnings",
                            "actions": [
                              {
                                "points": -10
                              },
                              {
                                "health": -10
                              }
                            ]
                          }
                        ]
                      },
                      {
                        "title": "The book of spells",
                        "content": "You open the book and find the spell of teleportation.",
                        "links": [
                          {
                            "text": "Cast the spell",
                            "reference": "Forest",
                            "actions": [
                              {
                                "gold": 10
                              }
                            ]
                          },
                          {
                            "text": "Go back",
                            "reference": "Another room",
                            "actions": [
                              {
                                "gold": -10
                              }
                            ]
                          }
                        ]
                      }
                    ],
                    "goals": [
                      {
                        "minimumPoints": 100
                      },
                      {
                        "minimumHealth": 50
                      },
                      {
                        "minimumGold": 10
                      },
                      {
                        "mandatoryItems": [
                          "Sword"
                        ]
                      }
                    ]
                  }
                ]
                """;

        assertEquals(expected, content.toString());
      } catch (IOException e) {
        logger.log(Level.WARNING, e.getMessage(), e);
      }
    }

    @Test
    @DisplayName("Should read existing games from file")
    void shouldReadExistingGamesFromFile() {
      try {
        FileGameHandler.writeGamesToFile(games, pathOfFile);
      } catch (IOException e) {
        logger.log(Level.WARNING, e.getMessage(), e);
      }
      List<Game> gamesReadFromFile = new ArrayList<>();
      try {
        gamesReadFromFile.addAll(FileGameHandler.parseGamesFromFile(pathOfFile));
      } catch (IOException | JsonSyntaxException e) {
        logger.log(Level.WARNING, e.getMessage(), e);
      }

      assertEquals(games.size(), gamesReadFromFile.size());

      for (int i = 0; i < games.size(); i++) {
        Game originalGame = games.get(i);
        Game readGame = gamesReadFromFile.get(i);

        assertEquals(originalGame.getPlayer(), readGame.getPlayer());

        assertEquals(originalGame.getStory().getTitle(), readGame.getStory().getTitle());
        assertEquals(originalGame.getStory().getOpeningPassage(), readGame.getStory().getOpeningPassage());
        assertTrue(readGame.getStory().getPassages().containsAll(originalGame.getStory().getPassages()));

        assertEquals(originalGame.getGoals().size(), readGame.getGoals().size());
        assertTrue(readGame.getGoals().containsAll(originalGame.getGoals()));
      }
    }

    @Test
    @DisplayName("Should read empty list of games from file")
    void shouldReadEmptyListOfGamesFromFile() {
      List<Game> nonExistingGames = new ArrayList<>();
      try {
        FileGameHandler.writeGamesToFile(nonExistingGames, pathOfFile);
      } catch (IOException e) {
        logger.log(Level.WARNING, e.getMessage(), e);
      }
      List<Game> gamesReadFromFile = new ArrayList<>();
      try {
        gamesReadFromFile.addAll(FileGameHandler.parseGamesFromFile(pathOfFile));
      } catch (IOException | JsonSyntaxException e) {
        logger.log(Level.WARNING, e.getMessage(), e);
      }
      assertTrue(gamesReadFromFile.isEmpty());
    }

    @Test
    @DisplayName("Should read valid games from file with invalid action")
    void shouldReadValidGamesFromFileWithInvalidAction() {
      String pathOfFileInvalidActions = "src/test/resources/games/invalid_action_game_objects.json";
      List<Game> gamesReadFromFile = new ArrayList<>();

      try {
        gamesReadFromFile.addAll(FileGameHandler.parseGamesFromFile(pathOfFileInvalidActions));
      } catch (IOException | JsonSyntaxException e) {
        logger.log(Level.WARNING, e.getMessage(), e);
      }

      assertEquals(1, gamesReadFromFile.size());
      assertTrue(gamesReadFromFile.contains(game2));
      assertFalse(gamesReadFromFile.contains(game1));
      assertEquals(1, FileGameHandler.getInvalidGames().size());
    }

    @Test
    @DisplayName("Should read valid games from file with invalid goal")
    void shouldReadValidGamesFromFileWithInvalidGoal() {
      String pathOfFileInvalidGoal = "src/test/resources/games/invalid_goal_game_objects.json";
      List<Game> gamesReadFromFile = new ArrayList<>();

      try {
        gamesReadFromFile.addAll(FileGameHandler.parseGamesFromFile(pathOfFileInvalidGoal));
      } catch (IOException | JsonSyntaxException e) {
        logger.log(Level.WARNING, e.getMessage(), e);
      }

      assertEquals(1, gamesReadFromFile.size());
      assertTrue(gamesReadFromFile.contains(game2));
      assertFalse(gamesReadFromFile.contains(game1));
      assertEquals(1, FileGameHandler.getInvalidGames().size());
    }

    @Test
    @DisplayName("Should read valid games from file with invalid object")
    void shouldReadValidGamesFromFileWithInvalidObject() {
      String pathOfFileInvalidGoal = "src/test/resources/games/invalid_object_game_objects.json";
      List<Game> gamesReadFromFile = new ArrayList<>();

      try {
        gamesReadFromFile.addAll(FileGameHandler.parseGamesFromFile(pathOfFileInvalidGoal));
      } catch (IOException | JsonSyntaxException e) {
        logger.log(Level.WARNING, e.getMessage(), e);
      }

      assertEquals(1, gamesReadFromFile.size());
      assertTrue(gamesReadFromFile.contains(game2));
      assertFalse(gamesReadFromFile.contains(game1));
      assertEquals(1, FileGameHandler.getInvalidGames().size());
    }

    @Test
    @DisplayName("Should read valid games from file with invalid game ID")
    void shouldReadValidGamesFromFileWithInvalidGameId() {
      String pathOfFileInvalidGoal = "src/test/resources/games/invalid_game_id_game_objects.json";
      List<Game> gamesReadFromFile = new ArrayList<>();

      try {
        gamesReadFromFile.addAll(FileGameHandler.parseGamesFromFile(pathOfFileInvalidGoal));
      } catch (IOException | JsonSyntaxException e) {
        logger.log(Level.WARNING, e.getMessage(), e);
      }

      assertEquals(1, gamesReadFromFile.size());
      assertTrue(gamesReadFromFile.contains(game2));
      assertFalse(gamesReadFromFile.contains(game1));
      assertEquals(1, FileGameHandler.getInvalidGames().size());
    }

    @Test
    @DisplayName("Should get file extension")
    void shouldGetFileExtension() {
      String expectedFileExtension = ".json";
      String actualFileExtension = FileGameHandler.getFileExtension();
      assertEquals(expectedFileExtension, actualFileExtension);
    }
  }

  @Nested
  @DisplayName("Negative tests file handling")
  class NegativeTestsFileHandling {
    String invalidPathOfFileNull = null;
    String invalidPathOfFileExtension = "src/test/resources/games/game_objects.txt";
    String invalidPathOfFileBlank = "";

    @Test
    @DisplayName("Should not write games to file throws NullPointerException")
    void shouldNotWriteStoryToFileThrowsNullPointerException() {
      List<Game> invalidList = null;
      assertThrows(NullPointerException.class,
              () -> FileGameHandler.writeGamesToFile(invalidList, pathOfFile));
      assertThrows(NullPointerException.class,
              () -> FileGameHandler.writeGamesToFile(games, invalidPathOfFileNull));
    }

    @Test
    @DisplayName("Should not write games to file throws IllegalArgumentException")
    void shouldNotWriteStoryToFileThrowsIllegalArgumentException() {
      assertThrows(IllegalArgumentException.class,
              () -> FileGameHandler.writeGamesToFile(games, invalidPathOfFileExtension));
      assertThrows(IllegalArgumentException.class,
              () -> FileGameHandler.writeGamesToFile(games, invalidPathOfFileBlank));
    }

    @Test
    @DisplayName("Should not read games from file throws NullPointerException")
    void shouldNotReadStoryFromFileThrowsNullPointerException() {
      assertThrows(NullPointerException.class,
              () -> FileGameHandler.parseGamesFromFile(invalidPathOfFileNull));
    }

    @Test
    @DisplayName("Should not read games from file throws IllegalArgumentException")
    void shouldNotReadStoryFromFileThrowsIllegalArgumentException() {
      assertThrows(IllegalArgumentException.class,
              () -> FileGameHandler.parseGamesFromFile(invalidPathOfFileExtension));
      assertThrows(IllegalArgumentException.class,
              () -> FileGameHandler.parseGamesFromFile(invalidPathOfFileBlank));
    }

    @Test
    @DisplayName("Should not read games from file throws IOException")
    void shouldNotReadGamesFromFileThrowsIOException() {
      assertThrows(IOException.class,
              () -> FileGameHandler.parseGamesFromFile("nonExistingFile.json"));
    }

    @Test
    @DisplayName("Should not read games from file invalid syntax throws JsonSyntaxException")
    void shouldNotReadGamesFromFileInvalidGoalThrowsJsonParseException() {
      assertThrows(JsonSyntaxException.class,
              () -> FileGameHandler.parseGamesFromFile(
                      "src/test/resources/games/invalid_syntax_game_objects.json"));
    }
  }
}