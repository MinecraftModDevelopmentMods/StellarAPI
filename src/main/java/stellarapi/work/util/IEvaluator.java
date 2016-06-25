package stellarapi.work.util;

/**
 * Evaluator which evaluates floating-point value with certain input.
 * Native replacement for Function<T, Double>.
 * */
public interface IEvaluator<T> {
	public double evaluate(T input);
}
