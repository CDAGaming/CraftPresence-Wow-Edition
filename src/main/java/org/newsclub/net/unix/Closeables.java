/*
 * junixsocket
 * <p>
 * Copyright 2009-2019 Christian Kohlschütter
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.newsclub.net.unix;

import com.google.common.collect.Lists;

import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;

/**
 * A set of {@link Closeables} that can be closed at once.
 *
 * @author Christian Kohlschütter
 */
public final class Closeables implements Closeable {
    private List<WeakReference<Closeable>> list = Lists.newArrayList();

    public Closeables() {
    }

    public Closeables(Closeable... closeable) {
        for (Closeable cl : closeable) {
            this.list.add(new HardReference<>(cl));
        }
    }

    @Override
    public void close() throws IOException {
        close(null);
    }

    /**
     * Closes all registered closeables.
     *
     * @param superException If set, any exceptions thrown in here will be chained to the given
     *                       exception via addSuppressed, and then thrown.
     * @throws IOException if an exception occurs.
     */
    public void close(IOException superException) throws IOException {
        IOException exc = superException;

        if (list != null) {
            for (WeakReference<Closeable> ref : list) {
                @SuppressWarnings("resource")
                Closeable cl = ref.get();
                if (cl == null) {
                    continue;
                }
                try {
                    cl.close();
                } catch (IOException e) {
                    if (exc == null) {
                        exc = e;
                    } else {
                        exc.addSuppressed(e);
                    }
                }
            }
        }

        if (exc != null) {
            throw exc;
        }
    }

    /**
     * Adds the given closeable, but only using a weak reference.
     *
     * @param closeable The closeable.
     * @return {@code true} iff the closeable was added, {@code false} if it was {@code null} or
     * already added before.
     */
    @SuppressWarnings("resource")
    public synchronized boolean add(WeakReference<Closeable> closeable) {
        Closeable cl = closeable.get();
        if (cl == null) {
            // ignore
            return false;
        }
        if (list == null) {
            list = Lists.newArrayList();
        } else {
            for (WeakReference<Closeable> ref : list) {
                if (ref.get() == cl) {
                    return false;
                }
            }
        }
        list.add(closeable);

        return true;
    }

    /**
     * Adds the given closeable.
     *
     * @param closeable The closeable.
     * @return {@code true} iff the closeable was added, {@code false} if it was {@code null} or
     * already added before.
     */
    public synchronized boolean add(Closeable closeable) {
        return add(new HardReference<>(closeable));
    }

    /**
     * Removes the given closeable.
     *
     * @param closeable The closeable.
     * @return {@code true} iff the closeable was removed, {@code fale} if it was {@code null} or not
     * previously added.
     */
    public synchronized boolean remove(Closeable closeable) {
        if (list == null || closeable == null) {
            return false;
        }
        for (Iterator<WeakReference<Closeable>> it = list.iterator(); it.hasNext(); ) {
            if (it.next().get() == closeable) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    private static final class HardReference<V> extends WeakReference<V> {
        private final V strongRef;

        private HardReference(final V referent) {
            super(null);
            this.strongRef = referent;
        }

        @Override
        public V get() {
            return strongRef;
        }
    }
}
