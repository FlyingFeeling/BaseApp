package com.feeling.mybaseapp.utils.compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.text.TextUtils;

import com.feeling.mybaseapp.utils.FileUtil;
import com.feeling.mybaseapp.utils.UriUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片处理工具类
 * <p>
 * Author: nanchen
 * Email: liushilin520@foxmail.com
 * Date: 2017-03-08  9:03
 */

public class CompressUtil {

    private CompressUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    static Bitmap getScaledBitmap(Context context, Uri imgUri, float maxWidth, float maxHeight, Bitmap.Config bitmapConfig) {
        String filePath = UriUtil.getRealPathFromURI(context, imgUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        //by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        //you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        if (bmp == null) {
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(filePath);
                BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        if (actualHeight == -1 || actualWidth == -1) {
            try {
                ExifInterface exifInterface = new ExifInterface(filePath);
                actualHeight = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                actualWidth = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (actualWidth < 0 || actualHeight < 0) {
            Bitmap bitmap2 = BitmapFactory.decodeFile(filePath);
            if (bitmap2 != null) {
                actualWidth = bitmap2.getWidth();
                actualHeight = bitmap2.getHeight();
            } else {
                return null;
            }
        }

        float imgRatio = (float) actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        //width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        //setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        //inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        //this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            //load the bitmap getTempFile its path
            bmp = BitmapFactory.decodeFile(filePath, options);
            if (bmp == null) {
                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(filePath);
                    BitmapFactory.decodeStream(inputStream, null, options);
                    inputStream.close();
                } catch (FileNotFoundException exception) {
                    exception.printStackTrace();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, bitmapConfig);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, 0, 0);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        //check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(),
                    matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scaledBitmap;
    }

    static File compressImage(Context context, Uri imageUri, float maxWidth, float maxHeight,
                              Bitmap.CompressFormat compressFormat, Bitmap.Config bitmapConfig,
                              int quality, String parentPath, String prefix, String fileName) {
        FileOutputStream out = null;
        String filename = generateFilePath(context, parentPath, imageUri, compressFormat.name().toLowerCase(), prefix, fileName);
        try {
            out = new FileOutputStream(filename);
            //write the compressed bitmap at the destination specified by filename.
            CompressUtil.getScaledBitmap(context, imageUri, maxWidth, maxHeight, bitmapConfig).compress(compressFormat, quality, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignored) {
            }
        }

        return new File(filename);
    }

    private static String generateFilePath(Context context, String parentPath, Uri uri,
                                           String extension, String prefix, String fileName) {
        File file = new File(parentPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        /** if prefix is null, set prefix "" */
        prefix = TextUtils.isEmpty(prefix) ? "" : prefix;
        /** reset fileName by prefix and custom file name */
        fileName = TextUtils.isEmpty(fileName) ? prefix + FileUtil.splitFileName(UriUtil.getFileName(context, uri))[0] : fileName;
        return file.getAbsolutePath() + File.separator + fileName + "." + extension;
    }


    /**
     * 计算inSampleSize
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private static Bitmap buildBitmap(Bitmap original, boolean canReuseInBitmap) {
        // First we should check the original
        if (original == null)
            throw new NullPointerException("Blur bitmap original isn't null");

        Bitmap.Config config = original.getConfig();
        if (config != Bitmap.Config.ARGB_8888 && config != Bitmap.Config.RGB_565) {
            throw new RuntimeException("Blur bitmap only supported Bitmap.Config.ARGB_8888 and Bitmap.Config.RGB_565.");
        }

        // If can reuse in bitmap return this or copy
        Bitmap rBitmap;
        if (canReuseInBitmap) {
            rBitmap = original;
        } else {
            rBitmap = original.copy(config, true);
        }
        return (rBitmap);
    }

    /**
     * @author Qiushui
     * @description 模糊图片工具类
     * @revision Xiarui 16.09.05
     */
    //图片缩放比例
    private static final float BITMAP_SCALE = 0.4f;

    /**
     * StackBlur By Java Bitmap
     *
     * @param original         Original Image
     * @param radius           Blur radius
     * @param canReuseInBitmap Can reuse In original Bitmap
     * @return Image Bitmap
     */
    public static Bitmap blur(Bitmap original, int radius, boolean canReuseInBitmap) {
        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>
        if (radius < 1) {
            return (null);
        }

        Bitmap bitmap = buildBitmap(original, canReuseInBitmap);

        // Return this none blur
        if (radius == 1) {
            return bitmap;
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        // get array
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        // run Blur
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        short r[] = new short[wh];
        short g[] = new short[wh];
        short b[] = new short[wh];
        int rSum, gSum, bSum, x, y, i, p, yp, yi, yw;
        int vMin[] = new int[Math.max(w, h)];

        int divSum = (div + 1) >> 1;
        divSum *= divSum;

        short dv[] = new short[256 * divSum];
        for (i = 0; i < 256 * divSum; i++) {
            dv[i] = (short) (i / divSum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackPointer;
        int stackStart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routSum, goutSum, boutSum;
        int rinSum, ginSum, binSum;

        for (y = 0; y < h; y++) {
            rinSum = ginSum = binSum = routSum = goutSum = boutSum = rSum = gSum = bSum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rbs = r1 - Math.abs(i);
                rSum += sir[0] * rbs;
                gSum += sir[1] * rbs;
                bSum += sir[2] * rbs;
                if (i > 0) {
                    rinSum += sir[0];
                    ginSum += sir[1];
                    binSum += sir[2];
                } else {
                    routSum += sir[0];
                    goutSum += sir[1];
                    boutSum += sir[2];
                }
            }
            stackPointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rSum];
                g[yi] = dv[gSum];
                b[yi] = dv[bSum];

                rSum -= routSum;
                gSum -= goutSum;
                bSum -= boutSum;

                stackStart = stackPointer - radius + div;
                sir = stack[stackStart % div];

                routSum -= sir[0];
                goutSum -= sir[1];
                boutSum -= sir[2];

                if (y == 0) {
                    vMin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vMin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinSum += sir[0];
                ginSum += sir[1];
                binSum += sir[2];

                rSum += rinSum;
                gSum += ginSum;
                bSum += binSum;

                stackPointer = (stackPointer + 1) % div;
                sir = stack[(stackPointer) % div];

                routSum += sir[0];
                goutSum += sir[1];
                boutSum += sir[2];

                rinSum -= sir[0];
                ginSum -= sir[1];
                binSum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinSum = ginSum = binSum = routSum = goutSum = boutSum = rSum = gSum = bSum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rSum += r[yi] * rbs;
                gSum += g[yi] * rbs;
                bSum += b[yi] * rbs;

                if (i > 0) {
                    rinSum += sir[0];
                    ginSum += sir[1];
                    binSum += sir[2];
                } else {
                    routSum += sir[0];
                    goutSum += sir[1];
                    boutSum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackPointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rSum] << 16) | (dv[gSum] << 8) | dv[bSum];

                rSum -= routSum;
                gSum -= goutSum;
                bSum -= boutSum;

                stackStart = stackPointer - radius + div;
                sir = stack[stackStart % div];

                routSum -= sir[0];
                goutSum -= sir[1];
                boutSum -= sir[2];

                if (x == 0) {
                    vMin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vMin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinSum += sir[0];
                ginSum += sir[1];
                binSum += sir[2];

                rSum += rinSum;
                gSum += ginSum;
                bSum += binSum;

                stackPointer = (stackPointer + 1) % div;
                sir = stack[stackPointer];

                routSum += sir[0];
                goutSum += sir[1];
                boutSum += sir[2];

                rinSum -= sir[0];
                ginSum -= sir[1];
                binSum -= sir[2];

                yi += w;
            }
        }

        // set Bitmap
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
}
