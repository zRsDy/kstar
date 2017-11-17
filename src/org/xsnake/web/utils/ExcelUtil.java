package org.xsnake.web.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	
	// 单表导出
	public static void exportExcel(HttpServletResponse response, List<List<Object>> dataList, String fileName) {
		Map<String, List<List<Object>>> map = new HashMap<String, List<List<Object>>>();
		map.put(fileName, dataList);
		try {
			exportExcel(response, map, fileName, "xlsx");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 多表导出
	public static void exportExcel(HttpServletResponse response, Map<String, List<List<Object>>> map, String fileName) throws IOException{
		exportExcel(response, map, fileName, "xlsx");
	}
	
	public static void exportExcel(HttpServletResponse response, Map<String, List<List<Object>>> map, String fileName, String type) throws IOException {
		fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setContentType("application/msexcel;charset=GBK");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName + "." + type);
		OutputStream out = response.getOutputStream();
		BufferedOutputStream b = new BufferedOutputStream(out);
		org.apache.poi.ss.usermodel.Workbook workBook = null;
		if (type.equals("xlsx"))
			workBook = new SXSSFWorkbook(1000);
		if (type.equals("xls"))
			workBook = new HSSFWorkbook();
		if (null != map) {
			Set<String> keys = map.keySet();
			int rowSize = 0;// 行
			int cellSize = 0;// 列
			try {
				for (String key : keys) {
					org.apache.poi.ss.usermodel.Sheet sheet = workBook.createSheet(key);
					List<List<Object>> dataList = map.get(key);
					rowSize = dataList.size();
					if (rowSize > 0)
						cellSize = dataList.get(0).size();

					for (int i = 0; i < rowSize; i++) {
						Row row = sheet.createRow(i);
						for (int j = 0; j < cellSize; j++) {
							Object str = dataList.get(i).get(j);
							String s = null;
							try {
								if (str != null) {
									if (str instanceof Integer) {
										row.createCell(j).setCellValue(Integer.parseInt(str.toString()));
										continue;
									} else if (str instanceof Double) {
										row.createCell(j).setCellValue(Double.parseDouble(str.toString()));
										continue;
									} else if (str instanceof BigDecimal) {
										BigDecimal _bd = new BigDecimal(str.toString());
										_bd.setScale(2, BigDecimal.ROUND_HALF_UP);
										row.createCell(j).setCellValue(_bd.doubleValue());
										continue;
									}
									s = (String) str;
								} else {
									s = "";
								}
							} catch (Exception e) {
								s = String.valueOf(str);
							}
							row.createCell(j).setCellValue(s);
						}
						// try{
						// Thread.sleep(10);
						// }catch (Exception e) {
						// }
					}
				}
				workBook.write(b);
				b.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				b.close();
			}
		}
	}

	

	public static List<List<Object>> readExcelPOI(String fileUrl) throws IOException {
		List<List<Object>> readExcelList = new ArrayList<List<Object>>();
		String fileType = fileUrl.substring(fileUrl.lastIndexOf("."));
		if (fileType != null) {
			if (fileType.equalsIgnoreCase(".xls")) {
				readExcelList = importExcelXLS(fileUrl, 1);
			} else if (fileType.equalsIgnoreCase(".xlsx")) {
				readExcelList = importExcelXLSX(fileUrl, 1);
			}
		}
		return readExcelList;
	}
	
	// POI excel导入(xls)
	public static List<List<Object>> importExcelXLS(String filePath, int ignoreRows) throws IOException {

		List<List<Object>> result = new ArrayList<List<Object>>();

		int rowSize = 0;
		InputStream infile = new FileInputStream(filePath);
		BufferedInputStream in = new BufferedInputStream(infile);
		POIFSFileSystem fs = new POIFSFileSystem(in);
		HSSFWorkbook wb = new HSSFWorkbook(fs);

		HSSFCell cell = null;
		for (int sheetIndex = 0; sheetIndex < 1; sheetIndex++) {
			// 取得工作簿
			HSSFSheet st = wb.getSheetAt(sheetIndex);
			// 标题行不取
			// 列长按照标题行的最后一行来获取
			HSSFRow lastHeaderRow = st.getRow(ignoreRows - 1);
			if (lastHeaderRow == null)
				continue;
			int headerColumnLen = lastHeaderRow.getLastCellNum();
			for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
				HSSFRow row = st.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				int tempRowSize = headerColumnLen + 1;
				if (tempRowSize > rowSize) {
					rowSize = tempRowSize;
				}
				List<Object> values = new ArrayList<Object>();
				boolean hasValue = false;
				for (int columnIndex = 0; columnIndex <= headerColumnLen; columnIndex++) {
					Object value = null;
					// 获取单元格
					cell = row.getCell(columnIndex);
					if (cell != null) {
						// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						switch (cell.getCellType()) {
						case HSSFCell.CELL_TYPE_STRING:
							value = cell.getStringCellValue();
							break;
						case HSSFCell.CELL_TYPE_NUMERIC:
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								short format = cell.getCellStyle().getDataFormat();
								SimpleDateFormat sdf = null;
								if(format == 14 || format == 31 || format == 57 || format == 58){
									//日期
									sdf = new SimpleDateFormat("yyyy-MM-dd");
								}else if(format==188||("dd/mm/yyyy").equals(cell.getCellStyle().getDataFormatString())){
									sdf = new SimpleDateFormat("dd/MM/yyyy");
								}else if (format == 20 || format == 32) {
									//时间
									sdf = new SimpleDateFormat("HH:mm");
								}
								double data = cell.getNumericCellValue();
								Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(data);
								value = sdf.format(date);
//								Date date = cell.getDateCellValue();
//								value = date;
							} else {
								NumberFormat nf = NumberFormat.getInstance();
								nf.setGroupingUsed(false);
								double d = Double.parseDouble(nf.format(cell.getNumericCellValue()));
								if (d % 1.0 != 0) {
									nf.setMinimumFractionDigits(8);
								}
								value = nf.format(cell.getNumericCellValue());
							}
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
							// 导入时如果为公式生成的数据则无值
							if (cell.getCachedFormulaResultType() == 0) {
								value = cell.getNumericCellValue();
							} else {
								value = cell.getRichStringCellValue();
							}
							break;
						case HSSFCell.CELL_TYPE_BLANK:
							break;
						case HSSFCell.CELL_TYPE_ERROR:
							value = "";
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN:
							value = (cell.getBooleanCellValue() == true ? "Y" : "N");
							break;
						default:
							value = null;
						}
					}
					if (columnIndex == 0 && value == null) {
						break;
					}
					values.add(value);
					hasValue = true;
				}
				if (hasValue) {
					result.add(values);
				}
			}
		}
		in.close();

		return result;
	}

	// POI excel导入(xlsx)
	public static List<List<Object>> importExcelXLSX(String filePath, int ignoreRows) throws IOException {
		List<List<Object>> result = new ArrayList<List<Object>>();
		int rowSize = 0;
		InputStream is = new FileInputStream(filePath);
		XSSFWorkbook wb = new XSSFWorkbook(is);
		XSSFCell cell = null;
		for (int sheetIndex = 0; sheetIndex < 1; sheetIndex++) {
			XSSFSheet st = (XSSFSheet) wb.getSheetAt(sheetIndex);
			XSSFRow lastHeaderRow = st.getRow(ignoreRows - 1);
			if (lastHeaderRow == null)
				continue;
			int headerColumnLen = lastHeaderRow.getLastCellNum();
			for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {

				XSSFRow row = st.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				int tempRowSize = headerColumnLen + 1;
				if (tempRowSize > rowSize) {
					rowSize = tempRowSize;
				}
				List<Object> values = new ArrayList<Object>();
				boolean hasValue = false;

				for (int columnIndex = 0; columnIndex <= headerColumnLen; columnIndex++) {
					Object value = null;
					cell = row.getCell(columnIndex);
					if (cell != null) {
						// 注意：一定要设成这个，否则可能会出现乱码
						// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						switch (cell.getCellType()) {
						case HSSFCell.CELL_TYPE_STRING:
							value = cell.getStringCellValue();
							break;
						case HSSFCell.CELL_TYPE_NUMERIC:
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								Date date = cell.getDateCellValue();
								value = date;
							} else {
								// String cellStr =
								// String.valueOf(cell.getNumericCellValue());
								value = cell.getRawValue();
							}
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
							// 导入时如果为公式生成的数据则无值
							if (cell.getCachedFormulaResultType() == 0) {
								value = cell.getNumericCellValue();
							} else {
								value = cell.getRichStringCellValue();
							}
							break;
						case HSSFCell.CELL_TYPE_BLANK:
							break;
						case HSSFCell.CELL_TYPE_ERROR:
							value = "";
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN:
							value = (cell.getBooleanCellValue() == true ? "Y" : "N");
							break;
						default:
							value = null;
						}
					}
					if (columnIndex == 0 && value == null) {
						break;
					}
					values.add(value);
					hasValue = true;
				}
				if (hasValue) {
					result.add(values);
				}
			}
		}
		is.close();

		return result;
	}
}
