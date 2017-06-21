package Domain.Effetti.lista;

import Domain.Effetti.Effetto;
import Domain.Effetti.lista.effectInterface.InizioTurno;
import Exceptions.SaltaTurnoException;

/**
 * Created by pietro on 18/05/17.
 */
public class DisabilitaTurni extends Effetto implements InizioTurno {
    int turnoDaSaltare;

    public DisabilitaTurni(int turno){
        turnoDaSaltare=turno;
    }
    @Override
    public void setupTurno(int numeroTurno) throws SaltaTurnoException {
            if (turnoDaSaltare==numeroTurno) throw new SaltaTurnoException();
    }
}
