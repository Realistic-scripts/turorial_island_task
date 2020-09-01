package state;


public interface TaskState {
    public abstract Boolean run();

    public abstract Boolean verify();

    public abstract TaskState previousState();

    public abstract TaskState nextState();
}
