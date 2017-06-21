package lorenzo;

import javafx.application.Platform;

/**
 * Created by Portatile on 17/05/2017.
 */
public class ClientStarter {

    private ClientStarter(){}

    /**
     * Metodo eseguito all'avvio del client
     */
    public static void main(String[] args)
    {
        javafx.application.Application.launch(Applicazione.class);
    }
}
