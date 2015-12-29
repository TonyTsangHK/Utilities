package utils.math;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2013-07-30
 * Time: 11:30
 */
public class Counter {
    private long counter, step;

    public Counter() {
        this(0, 1);
    }

    public Counter(long startValue, long step) {
        setCounter(startValue);
        setStep(step);
    }

    public Counter increment() {
        counter += step;
        return this;
    }

    public Counter decrement() {
        counter -= step;
        return this;
    }

    public Counter setCounter(long counter) {
        this.counter = counter;
        return this;
    }

    public long getCounter() {
        return counter;
    }

    public long getStep() {
        return step;
    }

    public Counter setStep(long step) {
        this.step = step;
        return this;
    }

    @Override
    public String toString() {
        return String.valueOf(counter);
    }
}
