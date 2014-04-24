package org.platform.utils.jacob;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.SystemUtils;
import org.platform.utils.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.LibraryLoader;
import com.jacob.com.Variant;

public class JacobUtils {

    private static Logger logger = LoggerFactory.getLogger(JacobUtils.class);

    private static JacobUtils instance = new JacobUtils();

    public static JacobUtils getInstance() {
        return instance;
    }

    private static final int WORD_WORD = 0;

    private static final int WORD_PDF = 17;

    private boolean wordAppInitialized = false;

    private ActiveXComponent wordApp = null;

    private Dispatch wordInstance = null;

    public void convertXmlToFile(String tempNo, File xmlFile, File dstFile) throws Exception {
        File tmpFile = new File(System.getProperty("user.dir"), "template/"
            + tempNo + ".doc");
        File srcFile;
        try {
            srcFile = File.createTempFile("doc", ".doc");
            FileUtils.copyFile(tmpFile, srcFile);
            convertXmlToFile(srcFile, xmlFile, dstFile);
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void convertXmlToFile(File srcFile, File xmlFile, File dstFile) throws Exception {
        try {
            invokeMacro(srcFile, dstFile, "loadXmlData", xmlFile.getCanonicalPath());
        } catch (IOException e) {
            logger.warn("cannot find xml file: '{}' ", xmlFile);
        }
    }

    private JacobUtils() {
        init();
    }

    private void init() {
        if (SystemUtils.IS_OS_WINDOWS) {
            String suffix = "x86".equals(SystemUtils.OS_ARCH) ? "x86.dll" : "x64.dll";
            File lib = new File(System.getProperty("user.dir"), "libs/jacob-"
                + suffix);
            if (lib.exists()) {
                System.setProperty(LibraryLoader.JACOB_DLL_PATH, lib.getPath());
            }
        }
    }

    public void releaseResources() {
        if (wordApp != null) {
            try {
                wordApp.invoke("Quit", new Variant[] {});
            } catch (Exception e) {
                logger.warn("Quit word app failed", e);
            }
            wordApp = null;
            wordInstance = null;
        }
        ComThread.Release();
    }

    private boolean wordConverterAvailable() {
        if (!wordAppInitialized) {
            wordAppInitialized = true;
            try {
                wordApp = new ActiveXComponent("Word.Application");
                wordApp.setProperty("Visible", new Variant(false));
                wordInstance = wordApp.getProperty("Documents").toDispatch();
                return true;
            } catch (Exception e) {
                logger.warn("Failed to init word application", e);
            }
        }
        return wordInstance != null;
    }

    private void invokeMacro(File srcFile, File dstFile, Object... macros) throws Exception {
        if (!wordConverterAvailable()) {
            throw new Exception("Word Application is not available");
        }
        try {
            String dstName = dstFile.getName().toLowerCase();
            String suffix = dstName.substring(dstName.lastIndexOf(".") + 1).toLowerCase();
            if (!("pdf".equals(suffix) || "doc".equals(suffix) || "docx".equals(suffix))) {
                throw new Exception("dst file type is not correct");
            }
            String word = srcFile.getCanonicalPath();
            String dstPath = dstFile.getCanonicalPath();
            Dispatch doc = Dispatch.invoke(wordInstance, "Open", Dispatch.Method,
                new Object[] { word, new Variant(false), new Variant(false)},
                new int[1]).toDispatch();
            Dispatch.call(wordApp, "Run", macros);
            Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {
                dstPath, !"pdf".equals(suffix) ? WORD_WORD : WORD_PDF}, new int[1]);
            Dispatch.call(doc, "Close", new Variant(false));
            doc = null;
        } catch (Exception e) {
            logger.warn("Failed to convert '{}' to '{}'.", srcFile, dstFile);
        }

    }

}
