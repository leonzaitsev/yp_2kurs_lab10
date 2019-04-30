import java.io.BufferedReader;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;

public class MailPerson extends UnicastRemoteObject implements Mud.MailClientInterface{
    private String name;
    private PrintWriter out;
    private final BufferedReader in;
    private ArrayList<String> letters;
    MailPerson(String name, PrintWriter out, BufferedReader in) throws RemoteException {
        this.name = name;
        this.out = out;
        this.in = in;
        letters = new ArrayList<>();
    }
    @Override
    public ArrayList<String> getLetters() throws RemoteException {
        return letters;
    }
    @Override
    public String getName() throws RemoteException {
        return name;
    }
    @Override
    public String showMessage() throws RemoteException{
        String res=(name+"'s messages:\n");
       for(String i:letters){
           res+=i+"\n";
       }
       return res;
    }
    @Override
    public void talk(String text)throws RemoteException {

        out.print(text);
        out.flush();
    }
    @Override
    public void addLetter(String letter) throws RemoteException {
        letters.add(letter);
    }
    @Override
    public void cleanLetters() throws RemoteException {
        letters.clear();
    }
    @Override
    public String print() throws RemoteException {
        return toString();
    }

    @Override
    public String toString() {
        return "{" + name + "}";
    }
    @Override
    public void sendMessage(Mud.MailClientInterface name, String message) throws RemoteException{

    }

}
