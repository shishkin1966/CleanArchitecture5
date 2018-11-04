package shishkin.cleanarchitecture.mvi.app.viewaction;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewAction {

    private Map<String, Object> actions = new LinkedHashMap<>();
    private ViewActionListener listener;

    public ViewAction(ViewActionListener listener) {
        this.listener = listener;
    }

    public void putViewAction(String name, Object value) {
        actions.put(name, value);
    }

    public void putViewAction(String name) {
        actions.put(name, null);
    }

    public void doViewActions() {
        if (listener == null) return;

        for (Map.Entry<String, Object> entry : actions.entrySet()) {
            listener.doViewAction(entry.getKey(), entry.getValue());
        }
        actions.clear();
    }

    public void doViewAction(String name, Object value) {
        if (listener == null) return;

        listener.doViewAction(name, value);
    }

    public void doViewAction(String name) {
        if (listener == null) return;

        listener.doViewAction(name, null);
    }
}
