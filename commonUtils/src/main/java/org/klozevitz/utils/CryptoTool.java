package org.klozevitz.utils;

import org.hashids.Hashids;

public class CryptoTool {
    private final Hashids hashId;

    /**
     * Getting rid of "magic numbers"- numbers, that have not been assigned to any variable&
     * */
    public CryptoTool(String salt) {
        int minHashLength = 10;
        this.hashId = new Hashids(salt, minHashLength);
    }

    public String hashOf(Long value) {
        return hashId.encode(value);
    }

    public Long idOf(String value) {
        long[] result = hashId.decode(value);
        if (result != null && result.length > 0) {
            return result[0];
        }
        return null;
    }
}
