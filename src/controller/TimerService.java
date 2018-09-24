
package controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;


public class TimerService extends ScheduledService<Integer> {

    private final IntegerProperty count = new SimpleIntegerProperty();

    public final void setCount(Integer value) {
        count.set(value);
    }

    public final Integer getCount() {
        return count.get();
    }

    public final IntegerProperty countProperty() {
        return count;
    }

    @Override
    protected Task<Integer> createTask() {
        return new Task<Integer>() {
            @Override
            protected Integer call() {
                //Adds 1 to the count
                count.set(getCount() + 1);
                return getCount();
            }
        };
    }
}
