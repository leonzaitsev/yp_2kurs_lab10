import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Mud {
    static String mudPrefix = "localhost";
    public interface MailServerInterface extends Remote {
        String getPersons()throws RemoteException;
        MailClientInterface getPerson(String name) throws RemoteException;
        void delClient(MailClientInterface current) throws RemoteException;
        boolean addPerson(MailClientInterface current) throws RemoteException;
        void sendMessage(MailClientInterface name,String message) throws RemoteException;
    }
    public interface MailClientInterface extends Remote {
        String showMessage() throws RemoteException;
        void talk(String text)throws RemoteException;
        String getName() throws RemoteException;
        void cleanLetters() throws RemoteException;
        public String print() throws RemoteException;
        ArrayList<String> getLetters() throws RemoteException;
        void addLetter(String letter) throws RemoteException;
        void sendMessage(MailClientInterface name,String message) throws RemoteException;
    }
}
