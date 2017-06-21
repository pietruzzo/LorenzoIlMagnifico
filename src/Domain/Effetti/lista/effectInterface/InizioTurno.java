package Domain.Effetti.lista.effectInterface;

import Exceptions.SaltaTurnoException;

/**
 * Created by pietro on 18/05/17.
 */
public interface InizioTurno {
    public void setupTurno(int numeroTurno) throws SaltaTurnoException;
}
