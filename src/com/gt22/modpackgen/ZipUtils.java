package com.gt22.modpackgen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils
{

	private List<String> fileList;
	private ZipOutputStream OUTPUT_ZIP_FILE;
	private String SOURCE_FOLDER; // SourceFolder path

	public ZipUtils()
	{
		fileList = new ArrayList<String>();
	}

	public void zipFolder(String absolutefolderpath, ZipOutputStream outputto)
	{	
		generateFileList(new File(SOURCE_FOLDER = absolutefolderpath));
		zipIt(OUTPUT_ZIP_FILE=outputto);
	}

	public void zipIt(ZipOutputStream zos)
	{
		byte[] buffer = new byte[1024];
		try
		{
			FileInputStream in = null;

			for (String file : this.fileList)
			{
				System.out.println("File Added : " + file);
				ZipEntry ze = new ZipEntry(file);
				zos.putNextEntry(ze);
				try
				{
					in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
					int len;
					while ((len = in.read(buffer)) > 0)
					{
						zos.write(buffer, 0, len);
					}
				}
				finally
				{
					in.close();
				}
			}

			zos.closeEntry();
			System.out.println("Folder successfully compressed");

		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public void generateFileList(File node)
	{

		// add file only
		if (node.isFile())
		{
			fileList.add(generateZipEntry(node.toString()));

		}

		if (node.isDirectory())
		{
			String[] subNote = node.list();
			for (String filename : subNote)
			{
				generateFileList(new File(node, filename));
			}
		}
	}

	private String generateZipEntry(String file)
	{
		return file.substring(SOURCE_FOLDER.length() + 1, file.length());
	}
}
