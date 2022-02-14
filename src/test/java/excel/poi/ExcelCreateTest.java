package excel.poi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Apache POI 학습 테스트
 * <br>
 * 참고한 사이트
 * <a href="https://poi.apache.org/components/spreadsheet/quick-guide.html">apache poi quick guide</a>
 * @author wedge
 */
@Disabled
public class ExcelCreateTest {
    private static final String EXCEL_FILE_NAME = "src/test/resources/test.xlsx";

    private Workbook workbook;

    @BeforeEach
    void setUp() {
        cleanUpFile();
        workbook = new XSSFWorkbook();
    }

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
        List<Sheet> sheets = List.of(
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

        //checkFile -> do cleanUpFile() comment out and check 'src/test/resources/test.xlsx'
        createXlsxFile(workbook);
    }

    @DisplayName("엑셀 시트에 row와 column을 생성할 때")
    @Nested
    class CreateRowAndColumnTest {
        private Sheet sheet;
        private CreationHelper creationHelper;

        @BeforeEach
        void setUp(){
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
            stringCell.setCellType(CellType.STRING);
            stringCell.setCellValue(givenString);

            //then
            Cell expectedString = row.getCell(STRING_COL);

            assertThat(expectedString.getStringCellValue()).isEqualTo(givenString);

            //checkFile -> do cleanUpFile() comment out and check 'src/test/resources/test.xlsx'
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
            numericIntegerCell.setCellType(CellType.NUMERIC);
            numericIntegerCell.setCellValue(givenNumber);

            //then
            Cell expectedInteger = row.getCell(INT_COL);
            assertThat(expectedInteger.getNumericCellValue()).isEqualTo(givenNumber);

            //checkFile -> do cleanUpFile() comment out and check 'src/test/resources/test.xlsx'
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
            numericDoubleCell.setCellType(CellType.NUMERIC);
            numericDoubleCell.setCellValue(givenDouble);

            //then
            Cell expectedDouble = row.getCell(DOUBLE_COL);
            assertThat(expectedDouble.getNumericCellValue()).isEqualTo(givenDouble);

            //checkFile -> do cleanUpFile() comment out and check 'src/test/resources/test.xlsx'
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
            dateCell.setCellType(CellType.NUMERIC);
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

            //checkFile -> do cleanUpFile() comment out and check 'src/test/resources/test.xlsx'
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
            booleanCell.setCellType(CellType.BOOLEAN);
            booleanCell.setCellValue(givenBoolean);

            //then
            Cell expectedBoolean = row.getCell(BOOLEAN_COL);
            assertThat(expectedBoolean.getBooleanCellValue()).isEqualTo(givenBoolean);

            //checkFile -> do cleanUpFile() comment out and check 'src/test/resources/test.xlsx'
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
            stringCell.setCellType(CellType.STRING);
            stringCell.setCellValue(givenString);

            //then
            Cell expectedString = row.getCell(STRING_COL);

            assertThatThrownBy(expectedString::getNumericCellValue).isInstanceOf(IllegalStateException.class);
        }
    }
}
