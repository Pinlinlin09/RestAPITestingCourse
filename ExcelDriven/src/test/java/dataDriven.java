import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xddf.usermodel.chart.AxisCrossBetween;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class dataDriven {

	public static void main(String[] args) throws FileNotFoundException {

	}

	public ArrayList<String> getData(String testCaseName) throws FileNotFoundException {
		ArrayList<String> a = new ArrayList<String>();
		// fileInputStream argument
		FileInputStream fis = new FileInputStream("C:\\workplace_eclipse\\TestCases.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();

		int numSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numSheets; i++) {
			if (workbook.getSheetName(i).equalsIgnoreCase("testdata")) {
				XSSFSheet sheet = workbook.getSheetAt(i); // return type XSSFSheet
				// Identify testcases column by scanning the entire 1st row
				Iterator<Row> rows = sheet.iterator();// sheet is collection of rows
				Row firstrow = rows.next();
				Iterator<Cell> ce = firstrow.cellIterator(); // row is collection of cells
				int k = 0;
				int column = 0;
				while (ce.hasNext()) {
					Cell value = ce.next();
					if (value.getStringCellValue().equalsIgnoreCase("TestCases")) {
						// desire column's value
						column = k;
					}
					k++;
				}
				System.out.println(column);
				// once column is identified then scan entire testcase column to identify
				// purchase test case row
				while (rows.hasNext()) {
					Row r = rows.next();
					if (r.getCell(column).getStringCellValue().equalsIgnoreCase(testCaseName)) {
						// after you grab purhcase testcase row = pull all the data of that row and feed
						// into test
						Iterator<Cell> cv = r.cellIterator();
						while (cv.hasNext()) {
							Cell c = cv.next();
							if (c.getCellType() == CellType.STRING) {
								a.add(c.getStringCellValue());
							} else {
								a.add(NumberToTextConverter.toText(c.getNumericCellValue()));
							}

						}
					}
				}
			}

		}
		return a;
	}
}
