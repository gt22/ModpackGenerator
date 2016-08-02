package com.gt22.modpackgen;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtils
{

	/**
	 * Create new BufferredReader for file
	 */
	public static BufferedReader createReader(File file) throws FileNotFoundException
	{
		return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	}

	/**
	 * Create new BufferredWriter for file
	 */
	public static BufferedWriter createWriter(File file) throws FileNotFoundException
	{
		return createWriter(file, true);
	}

	/**
	 * Create new BufferredWriter for file
	 * @param append file, or rewrite it using function wirte();
	 */
	public static BufferedWriter createWriter(File file, boolean append) throws FileNotFoundException
	{
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append)));
	}

	/**
	 * Crates file if it not created
	 * @param file
	 */
	public static void initFile(File file) throws IOException
	{
		if (!file.canWrite())
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
	}

	public static void recreateFile(File file) throws IOException
	{
		if (file.exists())
		{
			file.delete();
		}
		initFile(file);
	}

	/**
	 * Copy content of one file int another, files must exist, use {@link #initFile(File)}
	 * @param to
	 * @param from
	 * @throws IOException
	 */
	public static void copyFile(File to, File from) throws IOException
	{
		FileInputStream in = new FileInputStream(from);
		FileOutputStream out = new FileOutputStream(to);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) != -1)
		{
			out.write(buf, 0, len);
		}
		out.close();
		in.close();
	}

	/**
	 * Add line to file
	 * @param file
	 * @param line
	 */
	public static void addLine(File file, String line) throws IOException
	{
		addLine(file, line, true);
	}

	/**
	 * Add line to file
	 * @param file
	 * @param line
	 * @param breakLine - insert '\n' or not
	 */
	public static void addLine(File file, String line, boolean breakLine) throws IOException
	{
		BufferedWriter writer = createWriter(file);
		writer.write(line);
		if (breakLine)
		{
			writer.newLine();
		}
		writer.close();
	}

	/** 
	  	Deletes the line at specified position (File must exist)
		@param file
		@param linetodelete - line number of required line
	 */
	public static void deleteLine(File file, int linetodelete) throws IOException
	{
		BufferedReader br = createReader(file);
		StringBuffer sb = new StringBuffer("");
		int linenumber = 1;
		String line;

		while ((line = br.readLine()) != null)
		{
			if (linenumber != linetodelete)
				sb.append(line + "\n");
			linenumber++;
		}
		br.close();
		BufferedWriter fw = createWriter(file, false);
		fw.write(sb.toString());
		fw.close();
	}

	/**
	 	Deletes the given line (File must exist)
		@param file
		@param line
	 */
	public static void deleteLine(File file, String line) throws IOException
	{
		BufferedReader list = FileUtils.createReader(file);
		String temp = "";
		int i = 0;
		while ((temp = list.readLine()) != null && !temp.equals(line))
		{
			i++;
		}
		list.close();
		deleteLine(file, i);
	}

	public static String readFile(File file) throws IOException
	{
		String ret = "";
		BufferedReader in = createReader(file);
		for (String tmp = in.readLine(); tmp != null; tmp = in.readLine())
		{
			ret += tmp + '\n';
		}
		return ret;
	}

	public static void download(URL url, File dest) throws IOException
	{
		if (dest.exists())
			dest.delete();
		InputStream in = new BufferedInputStream(url.openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while ((n = in.read(buf)) != -1)
		{
			out.write(buf, 0, n);
		}
		out.close();
		in.close();
		initFile(dest);
		FileOutputStream fos = new FileOutputStream(dest);
		fos.write(out.toByteArray());
		fos.close();
	}

	public static void unzip(File destDir, File archive) throws IOException
	{
		if (!destDir.exists())
		{
			destDir.mkdirs();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(archive));
		ZipEntry entry = zipIn.getNextEntry();
		while (entry != null)
		{
			String filePath = destDir.getAbsolutePath() + File.separator + entry.getName();
			if (!entry.isDirectory())
			{
				extractFile(zipIn, filePath);
			}
			else
			{
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
		archive.delete();
	}

	private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException
	{
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[4096];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1)
		{
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	public static void zipFile(File file, ZipOutputStream archive, ZipEntry zipfile) throws IOException
	{
		archive.putNextEntry(zipfile);
		FileInputStream filetozip = new FileInputStream(file);
		byte[] buf = new byte[1024];
		for (int l = filetozip.read(buf); l != -1; l = filetozip.read(buf))
		{
			archive.write(buf, 0, l);
		}
		filetozip.close();
		archive.closeEntry();
	}

	public static void zipFolder(File folder, ZipOutputStream archive) throws IOException
	{
		zipFolder(folder, archive, false);
	}

	public static void zipFolder(File folder, ZipOutputStream archive, boolean createfolder) throws IOException
	{
		File[] files = folder.listFiles();
		if (createfolder)
		{
			zipFile(folder, archive, new ZipEntry(folder.getName()));
		}
		for (File f : files)
		{
			FileUtils.zipFile(f, archive, new ZipEntry(f.getName()));
			if (f.isDirectory())
			{
				zipFolder(f, archive, true);
			}
		}
	}

	public static boolean containsFile(File dir, String file)
	{
		return dir.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name == file;
			}
		}) != null;
	}

}
