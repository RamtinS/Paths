package edu.ntnu.idatt2001.paths.model.filehandling;

import edu.ntnu.idatt2001.paths.model.Link;
import edu.ntnu.idatt2001.paths.model.Passage;
import edu.ntnu.idatt2001.paths.model.Story;
import edu.ntnu.idatt2001.paths.model.actions.Action;
import edu.ntnu.idatt2001.paths.model.actions.GoldAction;
import edu.ntnu.idatt2001.paths.model.actions.HealthAction;
import edu.ntnu.idatt2001.paths.model.actions.InventoryAction;
import edu.ntnu.idatt2001.paths.model.actions.ScoreAction;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The class tests the FileStoryHandler class.
 *
 * @author Ramtin Samavat and Tobias Oftedal.
 * @version 1.0
 * @since April 23, 2023.
 */
class FileStoryHandlerTest {
  private static final Logger logger = Logger.getLogger(FileStoryHandler.class.getName());
  private String pathToFile;
  private File testFile;
  private Story story;

  @BeforeEach
  void setUp() {
    pathToFile = "src/test/resources/stories/story.paths";
    testFile = new File(pathToFile);

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

    story = new Story("Haunted House", openingPassage);
    story.addPassage(passage1);
    story.addPassage(passage2);
  }

  @AfterEach
  void tearDown() {
    Path path = Paths.get(testFile.getPath());
    try {
      Files.deleteIfExists(path);
    } catch (IOException e) {
      logger.log(Level.WARNING, "Error deleting file", e);
    }
  }

  @Nested
  @DisplayName("Positive tests file handling")
  class PositiveTestsFileHandling {
    @Test
    @DisplayName("Should write story to file")
    void shouldWriteStoryToFileTest() {
      try {
        FileStoryHandler.writeStoryToFile(story, pathToFile);
      } catch (IOException e) {
        logger.log(Level.WARNING, "Error occurred while writing story to file: " + pathToFile, e);
      }
      try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          content.append(line).append("\n");
        }

        String expected = """         
              Haunted House
                            
              ::Beginnings
              There is a door in front of you.
              [Try to open the door](Another room){Inventory:Sword}
                                     
              ::Another room
              The door opens to another room. You see a desk with a large, dusty book.
              [Open the book](The book of spells){Score:10}{Health:10}
              [Go back](Beginnings){Score:-10}{Health:-10}
                            
              ::The book of spells
              You open the book and find the spell of teleportation.
              [Cast the spell](Forest){Gold:10}
              [Go back](Another room){Gold:-10}
              """;

        assertEquals(expected, content.toString());
      } catch (IOException e) {
        logger.log(Level.WARNING, "Error occurred while reading file: " + pathToFile, e);
      }
    }

    @Test
    @DisplayName("Should read story from file")
    void shouldReadStoryFromFile() {
      try {
        FileStoryHandler.writeStoryToFile(story, pathToFile);
      } catch (IOException e) {
        logger.log(Level.WARNING, "Error occurred while writing story to file: " + pathToFile, e);
      }

      Story storyReadFromFile = null;
      try {
        storyReadFromFile = FileStoryHandler.readStoryFromFile(pathToFile);
      } catch (IOException e) {
        logger.log(Level.WARNING, "Error occurred while reading story from file: " + pathToFile, e);
      }

      assertEquals(story.getTitle(), storyReadFromFile.getTitle());
      assertEquals(story.getOpeningPassage(), storyReadFromFile.getOpeningPassage());
      assertTrue(storyReadFromFile.getPassages().containsAll(story.getPassages()));
    }

    @Test
    @DisplayName("Should read story from file with invalid actions")
    void shouldReadStoryFromFileWithInvalidActions() {
      String pathToFileWithInvalidActions = "src/test/resources/stories/invalid_actions_story.paths";

      Story storyReadFromFile = null;
      try {
        storyReadFromFile = FileStoryHandler.readStoryFromFile(pathToFileWithInvalidActions);
      } catch (IOException e) {
        logger.log(Level.WARNING, "Error occurred while reading story from file: "
                + pathToFileWithInvalidActions, e);
      }

      assertNotEquals(null, storyReadFromFile);
      assertEquals(story.getTitle(), storyReadFromFile.getTitle());
      assertEquals(3, FileStoryHandler.getInvalidActions().size());
    }
  }

  @Nested
  @DisplayName("Negative tests file handling")
  class NegativeTestsFileHandling {
    String invalidPathToFileNull = null;
    String invalidPathToFileExtension = "src/test/resources/stories/story.txt";
    String invalidPathToFileBlank = "";

    @Test
    @DisplayName("Should not write story to file throws NullPointerException")
    void shouldNotWriteStoryToFileThrowsNullPointerException() {
      Story invalidStory = null;
      assertThrows(NullPointerException.class,
              () -> FileStoryHandler.writeStoryToFile(invalidStory, pathToFile));
      assertThrows(NullPointerException.class,
              () -> FileStoryHandler.writeStoryToFile(story, invalidPathToFileNull));
    }

    @Test
    @DisplayName("Should not write story to file throws IllegalArgumentException")
    void shouldNotWriteStoryToFileThrowsIllegalArgumentException() {
      assertThrows(IllegalArgumentException.class,
              () -> FileStoryHandler.writeStoryToFile(story, invalidPathToFileExtension));
      assertThrows(IllegalArgumentException.class,
              () -> FileStoryHandler.writeStoryToFile(story, invalidPathToFileBlank));
    }

    @Test
    @DisplayName("Should not read story from file throws NullPointerException")
    void shouldNotReadStoryFromFileThrowsNullPointerException() {
      assertThrows(NullPointerException.class,
              () -> FileStoryHandler.readStoryFromFile(invalidPathToFileNull));
    }

    @Test
    @DisplayName("Should not read story from file throws IllegalArgumentException")
    void shouldNotReadStoryFromFileThrowsIllegalArgumentException() {
      assertThrows(IllegalArgumentException.class,
              () -> FileStoryHandler.readStoryFromFile(invalidPathToFileExtension));
      assertThrows(IllegalArgumentException.class,
              () -> FileStoryHandler.readStoryFromFile(invalidPathToFileBlank));
    }

    @Test
    @DisplayName("Should not read story from file throws IOException")
    void shouldNotReadStoryFromFileThrowsIOException() {
      assertThrows(IOException.class,
              () -> FileStoryHandler.readStoryFromFile("nonExistingFile.paths"));
    }
  }
}