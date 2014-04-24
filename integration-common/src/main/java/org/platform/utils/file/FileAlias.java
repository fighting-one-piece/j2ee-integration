package org.platform.utils.file;

import java.io.File;

/** 文件与别名映射文件  */
public class FileAlias {
	/** 文件的显示名称 */
    private String alias = null;
    /** 文件*/
    private File file = null;

    public FileAlias() {

    }

    public FileAlias(File file, String alias) {
        this.file = file;
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public int hashCode() {
        return file != null ? file.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (file != null && obj != null && obj instanceof FileAlias &&
            ((FileAlias) obj).getFile() != null && file.getAbsolutePath()
            .equals(((FileAlias) obj).getFile().getAbsolutePath())) {
              return true;
        }
        return false;
    }
}
