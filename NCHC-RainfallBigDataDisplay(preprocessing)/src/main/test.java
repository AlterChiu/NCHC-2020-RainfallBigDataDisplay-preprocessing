
package main;

import java.io.IOException;
import main.model.nc2png.AutoDeployNC2PNG;

public class test {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

		AutoDeployNC2PNG auto = new AutoDeployNC2PNG("F:\\BigDataDisplay-Data\\站存\\TainanSY\\");
		auto.deploy("F:\\BigDataDisplay-Data\\站存\\TainanSY\\SY_10m_500_metadata.csv");

//		Flood2PNG flood = new Flood2PNG("F:\\BigDataDisplay-Data\\站存\\TainanSY\\flood\\Event_00001_Flood.nc");
//		flood.saveAsPNG("F:\\BigDataDisplay-Data\\站存\\TainanSY\\test\\", "flood");
//
//		Rainfall2PNG rainfall = new Rainfall2PNG(
//				"F:\\BigDataDisplay-Data\\站存\\TainanSY\\rainfall\\Event_00001_Rainfall.nc");
//		rainfall.saveAsPNG("F:\\BigDataDisplay-Data\\站存\\TainanSY\\test\\", "rainfall");
	}

}
