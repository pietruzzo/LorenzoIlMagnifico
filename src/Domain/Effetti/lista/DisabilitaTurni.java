package Domain.Effetti.lista;

import Domain.Effetti.lista.effectInterface.InizioTurno;
import Exceptions.SaltaTurnoException;

/**
 * Created by pietro on 18/05/17.
 */
public class DisabilitaTurni implements InizioTurno {
    int[] turniDaSaltare;
    @Override
    public void setupTurno(int numeroTurno) throws SaltaTurnoException {
        for (int i: turniDaSaltare) {
            if (i==numeroTurno) throw new SaltaTurnoException();
        }
    }
}
