/*引入CryptoJS加密,此处替换为CDN*/

// import CryptoJS from "crypto-js";

/**
 * base64加密
 * @param word
 * @returns {string}
 */
function base64Encrypt(word) {
    if (null == word) {
        return null;
    }
    let wordArray = CryptoJS.enc.Utf8.parse(word);
    return CryptoJS.enc.Base64.stringify(wordArray);
}

/**
 * base64解密
 * @param wordArray
 * @returns {*}
 */
function base64Decrypt(wordArray) {
    if (null == wordArray) {
        return null;
    }
    let parsedWordArray = CryptoJS.enc.Base64.parse(wordArray);
    return parsedWordArray.toString(CryptoJS.enc.Utf8);
}

/**
 * md5加密
 * @param str
 * @returns {*}
 */
function md5(str) {
    if (null == str) {
        return null;
    }
    str = `weiziplus-${str}`;
    return CryptoJS.MD5(str).toString().toUpperCase();
}

/**
 * md5加密没有加密盐
 * @param str
 * @returns {*}
 */
function md5NoSalt(str) {
    if (null == str) {
        return null;
    }
    return CryptoJS.MD5(str).toString().toUpperCase();
}

/**
 * 将方法暴露出去
 */
export default {
    base64Encrypt,
    base64Decrypt,
    md5,
    md5NoSalt,
};