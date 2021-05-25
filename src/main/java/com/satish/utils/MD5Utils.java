package com.satish.utils;

import org.apache.commons.codec.digest.DigestUtils;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.MD5;

/**
 * @author satish.thulva
 **/
public class MD5Utils {

    private static DigestUtils digestUtils = new DigestUtils(MD5);

    public synchronized static String md5AsHexString(String input) {
        return digestUtils.digestAsHex(input);
    }

}
