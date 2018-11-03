package shishkin.cleanarchitecture.mvi.sl;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;


import java.util.Collection;
import java.util.Comparator;


import androidx.annotation.NonNull;

/**
 * Специалист преобразования данных
 */
@SuppressWarnings("unused")
public class DataSpecialistImpl extends AbsSpecialist implements DataSpecialist {
    public static final String NAME = DataSpecialistImpl.class.getName();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public <T> Stream<T> filter(final Collection<T> list, final Predicate<? super T> predicate) {
        return Stream.of(list).filter(predicate);
    }

    @Override
    public <T> Stream<T> sort(final Collection<T> list, final Comparator<? super T> comparator) {
        return Stream.of(list).sorted(comparator);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (DataSpecialist.class.isInstance(o)) ? 0 : 1;
    }

}
