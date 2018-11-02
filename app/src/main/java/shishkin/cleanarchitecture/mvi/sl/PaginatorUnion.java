package shishkin.cleanarchitecture.mvi.sl;

import shishkin.cleanarchitecture.mvi.sl.paginator.Paginator;

public interface PaginatorUnion extends SmallUnion<Paginator> {
    /**
     * получить Paginator
     *
     * @param name имя Paginator
     */
    Paginator getPaginator(String name);
}
