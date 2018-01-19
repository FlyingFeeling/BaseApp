package com.feeling.mybaseapp.utils;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/1/5.
 */

public class FileUtil {
    /**
     * 判断目录是否存在，不存在则创建目录，并判断是否创建成功
     *
     * @param dirPath 文件路径
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(String dirPath) {
        return createOrExistsDir(new File(dirPath));
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(File file) {
        //如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsFile(String filePath) {
        return createOrExistsFile(new File(filePath));
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsFile(File file) {
        if (file == null) return false;
        // 如果存在，是文件则返回true，是目录则返回false
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断文件是否存在，存在则在创建之前删除
     *
     * @param filePath 文件路径
     * @return {@code true}: 创建成功<br>{@code false}: 创建失败
     */
    public static boolean createFileByDeleteOldFile(String filePath) {
        return createFileByDeleteOldFile(new File(filePath));
    }

    /**
     * 判断文件是否存在，存在则在创建之前删除
     *
     * @param file 文件
     * @return {@code true}: 创建成功<br>{@code false}: 创建失败
     */
    public static boolean createFileByDeleteOldFile(File file) {
        if (file == null) return false;
        // 文件存在并且删除失败返回false
        if (file.exists() && file.isFile() && !file.delete()) return false;
        // 创建目录失败返回false
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 遍历文件夹下的所有文件，不包括子文件夹下的文件
     * */
    public static List<File> traversal(String path,boolean isTraversalChild){
        File file=new File(path);
        return traversal(file,isTraversalChild);
    }

    public static List<File> traversal(File file,boolean isTraversalChild){
        List<File> list = new ArrayList<>();
        if(file!=null||file.isDirectory()){
            File[] files = file.listFiles();
            if (files != null && files.length != 0) {
                if(isTraversalChild){
                    for (File f : files) {
                        list.add(f);
                        list.addAll(traversal(f,isTraversalChild));
                    }
                }else {
                    for (File f : files) {
                        list.add(f);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 删除目录下的文件(删除目录下的所有文件包含所有子文件夹 但不包括该文件)
     *
     * @param dirPath 目录路径
     * @return {@code true}: 删除成功<br>{@code false}: 删除失败
     */
    public static boolean deleteAllChild(String dirPath) {
        File dir = new File(dirPath);
        return deleteAllChild(dir);
    }

    /**
     * 删除目录下的文件(删除目录下的所有文件包含所有子文件夹 但不包括该文件)
     *
     * @param dir 目录
     * @return {@code true}: 删除成功<br>{@code false}: 删除失败
     */
    public static boolean deleteAllChild(File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return false;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    deleteDir(file);
                }
            }
        }
        return files.length==0 ? true : false;
    }

    /**
     * 删除目录(删除目录下的所有文件包含所有子文件夹)
     * */
    public static boolean deleteDir(String dirPath) {
        File dir = new File(dirPath);
        return deleteAllChild(dir);
    }

    /**
     * 删除目录(删除目录下的所有文件包含所有子文件夹)
     * */
    public static boolean deleteDir(File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    deleteDir(file);
                }
            }
        }
        return dir.delete();
    }

    /**
     * 获取指定文件夹下特定后缀名的文件
     * */
    public static List<File> traversalWithSuffix(String dirPath, String suffix,boolean isTraversalChild){
        File dir = new File(dirPath);
        return traversalWithSuffix(dir,suffix,isTraversalChild);
    }

    /**
     * 获取指定文件夹下特定后缀名的文件
     * */
    public static List<File> traversalWithSuffix(File dir, String suffix,boolean isTraversalChild){
        List<File> list = new ArrayList<>();
        if(dir!=null||dir.isDirectory()){
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                if(isTraversalChild){
                    for (File f : files) {
                        if (f.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                            list.add(f);
                        }
                        list.addAll(traversalWithSuffix(f,suffix,isTraversalChild));
                    }
                }else {
                    for (File f : files) {
                        if (f.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                            list.add(f);
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 根据文件路径，将指定的字符串内容写入文件
     *
     * @param filePath 文件路径
     * @param content  指定字符串
     * @param append   是否是附加内容，true：写入文件的尾部，false：写入文件的开头
     * @return 如果content为空则返回false
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred.", e);
        }
    }

    /**
     * 根据文件路径，将指定的字符串序列内容写入文件
     *
     * @param filePath    文件路径
     * @param contentList 字符串序列
     * @param append      是否是附加内容，true：写入文件的尾部，false：写入文件的开头
     * @return 如果content为空则返回false
     */
    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
        if (contentList.size() == 0 || null == contentList) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath, append);
            int i = 0;
            for (String line : contentList) {
                if (i++ > 0) {
                    fileWriter.write("\r\n");
                }
                fileWriter.write(line);
            }
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        }
    }

    /**
     * 根据文件路径，并将指定的字符串内容写入文件的开头
     *
     * @param filePath 文件路径
     * @param content  字符串
     * @return 是否写入成功
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    /**
     * 根据文件路径，将指定的字符串序列内容写入文件的开头
     *
     * @param filePath
     * @param contentList
     * @return
     */
    public static boolean writeFile(String filePath, List<String> contentList) {
        return writeFile(filePath, contentList, false);
    }


    /**
     * 将指定的输入流写入指定文件
     *
     * @param file   待被写入的文件
     * @param stream 指定的将写入文件的输入流
     * @param append true：写在文件尾部，false：写在文件开头
     * @return 是否写入成功
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        if(file==null)return false;
        OutputStream os = null;
        try {
            os = new FileOutputStream(file, append);
            byte[] data = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                os.write(data, 0, length);
            }
            os.flush();
            stream.close();
            os.close();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        }
    }

    /**
     * 将指定的输入流写入指定文件的开头
     *
     * @param filePath 待被写入的文件的路径
     * @param stream   指定的将写入文件的输入流
     * @return 是否写入成功
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);

    }

    /**
     * 将指定的输入流写入指定文件的开头
     *
     * @param file   待被写入的文件
     * @param stream 指定的将写入文件的输入流
     * @return 是否写入成功
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);

    }

    /**
     * 将指定的输入流写入指定文件
     *
     * @param filePath 待被写入的文件的路径
     * @param stream   指定的将写入文件的输入流
     * @param append   true：写在文件尾部，false：写在文件开头
     * @return 是否写入成功
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {

        return writeFile(filePath != null ? new File(filePath) : null, stream,
                append);
    }

    /**
     * 将字符串写入文件
     *
     * @param file    待被写入的文件
     * @param content 写入内容
     * @param append  是否追加在文件末
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFile(File file, String content, boolean append) {
        if (file == null || content == null) return false;
        createOrExistsFile(file);
        FileWriter bw = null;
        try {
            bw = new FileWriter(file, append);
            bw.write(content);
            bw.flush();
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getFileSize(File file){
        DecimalFormat df;
        double size=0;
        int flag=0;
        String unit="";
        if(file.exists()){
            size=file.length();
        }
        while (size>1024){
            size=size/(double) 1024;
            flag++;
        }
        switch (flag){
            case 0:
                df = new DecimalFormat("#");
                unit="B";
                break;
            case 1:
                df = new DecimalFormat("#.0");
                unit="KB";
                break;
            case 2:
                df = new DecimalFormat("#.0");
                unit="MB";
                break;
            case 3:
                df = new DecimalFormat("#.00");
                unit="GB";
                break;
            case 4:
                df = new DecimalFormat("#.00");
                unit="TB";
                break;
            default:
                df = new DecimalFormat("#.000");
                break;
        }
        return df.format(size)+unit;
    }

    /**
     * 文件拷贝
     * */
    public static void copyFile(File oldFile,File newFile) {
        if (oldFile.exists()) { //文件存在时
            try {
                int byteread = 0;
                InputStream in= new FileInputStream(oldFile); //读入原文件
                FileOutputStream out = new FileOutputStream(newFile);
                byte[] buffer = new byte[1024];
                while ((byteread = in.read(buffer)) != -1) {
                    out.write(buffer, 0, byteread);
                }
                in.close();
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 截取文件名称
     * @param fileName  文件名称
     */
    public static String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }


    /**
     * 更新相册图片
     * @param imgFile 图片文件
     * */
    public static void updataImages(Application application, File imgFile){
        ContentValues values = new ContentValues();
        ContentResolver resolver = application.getContentResolver();
        values.put(MediaStore.Images.ImageColumns.DATA, imgFile.getAbsolutePath());
        values.put(MediaStore.Images.ImageColumns.TITLE, imgFile.getName());
        Cursor cursor=resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.ImageColumns._ID},
                MediaStore.Images.ImageColumns.DATA+"=? and "+MediaStore.Images.ImageColumns.TITLE+"=?",
                new String[]{imgFile.getAbsolutePath(),imgFile.getName()},null);
        Uri uri=null;
        long time=System.currentTimeMillis();
        if(cursor.getCount()>0){
            if(cursor.moveToFirst()){
                String id=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                values.put(MediaStore.Images.ImageColumns.DATE_MODIFIED,time/1000);
                values.put(MediaStore.Images.ImageColumns.SIZE, imgFile.length());
                resolver.update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values, MediaStore.Images.ImageColumns._ID+"=?", new String[]{id});
                // 最后通知图库更新
                application.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file:/"+imgFile.getAbsolutePath())));
            }
        }else {
            values.put(MediaStore.Images.ImageColumns.DATE_TAKEN,time);
            values.put(MediaStore.Images.ImageColumns.DATE_ADDED,time/1000);
            values.put(MediaStore.Images.ImageColumns.SIZE, imgFile.length());
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
        cursor.close();
    }
}
