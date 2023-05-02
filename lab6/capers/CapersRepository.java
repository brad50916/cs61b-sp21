package capers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import static capers.Utils.*;

/** A repository for Capers 
 * @author TODO
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 * TODO: change the above structure if you do something different.
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = Utils.join(".capers");
    static final File DOGS_FOLDER = Utils.join(".capers", "dogs");
    static final File STORY_PATH = Utils.join(".capers", "story");

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
    public static void setupPersistence() {
        if (!DOGS_FOLDER.exists()) {
            DOGS_FOLDER.mkdir();
        }
        if (!CAPERS_FOLDER.exists()) {
            CAPERS_FOLDER.mkdir();
        }
        if (!STORY_PATH.exists()) {
            Model m = new Model();
            File outFile = STORY_PATH;
            // Serializing the Model object
            writeObject(outFile, m);
        }
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    private static class Model implements Serializable {
        private String s;

        public void addText(String s) {
            if (this.s == null) {
                this.s = s + "\n";
            } else {
                this.s += s + "\n";
            }
        }
        public void print() {
            System.out.print(this.s);
        }
    }
    public static void writeStory(String text) {
        Model m;
        // Deserializing the Model object
        m = readObject(STORY_PATH, Model.class);
        m.addText(text);
        m.print();

        // Serializing the Model object
        writeObject(STORY_PATH, m);
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        // TODO
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        // TODO
    }
}
