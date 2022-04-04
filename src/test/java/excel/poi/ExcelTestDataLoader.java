package excel.poi;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class ExcelTestDataLoader {
    private static final List<Student> CACHE;
    private static final int HEADER_ROW = 0;

    private final List<Student> students;

    static {
        CACHE = List.of(
            new Student("name1", 1, true, 50.5, LocalDateTime.of(2022, 1, 1, 0, 0)),
            new Student("name2", 2, false, 90.51, LocalDateTime.of(1970, 5, 21, 1, 15)),
            new Student("name3", 3, true, 0.533, LocalDateTime.of(2000, 1, 11, 4, 59)),
            new Student("name4", 4, false, 94.9, LocalDateTime.of(2013, 11, 30, 12, 0)),
            new Student("name5", 5, true, 100.0, LocalDateTime.of(2016, 7, 15, 17, 32)),
            new Student("name6", 6, false, 86.5, LocalDateTime.of(2021, 12, 31, 6, 56))
        );
    }

    private ExcelTestDataLoader(List<Student> students) {
        this.students = CACHE;
    }

    public static ExcelTestDataLoader getInstance() {
        return new ExcelTestDataLoader(
            List.copyOf(CACHE)
        );
    }

    public List<Student> getTestStudents() {
        return students;
    }

    public Workbook initialize() {
        Workbook workbook = new XSSFWorkbook();
        CreationHelper creationHelper = workbook.getCreationHelper();
        Sheet studentSheet = workbook.createSheet("학생 명부");

        createHeaderRow(workbook, studentSheet);
        for (int rowNum = 0; rowNum < students.size(); rowNum++) {
            Row row = studentSheet.createRow(rowNum + 1);
            Cell name = row.createCell(0);
            name.setCellValue(students.get(rowNum).getName());
            Cell number = row.createCell(1);
            number.setCellValue(students.get(rowNum).getNumber());
            Cell male = row.createCell(2);
            male.setCellValue(students.get(rowNum).isMale());
            Cell score = row.createCell(3);
            score.setCellValue(students.get(rowNum).getScore());
            Cell startDate = row.createCell(4);
            startDate.setCellValue(Date.from(students.get(rowNum).getStartDate().atZone(ZoneId.systemDefault()).toInstant()));

            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(
                creationHelper.createDataFormat().getFormat("yyyy/mm/d h:mm")
            );
            startDate.setCellStyle(dateCellStyle);
        }
        return workbook;
    }

    private void createHeaderRow(Workbook workbook, Sheet studentSheet) {
        Row headerRow = studentSheet.createRow(HEADER_ROW);
        CellStyle headerCellStyle = createHeaderCellStyle(workbook);
        createHeaderCellContent(headerRow);
        // 스타일 적용 후 setCellValue를 하면 스타일이 덮어씌워짐
        for (Cell cell : headerRow) {
            cell.setCellStyle(headerCellStyle);
        }
    }

    private void createHeaderCellContent(Row headerRow) {
        Cell name = headerRow.createCell(0);
        name.setCellValue("이름");
        Cell number = headerRow.createCell(1);
        number.setCellValue("숫자");
        Cell male = headerRow.createCell(2);
        male.setCellValue("남성 여부");
        Cell score = headerRow.createCell(3);
        score.setCellValue("점수");
        Cell startDate = headerRow.createCell(4);
        startDate.setCellValue("입학일");
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;
    }

}
