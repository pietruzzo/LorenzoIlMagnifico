package Domain.Effetti.lista.effectInterface;

/**
 * Created by pietro on 18/05/17.
 */
public interface Trigger {

    public boolean isTriggered();
    public void setDefaultTrigger();
    public void setTrigger(boolean stato);

}
