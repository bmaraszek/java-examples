package pl.bmaraszek.concurrency.executor;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * invokeAny() receives a list of tasks, launches them, and returns the result of the first task that finishes
 * without throwing an exception. This metod returns the same type that the call() method of the tasks return.
 *
 * Here we have 4 possibilites:
 *  - both tasks return true: invokeAny() returns the name of the task that finished first
 *  - first task returns true and second throws: the result of invokeAny() is the name of the first task
 *  - first task throws and second returns true: the result of invokeAny() is the name of the second task
 *  - both tasks throw: invokeAny() throws an ExecutionException
 *
 * invokeAny() also have a versin with timout.
 */
public class ExecutorProcessFirstResultExample {
    public static void main(String[] args) {
        String username = "test";
        String password = "test";

        UserValidator ldapValidator = new UserValidator("LDAP");
        UserValidator dbValidator = new UserValidator("Database");

        ValidatorTask ldapTask = new ValidatorTask(ldapValidator, username, password);
        ValidatorTask dbTask = new ValidatorTask(dbValidator, username, password);

        List<ValidatorTask> taskList = List.of(ldapTask, dbTask);

        ExecutorService executor = Executors.newCachedThreadPool();
        String result;
        try {
            result = executor.invokeAny(taskList);
            System.out.printf("Main: Result: %s\n", result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
        System.out.printf("Main: End of execution\n");
    }
}

@AllArgsConstructor
@Getter
class UserValidator {
    private final String name;

    public boolean validate(String username, String password) {
        Random random = new Random();
        long duration = (long)(Math.random()*10);
        System.out.printf("Validator %s: Validating user during %d seconds\n", name, duration);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            return false;
        }
        return random.nextBoolean();
    }
}

@AllArgsConstructor
class ValidatorTask implements Callable<String> {
    private final UserValidator validator;
    private final String user;
    private final String password;

    @Override
    public String call() throws Exception {
        if(!validator.validate(user, password)) {
            System.out.printf("%s: user not found\n", validator.getName());
            throw new Exception("Error validating user");
        }
        System.out.printf("%s: user found\n", validator.getName());
        return validator.getName();
    }
}

