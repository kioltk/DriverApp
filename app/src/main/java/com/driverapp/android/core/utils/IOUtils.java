package com.driverapp.android.core.utils;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Jesus Christ. Amen.
 */
public class IOUtils {
    private static ThreadLocal<byte[]> buffers = new ThreadLocal<byte[]>() {
        @Override
        protected byte[] initialValue() {
            return new byte[4 * 1024];
        }
    };
    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = buffers.get();
        int len;
        while ((len = in.read(buf)) > 0) {
            Thread.yield();
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static void copy(Uri uri, File dst, Context context) throws IOException {
        InputStream stream = context.getContentResolver().openInputStream(uri);
        try {
            copy(stream, dst);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public static void copy(InputStream in, File dst) throws IOException {
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = buffers.get();
        int len;
        while ((len = in.read(buf)) > 0) {
            Thread.yield();
            out.write(buf, 0, len);
        }
        out.close();
    }
}
