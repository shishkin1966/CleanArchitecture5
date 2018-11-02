package shishkin.cleanarchitecture.mvi.sl;

import androidx.annotation.NonNull;


import shishkin.cleanarchitecture.mvi.sl.paginator.Paginator;
import shishkin.cleanarchitecture.mvi.sl.paginator.PaginatorFactory;

public class PaginatorUnionImpl extends AbsSmallUnion<Paginator> implements PaginatorUnion {

    public static final String NAME = PaginatorUnionImpl.class.getName();

    private PaginatorFactory factory = new PaginatorFactory();

    @Override
    public Secretary createSecretary() {
        return new SecretaryImpl<>();
    }

    @Override
    public Paginator getPaginator(String name) {
        if (getSubscriber(name) == null) {
            register(factory.create(name));
        }
        return getSubscriber(name);
    }

    @Override
    public void register(final Paginator paginator) {
        if (paginator == null) return;

        if (hasSubscriber(paginator.getName())) {
            final Paginator oldpaginator = getSubscriber(paginator.getName());
            if (oldpaginator != null) {
                oldpaginator.stop();
            }
        }
        super.register(paginator);
    }


    @Override
    public void unregister(final Paginator paginator) {
        if (paginator == null) return;

        paginator.stop();
        super.unregister(paginator);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (PaginatorUnion.class.isInstance(o)) ? 0 : 1;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void stop() {
        for (Paginator paginator : getSubscribers()) {
            paginator.stop();
        }
        super.stop();
    }

}
