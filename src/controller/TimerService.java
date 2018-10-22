
package controller;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;


public class TimerService extends ScheduledService<Void> {


    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                return null;               
            }
        };
    }
}
