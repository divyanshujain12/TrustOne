package com.example.deii.Utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Utils {

    public static Bitmap GetBitmapClippedCircle(Bitmap bitmap, int widthCircle) {

        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height,
                Config.ARGB_8888);

        final Path path = new Path();
        path.addCircle((float) (width / 2), (float) (height / 2),
                (float) Math.min(width, (height / 2) - widthCircle),
                Path.Direction.CCW); // reduce radius by 2 to fill the yellow
        // stroke

        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outputBitmap;
    }

    // get rounded video

    // check network
    public static boolean haveNetworkConnection(Context ctx) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    /*
     * ==========================================================================
     * ========================== Decodes image and scales it to reduce memory
     * consumption
     * ==============================================================
     * ======================================
     */
    public static Bitmap decodeFile(File f) {
        Bitmap b = null;
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            // o.inPreferredConfig = Bitmap.Config.ARGB_8888;
            o.inScaled = true;
            o.inJustDecodeBounds = true;
            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
            // The new size we want to scale to
            final int IMAGE_MAX_SIZE = 400;
            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(
                        2,
                        (int) Math.round(Math.log(IMAGE_MAX_SIZE
                                / (double) Math.max(o.outHeight, o.outWidth))
                                / Math.log(0.5)));
            }
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (IOException e) {
        }
        return b;
    }

    public static int getCameraPhotoOrientation(Context context, Uri imageUri,
                                                String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {

        }
        return rotate;
    }

    public static void showAlert(Context ctx, String alertMsg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
        builder1.setMessage(alertMsg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {

        if (bitmap == null)
            return null;

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff000000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDateTime() {
        String date = "";

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        date = df.format(c.getTime());
        return date;
    }


    public static String encodeFileToBase64Binary(File fileName)
            throws IOException {
        byte[] bytes = convertFileToByteArray(fileName);
        byte[] encoded = Base64.encode(bytes, Base64.NO_WRAP);
        String encodedString = new String(encoded);
        return encodedString;
    }

    public static byte[] convertFileToByteArray(File f) {
        byte[] byteArray = null;
        try {
            @SuppressWarnings("resource")
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 8];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    public static Bitmap rotateImageBitmap(String imagePath,Bitmap bitmap){
        Bitmap capturedImage = bitmap;
        try {

            ExifInterface ei = new ExifInterface(imagePath);
            int orientation = ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    capturedImage = rotateImage(capturedImage, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    capturedImage = rotateImage(capturedImage, 180);
                    break;
                // etc.
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return capturedImage;
    }

    private static Bitmap rotateImage(Bitmap yourSelectedImage, int i) {
        // TODO Auto-generated method stub
        Matrix mat = new Matrix();
        mat.postRotate(90);
        Bitmap bMapRotate = Bitmap.createBitmap(yourSelectedImage, 0, 0,
                yourSelectedImage.getWidth(), yourSelectedImage.getHeight(),
                mat, true);

        return bMapRotate;
    }
}
