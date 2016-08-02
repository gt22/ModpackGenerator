package com.gt22.modpackgen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.google.gson.JsonObject;

public class Generator
{
	public static void generate(String id, String name, String version, String mcversion, File logo, File desc, File modpackdir, boolean useScript) throws IOException
	{
		File centralfolder = new File(new File("").getAbsolutePath() + File.separator + id);
		createDefArchive(logo, desc, generateMPDEF(id, name, version, mcversion, centralfolder), centralfolder);
		createPackArchive(centralfolder, modpackdir, useScript);
	}
	
	private static void createPackArchive(File folder, File modpackfolder, boolean useScript) throws IOException
	{
		File pack = new File(folder, "pack.zip");
		FileUtils.initFile(pack);
		ZipOutputStream packwriter = new ZipOutputStream(new FileOutputStream(pack));
		/*new ZipUtils().zipFolder(new File(folder, "mods").getAbsolutePath(), packwriter);
		new ZipUtils().zipFolder(new File(folder, "config").getAbsolutePath(), packwriter);
		if(useScript)
			new ZipUtils().zipFolder(new File(folder, "scripts").getAbsolutePath(), packwriter);*/
		new ZipUtils().zipFolder(modpackfolder.getAbsolutePath(), packwriter);
		packwriter.close();
	}

	private static void createDefArchive(File logo, File desc, File modpackdef, File folder) throws IOException
	{
		File def = new File(folder, "def.zip");
		FileUtils.initFile(def);
		ZipOutputStream zo = new ZipOutputStream(new FileOutputStream(def));
		FileUtils.zipFile(logo, zo, new ZipEntry("logo.png"));
		FileUtils.zipFile(desc, zo, new ZipEntry("desc.txt"));
		FileUtils.zipFile(modpackdef, zo, new ZipEntry("mpdef.txt"));
		zo.close();
		modpackdef.delete();
	}
	
	private static File generateMPDEF(String id, String name, String version, String mcversion, File folder) throws IOException
	{
		File modpackdef = new File(folder, "mpdef.json");
		FileUtils.initFile(modpackdef);
		FileOutputStream o = new FileOutputStream(modpackdef);
		o.write(generateJson(id, name, version, mcversion).toString().getBytes());
		o.close();
		return modpackdef;
	}

	private static JsonObject generateJson(String id, String name, String version, String mcversion)
	{
		JsonObject ret = new JsonObject();
		ret.addProperty("id", id);
		ret.addProperty("name", name);
		ret.addProperty("version", version);
		ret.addProperty("mcversion", mcversion);
		return ret;
	}
}
