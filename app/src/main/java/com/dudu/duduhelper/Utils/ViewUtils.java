package com.dudu.duduhelper.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lwz on 2016/8/23.
 */
public class ViewUtils {
	/**
	 * 把图片转换为字节码流
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static byte[] imageToBytes(String path) throws IOException {
		InputStream inputStream = new FileInputStream(path);
		BufferedInputStream in = new BufferedInputStream(inputStream);
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

		byte[] temp = new byte[1024];
		int size = 0;
		while ((size = in.read(temp)) != -1) {
			out.write(temp, 0, size);
		}
		in.close();
		byte[] content = out.toByteArray();
		return content;
	}
	/**
	 * Try to return the absolute file path from the given Uri
	 *
	 * @param context
	 * @param uri
	 * @return the file path or null
	 */
	public static String getRealFilePath(final Context context, final Uri uri ) {
		if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}
}
