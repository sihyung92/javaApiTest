package excel.poi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import lombok.Getter;
import lombok.ToString;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Apache POI 학습 테스트
 * <br>
 * 참고한 사이트
 * <a href="https://poi.apache.org/components/spreadsheet/quick-guide.html">apache poi quick guide</a>
 *
 * @author wedge
 */
public class ExcelCreateTest {
    private static final String EXCEL_FILE_NAME = "src/test/resources/test.xlsx";

    private Workbook workbook;

    @BeforeEach
    void setUp() {
        cleanUpFile();
        workbook = new XSSFWorkbook();
    }

    /*
     * 결과를 실제 파일로 확인하고 싶으면 @AfterEach를 주석하고
     * createXlsxFile(workbook);
     * 메서드를 테스트에 추가한 후, 실행한다.
     * resources/sample.xlsx 이라는 이름으로 엑셀파일이 추가된다.
     */
    @AfterEach
    public void cleanUpFile() {
        File targetFile = new File(EXCEL_FILE_NAME);
        targetFile.delete();
    }

    @DisplayName("엑셀 시트 파일을 생성한다.")
    @Test
    void createExcelFileTest() {
        workbook.createSheet("test");
        File testFile = createXlsxFile(workbook);

        assertThat(testFile.exists()).isTrue();
    }

    private File createXlsxFile(Workbook workbook) {
        File testFile = new File(EXCEL_FILE_NAME);

        try (FileOutputStream testFileOutputStream = new FileOutputStream(testFile)) {
            workbook.write(testFileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testFile;
    }

    @DisplayName("엑셀 시트에 sheet를 추가한다")
    @Test
    void createSheetTest() {
        //given
        List<Sheet> sheets = Arrays.asList(
            workbook.createSheet("test"),
            workbook.createSheet("test2")
        );

        //when
        Sheet existSheet = workbook.getSheet("test");
        Sheet notExistSheet = workbook.getSheet("neverExistSheet");

        //then
        assertThat(notExistSheet).isNull();
        assertThat(existSheet).isNotNull();
        assertThat(workbook.getNumberOfSheets()).isEqualTo(sheets.size());

        createXlsxFile(workbook);
    }

    @DisplayName("엑셀 시트에 row와 column을 생성할 때")
    @Nested
    class CreateRowAndColumnTest {
        private Sheet sheet;
        private CreationHelper creationHelper;

        @BeforeEach
        void setUp() {
            sheet = workbook.createSheet("test");
            creationHelper = workbook.getCreationHelper();
        }

        @DisplayName("문자열 셀 생성에 성공한다.")
        @Test
        void createStringCellTest() {
            //given
            final String givenString = "StringData";
            final int STRING_COL = 0;

            //when
            Row row = sheet.createRow(0);

            Cell stringCell = row.createCell(STRING_COL);
            stringCell.setCellValue(givenString);

            //then
            Cell expectedString = row.getCell(STRING_COL);

            assertThat(expectedString.getStringCellValue()).isEqualTo(givenString);

            createXlsxFile(workbook);
        }

        @DisplayName("정수형 열 생성에 성공한다.")
        @Test
        void createIntegerCellTest() {
            //given
            int givenNumber = 10;
            final int INT_COL = 1;

            //when
            Row row = sheet.createRow(0);

            Cell numericIntegerCell = row.createCell(INT_COL);
            numericIntegerCell.setCellValue(givenNumber);

            //then
            Cell expectedInteger = row.getCell(INT_COL);
            assertThat(expectedInteger.getNumericCellValue()).isEqualTo(givenNumber);

            createXlsxFile(workbook);
        }

        @DisplayName("부동소수점 열 생성에 성공한다.")
        @Test
        void creatDoubleColumnTest() {
            //given
            final double givenDouble = 15.0;
            final int DOUBLE_COL = 0;

            //when
            Row row = sheet.createRow(0);

            Cell numericDoubleCell = row.createCell(DOUBLE_COL);
            numericDoubleCell.setCellValue(givenDouble);

            //then
            Cell expectedDouble = row.getCell(DOUBLE_COL);
            assertThat(expectedDouble.getNumericCellValue()).isEqualTo(givenDouble);

            createXlsxFile(workbook);
        }

        @DisplayName("날짜 열 생성에 성공한다.")
        @Test
        void createDateCellTest() {
            //given
            final Date givenDate = new Date();
            final int DATE_COL = 0;

            //when
            Row row = sheet.createRow(0);

            Cell dateCell = row.createCell(DATE_COL);
            dateCell.setCellValue(givenDate);

            //날짜 포맷 설정
            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(
                creationHelper.createDataFormat().getFormat("m/d/yy h:mm")
            );
            dateCell.setCellStyle(dateCellStyle);

            //then
            Cell expectedDate = row.getCell(DATE_COL);
            assertThat(expectedDate.getDateCellValue()).isEqualTo(givenDate);

            createXlsxFile(workbook);
        }

        @DisplayName("부울 셀 생성에 성공한다.")
        @Test
        void createBooleanCellTest() {
            //given
            final boolean givenBoolean = true;
            final int BOOLEAN_COL = 0;

            //when
            Row row = sheet.createRow(0);

            Cell booleanCell = row.createCell(BOOLEAN_COL);
            booleanCell.setCellValue(givenBoolean);

            //then
            Cell expectedBoolean = row.getCell(BOOLEAN_COL);
            assertThat(expectedBoolean.getBooleanCellValue()).isEqualTo(givenBoolean);

            createXlsxFile(workbook);
        }

        @DisplayName("잘못된 데이터 타입으로 파싱하면 예외가 발생한다.")
        @Test
        void whenWrongCellTypeParsing() {
            //given
            final String givenString = "StringData";
            final int STRING_COL = 0;

            //when
            Row row = sheet.createRow(0);

            Cell stringCell = row.createCell(STRING_COL);
            stringCell.setCellValue(givenString);

            //then
            Cell expectedString = row.getCell(STRING_COL);

            assertThatThrownBy(expectedString::getNumericCellValue).isInstanceOf(IllegalStateException.class);
        }

        @DisplayName("셀 병합에 성공한다.")
        @Test
        void mergeCellTest() {
            //given
            final String givenString = "This is a test of merging";
            final int firstColumn = 0;
            final int lastColumn = 6;

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            cellStyle.setBorderTop(BorderStyle.MEDIUM);
            cellStyle.setBorderBottom(BorderStyle.MEDIUM);
            cellStyle.setBorderLeft(BorderStyle.MEDIUM);
            cellStyle.setBorderRight(BorderStyle.MEDIUM);

            //when
            Row row = sheet.createRow(0);

            Cell cell = row.createCell(firstColumn);
            cell.setCellValue(givenString);
            cell.setCellStyle(cellStyle);

            int regionIndex = sheet.addMergedRegion(new CellRangeAddress(
                0,              //first row (0-based)
                0,              //last row  (0-based)
                firstColumn,    //first column (0-based)
                lastColumn      //last column  (0-based)
            ));

            //then
            Cell mergedCell = row.getCell(regionIndex);
            CellAddress address = mergedCell.getAddress();
            assertThat(address.getColumn()).isEqualTo(firstColumn);
            assertThat(regionIndex).isEqualTo(firstColumn);
        }

        @DisplayName("셀에 수정이 불가능하도록 락을 건다.")
        @Test
        void createCellLockTest() {
            //given
            final String givenString = "StringData";
            final int LOCKED_CELL = 0;
            final int UNLOCKED_CELL = 1;

            // protectSheet로 만든 후, 잠금이 필요없는 셀에 setLock(false) 셀 스타일을 추가해야한다.
            Row row = sheet.createRow(0);

            Cell lockCell = row.createCell(LOCKED_CELL);
            lockCell.setCellValue(givenString);

            Cell unlockCell = row.createCell(UNLOCKED_CELL);
            unlockCell.setCellValue(givenString);

            CellStyle unlockCellStyle = workbook.createCellStyle();
            unlockCellStyle.setLocked(false);
            unlockCell.setCellStyle(unlockCellStyle);

            //when
            sheet.protectSheet("this is password");

            //then
            CellStyle expectedLockCellStyle = row.getCell(LOCKED_CELL).getCellStyle();
            CellStyle expectedUnlockCellStyle = row.getCell(UNLOCKED_CELL).getCellStyle();
            assertThat(expectedLockCellStyle.getLocked()).isTrue();
            assertThat(expectedUnlockCellStyle.getLocked()).isFalse();
        }

        @DisplayName("일부 열과 행을 고정한다.")
        @Test
        void freezePaneTest() {
            // given
            Sheet sheet1 = workbook.createSheet("new sheet");
            Sheet sheet2 = workbook.createSheet("second sheet");
            Sheet sheet3 = workbook.createSheet("third sheet");
            Sheet sheet4 = workbook.createSheet("fourth sheet");
            // Freeze just one row
            sheet2.createFreezePane(0, 1);
            // Freeze just one column
            sheet1.createFreezePane(1, 0);
            // Freeze the columns and rows
            sheet3.createFreezePane(2, 2);
            // Create a split with the lower left side being the active quadrant
            sheet4.createSplitPane(2000, 2000, 0, 0, Sheet.PANE_LOWER_LEFT);

            // @afterEach를 주석하고 결과 확인
            createXlsxFile(workbook);
        }
    }

}
