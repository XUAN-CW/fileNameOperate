import java.io.*;

public class FileOperate {
    public void copy(File sourceFile,File destination) {
        try {
            //创建输入输出流对象
            FileInputStream fis = new FileInputStream(sourceFile);
            FileOutputStream fos = new FileOutputStream(destination);
            //创建搬运工具
            byte datas[] = new byte[1024 * 8];
            //创建长度
            int len = 0;
            //循环读取数据
            while ((len = fis.read(datas)) != -1) {
                fos.write(datas, 0, len);
            }
            //3.释放资源
            fis.close();
            fos.close();
            System.out.println(sourceFile.getAbsolutePath()+" →copy→ "+destination.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cutFile(File file1, File file2) {
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        byte[] bytes = new byte[1024];
        int temp = 0;
        try {
            inputStream = new FileInputStream(file1);
            outputStream = new FileOutputStream(file2);
            while ((temp = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, temp);
                outputStream.flush();
            }
            System.out.println(file1.getAbsolutePath()+" →cut→ "+file1.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
