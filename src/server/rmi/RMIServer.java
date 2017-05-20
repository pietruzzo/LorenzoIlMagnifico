package Server.RMI;

import Server.AbstractServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Portatile on 19/05/2017.
 */
public class RMIServer extends AbstractServer implements IRMIServer {

    /**
     * Avvia il server RMI
     */
    @Override
    public void StartServer(int porta) throws Exception {
        Registry registro = creaCaricaRegistro(porta);
        try {
            registro.bind("IRMIServer", this);
            UnicastRemoteObject.exportObject(this, porta);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Se non è ancora stato creato, crea il registro.
     * Altrimenti prova a ritornare il registro creato in precedenza.
     */
    private Registry creaCaricaRegistro(int porta) throws Exception {
        try {
            return LocateRegistry.createRegistry(porta);
        } catch (RemoteException e) {
            //Il registro è già stato creato
        }
        try {
            return LocateRegistry.getRegistry(porta);
        } catch (RemoteException e) {
            throw new Exception("Impossibile avviare il registro RMI");
        }

    }
}
