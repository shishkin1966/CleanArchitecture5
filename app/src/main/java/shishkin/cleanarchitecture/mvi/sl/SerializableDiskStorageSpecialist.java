package shishkin.cleanarchitecture.mvi.sl;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public interface SerializableDiskStorageSpecialist extends Specialist {
    /**
     * Put value to storage.
     *
     * @param key     the key
     * @param value   the value
     * @param expired expired date
     */
    void put(String key, Serializable value, long expired);

    /**
     * Check expired period all keys.
     */
    void check();

    /**
     * Put value to storage.
     *
     * @param key   the key
     * @param value the value
     */
    void put(String key, Serializable value);

    /**
     * Put values to storage.
     *
     * @param key    the key
     * @param values the list of values
     */
    void put(String key, List<Serializable> values);

    /**
     * Get value from storage.
     *
     * @param key the key
     * @return the value
     */
    Serializable get(String key);

    /**
     * Get values from storage.
     *
     * @param key the key
     * @return the list of values
     */
    List<Serializable> getList(String key);

    /**
     * Get value from storage.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the value
     */
    Serializable get(final String key, final Serializable defaultValue);

    /**
     * Clear value.
     *
     * @param key the key
     */
    void clear(String key);

    /**
     * Clear all values.
     */
    void clear();

    /**
     * Convert list to serializable
     *
     * @param list the list
     */
    <T> Serializable toSerializable(List<T> list);

    /**
     * Convert serializable to list
     *
     * @param value the serializable
     */
    <T> List<T> serializableToList(Serializable value);

    /**
     * Convert serializable to Json
     *
     * @param obj the object
     */
    <T> Serializable toJson(final T obj);

    /**
     * Convert serializable to Json
     *
     * @param obj  the object
     * @param type the object type
     */
    <T> Serializable toJson(final T obj, Type type);

    /**
     * Convert Json to serializable
     *
     * @param json the json
     * @param cl   the object class
     */
    <T> T fromJson(final String json, final Class<T> cl);

    /**
     * Convert Json to serializable
     *
     * @param json the json
     * @param type the object type
     */
    <T> T fromJson(final String json, Type type);

}
