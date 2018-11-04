package shishkin.cleanarchitecture.mvi.app.viewstate;

import shishkin.cleanarchitecture.mvi.sl.Secretary;
import shishkin.cleanarchitecture.mvi.sl.SecretaryImpl;

public class ViewState {
    private Secretary<Object> secretary = new SecretaryImpl<>();
}
