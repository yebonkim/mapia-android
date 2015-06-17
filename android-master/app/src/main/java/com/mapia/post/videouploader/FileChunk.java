package com.mapia.post.videouploader;

import java.io.File;

/**
 * Created by daehyun on 15. 6. 13..
 */
public class FileChunk
{
    public File file;
    public int sequence;

    public FileChunk(final int sequence, final File file) {
        super();
        this.sequence = sequence;
        this.file = file;
    }
}
