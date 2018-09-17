package shishkin.cleanarchitecture.mvi.sl;

import android.support.annotation.NonNull;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.mail.Mail;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateObserver;

/**
 * Объединение, предоставляющее почтовый сервис подписчикам
 */
public class MailUnionImpl extends AbsSmallUnion<MailSubscriber> implements MailUnion {

    public static final String NAME = MailUnionImpl.class.getName();

    private Map<Long, Mail> mMail = Collections.synchronizedMap(new ConcurrentHashMap<Long, Mail>());
    private AtomicLong mId = new AtomicLong(0L);

    public MailUnionImpl() {
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<Mail> getMail(final MailSubscriber subscriber) {
        if (subscriber != null) {
            if (mMail.isEmpty()) {
                return new ArrayList<>();
            }

            // удаляем старые письма
            final String name = subscriber.getName();
            final long currentTime = System.currentTimeMillis();
            final List<Mail> list = SLUtil.getDataSpecialist().filter(mMail.values(), mail -> (mail.contains(name) && mail.getEndTime() != -1 && mail.getEndTime() < currentTime)).toList();
            if (!list.isEmpty()) {
                for (Mail mail : list) {
                    mMail.remove(mail.getId());
                }
            }

            if (mMail.isEmpty()) {
                return new ArrayList<>();
            }

            final Comparator<Mail> byId = (left, right) -> left.getId().compareTo(right.getId());
            return SLUtil.getDataSpecialist().filter(mMail.values(), mail -> mail.contains(name) && (mail.getEndTime() == -1 || (mail.getEndTime() != -1 && mail.getEndTime() > currentTime))).sorted(byId).toList();
        }
        return new ArrayList<>();
    }

    @Override
    public void clearMail(final MailSubscriber subscriber) {
        if (subscriber != null) {
            if (mMail.isEmpty()) {
                return;
            }

            final String name = subscriber.getName();
            final List<Mail> list = SLUtil.getDataSpecialist().filter(mMail.values(), mail -> mail.contains(name)).toList();
            if (!list.isEmpty()) {
                for (Mail mail : list) {
                    mMail.remove(mail.getId());
                }
            }
        }
    }

    @Override
    public void addMail(final Mail mail) {
        if (mail != null) {
            final List<String> list = mail.getCopyTo();
            list.add(mail.getAddress());
            for (String address : list) {
                final long id = mId.incrementAndGet();
                final Mail newMail = mail.copy();
                newMail.setId(id);
                newMail.setAddress(address);
                newMail.setCopyTo(new ArrayList<>());

                if (!mail.isCheckDublicate()) {
                    mMail.put(id, newMail);
                } else {
                    removeDublicate(newMail);
                    mMail.put(id, newMail);
                }

                checkAddMailSubscriber(address);
            }
        }
    }

    private void checkAddMailSubscriber(final String address) {
        if (StringUtils.isNullOrEmpty(address)) {
            return;
        }

        List<WeakReference<MailSubscriber>> list = getSubscribers();
        for (WeakReference<MailSubscriber> reference : list) {
            final MailSubscriber subscriber = reference.get();
            if (address.equalsIgnoreCase(subscriber.getName())) {
                if (subscriber.getState() == ViewStateObserver.STATE_RESUME) {
                    SLUtil.readMail(subscriber);
                }
            }
        }
    }

    private void removeDublicate(final Mail mail) {
        if (mail != null && !StringUtils.isNullOrEmpty(mail.getName()) && !StringUtils.isNullOrEmpty(mail.getAddress())) {
            for (Mail tmpMail : mMail.values()) {
                if (tmpMail != null) {
                    if (mail.getName().equals(tmpMail.getName()) && mail.getAddress().equals(tmpMail.getAddress())) {
                        removeMail(tmpMail);
                    }
                }
            }
        }
    }

    @Override
    public void removeMail(final Mail mail) {
        if (mail != null) {
            if (mMail.containsKey(mail.getId())) {
                mMail.remove(mail.getId());
            }
        }
    }

    @Override
    public void clearMail() {
        mMail.clear();
    }

    @Override
    public void onFinishApplication() {
        clearMail();
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (MailUnion.class.isInstance(o)) ? 0 : 1;
    }
}
