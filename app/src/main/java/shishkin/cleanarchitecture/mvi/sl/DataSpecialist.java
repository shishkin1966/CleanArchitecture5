package shishkin.cleanarchitecture.mvi.sl;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;


import java.util.Collection;
import java.util.Comparator;

/**
 * Интерфейс специалиста преобразования данных
 */
public interface DataSpecialist extends Specialist {

    <T> Stream<T> filter(final Collection<T> list, final Predicate<? super T> predicate);

    <T> Stream<T> sort(final Collection<T> list, final Comparator<? super T> comparator);
}
