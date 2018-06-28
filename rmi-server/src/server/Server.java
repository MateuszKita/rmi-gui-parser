package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import rmi.api.Parser;

public class Server {

    public static void main(String[] args) throws RemoteException {
        int port = 8000;

        Registry registry = LocateRegistry.createRegistry(port);

        ParserImplementation parserImplementation = new ParserImplementation();

        Parser parser = (Parser) UnicastRemoteObject.exportObject(parserImplementation, 0);

        registry.rebind("parser", parser);

        System.out.println("Server is running on port " + port + ".");
    }
}
