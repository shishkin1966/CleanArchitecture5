package shishkin.cleanarchitecture.mvi.sl;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;


import java.util.Collection;
import java.util.Comparator;

/**
 * Интерфейс специалиста преобразования данных
 */
public interface DataSpecialist extends Specialist {

    /**
     * Отфильтровать коллекцию
     *
     * @param list      коллекция
     * @param predicate условие фильтрации
     */
    <T> Stream<T> filter(final Collection<T> list, final Predicate<? super T> predicate);

    /**
     * Отсортировать коллекцию
     *
     * @param list       коллекция
     * @param comparator условие сортировки
     */
    <T> Stream<T> sort(final Collection<T> list, final Comparator<? super T> comparator);
}
