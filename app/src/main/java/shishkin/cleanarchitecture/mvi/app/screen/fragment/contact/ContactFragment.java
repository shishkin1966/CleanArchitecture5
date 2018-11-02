package shishkin.cleanarchitecture.mvi.app.screen.fragment.contact;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class ContactFragment extends AbsContentFragment<ContactModel> implements ContactView {

    public static final String NAME = ContactFragment.class.getName();

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findView(R.id.web).setOnClickListener(getModel().getPresenter());
        findView(R.id.mail).setOnClickListener(getModel().getPresenter());
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ContactModel createModel() {
        return new ContactModel(this);
    }

    @Override
    public boolean onBackPressed() {
        SLUtil.getViewUnion().switchToTopFragment();
        return true;
    }
}
