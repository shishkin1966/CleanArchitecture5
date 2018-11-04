package shishkin.cleanarchitecture.mvi.sl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


import androidx.annotation.NonNull;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.mail.Mail;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateObserver;

/**
 * Объединение, предоставляющее почтовый сервис подписчикам
 */
public class MessagerUnionImpl extends AbsSmallUnion<MessagerSubscriber> implements MessagerUnion {

    public static final String NAME = MessagerUnionImpl.class.getName();

    private Map<Long, Mail> mMail = Collections.synchronizedMap(new ConcurrentHashMap<>());
    private Secretary<List<String>> mMailingList = new SecretaryImpl<>();
    private AtomicLong mId = new AtomicLong(0L);

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getPasport() {
        return getName();
    }

    @Override
    public void onAddSubscriber(final MessagerSubscriber subscriber) {
        readMail(subscriber);
    }

    @Override
    public List<Mail> getMail(final MessagerSubscriber subscriber) {
        if (subscriber != null) {
            if (mMail.isEmpty()) {
                return new ArrayList<>();
            }

            // удаляем старые письма
            final String name = subscriber.getName();
            final long currentTime = System.currentTimeMillis();
            final List<Mail> list = ((DataSpecialist) SL.getInstance().get(DataSpecialistImpl.NAME)).filter(mMail.values(), mail -> (mail.contains(name) && mail.getEndTime() != -1 && mail.getEndTime() < currentTime)).toList();
            if (!list.isEmpty()) {
                for (Mail mail : list) {
                    mMail.remove(mail.getId());
                }
            }

            if (mMail.isEmpty()) {
                return new ArrayList<>();
            }

            final Comparator<Mail> byId = (left, right) -> left.getId().compareTo(right.getId());
            return ((DataSpecialist) SL.getInstance().get(DataSpecialistImpl.NAME)).filter(mMail.values(), mail -> mail.contains(name) && (mail.getEndTime() == -1 || (mail.getEndTime() != -1 && mail.getEndTime() > currentTime))).sorted(byId).toList();
        }
        return new ArrayList<>();
    }

    @Override
    public void clearMail(final MessagerSubscriber subscriber) {
        if (subscriber != null) {
            if (mMail.isEmpty()) {
                return;
            }

            final String name = subscriber.getName();
            final List<Mail> list = ((DataSpecialist) SL.getInstance().get(DataSpecialistImpl.NAME)).filter(mMail.values(), mail -> mail.contains(name)).toList();
            if (!list.isEmpty()) {
                for (Mail mail : list) {
                    mMail.remove(mail.getId());
                }
            }
        }
    }

    @Override
    public void clearMail(final String name) {
        if (StringUtils.isNullOrEmpty(name)) return;

        final List<Mail> list = ((DataSpecialist) SL.getInstance().get(DataSpecialistImpl.NAME)).filter(mMail.values(), mail -> mail.contains(name)).toList();
        if (!list.isEmpty()) {
            for (Mail mail : list) {
                mMail.remove(mail.getId());
            }
        }
    }

    @Override
    public void addMailingList(String name, List<String> addresses) {
        if (StringUtils.isNullOrEmpty(name) || addresses == null) return;

        mMailingList.put(name, addresses);
    }

    @Override
    public void addMailingList(String name, String[] addresses) {
        if (StringUtils.isNullOrEmpty(name) || addresses == null) return;

        mMailingList.put(name, Arrays.asList(addresses));
    }

    @Override
    public void removeMailingList(String name) {
        if (StringUtils.isNullOrEmpty(name)) return;

        mMailingList.remove(name);
    }

    @Override
    public List<String> getMailingList(String name) {
        if (StringUtils.isNullOrEmpty(name)) return null;

        if (mMailingList.containsKey(name)) {
            return mMailingList.get(name);
        } else {
            return null;
        }
    }

    private List<String> getAddresses(String address) {
        final List<String> addresses = new ArrayList<>();
        if (mMailingList.containsKey(address)) {
            for (String adr : mMailingList.get(address)) {
                addresses.addAll(getAddresses(adr));
            }
        } else {
            addresses.add(address);
        }
        return addresses;
    }

    @Override
    public void addMail(final Mail mail) {
        if (mail != null) {
            List<String> list = mail.getCopyTo();
            list.add(mail.getAddress());
            List<String> addresses = new ArrayList<>();
            for (String address : list) {
                addresses.addAll(getAddresses(address));
            }
            for (String address : addresses) {
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

    @Override
    public void addNotMandatoryMail(final Mail mail) {
        if (mail != null) {
            List<String> list = mail.getCopyTo();
            list.add(mail.getAddress());
            List<String> addresses = new ArrayList<>();
            for (String address : list) {
                addresses.addAll(getAddresses(address));
            }
            for (String address : addresses) {
                if (checkSubscriber(address)) {
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
    }

    @Override
    public void replaceMail(final Mail mail) {
        if (mail != null) {
            final List<String> list = mail.getCopyTo();
            list.add(mail.getAddress());
            List<String> addresses = new ArrayList<>();
            for (String address : list) {
                addresses.addAll(getAddresses(address));
            }
            for (String address : addresses) {
                final long id = mId.incrementAndGet();
                final Mail newMail = mail.copy();
                newMail.setId(id);
                newMail.setAddress(address);
                newMail.setCopyTo(new ArrayList<>());

                removeDublicate(newMail);
                mMail.put(id, newMail);

                checkAddMailSubscriber(address);
            }
        }
    }

    private void checkAddMailSubscriber(final String address) {
        if (StringUtils.isNullOrEmpty(address)) {
            return;
        }

        for (MessagerSubscriber subscriber : getSubscribers()) {
            if (address.equalsIgnoreCase(subscriber.getName())) {
                final int state = subscriber.getState();
                if (state == ViewStateObserver.STATE_RESUME || state == ViewStateObserver.STATE_READY) {
                    readMail(subscriber);
                }
            }
        }
    }

    private boolean checkSubscriber(final String address) {
        if (StringUtils.isNullOrEmpty(address)) {
            return false;
        }

        for (MessagerSubscriber subscriber : getSubscribers()) {
            if (address.equalsIgnoreCase(subscriber.getName())) {
                final int state = subscriber.getState();
                if (state == ViewStateObserver.STATE_RESUME || state == ViewStateObserver.STATE_READY) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Читать почту
     *
     * @param subscriber почтовый подписчик
     */
    @Override
    public void readMail(final MessagerSubscriber subscriber) {
        if (subscriber == null) return;

        final List<Mail> list = getMail(subscriber);
        for (Mail mail : list) {
            final int state = subscriber.getState();
            if (state == ViewStateObserver.STATE_RESUME || state == ViewStateObserver.STATE_READY) {
                mail.read(subscriber);
                removeMail(mail);
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
            mMail.remove(mail.getId());
        }
    }

    @Override
    public void clearMail() {
        mMail.clear();
    }

    @Override
    public void stop() {
        clearMail();
        mMailingList.clear();

        super.stop();
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (MessagerUnion.class.isInstance(o)) ? 0 : 1;
    }
}
