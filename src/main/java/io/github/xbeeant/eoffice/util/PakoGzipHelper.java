package io.github.xbeeant.eoffice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * pako gzip压缩 解压缩
 *
 * @author xiaobiao
 * @date 2022/1/5
 */
public class PakoGzipHelper {
    private static final Logger logger = LoggerFactory.getLogger(PakoGzipHelper.class);

    /**
     * @param str：正常的字符串
     * @return 压缩字符串 类型为：  ³)°K,NIc i£_`Çe#  c¦%ÂXHòjyIÅÖ`
     */
    public static String compress(String str) {
        try {
            if (str == null || str.length() == 0) {
                return str;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes());
            gzip.close();
            return out.toString("ISO-8859-1");
        } catch (Exception e) {
            logger.error("gzip compress:" + str, e);
        }
        return "";
    }

    /**
     * @param str：类型为：  ³)°K,NIc i£_`Çe#  c¦%ÂXHòjyIÅÖ`
     * @return 解压字符串  生成正常字符串。
     */
    public static String uncompress(String str) {
        try {
            if (str == null || str.length() == 0) {
                return str;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(str
                    .getBytes(StandardCharsets.ISO_8859_1));
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return URLDecoder.decode(out.toString(), "UTF-8");
        } catch (Exception e) {
            logger.error("gzip uncompress:" + str, e);
        }
        return "";

    }
}
