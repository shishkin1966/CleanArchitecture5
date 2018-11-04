package shishkin.cleanarchitecture.mvi.sl.viewaction;

public class ViewAction {

    private String name;
    private Object[] value;

    public ViewAction(String name) {
        this.name = name;
    }

    public ViewAction(String name, Object... value) {
        this(name);

        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value[0];
    }

    public Object getValue(int position) {
        return value[position];
    }
}
