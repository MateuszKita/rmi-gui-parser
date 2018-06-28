package rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Parser extends Remote {

    public String urlPrefix = "http://www.plan.uz.zgora.pl";
    public String groupsIt = "/grupy_lista_grup_kierunku.php?pId_kierunek=401";

    List parseGroups() throws RemoteException;

    List<String> parseSchedule(String url) throws RemoteException;
}
