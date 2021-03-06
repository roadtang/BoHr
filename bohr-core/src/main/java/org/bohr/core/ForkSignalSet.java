/**
 * Copyright (c) 2019 The Bohr Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.bohr.core;

import org.bohr.util.SimpleDecoder;
import org.bohr.util.SimpleEncoder;
import org.bohr.util.exception.UnreachableException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ForkSignalSet {

    public static final int MAX_PENDING_FORKS = 8;

    private final Set<Short> pendingForks;

    private ForkSignalSet(Set<Short> pendingForks) {
        this.pendingForks = pendingForks;
    }

    public static ForkSignalSet of(Fork... forks) {
        return of(Arrays.asList(forks));
    }

    public static ForkSignalSet of(Collection<Fork> forks) {
        if (forks.size() > MAX_PENDING_FORKS) {
            throw new UnreachableException("There must be no more than " + MAX_PENDING_FORKS + " pending forks.");
        }

        return new ForkSignalSet(forks.stream().map(f -> f.id()).collect(Collectors.toSet()));
    }

    public static ForkSignalSet fromBytes(byte[] bytes) {
        SimpleDecoder decoder = new SimpleDecoder(bytes);
        byte size = decoder.readByte();
        Set<Short> forks = new HashSet<>();
        for (int i = 0; i < size; i++) {
            forks.add(decoder.readShort());
        }
        return new ForkSignalSet(forks);
    }

    public boolean contains(Fork fork) {
        return pendingForks.contains(fork.id());
    }

    public byte[] toBytes() {
        SimpleEncoder encoder = new SimpleEncoder();
        encoder.writeByte((byte) pendingForks.size());
        for (Short pendingFork : pendingForks) {
            encoder.writeShort(pendingFork);
        }
        return encoder.toBytes();
    }
}
