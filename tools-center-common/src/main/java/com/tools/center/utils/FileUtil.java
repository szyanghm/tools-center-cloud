package com.tools.center.utils;

import com.tools.center.enums.SuffixEnum;
import com.tools.center.enums.SystemEnum;
import com.tools.center.utils.ofd.OFDInfo;
import com.tools.center.utils.ofd.OfdParser;
import com.tools.center.utils.ofd.model.Invoice;
import com.tools.center.vo.FileVO;
import com.tools.center.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Slf4j
public class FileUtil {


    /**
     * 使用GBK编码可以避免压缩中文文件名乱码
     */
    private static final String CHINESE_CHARSET = "GBK";

    /**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;

    /**
     * 去除后缀，截取文件名
     * @param fileName
     * @return
     */
    public static String getFileName(String fileName){
        fileName = fileName.substring(0,fileName.length()-4);
        return fileName;
    }

    /**
     * 设置判断文件大小
     * @param fileSize kb
     * @param maxSize kb
     * @return
     */
    public static boolean checkFileSize(Long fileSize,Long maxSize) {
        long size = fileSize/1024;
        if(size>maxSize||size<10){
            return false;
        }
        return true;
    }

    /**
     * 截取文件名后缀
     * @param fileName 文件名
     * @return
     */
    public static String takeOutSuffix(String fileName){
        int begin = fileName.indexOf(".");
        int last = fileName.length();
        String suffix = fileName.substring(begin, last);
        return suffix;
    }

    /**
     * 批量解析OFD及PDF发票
     * @param file zip压缩包
     * @param fileSize 文件大小
     * @param maxSize 配置文件最大值
     * @return
     */
    public static ResultVO getAllInvoice(File file,Long fileSize,Long maxSize){
        if(!checkFileSize(fileSize,maxSize)){
            return ResultVO.error(SystemEnum.UPLOAD_FILE_SIZE_LIMIT);
        }
        String suffix = takeOutSuffix(file.getName());
        if(!suffix.endsWith(SuffixEnum.ZIP.getSuffix())){
            return ResultVO.error(SystemEnum.FILE_TYPE_NOT);
        }
        List<File> files = unZip(file);
        List<Invoice> list = new ArrayList<>();
        for (File f:files){
            try {
                OFDInfo ofdInfo = OfdParser.parse(f);
                Invoice invoice = ofdInfo.getInvoice();
                list.add(invoice);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FileUtil.delTempFile(f);
        }
        return ResultVO.success(list);
    }

    /**
     * 功能:压缩多个文件成一个zip文件
     * @param list：源文件列表
     * @param zipfile：压缩后的文件
     */
    public static void zipFiles(List<FileVO> list,File zipfile){
        byte[] buf=new byte[1024];
        try {
            //ZipOutputStream类：完成文件或文件夹的压缩
            ZipOutputStream out=new ZipOutputStream(new FileOutputStream(zipfile));
            for(FileVO vo: list){
                out.putNextEntry(new ZipEntry(vo.getFileName()));
                int len;
                InputStream in = vo.getInputStream();
                while((len=in.read(buf))>0){
                    out.write(buf,0,len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
            System.out.println("压缩完成.");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 功能:压缩多个文件成一个zip文件
     * @param srcfile：源文件列表
     * @param zipfile：压缩后的文件
     */
    public static void zipFiles(File[] srcfile,File zipfile){
        byte[] buf=new byte[1024];
        try {
            //ZipOutputStream类：完成文件或文件夹的压缩
            ZipOutputStream out=new ZipOutputStream(new FileOutputStream(zipfile));
            for(int i=0;i<srcfile.length;i++){
                FileInputStream in=new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while((len=in.read(buf))>0){
                    out.write(buf,0,len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
            System.out.println("压缩完成.");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    /**
     *
     * @param filePath 下载路径
     * @param zipName 导出zip文件名字  如：XXX.zip
     * @param list 下载文件名列表
     * @param response
     * @throws Exception
     */
    public static void downloadZip(String filePath, String zipName,
                                   List<FileVO> list, HttpServletResponse response) {
        //导出压缩文件的全路径
        String zipFilePath = filePath + zipName ;//"xxx.zip";
        //导出zip
        File zip = new File(zipFilePath);
        zipFiles(list, zip);
        response.setContentType("application/zip;charset=UTF-8");
        response.addHeader("responseType","blob");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" +
                    new String(zip.getName().getBytes("UTF-8"), "ISO-8859-1"));
            OutputStream outputStream = response.getOutputStream();
            InputStream inputStream = new FileInputStream(zipFilePath);
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            // 删除完里面所有内容
            delAllFile(filePath);
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); // 删除空文件夹
        }catch (Exception e){
            log.error("下载流程定义文件异常:{}",e.getMessage());
        }
    }





    /**
     * 解压zip
     * @param file zip压缩包
     * @return base64字符串集合
     */
    public static List<File> unZip(File file){
        List<File> list = new ArrayList<>();
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        try {
            ZipFile zipFile = new ZipFile(file,"gbk");
            Enumeration<ZipEntry> enumeration =  zipFile.getEntries();
            ZipEntry zipEntry;
            int i=0;
            while (enumeration.hasMoreElements()){
                zipEntry = enumeration.nextElement();
                i++;
                if(i==1){
                    continue;
                }
                String suffix = takeOutSuffix(zipEntry.getName());
                if(suffix.endsWith(SuffixEnum.PDF.getSuffix())||suffix.endsWith(SuffixEnum.OFD.getSuffix())){
                    // 表示文件
                    File f = new File(path+zipEntry.getName());
                    InputStream is = zipFile.getInputStream(zipEntry);
                    if (!f.exists()) {
                        File fileParent = f.getParentFile();
                        if(!fileParent.exists()){
                            fileParent.mkdirs();
                        }
                    }
                    f.createNewFile();
                    // 将压缩文件内容写入到这个文件中
                    FileOutputStream fos = new FileOutputStream(f);
                    int count;
                    byte[] buf = new byte[8192];
                    while ((count = is.read(buf)) != -1) {
                        fos.write(buf, 0, count);
                    }
                    fos.flush();
                    fos.close();
                    is.close();
                    list.add(f);
                }
            }
        } catch (Exception e) {
            log.error("文件转换发生异常:{}", e);
        }
        return list;
    }

    /**
     * 文件转base64
     * @param file
     * @return
     */
    public static String PDFToBase64(File file) {
        BASE64Encoder encoder = new BASE64Encoder();
        FileInputStream fin =null;
        BufferedInputStream bin =null;
        ByteArrayOutputStream baos = null;
        BufferedOutputStream bout =null;
        try {
            fin = new FileInputStream(file);
            bin = new BufferedInputStream(fin);
            baos = new ByteArrayOutputStream();
            bout = new BufferedOutputStream(baos);
            byte[] buffer = new byte[1024];
            int len = bin.read(buffer);
            while(len != -1){
                bout.write(buffer, 0, len);
                len = bin.read(buffer);
            }
            //刷新此输出流并强制写出所有缓冲的输出字节
            bout.flush();
            byte[] bytes = baos.toByteArray();
            return encoder.encodeBuffer(bytes).trim();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fin.close();
                bin.close();
                bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {
        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    /**
     * 将inputStream转化为file
     * @param ins
     * @param file 要输出的文件目录
     */
    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     * @param file
     */
    public static void delTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }

    /***
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static  boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                flag = true;
            }
        }
        return flag;
    }


    public static byte[] imageToBytes(BufferedImage bImage) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, "jpg", out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return out.toByteArray();
    }

}
