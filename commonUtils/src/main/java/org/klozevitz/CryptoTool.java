package org.klozevitz;

import org.hashids.Hashids;

public class CryptoTool {
    private final Hashids hashId;

    /**
     * Избавляемся от так называемых magic numbers- чисел, закинутых в аргументы метода
     * без объявления переменной. Не понятно, откуда они взялись, что обозначают- говнокод)
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
