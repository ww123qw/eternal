package com.rocketmq.rocketmq.utils;



    import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


    /**
     * 通过Java的Zip输入输出流实现压缩和解压文件
     *
     * @author liujiduo
     *
     */
    public final class ZipUtils {

        private ZipUtils() {
            // empty
        }

        /**
         * 压缩文件
         *
         * @param filePath
         *            待压缩的文件路径
         * @return 压缩后的文件
         */
        public static File zip(String filePath) {
            File target = null;
            File source = new File(filePath);
            if (source.exists()) {
                // 压缩文件名=源文件名.zip
                String zipName = source.getName() + ".zip";
                target = new File(source.getParent(), zipName);
                if (target.exists()) {
                    target.delete(); // 删除旧的文件
                }
                FileOutputStream fos = null;
                ZipOutputStream zos = null;
                try {
                    fos = new FileOutputStream(target);
                    zos = new ZipOutputStream(new BufferedOutputStream(fos));
                    // 添加对应的文件Entry
                    addEntry("/", source, zos);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    IOUtil.closeQuietly(zos, fos);
                }
            }
            return target;
        }

        /**
         * 扫描添加文件Entry
         *
         * @param base
         *            基路径
         *
         * @param source
         *            源文件
         * @param zos
         *            Zip文件输出流
         * @throws IOException
         */
        private static void addEntry(String base, File source, ZipOutputStream zos)
                throws IOException {
            // 按目录分级，形如：/aaa/bbb.txt
            String entry = base + source.getName();
            if (source.isDirectory()) {
                for (File file : source.listFiles()) {
                    // 递归列出目录下的所有文件，添加文件Entry
                    addEntry(entry + "/", file, zos);
                }
            } else {
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    byte[] buffer = new byte[1024 * 10];
                    fis = new FileInputStream(source);
                    bis = new BufferedInputStream(fis, buffer.length);
                    int read = 0;
                    zos.putNextEntry(new ZipEntry(entry));
                    while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                        zos.write(buffer, 0, read);
                    }
                    zos.closeEntry();
                } finally {
                    IOUtil.closeQuietly(bis, fis);
                }
            }
        }

        /**
         * 解压文件
         *
         * @param filePath
         *            压缩文件路径
         */
        public static void unzip(String filePath) {
            File source = new File(filePath);
            if (source.exists()) {
                ZipInputStream zis = null;
                BufferedOutputStream bos = null;
                try {
                    zis = new ZipInputStream(new FileInputStream(source));
                    ZipEntry entry = null;
                    while ((entry = zis.getNextEntry()) != null
                            && !entry.isDirectory()) {
                        File target = new File(source.getParent(), entry.getName());
                        if (!target.getParentFile().exists()) {
                            // 创建文件父目录
                            target.getParentFile().mkdirs();
                        }
                        // 写入文件
                        bos = new BufferedOutputStream(new FileOutputStream(target));
                        int read = 0;
                        byte[] buffer = new byte[1024 * 10];
                        while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                            bos.write(buffer, 0, read);
                        }
                        bos.flush();
                    }
                    zis.closeEntry();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    IOUtil.closeQuietly(zis, bos);
                }
            }
        }

        public boolean deletes(File file){
            if(file.isFile()){
                file.delete();
                return true;
            }
            File[] files = file.listFiles();
            for (File file1 : files) {
                if(file1 != null){
                    if(file1.isFile()){
                        file1.delete();
                    }else{
                        deletes(file1);
                    }
                }
            }
            file.delete(); // 删除剩余空文件夹
            return true;
        }

        public static void main(String[] args) {
            String targetPath = "D:\\house_img";//D:\temp_by_zongdi
            File file1 = new File("D:\\temp_by_zongdi");
           //删除文件夹
            ZipUtils zipUtils = new ZipUtils();
            boolean deletes = zipUtils.deletes(new File("D:\\temp_by_zongdi"));
            System.out.println("deletes = " + deletes);

            File file = ZipUtils.zip(targetPath);
            System.out.println(file);
            ZipUtils.unzip("F:\\Win7壁纸.zip");
        }
    }

