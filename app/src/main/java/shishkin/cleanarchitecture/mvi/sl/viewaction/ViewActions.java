package shishkin.cleanarchitecture.mvi.sl.viewaction;

import java.util.LinkedHashMap;
import java.util.Map;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

public class ViewActions {

    private Map<String, Object> actions = new LinkedHashMap<>();
    private ViewActionListener listener;

    public ViewActions(ViewActionListener listener) {
        this.listener = listener;
    }

    public void putViewAction(ViewAction action) {
        if (action == null) return;

        if (StringUtils.isNullOrEmpty(action.getName())) return;

        actions.put(action.getName(), action.getValue());
    }

    public void doViewActions() {
        if (listener == null) return;

        for (Map.Entry<String, Object> entry : actions.entrySet()) {
            listener.doViewAction(new ViewAction(entry.getKey(), entry.getValue()));
        }
        actions.clear();
    }

    public void doViewAction(ViewAction action) {
        if (action == null) return;

        if (listener == null) return;

        listener.doViewAction(action);
    }
}
