package com.xuecheng.media;

import com.baomidou.mybatisplus.extension.api.R;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ChunkTest {

    @Test
    public void chunk() throws IOException {
        //源文件
        File sourFile = new File("F:\\视频素材\\1.mp4");
        //上传文件的路径
        String chunkFilePath = "F:\\视频素材\\分块测试\\";
        //分块大小
        int chunkSize = 1024 * 1024 * 1;
        int chunkNumber = (int) Math.ceil(sourFile.length() * 1.0 / chunkSize);
        RandomAccessFile readStream = new RandomAccessFile(sourFile, "r");
        byte[] bytes = new byte[1024];
        for (int i = 0; i < chunkNumber; i++) {
            File chunkFile = new File(chunkFilePath + i);
            RandomAccessFile writeStream = new RandomAccessFile(chunkFile, "rw");
            int len;
            while ((len = readStream.read(bytes)) != -1) {
                writeStream.write(bytes,0,len);
                if (chunkFile.length() > +chunkSize) {
                    break;
                }
            }
            writeStream.close();
        }
        readStream.close();
    }
}
