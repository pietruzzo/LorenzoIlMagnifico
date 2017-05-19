package Lorenzo;

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
        MainGame mainGame = new MainGame();
        mainGame.Start();
    }
}
