/*
 * junixsocket
 *
 * Copyright 2009-2019 Christian Kohlschütter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gitlab.cdagaming.craftpresence.impl.junixsocket;

import java.util.Arrays;
import java.util.UUID;

/**
 * AF_UNIX socket credentials.
 */
public final class AFUNIXSocketCredentials {
    private long pid = -1; // NOPMD -- Set in native code
    private long uid = -1; // NOPMD -- Set in native code
    private long[] gids = null;
    private UUID uuid = null;

    AFUNIXSocketCredentials() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append('[');
        if (uid != -1) {
            sb.append("uid=").append(uid).append(";");
        }
        if (gids != null) {
            sb.append("gids=").append(Arrays.toString(gids)).append(";");
        }
        if (pid != -1) {
            sb.append("pid=").append(pid).append(";");
        }
        if (uuid != null) {
            sb.append("uuid=").append(uuid).append(";");
        }
        if (sb.charAt(sb.length() - 1) == ';') {
            sb.setLength(sb.length() - 1);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(gids);
        result = prime * result + (int) (pid ^ (pid >>> 32));
        result = prime * result + (int) (uid ^ (uid >>> 32));
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AFUNIXSocketCredentials other = (AFUNIXSocketCredentials) obj;
        if (!Arrays.equals(gids, other.gids))
            return false;
        if (pid != other.pid)
            return false;
        if (uid != other.uid)
            return false;
        if (uuid == null) {
            return other.uuid == null;
        } else return uuid.equals(other.uuid);
    }
}