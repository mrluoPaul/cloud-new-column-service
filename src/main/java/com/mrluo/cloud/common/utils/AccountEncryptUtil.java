/**
 * Copyright 2018-2020 by Admin. All Rights Reserved.
 * <p>
 * <p>
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose and without fee is hereby granted,
 * provided that the above copyright notice appear in all copies and that
 * both that copyright notice and this permission notice appear in
 * supporting documentation, and that the name of Vinay Sajip
 * not be used in advertising or publicity pertaining to distribution
 * of the software without specific, written prior permission.
 * VINAY SAJIP DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE, INCLUDING
 * ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL
 * VINAY SAJIP BE LIABLE FOR ANY SPECIAL, INDIRECT OR CONSEQUENTIAL DAMAGES OR
 * ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER
 * IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT
 * OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.mrluo.cloud.common.utils;

import com.mrluo.cloud.common.exception.BusinessException;
import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.crypto.symmetric.AES;
import com.xiaoleilu.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.xiaoleilu.hutool.crypto.symmetric.SymmetricCrypto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 登录，找回密码用的加密
 *
 * @author lpeng
 * @version 1.0.0
 * @date 2020/06/05 10:24
 */
public class AccountEncryptUtil {

    private static String ENCRYPT_KEY = "mrluo";

    private static AccountEncryptUtil instance = new AccountEncryptUtil();

    private AccountEncryptUtil() {
    }

    public static AccountEncryptUtil getInstance() {
        return instance;
    }

    public static String encrypt(String content) {
        byte[] key = SecureUtil.generateDESKey(SymmetricAlgorithm.DESede.getValue(), ENCRYPT_KEY.getBytes()).getEncoded();
        SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.DESede, key);
        return des.encryptHex(content);
    }

    public static String decrypt(String content) {
        byte[] key = SecureUtil.generateDESKey(SymmetricAlgorithm.DESede.getValue(), ENCRYPT_KEY.getBytes()).getEncoded();
        SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.DESede, key);
        return des.decryptStr(content);
    }
}
