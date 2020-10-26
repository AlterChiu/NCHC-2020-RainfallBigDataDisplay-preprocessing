
package main;

import java.io.IOException;
import main.model.nc2png.Flood2PNG;

public class test {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

//		AutoDeployNC2PNG auto = new AutoDeployNC2PNG(
//				"C:\\Users\\alter\\Desktop\\temptFolder\\NCHC-RainfallBigDataDisplay_preprocessing\\autoDeploy\\");
//		auto.deploy(
//				"C:\\Users\\alter\\Desktop\\temptFolder\\NCHC-RainfallBigDataDisplay_preprocessing\\autoDeploy\\Yilan_3000_metadata.csv");

		Flood2PNG flood = new Flood2PNG(
				"C:\\Users\\alter\\Desktop\\temptFolder\\NCHC-RainfallBigDataDisplay_preprocessing\\flood2PNG\\Event_00001_Flood.nc");
		flood.saveAsPNG("C:\\Users\\alter\\Desktop\\temptFolder\\NCHC-RainfallBigDataDisplay_preprocessing\\flood2PNG",
				"flood");
//
//		Rainfall2PNG rainfall = new Rainfall2PNG(
//				"F:\\BigDataDisplay-Data\\站存\\TainanSY\\rainfall\\Event_00001_Rainfall.nc");
//		rainfall.saveAsPNG("F:\\BigDataDisplay-Data\\站存\\TainanSY\\test\\", "rainfall");
	}

}
