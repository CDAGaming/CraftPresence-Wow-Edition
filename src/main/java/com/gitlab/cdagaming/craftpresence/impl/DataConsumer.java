package com.gitlab.cdagaming.craftpresence.impl;

/**
 * Represents an operation that accepts a single input argument and returns no
 * result. Unlike most other functional interfaces, {@code Consumer} is expected
 * to operate via side-effects.
 *
 * <p>This is a functional interface,
 * whose functional method is {@link #accept(Object)}.
 *
 * @param <T> the type of the input to the operation
 */
public interface DataConsumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t);

    /**
     * Returns a composed {@code DataConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is reported to the original caller.
     * <p>
     * If performing this operation throws an exception or is null,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this
     * operation followed by the {@code after} operation
     */
    default DataConsumer<T> andThen(DataConsumer<? super T> after) {
        if (after != null) {
            return (T t) -> {
                accept(t);
                after.accept(t);
            };
        } else {
            return null;
        }
    }
}

