
package main;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import geo.gdal.GdalGlobal;
import main.model.nc2png.AutoDeployNC2PNG;
import main.model.nc2png.Flood2PNG;
import main.model.nc2png.Rainfall2PNG;
import usualTool.FileFunction;

public class NCHC_RainfallBigDataDisplay_preprocessing {

	public static String model = "-model";
	public static String outputAdd = "-outputFolder";
	public static String inputNC = "-inputNC";
	public static String GDAL_env = "-gdalENV";
	public static String AutoDeployConfig = "-configFile";
	public static String AutoDeployRootFolder = "-rootFolder";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, String> argsMap = getArgs(args);

//		Map<String, String> argsMap = new HashMap<>();
//		argsMap.put("-model", "NC2PNG_FLOOD");
//		argsMap.put("-inputNC",
//				"C:\\Users\\alter\\Desktop\\temptFolder\\NCHC-RainfallBigDataDisplay_preprocessing\\flood2PNG\\Event_00001_Flood.nc");
//		argsMap.put("-outputFolder",
//				"C:\\Users\\alter\\Desktop\\temptFolder\\NCHC-RainfallBigDataDisplay_preprocessing\\flood2PNG\\");
//		argsMap.put("-gdalENV", "K:\\Qgis\\3.4.13");


		try {
			// setting enviroment
			String qgisBinFolder = argsMap.get(GDAL_env) + "\\";
			GdalGlobal.qgisBinFolder = qgisBinFolder;
			GdalGlobal.gdalBinFolder = qgisBinFolder + "bin\\";
			GdalGlobal.sagaBinFolder = qgisBinFolder + "apps\\saga-ltr\\";
			GdalGlobal.grassBinFolder = qgisBinFolder + "apps\\grass\\grass78\\bin\\";
			GdalGlobal.qgisProcessingPluigins = qgisBinFolder + "apps\\qgis-ltr\\python\\plugins";
			GdalGlobal.temptFolder = qgisBinFolder + "temptFolder\\";
			FileFunction.newFolder(GdalGlobal.qgisBinFolder + "temptFolder");

			if (argsMap.get(model).toUpperCase().equals("NC2PNG_RAINFALL")) {
				Rainfall2PNG nc2PNG = new Rainfall2PNG(Paths.get(argsMap.get(inputNC)).toAbsolutePath().toString());
				nc2PNG.saveAsPNG(Paths.get(argsMap.get(outputAdd)).toAbsolutePath().toString());
				nc2PNG.close();

			} else if (argsMap.get(model).toUpperCase().equals("NC2PNG_FLOOD")) {
				Flood2PNG nc2PNG = new Flood2PNG(Paths.get(argsMap.get(inputNC)).toAbsolutePath().toString());
				nc2PNG.saveAsPNG(Paths.get(argsMap.get(outputAdd)).toAbsolutePath().toString());
				nc2PNG.close();

			} else if (argsMap.get(model).toUpperCase().equals("AUTODEPLOY")) {
				AutoDeployNC2PNG auto = new AutoDeployNC2PNG(
						Paths.get(argsMap.get(AutoDeployRootFolder)).toAbsolutePath().toString());
				auto.deploy(Paths.get(argsMap.get(AutoDeployConfig)).toAbsolutePath().toString());

			} else {
				System.out.println("ERROR Model selection");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static Map<String, String> getArgs(String[] args) {

		Map<String, String> outMap = new HashMap<>();
		for (int index = 0; index < args.length; index++) {
			if (args[index].contains("-")) {

				try {
					outMap.put(args[index], args[index + 1]);
				} catch (Exception e) {
					e.printStackTrace();
				}

				index++;
			}
		}

		return outMap;
	}

}
