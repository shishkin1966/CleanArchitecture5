package shishkin.cleanarchitecture.mvi.sl.state;

@SuppressWarnings("unused")
public interface Stateable {

    /**
     * Получить состояние объекта
     *
     * @return the state
     */
    int getState();

    /**
     * Установить состояние объекта
     *
     * @param state the state
     */
    void setState(int state);

}
