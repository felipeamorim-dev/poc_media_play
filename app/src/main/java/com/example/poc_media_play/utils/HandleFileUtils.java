package com.example.poc_media_play.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class HandleFileUtils {

    public static void VerifyExistDirectoryAssets(File assets) {
        if (!assets.exists()) {
            assets.mkdir();
        }
    }

    public static boolean isFileDirectory(String title, Context context) {
        File dicFile = new File(context.getFilesDir() + "/assets", title);
        return dicFile.exists();
    }

    public static void transferMusicDirectory(File inFile, File outFile) throws Exception {

        if (inFile.exists()) {
            try (FileChannel inChannel = new FileInputStream(inFile).getChannel(); FileChannel outChannel = new FileOutputStream(outFile).getChannel()) {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            }
        }

        boolean result = inFile.delete();
        Log.d("File", "Arquivo deletado: " + result);
    }
}
