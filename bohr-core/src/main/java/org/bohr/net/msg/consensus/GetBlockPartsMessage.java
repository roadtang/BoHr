/**
 * Copyright (c) 2019 The Bohr Developers
 *
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.bohr.net.msg.consensus;

import org.bohr.net.msg.Message;
import org.bohr.net.msg.MessageCode;
import org.bohr.util.SimpleDecoder;
import org.bohr.util.SimpleEncoder;

public class GetBlockPartsMessage extends Message {

    private final long number;
    private final int parts;

    public GetBlockPartsMessage(long number, int parts) {
        super(MessageCode.GET_BLOCK_PARTS, BlockPartsMessage.class);

        this.number = number;
        this.parts = parts;

        SimpleEncoder enc = new SimpleEncoder();
        enc.writeLong(number);
        enc.writeInt(parts);
        this.body = enc.toBytes();
    }

    public GetBlockPartsMessage(byte[] body) {
        super(MessageCode.GET_BLOCK_PARTS, BlockPartsMessage.class);

        SimpleDecoder dec = new SimpleDecoder(body);
        this.number = dec.readLong();
        this.parts = dec.readInt();

        this.body = body;
    }

    public long getNumber() {
        return number;
    }

    public int getParts() {
        return parts;
    }

    @Override
    public String toString() {
        return "GetBlockPartsMessage [number=" + number + ", parts = " + parts + "]";
    }
}
