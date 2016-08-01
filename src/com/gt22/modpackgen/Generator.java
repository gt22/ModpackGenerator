package com.gt22.modpackgen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.google.gson.JsonObject;

public class Generator
{
	public static void generate(String id, String name, String version, String mcversion, File logo, File desc) throws IOException
	{
		File centralfolder = new File(new File("").getAbsolutePath() + File.separator + id);
		File logoout = new File(centralfolder, "logo.png");
		FileUtils.initFile(logoout);
		FileUtils.copyFile(logoout, logo);
		File descout = new File(centralfolder, "desc.txt");
		FileUtils.initFile(descout);
		FileUtils.copyFile(descout, desc);
		File modpackdef = new File(centralfolder, "mpdef.json");
		FileUtils.initFile(modpackdef);
		FileOutputStream o = new FileOutputStream(modpackdef);
		o.write(generateJson(id, name, version, mcversion).toString().getBytes());
		o.close();
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
