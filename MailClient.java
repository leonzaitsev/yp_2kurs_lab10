import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.TreeSet;
public class MailClient {
    private static ArrayList<String> messages;
    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) {
        try {
            messages=new ArrayList<>();
            String hostname = "localhost";
            String username = "qwe";

            Mud.MailServerInterface server = (Mud.MailServerInterface) Naming.lookup("rmi://" + hostname
                    + ":" + MailServer.PORT + "/" + username);
            System.out.println("You are connected to MailServerInterface!");
            runUser(username, server);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.out.println("Usage: java FreeTimeClient <host> <user>");
            System.exit(1);
        }

    }
    private static void runUser(String username, Mud.MailServerInterface server) throws IOException, InterruptedException {

        String currentPersonName = welcome(username, server);
        Mud.MailClientInterface curname=server.getPerson(currentPersonName);
        String cmd;

        for (; ; ) {

//            server.getPerson(currentPersonName).setInMeet(cmd);
            try {
                // Catch any exceptions that occur in the loop
                // Pause just a bit before printing the prompt, to give output
                // generated indirectly by the last command a chance to appear.
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {
                }

                cmd = getLine(">> ");
//                currentPerson.setInMeet(cmd);

                switch (cmd) {
                    case "s":
                        sendMesssage(server, currentPersonName);
                        break;
                    case "sh":
                        showMessage(server,currentPersonName);
                        break;
                    case "u":
                        showClients(server);
                        break;
                    case "help":
                    case "h":
                        System.out.println(help);
                        break;

                    case "quit":
                    case "q":
                        delClient(server,currentPersonName);
                        System.out.println("Bye!");
                        System.out.flush();
                        System.exit(0);

                    default:
                        System.out.println("Unknown command.  Try 'help'.\n");

                }

            } catch (Exception e) {
                System.out.println("Syntax or other error:");
                System.out.println(e);
                System.out.println("Try using the 'help' command.");
            }
        }

    }
    private static String welcome(String username, Mud.MailServerInterface server) throws IOException, InterruptedException {
        Mud.MailClientInterface currentPerson = null;
        do {
            String cmd = getLine("[" + username + "]:" + "Do you want to register or login?\n" +
                    "\tr : register\n" +
                    "\tl : login\n" +
                    ">> ");

            String name = getLine(">> Input your name: ");

            switch (cmd) {
                case "r": {
                    PrintWriter out = new PrintWriter(System.out);
                    MailPerson current = new MailPerson(name, out, in);

                    if (server.addPerson(current)) {
                        System.out.println(">> You are successful registered as \"" + name + "\"");
                        currentPerson = server.getPerson(name);
                    } else {
                        System.out.println(">> Name \"" + name + "\" is already used.");
                    }
                    break;
                }
                case "l": {
                    currentPerson = server.getPerson(name);
                    if (currentPerson != null) {
                        System.out.println(">> You are logged in as \"" + currentPerson.getName() + "\".");
                    } else {
                        System.out.println(">> No user with this name was found.");
                    }
                    break;
                }
                default: {
                    System.out.println(">> Incorrect key + \"" + cmd + "\"");
                    currentPerson = null;
                    break;
                }
            }
        } while (currentPerson == null);
//        System.out.println();
//        System.out.flush();
        return currentPerson.getName();
    }

    private static String getLine(String prompt) throws InterruptedException {
        String line = null;
        do {                      // Loop until a non-empty line is entered
            try {
                System.out.print(prompt);             // Display prompt
                System.out.flush();                   // Display it right away

                line = in.readLine();                 // Get a line of input
                if (line != null) line = line.trim(); // Strip off whitespace
            } catch (Exception ignored) {
            }
        } while ((line == null) || (line.length() == 0));

        return line;
    }
    private  static  void delClient(Mud.MailServerInterface server,String personName)throws IOException, InterruptedException{
        Mud.MailClientInterface person = server.getPerson(personName);
        server.delClient(person);
    }
    private static void showClients(Mud.MailServerInterface server)throws IOException, InterruptedException{
        System.out.println(server.getPersons());
    }
    private static void showMessage(Mud.MailServerInterface server,String personName)throws IOException, InterruptedException{

        String secondPersonName = getLine(">> Whose messages do you want to see ?\n" +
                ">> ");

        Mud.MailClientInterface person = server.getPerson(personName);
        Mud.MailClientInterface secondPerson = server.getPerson(secondPersonName);

        if (secondPerson != null) {
            System.out.println(secondPerson.showMessage());
        }
        else {
            System.out.println(">> No user with this name was found.\n");
        }

        System.out.flush();

    }
    private static void sendMesssage(Mud.MailServerInterface server, String personName) throws IOException, InterruptedException {

        String secondPersonName = getLine(">> Who do you want to write a message to ?\n" +
                ">> ");

        Mud.MailClientInterface person = server.getPerson(personName);
        Mud.MailClientInterface secondPerson = server.getPerson(secondPersonName);

        if (secondPerson != null) {
            String message=personName+":  "+getLine(">> input your message\n" + ">> ");
            secondPerson.talk(message);
            secondPerson.addLetter(message);
        }
        else {
            System.out.println(">> No user with this name was found.\n");
        }

        System.out.flush();
    }
    private static final String help = "Commands:\n" +
            "s : send message\n" +
            "sh: show your message\n" +
            "u: show clients"+
            "help | h: display this message\n" +
            "q : quit\n";
}
