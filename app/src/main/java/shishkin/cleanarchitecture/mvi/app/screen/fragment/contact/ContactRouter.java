package shishkin.cleanarchitecture.mvi.app.screen.fragment.contact;

import android.content.Intent;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.io.Files;


import java.io.File;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.model.BaseModelRouter;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
import shishkin.cleanarchitecture.mvi.sl.event.StartActivityEvent;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsActivity;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class ContactRouter extends BaseModelRouter {

    public ContactRouter(ContactModel model) {
        super(model);
    }

    public void showUrl() {
        ApplicationUtils.showUrl(SLUtil.getActivity(), "https://github.com/shishkin1966/CleanArchitecture5");
    }

    public void sendMail() {
        final String rec = "oleg_shishkin@mail.ru";
        final String path = ErrorSpecialistImpl.getInstance().getPath();
        String body = "\n" + ApplicationUtils.getPhoneInfo();
        if (!StringUtils.isNullOrEmpty(path)) {
            try {
                final File file = new File(path);
                if (file.exists()) {
                    final String log = Files.toString(file, Charsets.UTF_8);
                    body = "\n" + log;
                }
            } catch (Exception e) {
            }
        }
        final Intent intent = ApplicationUtils.sendEmailIntent(rec, "Ошибка", body);
        if (intent.resolveActivity(((AbsActivity) SLUtil.getActivity()).getPackageManager()) != null) {
            SLUtil.getActivityUnion().startActivity(new StartActivityEvent(intent));
        } else {
            SLUtil.getActivityUnion().showFlashbar(new ShowMessageEvent(SLUtil.getContext().getString(R.string.mail_error)).setDuration(Toast.LENGTH_LONG).setType(ApplicationUtils.MESSAGE_TYPE_WARNING).setTitle(SLUtil.getContext().getString(R.string.warning)));
        }
    }
}
