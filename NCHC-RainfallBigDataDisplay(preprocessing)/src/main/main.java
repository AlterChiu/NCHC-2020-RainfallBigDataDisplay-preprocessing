
package main;

import java.util.HashMap;
import java.util.Map;
import main.model.nc2png.Rainfall2PNG;

public class main {

	public static String model = "-model";
	public static String outputAdd = "-outputFolder";
	public static String inputNC = "-inputNC";
	public static String GDAL_env = "-gdalENV";
	public static String AutoDeployConfig = "-AutoDeploy";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, String> argsMap = getArgs(args);


		try {

			if (argsMap.get(model).toUpperCase().equals("NC2PNG_RAINFALL")) {
				Rainfall2PNG nc2PNG = new Rainfall2PNG(argsMap.get(inputNC));
				nc2PNG.saveAsPNG(outputAdd);
				nc2PNG.close();

			} else if (argsMap.get(model).toUpperCase().equals("NC2PNG_FLOOD")) {
				Rainfall2PNG nc2PNG = new Rainfall2PNG(argsMap.get(inputNC));
				nc2PNG.saveAsPNG(outputAdd);
				nc2PNG.close();

			} else if (argsMap.get(model).toUpperCase().equals("AUTODEPLOY")) {


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
