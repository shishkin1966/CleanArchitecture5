package shishkin.cleanarchitecture.mvi.sl.event;

/**
 * Событие - выполнить Action
 */
@SuppressWarnings("unused")
public class OnActionEvent extends AbsEvent {
    private String mAction;

    public OnActionEvent(final String action) {
        mAction = action;
    }

    public String getAction() {
        return mAction;
    }

    public OnActionEvent setAction(String action) {
        this.mAction = action;
        return this;
    }

}
