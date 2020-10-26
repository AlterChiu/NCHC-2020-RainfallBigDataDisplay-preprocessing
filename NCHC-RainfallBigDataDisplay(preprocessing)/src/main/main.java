
package main;

import java.util.HashMap;
import java.util.Map;
import geo.gdal.GdalGlobal;
import main.model.nc2png.AutoDeployNC2PNG;
import main.model.nc2png.Rainfall2PNG;
import usualTool.FileFunction;

public class main {

	public static String model = "-model";
	public static String outputAdd = "-outputFolder";
	public static String inputNC = "-inputNC";
	public static String GDAL_env = "-gdalENV";
	public static String AutoDeployConfig = "-configFile";
	public static String AutoDeployRootFolder = "-rootFolder";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, String> argsMap = getArgs(args);


		try {
			// setting enviroment
			GdalGlobal.qgisBinFolder = argsMap.get(GDAL_env + "\\");
			try {
				FileFunction.newFolder(GdalGlobal.temptFolder);
			} catch (Exception s) {
			}


			if (argsMap.get(model).toUpperCase().equals("NC2PNG_RAINFALL")) {
				Rainfall2PNG nc2PNG = new Rainfall2PNG(argsMap.get(inputNC));
				nc2PNG.saveAsPNG(outputAdd);
				nc2PNG.close();

			} else if (argsMap.get(model).toUpperCase().equals("NC2PNG_FLOOD")) {
				Rainfall2PNG nc2PNG = new Rainfall2PNG(argsMap.get(inputNC));
				nc2PNG.saveAsPNG(outputAdd);
				nc2PNG.close();

			} else if (argsMap.get(model).toUpperCase().equals("AUTODEPLOY")) {
				AutoDeployNC2PNG auto = new AutoDeployNC2PNG(argsMap.get(AutoDeployRootFolder));
				auto.deploy(argsMap.get(AutoDeployConfig));

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
