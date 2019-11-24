package com.gitlab.cdagaming.craftpresence.impl;

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

    /**
     * Determines if elements in two different Tuple's are equivalent
     *
     * @param obj The Tuple to compare against
     * @return If the Two Opposing Tuple's are equivalent
     */
    public boolean equals(Tuple<?, ?> obj) {
        try {
            // Case 1: Attempt ToString Conversion Checking
            return (this.getFirst().toString().equals(obj.getFirst().toString())) && (this.getSecond().toString().equals(obj.getSecond().toString()));
        } catch (Exception ex) {
            // Case 2: Automated Checking
            // Note: Can Likely return false positives depending on conditions
            return ((this.getFirst() == obj.getFirst()) && (this.getSecond() == obj.getSecond())) || (this == obj);
        }
    }
}
