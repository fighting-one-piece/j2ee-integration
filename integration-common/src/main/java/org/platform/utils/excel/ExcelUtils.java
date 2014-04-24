package org.platform.utils.excel;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**Excel工具类*/
public class ExcelUtils {

	private static Logger logger = Logger.getLogger(ExcelUtils.class);

	/**
	 *
		 *<p>包名类名：main.cn.com.project.util.excel.ExcelUtil</p>
		 *<p>  方法名：deleteTempExcelFileDirect</p>
		 *<p>    描述：删除路径下的Excel文件</p>
		 *<p>    参数：@param excelFileDir</p>
		 *<p>返回类型：void</p>
		 *<p>创建时间：2012-7-7 上午11:16:10</p>
		 *<p>    作者: wl </p>
	 */
	public static void deleteExcelFileDirect(File excelFileDir) {
		if(excelFileDir.exists() && excelFileDir.isDirectory()) {
			for (File file : excelFileDir.listFiles()) {
				file.delete();
			}
		}
	}

	/**
	 *
		 *<p>包名类名：main.cn.com.project.util.excel.ExcelUtil</p>
		 *<p>  方法名：getFileListByDirect</p>
		 *<p>    描述：获取目录下的文件列表</p>
		 *<p>    参数：@param fileDir
		 *<p>    参数：@return</p>
		 *<p>返回类型：List<File></p>
		 *<p>创建时间：2012-7-7 上午11:44:32</p>
		 *<p>    作者: wl </p>
	 */
	public static List<File> getFileListByDirect(String fileDir) {
		List<File> fileList = new ArrayList<File>();
		File file = new File(fileDir);
		if (file.exists() && file.isDirectory()) {
			for (File f : file.listFiles()) {
				fileList.add(f);
			}
		}
		return fileList;
	}

	/**
	 *
		 *<p>包名类名：main.cn.com.project.util.excel.ExcelUtil</p>
		 *<p>  方法名：getRealFileName</p>
		 *<p>    描述：获取真实文件名称</p>
		 *<p>    参数：@param readFileDir
		 *<p>    参数：@param realFile
		 *<p>    参数：@return</p>
		 *<p>返回类型：String</p>
		 *<p>创建时间：2012-7-7 下午12:05:17</p>
		 *<p>    作者: wl </p>
	 */
	public static String getRealFileName(String readFileDir, File realFile) {
		File real = realFile;
		File base = new File(readFileDir);
		String ret = real.getName();
		while (true) {
			real = real.getParentFile();
			if (real == null)
				break;
			if (real.equals(base))
				break;
			else
				ret = real.getName() + File.separator + ret;
		}
		return ret;
	}

	/**
	 *
		 *<p>包名类名：main.cn.com.project.util.excel.ExcelUtil</p>
		 *<p>  方法名：newWorkbook</p>
		 *<p>    描述：新建一个Wookbook对象</p>
		 *<p>    参数：@param excelFile
		 *<p>    参数：@return</p>
		 *<p>返回类型：HSSFWorkbook</p>
		 *<p>创建时间：2012-7-7 上午11:17:01</p>
		 *<p>    作者: wl </p>
	 */
	public static HSSFWorkbook newWorkbook(File excelFile) {
		HSSFWorkbook wb = new HSSFWorkbook();
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(excelFile);
			wb.write(fileOut);
			fileOut.flush();
		} catch (FileNotFoundException ex) {
			logger.info(ex.getMessage(), ex);
		} catch (IOException ex) {
			logger.info(ex.getMessage(), ex);
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					logger.info(e.getMessage(), e);
				}
			}
		}
		return wb;
	}

	/**
	 *
		 *<p>包名类名：main.cn.com.project.util.excel.ExcelUtil</p>
		 *<p>  方法名：saveWorkBook</p>
		 *<p>    描述：保存Excel文件</p>
		 *<p>    参数：@param wb
		 *<p>    参数：@param excelFile</p>
		 *<p>返回类型：void</p>
		 *<p>创建时间：2012-7-7 上午11:18:26</p>
		 *<p>    作者: wl </p>
	 */
	public static void saveWorkBook(HSSFWorkbook wb, File excelFile) {
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(excelFile);
			wb.write(fileOut);
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					logger.info(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 *
		 *<p>包名类名：main.cn.com.project.util.excel.ExcelUtil</p>
		 *<p>  方法名：getWorkBook</p>
		 *<p>    描述：获取WorkBook对象</p>
		 *<p>    参数：@param excelFile
		 *<p>    参数：@return</p>
		 *<p>返回类型：HSSFWorkbook</p>
		 *<p>创建时间：2012-7-7 上午11:43:02</p>
		 *<p>    作者: wl </p>
	 */
	public static HSSFWorkbook getWorkBook(File excelFile) {
		FileInputStream fileIn = null;
		HSSFWorkbook wb = null;
		try {
			fileIn = new FileInputStream(excelFile);
			wb = new HSSFWorkbook(fileIn);
			return wb;
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			return null;
		} finally {
			if (fileIn != null) {
				try {
					fileIn.close();
				} catch (IOException e) {
					logger.info(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 *
		 *<p>包名类名：main.cn.com.project.util.excel.ExcelUtil</p>
		 *<p>  方法名：createExcel</p>
		 *<p>    描述：创建Excel文件</p>
		 *<p>    参数：@param dataList 数据集
		 *<p>    参数：@param excelInfoMap 属性集合
		 *<p>    参数：@param excelFile 文件名
		 *<p>    参数：@param rowCount 每个文件行数</p>
		 *<p>返回类型：void</p>
		 *<p>创建时间：2012-7-22 下午05:44:55</p>
		 *<p>    作者: wl </p>
	 */
	@SuppressWarnings({"rawtypes"})
	public static void createExcel(List dataList, Map excelInfoMap, File excelFile, int rowCount) {
		int excelFileCount = dataList.size() / rowCount + 1;
		for(int i = 0; i < excelFileCount; i++){
			int start = 0 + i * rowCount;
			int end = 0;
			if(i != excelFileCount - 1){
				end = (i+1) * rowCount - 1;
			}else{
				end = dataList.size();
			}
			System.out.println("~~~~~~~~~~~~~~~start:"+start+"~~~~~~~~~~~end:"+end);
			String excelFileDir = excelFile.getParent() + "\\";
			String excelFileName = excelFile.getName().substring(0, excelFile.getName().lastIndexOf("."));
			String excelFileSuffix = excelFile.getName().substring(excelFile.getName().lastIndexOf("."));
			ExcelUtils.createExcel(dataList.subList(start, end), excelInfoMap, new File(excelFileDir + excelFileName + i + excelFileSuffix));
		}
	}

	/**
	 *
		 *<p>包名类名：main.cn.com.project.util.excel.ExcelUtil</p>
		 *<p>  方法名：createExcel</p>
		 *<p>    描述：创建Excel文件</p>
		 *<p>    参数：@param dataList 数据集
		 *<p>    参数：@param excelInfoMap 属性集合
		 *<p>    参数：@param excelFile 文件名
		 *<p>    参数：@throws Exception</p>
		 *<p>返回类型：void</p>
		 *<p>创建时间：2012-7-7 上午10:34:18</p>
		 *<p>    作者: wl </p>
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void createExcel(List dataList, Map excelInfoMap, File excelFile) {
		try{
			HSSFWorkbook wb = newWorkbook(excelFile);
			HSSFSheet sheet = wb.createSheet("sheet1");

			HSSFRow row = sheet.createRow(0);
			Map<String,String> excel_map = excelInfoMap;
			String[] titles = getTitleTexts(excel_map);
			String[] methods = getMethodTexts(excel_map);
			HSSFCell cell;
			//创建标题行
			for(int i=0; i<titles.length; i++){
				cell = row.createCell(i);
				cell.setCellValue(titles[i]);
			}

			for(int j=1; j<=dataList.size(); j++){
				row = sheet.createRow(j);
				Object object = dataList.get(j-1);
				for(int k=0;k<titles.length;k++){
					Object cellValue = getMethodValue(methods[k],object);
					cell = row.createCell(k);
					String cellString = null != String.valueOf(cellValue) ? String.valueOf(cellValue) : "";
				    cell.setCellValue(cellString);
				}

			}
			saveWorkBook(wb, excelFile);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 *
		 *<p>包名类名：main.cn.com.project.util.excel.ExcelUtil</p>
		 *<p>  方法名：readXlsFile</p>
		 *<p>    描述：读取Excel文件</p>
		 *<p>    参数：@param baseDir
		 *<p>    参数：@return
		 *<p>    参数：@throws Exception</p>
		 *<p>返回类型：InputStream</p>
		 *<p>创建时间：2012-7-7 下午01:48:18</p>
		 *<p>    作者: wl </p>
	 */
	public static InputStream readXlsFile(String excelFileDir) {
		File fileDir = new File(excelFileDir);
		HSSFWorkbook wb = new HSSFWorkbook();
		try {
			if(fileDir.exists()){
				File[] fileArray = fileDir.listFiles();
				for(File file : fileArray){
					logger.info("~~~~~~~~~~fileName:"+file.getName());
					InputStream inStream = new FileInputStream(file);
					HSSFWorkbook w = new HSSFWorkbook(inStream);
					HSSFSheet sheets = wb.createSheet(file.getName());
					HSSFSheet sheet = w.getSheetAt(0);
					logger.info("~~~~~~~~~~lastNum:"+sheet.getLastRowNum());
					int rowCount = sheet.getLastRowNum();
					for(int i=0;i<rowCount;i++){
						HSSFRow row = sheets.createRow(i);
						HSSFRow r = sheet.getRow(i);
						int columnNum = r.getLastCellNum();
						for(int j=0;j<columnNum;j++){
							HSSFCell cell = row.createCell(j);
							HSSFCell c = r.getCell(j);
							cell.setCellValue(c.getStringCellValue());
						}
					}
				}
			} else {
				fileDir.mkdir();
			}
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			wb.write(outStream);
			byte[] byteArray = outStream.toByteArray();
			InputStream is = new ByteArrayInputStream(byteArray);
			return is;
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
		} finally {

		}
		return null;
	}

	/**
	 *
		 *<p>包名类名：main.cn.com.project.util.excel.ExcelUtil</p>
		 *<p>  方法名：getTitleTexts</p>
		 *<p>    描述：获取Excel标题文本</p>
		 *<p>    参数：@param map
		 *<p>    参数：@return</p>
		 *<p>返回类型：String[]</p>
		 *<p>创建时间：2012-7-7 下午01:45:19</p>
		 *<p>    作者: wl </p>
	 */
	public static String[] getTitleTexts(Map<String,String> map) {
		String[] titleTexts = new String[map.size()];
		int i = 0;
		for(Map.Entry<String,String> entry : map.entrySet()){
			titleTexts[i] = entry.getKey();
			i++;
		}
		return titleTexts;
	}

	/**
	 *
		 *<p>包名类名：main.cn.com.project.util.excel.ExcelUtil</p>
		 *<p>  方法名：getMethodTexts</p>
		 *<p>    描述：获取标题文本所对应的属性</p>
		 *<p>    参数：@param map
		 *<p>    参数：@return</p>
		 *<p>返回类型：String[]</p>
		 *<p>创建时间：2012-7-7 下午01:45:45</p>
		 *<p>    作者: wl </p>
	 */
	public static String[] getMethodTexts(Map<String,String> map) {
		String[] methodTexts = new String[map.size()];
		int i = 0;
		for(Map.Entry<String,String> entry : map.entrySet()){
			methodTexts[i] = entry.getValue();
			i++;
		}
		return methodTexts;
	}

	/**
	 *
		 *<p>包名类名：main.cn.com.project.util.excel.ExcelUtil</p>
		 *<p>  方法名：getMethodValue</p>
		 *<p>    描述：获取对象属性值</p>
		 *<p>    参数：@param methodName
		 *<p>    参数：@param obj
		 *<p>    参数：@return
		 *<p>    参数：@throws Exception</p>
		 *<p>返回类型：Object</p>
		 *<p>创建时间：2012-7-7 下午01:43:23</p>
		 *<p>    作者: wl </p>
	 */
	public static Object getMethodValue(String methodName,Object obj) {
		Object returnValue = null;
		try {
			String mName = "get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
			Method method = obj.getClass().getMethod(mName, new Class[]{});
			returnValue = method.invoke(obj, new Object[]{});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	/**
	 *
		 *<p>包名类名：main.cn.com.project.util.excel.ExcelUtil</p>
		 *<p>  方法名：zipFile</p>
		 *<p>    描述：打包ZIP文件</p>
		 *<p>    参数：@param excelFileDir
		 *<p>    参数：@param zipFile
		 *<p>    参数：@throws Exception</p>
		 *<p>返回类型：void</p>
		 *<p>创建时间：2012-7-7 上午11:53:04</p>
		 *<p>    作者: wl </p>
	 */
	public static void createZipFile(String excelFileDir, File zipFile) {
		String zipFileDir = zipFile.getParent();
		File zipDirFile = new File(zipFileDir);
		if(!zipDirFile.exists()){
			zipDirFile.mkdirs();
		}
		logger.info("~~~~~~~~~~zip file dir:" + zipFileDir);
		ZipOutputStream zipOutStream = null;
		try {
		    zipOutStream = new ZipOutputStream(new FileOutputStream(zipFile));
		    ZipEntry zipEntry = null;
			List<File> fileList = getFileListByDirect(excelFileDir);
			byte[] buff = new byte[2048];
			int readLen = 0;
			for(int i=0; i<fileList.size(); i++){
				File file = fileList.get(i);
				zipEntry = new ZipEntry(getRealFileName(excelFileDir, file));
				zipEntry.setSize(file.length());
				zipEntry.setTime(file.lastModified());
				zipOutStream.putNextEntry(zipEntry);
				InputStream inStream = new BufferedInputStream(new FileInputStream(file));
				while((readLen = inStream.read(buff)) != -1){
					zipOutStream.write(buff, 0, readLen);
				}
				inStream.close();
			}
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
		} finally {
			try {
				if (zipOutStream != null) {
					zipOutStream.closeEntry();
					zipOutStream.close();
				}
			} catch (IOException e) {
				logger.info(e.getMessage(), e);
			}
		}
	}

	/**
	 *
		 *<p>包名类名：main.cn.com.project.util.excel.ExcelUtil</p>
		 *<p>  方法名：readZipFile</p>
		 *<p>    描述：读取ZIP文件</p>
		 *<p>    参数：@return
		 *<p>    参数：@throws Exception</p>
		 *<p>返回类型：byte[]</p>
		 *<p>创建时间：2012-7-7 下午12:11:57</p>
		 *<p>    作者: wl </p>
	 */
	public static byte[] readZipFile(File zipFile) throws Exception{
		if(zipFile.exists()){
			InputStream inStream = new FileInputStream(zipFile);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			int readLen = 0;
			byte[] buff = new byte[2048];
			while((readLen = inStream.read(buff)) != -1){
				outStream.write(buff, 0, readLen);
			}
			inStream.close();
			outStream.flush();
			outStream.close();
			byte[] byteArray = outStream.toByteArray();
			return byteArray;
		}
		return null;
	}

	/**
	 *
		 *<p>包名类名：main.cn.com.project.util.excel.ExcelUtil</p>
		 *<p>  方法名：readZipFileStream</p>
		 *<p>    描述：读取ZIP文件</p>
		 *<p>    参数：@param zipFile
		 *<p>    参数：@return
		 *<p>    参数：@throws Exception</p>
		 *<p>返回类型：InputStream</p>
		 *<p>创建时间：2012-7-7 下午01:52:48</p>
		 *<p>    作者: wl </p>
	 */
	public static InputStream readZipFileStream(File zipFile) {
		InputStream inStream = null;
		try {
			if(zipFile.exists()){
				logger.info("~~~~~~~~~~zipFile exists!");
				inStream = new FileInputStream(zipFile);
			}
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
		}
		return inStream;
	}

}
