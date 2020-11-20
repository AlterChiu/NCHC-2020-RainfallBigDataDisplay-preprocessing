
package main.model.nc2png;

import java.io.IOException;
import java.nio.file.Paths;
import geo.gdal.raster.GDAL_RASTER_ToPNG;
import netCDF.NetcdfBasicControl;

public class Rainfall2PNG extends NC2PNG {

	private NetcdfBasicControl nc;
	private String fileName = "";

	public Rainfall2PNG(String inputFileAdd) throws IOException {
		this.nc = new NetcdfBasicControl(inputFileAdd);
		this.fileName = Paths.get(inputFileAdd).getFileName().toString().split(".nc")[0];
	}

	public void saveAsPNG(String outputFolder) throws Exception {
		this.saveAsPNG(outputFolder, this.fileName);
	}

	public void saveAsPNG(String outputFolder, String folderName) throws Exception {
		this.saveAsPNG(outputFolder, folderName, GDAL_RASTER_ToPNG.CWB_RainfallScale(), this.nc,
				NC2PNG.rainfallValueArray);
	}

	public void close() {
		this.nc = null;
	}
}
