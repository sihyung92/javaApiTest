package imageio;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageTest {
    private static final String IMAGE_DIR = "src/test/resources/image/";

    /**
     * https://www.canto.com/blog/convert-png-to-jpg/
     * png와 jpg 알아보기
     * */
    @DisplayName("PNG -> JPG 이미지 변환 테스트")
    @Test
    void imageConvertPngToJpg() throws IOException {
        // given
        // originalPngFile은 tiff 포맷으로, jpg write를 하면 실패한다. (0 byte 리턴)
        File originalPngFile = new File(IMAGE_DIR + "example.png");
        File destinyJpgFile = new File(IMAGE_DIR + "converted.jpg");

        // when
        BufferedImage original = ImageIO.read(originalPngFile);

        BufferedImage jpegImage = new BufferedImage(original.getWidth(),
            original.getHeight(), BufferedImage.TYPE_INT_RGB);

        jpegImage.createGraphics().drawImage(original, 0, 0, Color.WHITE, null);

        // then
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean originalConvert = ImageIO.write(original, "jpg", destinyJpgFile);
        boolean jpgConvert = ImageIO.write(jpegImage, "jpg", destinyJpgFile);

        assertThat(originalConvert).isFalse();
        assertThat(jpgConvert).isTrue();
    }

    @DisplayName("Image -> Byte Array 테스트")
    @Test
    void imageConvertBytes() throws IOException {
        // given
        File originalPngFile = new File(IMAGE_DIR + "example.png");

        // when
        BufferedImage original = ImageIO.read(originalPngFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean byteArrayConvert = ImageIO.write(original, "png", baos);
        byte[] bytes = baos.toByteArray();

        // then
        assertThat(byteArrayConvert).isTrue();
        // 파일이 약간 더 크다.
        System.out.println("original file size : " + originalPngFile.length());
        System.out.println("byte array size : " + bytes.length);
        assertThat(originalPngFile.length()).isGreaterThan(bytes.length);
    }
}
