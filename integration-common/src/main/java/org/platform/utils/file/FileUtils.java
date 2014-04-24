package org.platform.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 文件操作类*/
public final class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
    }

    /**
     * NIO拷贝文件
     * @param in 需要拷贝的文件
     * @param out 目标文件
     * @throws IOException 异常
     */
    @SuppressWarnings("resource")
	public static void copyFile(File in, File out) throws IOException {
        FileChannel fcin = null;
        FileChannel fcout = null;
        try {
            fcin = new FileInputStream(in).getChannel();
            fcout = new FileOutputStream(out).getChannel();
            int maxCount = 64 * 1024 * 1024 - 32 * 1024;
            long size = fcin.size();
            long position = 0;
            while (position < size) {
                position += fcin.transferTo(position, maxCount, fcout);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (fcin != null) {
                fcin.close();
            }
            if (fcout != null) {
                fcout.close();
            }
        }
    }

    /**
     * 将输入流写入文件
     * @param is 输入流
     * @param file 文件
     * @throws IOException 异常
     */
    public static void streamToFile(InputStream is, File file) throws IOException {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            int len = 0;
            byte[] buff = new byte[4096];
            while ((len = is.read(buff)) != -1) {
                bos.write(buff, 0, len);
            }
        } finally {
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * 列出一个文件夹下的所有文件
     * @param dir 文件夹
     * @return 该文件夹下的所有文件
     */
    public static List<File> listFiles(File dir) {
        return listFiles(dir, false);
    }

    /**
     * 列出一个文件夹下的所有文件
     * @param dir 文件夹
     * @param includeDirectory 返回值中是否包含文件夹
     * @return 该文件夹下的所有文件
     */
    public static List<File> listFiles(File dir, boolean includeDirectory) {
        List<File> filesList = new ArrayList<File>();
        if (dir.exists()) {
            if (dir.isDirectory()) {
                for (File file : dir.listFiles()) {
                    if (file.isDirectory()) {
                        if (includeDirectory) {
                            filesList.add(file);
                        }
                        filesList.addAll(listFiles(file, includeDirectory));
                    } else {
                        filesList.add(file);
                    }
                }
            } else {
                filesList.add(dir);
            }
        }
        return filesList;
    }

    /**
     * 返回特定文件夹下的文件总大小
     * @param directory 文件夹
     * @return 特定文件夹下的文件总大小
     */
    public static long directorySize(File directory) {
        return directorySize(directory, null);
    }

    /**
     * 返回特定文件夹下的文件总大小
     * @param directory 文件夹
     * @param excludeFiles 排除的文件
     * @return 特定文件夹下的文件总大小
     */
    public static long directorySize(File directory, Set<File> excludeFiles) {
        if (directory.isDirectory()) {
            long size = 0L;
            File[] files = directory.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (excludeFiles != null && excludeFiles.contains(f)) {
                        continue;
                    }
                    if (f.isFile()) {
                        size += f.length();
                    } else {
                        size += directorySize(f, excludeFiles);
                    }
                }
            }
            return size;
        }
        return directory.length();
    }

    /**
     * 删除文件夹或者文件
     * @param path 需要删除的文件夹或者文件
     * @param stopWhenFail 如果某个路径或文件删除失败，是否终止
     * @return 是否删除成功。如果 stopWhenFail 为 false，则总是返回 true
     */
    public static boolean deleteDirectory(File path, boolean stopWhenFail) {
        if (path == null || !path.exists()) {
            return true;
        }
        if (path.isFile()) {
            if (!path.delete()) {
                logger.warn("Failed to delete file - " + path);
                if (stopWhenFail) {
                    return false;
                }
            }
        } else {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (!file.delete()) {
                            logger.warn("Failed to delete file - " + file);
                            if (stopWhenFail) {
                                return false;
                            }
                        }
                    } else {
                        if (!deleteDirectory(file, stopWhenFail)) {
                            return false;
                        }
                    }
                }
            }
            if (!path.delete()) {
                logger.warn("Failed to delete file - " + path);
                if (stopWhenFail) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 压缩文件
     * @param zipFile 压缩的目标文件
     * @param includeParentFolder 压缩路径中是否需要包含此文件夹
     * @param comment 压缩文件注释信息，可以为 <code>null</code>
     * @param fileOrFolders 需要压缩的文件或文件夹
     * @throws IOException 异常
     */
    public static void zip(File zipFile, boolean includeParentFolder, String comment, File... fileOrFolders) throws IOException {
    	// 参数检查
        if (fileOrFolders == null) {
            throw new NullPointerException("fileOrFolder can not be null");
        } else if (zipFile == null) {
            throw new NullPointerException("zipFile can not be null");
        }

        // 处理压缩文件所在路径
        File zipParent = zipFile.getParentFile();
        if (zipParent != null && !zipParent.exists()) {
            zipParent.mkdirs();
        }
        ZipOutputStream zos = null;
        BufferedOutputStream bos = null;
        for (File fileOrFolder : fileOrFolders) {
            if (fileOrFolder == null) {
                throw new NullPointerException("fileOrFolder can not be null");
            } else if (!fileOrFolder.exists()) {
                logger.warn("Can not find fileOrFolder to zip: " + fileOrFolder);
                continue;
            }
            fileOrFolder = fileOrFolder.getCanonicalFile();
            List<File> files = listFiles(fileOrFolder);
            int len = 0;
            byte[] buffer = new byte[40960];
            int prefixLen = fileOrFolder.getPath().length() - fileOrFolder.getName().length();
            if (fileOrFolder.isDirectory() && !includeParentFolder) {
                prefixLen = fileOrFolder.getPath().length() + 1;
            }
            if (zos == null) {
                zos = new ZipOutputStream(new FileOutputStream(zipFile));
                if (comment != null) {
                    zos.setComment(comment);
                }
            }
            bos = new BufferedOutputStream(zos);
            for (File file : files) {
                ZipEntry entry = new ZipEntry(file.getPath().substring(prefixLen));
                entry.setSize(file.length());
                entry.setTime(file.lastModified());
                zos.putNextEntry(entry);
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                while ((len = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                bis.close();
                bos.flush();
                zos.closeEntry();
            }
        }
        zos.finish();
        bos.close();
        zos.close();
    }


    /**
     * 解压缩文件
     * @param zipFile 需要解压缩的文件
     * @param destPath 目标路径
     * @param delDestPathIfExists 如果目标路径存在，是否尝试先删除目标路径
     * @param update 如果文件存在，是否覆盖
     * @throws IOException 异常
     */
    public static void unzip(File zipFile, File destPath, boolean delDestPathIfExists, boolean update) throws IOException {
    	// 是否删除目标路径
        if (delDestPathIfExists && destPath.exists()) {
            deleteDirectory(destPath, false);
        }
        BufferedInputStream bis = null;
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile), Charset.defaultCharset());
            bis = new BufferedInputStream(zis);
            ZipEntry ze = null;
            byte[] buffer = new byte[40960];
            while ((ze = zis.getNextEntry()) != null) {
            	//删除所有解压缩中涉及到的文件相对路径
                String name = ze.getName().replace("..\\", "").replace("\\", File.separator);
                File destFile = new File(destPath, name);
                if (ze.isDirectory()) {
                    zis.closeEntry();
                    if ((!destFile.exists() || !destFile.isDirectory())
                        && !destFile.mkdirs()) {
                        logger.warn("Failed to mkdir: " + destFile);
                    }
                } else {
                	// 是否需要覆盖
                    if (destFile.exists() && !update) {
                        zis.closeEntry();
                        continue;
                    }
                    // 建立文件夹
                    if (!destFile.getParentFile().exists()) {
                        if (!destFile.getParentFile().mkdirs()) {
                            logger.warn("Failed to mkdir: " + destFile.getParentFile());
                        }
                    }
                    // 
                    if (destFile.exists() && !destFile.canWrite()) {
                        destFile.setWritable(true);
                    }
                    // 如果文件存在，强制设置为可写
                    BufferedOutputStream bos = null;
                    try {
                        bos = new BufferedOutputStream(new FileOutputStream(destFile));
                        int len;
                        while ((len = bis.read(buffer)) != -1) {
                            bos.write(buffer, 0, len);
                        }
                    } finally {
                        IOUtils.closeQuietly(bos);
                    }
                    zis.closeEntry();
                }
            }
        } finally {
            IOUtils.closeQuietly(bis);
        }
    }

    /**
     * 压缩文件到Zip
     * @param zipAlias 压缩后的文件
     * @param files 文件列表
     */
    public static void compressFiles2Zip(FileAlias zipAlias, FileAlias... files) {
        ZipArchiveOutputStream zaos = null;
        try {
            File zipFile = zipAlias.getFile();
            zaos = new ZipArchiveOutputStream(zipFile);
            zaos.setEncoding(Charset.defaultCharset().toString());
            //Use Zip64 extensions for all entries where they are required
            zaos.setUseZip64(Zip64Mode.AsNeeded);
            //将每个文件用ZipArchiveEntry封装
            //再用ZipArchiveOutputStream写到压缩文件中
            String parentPath = zipAlias.getAlias();
            for (FileAlias file : files) {
                String fileName = parentPath + File.separator + file.getAlias();
                ZipArchiveEntry zipArchiveEntry  = new ZipArchiveEntry(file.getFile(), fileName);
                zaos.putArchiveEntry(zipArchiveEntry);
                InputStream is = null;
                try {
                    is = new BufferedInputStream(new FileInputStream(file.getFile()));
                    IOUtils.copy(is, zaos);
                    zaos.closeArchiveEntry();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                } finally {
                    IOUtils.closeQuietly(is);
                }
            }
            zaos.finish();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(zaos);
        }
    }

}
