package gitlet;
/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author bard
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "test":
                break;
            case "init":
                validateNumArgs("init", args, 1);
                Repository.initialCommit();
                break;
            case "add":
                validateNumArgs("add", args, 2);
                Repository.addFile(args[1]);
                break;
            case "commit":
                validateNumArgs("commit", args, 2);
                Repository.commitBolb(args[1]);
                break;
            case "rm":
                validateNumArgs("commit", args, 2);
                Repository.rmFile(args[1]);
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
                } else if (args.length == 3 && args[1].equals("--")) {
                    /** checkout -- [file name] */
                    Repository.checkoutFileName(args[2]);
                } else if (args.length == 4 && args[2].equals("--")) {
                    /** checkout [commit id] -- [file name] */
                    if (args[1].length() < 6) {
                        System.out.println("Commit id length must be greater than or equal to 6");
                        System.exit(0);
                    }
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
            case "rm-branch":
                validateNumArgs("rm-branch", args, 2);
                Repository.rmBranch(args[1]);
                break;
            case "reset":
                validateNumArgs("reset", args, 2);
                Repository.reset(args[1]);
                break;
            case "merge":
                validateNumArgs("merge", args, 2);
                Repository.mergeBranch(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            if (cmd.equals("commit")) {
                System.out.println("Please enter a commit message.");
                System.exit(0);
            }
            System.out.println("Invalid number of arguments");
            System.exit(0);
        }
    }
}
