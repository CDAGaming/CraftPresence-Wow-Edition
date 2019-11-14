package com.gitlab.cdagaming.craftpresence.utils;

public class Tuple<U, V> {

    /**
     * The first element of this <code>Tuple</code>
     */
    private U first;

    /**
     * The second element of this <code>Tuple</code>
     */
    private V second;

    /**
     * Constructs a new <code>Tuple</code> with the given values.
     *
     * @param first  the first element
     * @param second the second element
     */
    public Tuple(U first, V second) {
        put(first, second);
    }

    /**
     * Constructs a new empty <code>Tuple</code>.
     */
    public Tuple() {
        //
    }

    /**
     * Retrieves the first element of this <code>Tuple</code>.
     */
    public U getFirst() {
        return first;
    }

    /**
     * Sets the first element of this <code>Tuple</code> to the given value.
     *
     * @param first the first element to be applied
     */
    public void setFirst(U first) {
        this.first = first;
    }

    /**
     * Retrieves the first element of this <code>Tuple</code>.
     */
    public V getSecond() {
        return second;
    }

    /**
     * Sets the second element of this <code>Tuple</code> to the given value.
     *
     * @param second the second element to be applied
     */
    public void setSecond(V second) {
        this.second = second;
    }

    /**
     * Sets the elements of this <code>Tuple</code> to the given values.
     *
     * @param first  the first element to be applied
     * @param second the second element to be applied
     */
    public void put(U first, V second) {
        this.first = first;
        this.second = second;
    }
}
