package com.example.deii.Utils;

import android.content.Context;

import java.io.File;

public class FileCache {

    private File cacheDir;

    public FileCache(Context context) {
        // Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            //cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "KlicksCache");
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "data");
        else
            cacheDir = context.getCacheDir();

        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String url) {
        // I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename = String.valueOf(url.hashCode());
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        File f = new File(cacheDir, filename);
        return f;
    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        for (File f : files)
            f.delete();
    }

    public boolean isFileEmpity() {
        File[] files = cacheDir.listFiles();
        boolean flag = false;
        for (File f : files) {
            if (f == null) {
                flag = true;
            }
        }
        return flag;
    }

}