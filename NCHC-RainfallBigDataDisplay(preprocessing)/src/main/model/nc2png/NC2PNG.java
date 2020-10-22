
package main.model.nc2png;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import asciiFunction.AsciiBasicControl;
import asciiFunction.XYZToAscii;
import geo.gdal.raster.GDAL_RASTER_ToPNG;
import netCDF.NetcdfBasicControl;
import usualTool.AtCommonMath;
import usualTool.AtFileWriter;
import usualTool.FileFunction;

public class NC2PNG {
	public static String rainfallValueArray = "precipitation_radar";
	public static String floodValueArray = "depth_below_surface_simulated";
	public static String xArray = "x";
	public static String yArray = "y";
	public static String timeArray = "time";
	public static int dataDecimal = 4;


	protected List<Double> getXArray(NetcdfBasicControl nc) throws IOException {
		return nc.getVariableValues(NC2PNG.xArray).parallelStream().map(e -> (Double) e).collect(Collectors.toList());
	}

	protected List<Double> getYArray(NetcdfBasicControl nc) throws IOException {
		return nc.getVariableValues(NC2PNG.yArray).parallelStream().map(e -> (Double) e).collect(Collectors.toList());
	}

	protected List<Double> getTimeArray(NetcdfBasicControl nc) throws IOException {
		return nc.getVariableValues(NC2PNG.timeArray).parallelStream().map(e -> (Double) e)
				.collect(Collectors.toList());
	}

	protected double getNullValue(NetcdfBasicControl nc, String variableKey) {
		return Double.parseDouble(nc.getVaiable(variableKey).findAttribute("_FillValue").getValue(0).toString());
	}

	private AsciiBasicControl getAsciiTemplate(NetcdfBasicControl nc, String variableKey) throws IOException {

		List<Double[]> outList = new ArrayList<>();
		List<Double> xList = this.getXArray(nc);
		List<Double> yList = this.getYArray(nc);
		List<Object> valueContainer = (List<Object>) nc.getVariableValues(variableKey).get(0);
		double nullValue = this.getNullValue(nc, variableKey);


		for (int yIndex = 0; yIndex < yList.size(); yIndex++) {

			List<Object> temptContainer = (List<Object>) valueContainer.get(yIndex);
			for (int xIndex = 0; xIndex < xList.size(); xIndex++) {

				outList.add(new Double[] { xList.get(xIndex), yList.get(yIndex),
						Double.parseDouble(temptContainer.get(xIndex).toString()) });
			}
		}

		double xSize = (Math.abs(xList.get(0) - xList.get(1))
				+ Math.abs(xList.get(xList.size() - 1) - xList.get(xList.size() - 2))) / 2;
		double ySize = (Math.abs(yList.get(0) - yList.get(1))
				+ Math.abs(yList.get(yList.size() - 1) - yList.get(yList.size() - 2))) / 2;
		double cellSize = AtCommonMath.getDecimal_Double((xSize + ySize) / 2, 4);


		// xyzFormat to asciiFormat
		XYZToAscii toAscii = new XYZToAscii(outList);
		toAscii.setCellSize(cellSize);
		toAscii.setNullValue(AtCommonMath.getDecimal_String(nullValue, NC2PNG.dataDecimal));

		return toAscii.getEmptyAscii();
	}

	protected void outputProperties(String outputFolder, String fileName, AsciiBasicControl asciiTemplate)
			throws IOException {
		List<String[]> outputContent = new ArrayList<>();

		outputContent.add(new String[] { "Name", fileName });
		outputContent.add(new String[] { "maxX", asciiTemplate.getBoundary().get("maxX") + "" });
		outputContent.add(new String[] { "minX", asciiTemplate.getBoundary().get("minX") + "" });
		outputContent.add(new String[] { "maxY", asciiTemplate.getBoundary().get("maxY") + "" });
		outputContent.add(new String[] { "minY", asciiTemplate.getBoundary().get("minY") + "" });

		new AtFileWriter(outputContent.parallelStream().toArray(String[][]::new),
				outputFolder + "\\" + fileName + ".properties").textWriter(" ");
	}

	protected void saveAsPNG(String outputFolder, String folderName, Map<Double, Integer[]> colorStyle,
			NetcdfBasicControl nc, String valueVariableKey) throws IOException, InterruptedException {

		// doing the save folder
		String saveFolder = outputFolder + "\\" + folderName + "\\";
		try {
			FileFunction.delete(saveFolder);
		} catch (Exception e) {
		}
		FileFunction.newFolder(saveFolder);


		// initial variables
		List<Double> xList = this.getXArray(nc);
		List<Double> yList = this.getYArray(nc);
		List<Double> timeList = this.getTimeArray(nc);
		List<Object> valueContainer = nc.getVariableValues(valueVariableKey);
		AsciiBasicControl asciiTemplate = this.getAsciiTemplate(nc, valueVariableKey);


		// doing for each timeSteps
		for (int timeIndex = 0; timeIndex < timeList.size(); timeIndex++) {

			// create new asciiFIle
			List<Object> yContainer = (List<Object>) valueContainer.get(timeIndex);
			for (int yIndex = 0; yIndex < yList.size(); yIndex++) {

				List<Object> xContainer = (List<Object>) yContainer.get(yIndex);
				for (int xIndex = 0; xIndex < xList.size(); xIndex++) {

					String temptValue = AtCommonMath.getDecimal_String(xContainer.get(xIndex).toString(),
							NC2PNG.dataDecimal);
					asciiTemplate.setValue(xList.get(xIndex), yList.get(yIndex), temptValue);
				}
			}


			// asciiFile to pngFile
			String temptSaveFileAdd = saveFolder + timeIndex;
			new AtFileWriter(asciiTemplate.getAsciiFile(), temptSaveFileAdd + ".asc").textWriter(" ");

			GDAL_RASTER_ToPNG.save(temptSaveFileAdd + ".asc", colorStyle, temptSaveFileAdd + ".png", xList.size() * 4,
					yList.size() * 4);
		}

		// delete not pngFiles
		for (String fileName : new File(saveFolder).list()) {
			if (fileName.contains(".xml") || fileName.contains(".asc")) {
				FileFunction.delete(saveFolder + fileName);
			}
		}

		this.outputProperties(outputFolder, folderName, asciiTemplate);
	}

}

