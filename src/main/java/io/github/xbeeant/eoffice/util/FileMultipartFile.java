package io.github.xbeeant.eoffice.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author xiaobiao
 * @version 2022/1/19
 */
public class FileMultipartFile implements MultipartFile {

    private final String name;

    private final String originalFilename;

    @Nullable
    private final String contentType;

    private final byte[] content;

    public FileMultipartFile(
            String name, @Nullable String originalFilename, @Nullable String contentType, @Nullable byte[] content) {
        this.name = name;
        this.originalFilename = (originalFilename != null ? originalFilename : "");
        this.contentType = contentType;
        this.content = (content != null ? content : new byte[0]);
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    @NonNull
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    @Override
    @Nullable
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return (this.content.length == 0);
    }

    @Override
    public long getSize() {
        return this.content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return this.content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        FileCopyUtils.copy(this.content, dest);
    }
}
