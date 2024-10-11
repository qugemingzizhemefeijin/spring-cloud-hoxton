package com.atguigu.springcloud.other.pictures.bufferedimage;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
public final class ImageUtils {

    /**
     * 获得一个图象对象
     *
     * @param pathname 图象文件获取路径
     * @return BufferedImage
     */
    public static BufferedImage getImage(String pathname) {
        return getImage(new File(pathname));
    }

    /**
     * 获得一个图象对象
     *
     * @param imageFile - 图象文件对象
     * @return BufferedImage
     */
    public static BufferedImage getImage(File imageFile) {
        BufferedImage image = null;
        try (FileInputStream fis = new FileInputStream(imageFile);) {
            image = ImageIO.read(fis);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return image;
    }

    /**
     * 重新制作图片
     *
     * @param source BufferedImage 原图
     * @param width  int 图片宽
     * @param height int 图片高
     * @param x      int 坐标x轴
     * @param y      int 坐标y轴
     * @return BufferedImage
     */
    public static BufferedImage resize(BufferedImage source, int x, int y, int width, int height) {
        BufferedImage image = null;

        int type = source.getType();//获取图片类型

        int sourceW = source.getWidth();//获取图象宽度
        int sourceH = source.getHeight();//获取图象高度

        //剪裁宽度不能超过图片的宽度
        if (width > sourceW) {
            width = sourceW;
        }

        //剪裁高度不能超过图片的高度
        if (height > sourceH) {
            height = sourceH;
        }

        //用于裁剪图像的 ImageFilter 类。此类扩展了基本 ImageFilter 类，
        //可提取现有 Image 中的给定矩形区域，为包含刚提取区域的新图像提供源
        CropImageFilter crop = new CropImageFilter(x, y, width, height);

        //可为 Image 生成图像数据的对象的接口
        ImageProducer producer = source.getSource();

        //ImageProducer 接口的一个实现，该接口使用现有的图像和过滤器对象作为参数
        FilteredImageSource filterimage = new FilteredImageSource(producer, crop);

        //获取默认工具包
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        Image img = toolkit.createImage(filterimage);

        //绘制切割后的图片
        image = new BufferedImage(width, height, type);
        Graphics2D g = image.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return image;
    }

    /**
     * 图象缩放 给出指定的比例来等比例缩放
     *
     * @param source 原图象 BufferedImage
     * @param scale  缩放比例 double
     * @return BufferedImage
     */
    public static BufferedImage scaleImage(BufferedImage source, double scale) {

        int type = source.getType();//图片类型(不是格式gif jpg)
        double width = source.getWidth();
        double height = source.getHeight();

        double scaleW = width * scale;//缩放后的宽度
        double scaleH = height * scale;//缩放后的高度

        Image image = source.getScaledInstance((int) scaleW, (int) scaleH, Image.SCALE_DEFAULT);

        BufferedImage img = new BufferedImage((int) scaleW, (int) scaleH, type);
        Graphics g = img.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return img;
    }

    /**
     * 图象缩放 给出指定的宽度来等比例缩放
     *
     * @param source 原图象 BufferedImage
     * @param scaleW int 缩放到的宽度
     * @return BufferedImage
     */
    public static BufferedImage scaleImageByWidth(BufferedImage source, int scaleW) {
        double width = source.getWidth();// 图象当前的宽度
        double scale = scaleW / width;// 求出缩放比
        return scaleImage(source, scale);
    }

    /**
     * 图象缩放 给出指定的高度来等比例缩放
     *
     * @param source BufferedImage 原图象
     * @param scaleH int 缩放到的高度
     * @return BufferedImage
     */
    public static BufferedImage scaleImageByHeight(BufferedImage source, int scaleH) {
        double height = source.getHeight();// 图象当前的高度
        double scale = scaleH / height;// 求出缩放比
        return scaleImage(source, scale);
    }

    /**
     * 给出固定的比来缩放图片。
     *
     * @param source BufferedImage
     * @param scaleH 图片高
     * @param scaleW 图片宽
     * @return BufferedImage
     */
    public static BufferedImage scaleImageFile(BufferedImage source, int scaleW, int scaleH) {
        double w = (double) scaleW / source.getWidth();
        double h = (double) scaleH / source.getHeight();
        if (w < h) {
            return scaleImageByHeight(source, scaleH);
        } else {
            return scaleImageByWidth(source, scaleW);
        }
    }

    /**
     * 把彩色图片变成黑白
     *
     * @param source 原图象 BufferedImage
     * @return BufferedImage
     */
    public static BufferedImage getGray(BufferedImage source) {
        return getSpaceImage(source, ColorSpace.CS_GRAY);
    }

    /**
     * 使图片变得更香艳。
     *
     * @param source 原图象 BufferedImage
     * @return BufferedImage
     */
    public static BufferedImage getLightRGB(BufferedImage source) {
        return getSpaceImage(source, ColorSpace.CS_LINEAR_RGB);
    }

    /**
     * 图片样式的变化，比如变成黑白啊，颜色香艳啊。。
     *
     * @param source     原图象 BufferedImage
     * @param colorspace int 特定颜色空间
     * @return BufferedImage
     */
    public static BufferedImage getSpaceImage(BufferedImage source, int colorspace) {
        ColorSpace cs = ColorSpace.getInstance(colorspace);
        ColorConvertOp op = new ColorConvertOp(cs, null);
        source = op.filter(source, null);
        return source;
    }

    /**
     * 把图片对象保存为一个JPEG文件
     *
     * @param image    图片对象 bufferedImage
     * @param pathname 图象文件输出路径 file
     * @return boolean 成功则是true 失败是flase
     */
    public static boolean saveImageJPG(BufferedImage image, String pathname) {
        boolean b = false;
        try {
            b = ImageIO.write(image, "JPEG", new File(pathname));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return b;
    }

    /**
     * 返回切割好的图片的路径
     *
     * @param filepath String
     * @param suffix   - 后缀
     * @return filepath
     */
    public static String getImagePath(String filepath, String suffix) {
        if (filepath != null) {
            int i = filepath.lastIndexOf(".");
            if (i > -1) {
                return filepath.substring(0, i) + "_" + suffix + ".jpg";
            }
        }
        return null;
    }

    /**
     * 设置图片的透明度
     *
     * @param source     bufferedImage 图片对象
     * @param alphavalue iny 透明度 100最高透明，0为最低透明
     * @return BufferedImage
     */
    public static BufferedImage setAlpha(BufferedImage source, int alphavalue) {
        float alpha = (float) alphavalue / 100;

        Graphics2D g = source.createGraphics();
        g.drawImage(source, 0, 0, null);

        //设置透明
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g.setComposite(ac);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, source.getWidth(), source.getHeight());

        g.dispose();

        return source;
    }

    /**
     * 先根据图片的路径获取图片的对象,然后就先进行等比例缩放,然后进行裁减,最后生成另外一张图片.
     *
     * @param pathname String 图片的路径
     * @param suffix   - 图片后缀
     * @param width    int 要生成图片的宽度
     * @param height   int 要生成图片的高度
     * @return boolean
     */
    public static boolean resizeImage(String pathname, String suffix, int width, int height) {
        try {
            BufferedImage image = getImage(pathname);
            image = scaleImageFile(image, width, height);
            int x = Math.abs(image.getWidth() - width) / 2;
            int y = Math.abs(image.getHeight() - height) / 2;

			/*System.out.println("x=="+x+",y=="+y);
			System.out.println(image.getWidth()+"--"+width+"--"+(image.getWidth()-width));
			System.out.println(image.getHeight()+"--"+height+"--"+(image.getHeight()-height));*/

            image = resize(image, x, y, width, height);//裁减图片
            return saveImageJPG(image, getImagePath(pathname, suffix));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 先根据图片的路径获取图片的对象,然后就先进行等比例缩放,然后进行裁减,最后生成另外一张图片.
     *
     * @param pathname       String 图片的路径
     * @param targetFilePath - 目标图片保存地址
     * @param width          int 要生成图片的宽度
     * @param height         int 要生成图片的高度
     * @return boolean
     */
    public static boolean resizeImage2(String pathname, String targetFilePath, int width, int height) {
        try {
            BufferedImage image = getImage(pathname);
            image = scaleImageFile(image, width, height);
            int x = Math.abs((image.getWidth() - width)) / 2;
            int y = Math.abs(image.getHeight() - height) / 2;
            image = resize(image, x, y, width, height);//裁减图片
            return saveImageJPG(image, targetFilePath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 增加水印
     *
     * @param source 原图象
     * @param align  水印位置 1左下 2右下 3 左上 4 右上 5 居中
     * @return BufferedImage
     */
    public static BufferedImage waterMark(BufferedImage source, int align) {
        try {
            int width = source.getWidth(null);
            int height = source.getHeight(null);
            Graphics g = source.createGraphics();

            //水印文件
            BufferedImage imageWaterMark = ImageIO.read(new File("E:/shede.gif"));
            setAlpha(imageWaterMark, 50);
            int widthWaterMark = imageWaterMark.getWidth(null);
            int heightWaterMark = imageWaterMark.getHeight(null);
            if (align == 1) {//左下
                g.drawImage(imageWaterMark, 5, height - heightWaterMark, widthWaterMark, heightWaterMark, null);
            } else if (align == 2) {//右下
                g.drawImage(imageWaterMark, width - widthWaterMark, height - heightWaterMark, widthWaterMark, heightWaterMark, null);
            } else if (align == 3) {//左上
                g.drawImage(imageWaterMark, 5, 5, widthWaterMark, heightWaterMark, null);
            } else if (align == 4) {//右上
                g.drawImage(imageWaterMark, width - widthWaterMark, 5, widthWaterMark, heightWaterMark, null);
            } else {//居中
                g.drawImage(imageWaterMark, (width - widthWaterMark) / 2, (height - heightWaterMark) / 2, widthWaterMark, heightWaterMark, null);
            }
            g.dispose();
            return source;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private ImageUtils() {
        throw new AssertionError("No Instance.");
    }

}
