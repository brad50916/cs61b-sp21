package gitlet;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Must have at least one argument");
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown command: %s", args[0]));
        }
    }
}
