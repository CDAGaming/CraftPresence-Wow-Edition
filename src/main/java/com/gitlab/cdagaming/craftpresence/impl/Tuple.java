/*
 * MIT License
 *
 * Copyright (c) 2018 - 2019 CDAGaming (cstack2011@yahoo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.gitlab.cdagaming.craftpresence.impl;

/**
 * A Set of Two Objects contained within a Mapping
 *
 * @param <U> The first element of this {@link Tuple}
 * @param <V> The second element of this {@link Tuple}
 */
public class Tuple<U, V> {

    /**
     * The first element of this {@link Tuple}
     */
    private U first;

    /**
     * The second element of this {@link Tuple}
     */
    private V second;

    /**
     * Constructs a new {@link Tuple} with the given values.
     *
     * @param first  the first element
     * @param second the second element
     */
    public Tuple(U first, V second) {
        put(first, second);
    }

    /**
     * Constructs a new empty {@link Tuple}.
     */
    public Tuple() {
        // N/A
    }

    /**
     * Retrieves the first element of this {@link Tuple}.
     *
     * @return the first element of this {@link Tuple}
     */
    public U getFirst() {
        return first;
    }

    /**
     * Sets the first element of this {@link Tuple} to the given value.
     *
     * @param first the first element to be applied
     */
    public void setFirst(U first) {
        this.first = first;
    }

    /**
     * Retrieves the first element of this {@link Tuple}.
     *
     * @return the second element of this {@link Tuple}
     */
    public V getSecond() {
        return second;
    }

    /**
     * Sets the second element of this {@link Tuple} to the given value.
     *
     * @param second the second element to be applied
     */
    public void setSecond(V second) {
        this.second = second;
    }

    /**
     * Sets the elements of this {@link Tuple} to the given values.
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
