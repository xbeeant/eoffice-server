package io.github.xbeeant.eoffice.util;

import io.github.xbeeant.core.exception.FolderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaobiao
 * @version 2021/7/1
 */
public class FileHelper {
    private static final Logger logger = LoggerFactory.getLogger(FileHelper.class);


    private FileHelper() {
        // do nothing
    }

    public static String getPath(String folderName) {
        //获取跟目录---与jar包同级目录的upload目录下指定的子目录subdirectory
        File upload;
        try {
            //本地测试时获取到的是"工程目录/target/upload/subdirectory
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!path.exists()) {
                path = new File("");
            }
            upload = new File(path.getAbsolutePath(), folderName);
            if (!upload.exists()) {
                //如果不存在则创建目录
                boolean mkdirs = upload.mkdirs();
                if (!mkdirs) {
                    throw new FolderNotFoundException("文件夹创建失败！");
                }
            }
            return upload + "/";
        } catch (FileNotFoundException e) {
            throw new FolderNotFoundException("获取服务器路径发生错误！");
        }
    }

    public static String extension(String filename) {
        if (filename == null) {
            return "";
        }
        int idx = filename.lastIndexOf(".");
        return filename.substring(idx + 1);
    }

    public static String md5(String string) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");

        // Get file input stream for reading the file
        // content
        byte[] bs = digest.digest(string.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder(40);
        for (byte x : bs) {
            if ((x & 0xff) >> 4 == 0) {
                sb.append("0").append(Integer.toHexString(x & 0xff));
            } else {
                sb.append(Integer.toHexString(x & 0xff));
            }
        }
        return sb.toString();
    }

    public static String md5(File file) throws NoSuchAlgorithmException {
        // instantiate a MessageDigest Object by passing
        // string "MD5" this means that this object will use
        // MD5 hashing algorithm to generate the checksum
        MessageDigest digest = MessageDigest.getInstance("MD5");

        // Get file input stream for reading the file
        // content
        try (FileInputStream fis = new FileInputStream(file);) {
            // Create byte array to read data in chunks
            byte[] byteArray = new byte[1024];
            int bytesCount = 0;

            // read the data from file and update that data in
            // the message digest
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        } catch (Exception e) {
            logger.error("文件读取异常", e);
        }

        // store the bytes returned by the digest() method
        byte[] bytes = digest.digest();

        // this array of bytes has bytes in decimal format
        // so we need to convert it into hexadecimal format

        // for this we create an object of StringBuilder
        // since it allows us to update the string i.e. its
        // mutable
        StringBuilder sb = new StringBuilder();

        // loop through the bytes array
        for (byte aByte : bytes) {
            // the following line converts the decimal into
            // hexadecimal format and appends that to the
            // StringBuilder object
            sb.append(Integer
                    .toString((aByte & 0xff) + 0x100, 16)
                    .substring(1));
        }

        // finally we return the complete hash
        return sb.toString();
    }

    public static String md5(MultipartFile upload) throws NoSuchAlgorithmException {
        // instantiate a MessageDigest Object by passing
        // string "MD5" this means that this object will use
        // MD5 hashing algorithm to generate the checksum
        MessageDigest digest = MessageDigest.getInstance("MD5");

        // Get file input stream for reading the file
        // content
        try (InputStream fis = upload.getInputStream();) {
            // Create byte array to read data in chunks
            byte[] byteArray = new byte[1024];
            int bytesCount = 0;

            // read the data from file and update that data in
            // the message digest
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        } catch (Exception e) {
            logger.error("文件读取异常", e);
        }

        // store the bytes returned by the digest() method
        byte[] bytes = digest.digest();

        // this array of bytes has bytes in decimal format
        // so we need to convert it into hexadecimal format

        // for this we create an object of StringBuilder
        // since it allows us to update the string i.e. its
        // mutable
        StringBuilder sb = new StringBuilder();

        // loop through the bytes array
        for (byte aByte : bytes) {
            // the following line converts the decimal into
            // hexadecimal format and appends that to the
            // StringBuilder object
            sb.append(Integer
                    .toString((aByte & 0xff) + 0x100, 16)
                    .substring(1));
        }

        // finally we return the complete hash
        return sb.toString();
    }

    public static String getStoragePath() {
        return getPath("storage");
    }
}
