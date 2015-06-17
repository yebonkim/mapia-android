package com.mapia.camera.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daehyun on 15. 6. 16..
 */

public class RecordClipHistory
{
    private List<RecordClipFile> mFiles;

    public RecordClipHistory() {
        super();
        this.mFiles = null;
    }

    public boolean addClipFile(final RecordClipFile recordClipFile) {
        if (recordClipFile == null) {
            return false;
        }
        if (this.mFiles == null) {
            this.mFiles = new ArrayList<RecordClipFile>();
        }
        return this.mFiles.add(recordClipFile);
    }

    public RecordClipFile deleteLastClipFile() {
        if (this.mFiles != null && this.mFiles.size() > 0) {
            this.mFiles.remove(this.mFiles.size() - 1);
        }
        return null;
    }

    public List<RecordClipFile> getmFiles() {
        return this.mFiles;
    }

    public void setmFiles(final List<RecordClipFile> mFiles) {
        this.mFiles = mFiles;
    }
}