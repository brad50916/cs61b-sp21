package gitlet;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Please enter a command.");
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                validateNumArgs("init", args, 1);
                Repository.initialCommit();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                validateNumArgs("add", args, 2);
                Repository.addFile(args[1]);
                break;
            case "commit":
                validateNumArgs("commit", args, 2);
                Repository.Commit(args[1]);
                break;
            case "rm":
                validateNumArgs("commit", args, 2);
                Repository.Rm(args[1]);
                break;
            case "log":
                validateNumArgs("log", args, 1);
                Repository.log();
                break;
            case "global-log":
                validateNumArgs("global-log", args, 1);
                Repository.globalLog();
                break;
            case "find":
                validateNumArgs("find", args, 2);
                Repository.find(args[1]);
                break;
            case "status":
                validateNumArgs("status", args, 1);
                Repository.status();
                break;
            case "checkout":
                /** checkout [branch name] */
                if (args.length == 2) {
                    Repository.checkoutBranch(args[1]);
                }
                /** checkout -- [file name] */
                else if (args.length == 3 && args[1].equals("--")) {
                    Repository.checkoutFileName(args[2]);
                }
                /** checkout [commit id] -- [file name] */
                else if (args.length == 4 && args[2].equals("--")) {
                    Repository.checkoutIDFileName(args[1], args[3]);
                } else {
                    throw new RuntimeException(
                            String.format("Invalid command for checkout."));
                }
                break;
            case "branch":
                validateNumArgs("branch", args, 2);
                Repository.branch(args[1]);
                break;
            default:
                throw new IllegalArgumentException("No command with that name exists.");
        }
    }
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            if (cmd.equals("commit")) {
                System.out.println("Please enter a commit message.");
                System.exit(0);
            }
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
}
