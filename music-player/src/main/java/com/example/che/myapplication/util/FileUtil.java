package com.example.che.myapplication.util;

import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {
    public static byte[] file2bytes(File file) {
        byte[] ret = null;
        try {
            if (file == null) {
                return null;
            }
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
            ret = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String file2string(File file) {
        try {
            byte[] ret = file2bytes(file);
            if (ret != null) {
                return Base64.encodeToString(ret, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除目录（文件夹）以及目录下的文�?
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔�?
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则�??�?
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        // 删除文件夹下的所有文�?(包括子目�?)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文�?
            if (files[i].isFile()) {
                deleteFile(files[i].getAbsolutePath());
            } // 删除子目�?
            else {
                deleteDirectory(files[i].getAbsolutePath());
            }
        }
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFiles(String sPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔�?
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则�??�?
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        // 删除文件夹下的所有文�?(包括子目�?)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文�?
            if (files[i].isFile()) {
                deleteFile(files[i].getAbsolutePath());
            } // 删除子目�?
            else {
                deleteDirectory(files[i].getAbsolutePath());
            }
        }
        // 判断目录下的文件是否清除
        if (dirFile.listFiles().length > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与�?
     *
     * @param sPath 要删除的目录或文�?
     * @return 删除成功返回 true，否则返�? false�?
     */
    public static boolean deleteFolder(String sPath) {
        File file = new File(sPath);
        // 判断目录或文件是否存�?
        if (!file.exists()) { // 不存在返�? false
            return false;
        } else {
            // 判断是否为文�?
            if (file.isFile()) { // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else { // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件�?
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
        }
        return true;
    }

    /**
     * 创建文件�?
     *
     * @param file
     */
    public static void mkdirs(File file) {
        if (!file.exists()) {
            if (file.mkdirs()) {
                MLogUtil.e(file.getAbsoluteFile() + "文件夹创建成�?");
            } else {
                MLogUtil.e(file.getAbsoluteFile() + "文件夹创建失�?");
            }
        } else {
            MLogUtil.e(file.getAbsoluteFile() + "文件夹已存在");
        }
    }

    /**
     * 下载文件
     *
     * @param urlPath
     * @param file
     * @return
     */
    public static File downloadFile(String urlPath, File file) {
        if (file.exists()) {
            return file;
        } else {
            String path = file.getPath();
            mkdirs(new File(path.substring(0, path.lastIndexOf("/"))));
        }
        try {
            String str = java.net.URLEncoder.encode(urlPath, "UTF-8");
            str = str.replaceAll("%2F", "/");// �?要把网址的特殊字符转过来
            str = str.replaceAll("%3A", ":");
            URL url = new URL(str);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.getResponseCode();
            if (conn.getResponseCode() == 200) {
                InputStream inSream = conn.getInputStream();
                file.createNewFile();
                FileOutputStream outStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = inSream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                outStream.close();
                inSream.close();
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取文本文件
     *
     * @param strFilePath,要读取的文件
     * @return 文件内容
     */

    public static String ReadTxtFile(String strFilePath) {
        MLogUtil.e("打开txt文件:" + strFilePath);
        String path = strFilePath;
        String content = ""; // 文件内容字符�?
        // 打开文件
        File file = new File(path);
        // 如果path是传递过来的参数，可以做�?个非目录的判�?
        System.out.println(file.exists());
        if (file.exists()) {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream, "UTF-8");
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    // 分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    public static File byte2file(byte[] bfile, String filePath) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    public static boolean fileToZip(List<String> files, String zipFilePath) {
        boolean flag = false;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            File zipFile = new File(zipFilePath);
            if (zipFile.exists()) {
                zipFile.delete();
            }
            zipFile.createNewFile();
            String[] sourceFiles = files.toArray(new String[files.size()]);
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(new BufferedOutputStream(fos));
            byte[] bufs = new byte[1024 * 10];
            for (int i = 0; i < sourceFiles.length; i++) {
                // 创建ZIP实体,并添加进压缩包
                File f=new File(sourceFiles[i]);
                if(f.exists()){
                    try{
                        ZipEntry zipEntry = new ZipEntry(f.getName());
                        zos.putNextEntry(zipEntry);
                        // 读取待压缩的文件并写进压缩包里
                        fis = new FileInputStream(f);
                        bis = new BufferedInputStream(fis, 1024 * 10);
                        int read = 0;
                        while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                            zos.write(bufs, 0, read);
                        }
                    }catch (Exception e){

                    }
                }
            }
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                if (null != bis)
                    bis.close();
                if (null != zos)
                    zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }
}
