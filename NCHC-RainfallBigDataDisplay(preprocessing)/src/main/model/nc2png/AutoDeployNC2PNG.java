
package main.model.nc2png;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import usualTool.AtFileReader;
import usualTool.FileFunction;

public class AutoDeployNC2PNG extends NC2PNG {

	private String rootFolder;
	private String outputFolder;
	private String floodFolder;
	private String rainfallFolder;

	private String durations[] = new String[] { "12", "24", "48", "72" };
	private String accumulations[] = new String[] { "100", "200", "350", "500" };
	private String intensives[] = new String[] { "10", "50", "100", "150" };
	private String patterns[] = new String[] { "forward", "mean", "midium", "delay" };

	private TreeMap<Double, String> durationMap;
	private TreeMap<Double, String> accumulationMap;
	private TreeMap<Double, String> intensiveMap;

	public AutoDeployNC2PNG(String rootFolder) {
		this.rootFolder = rootFolder + "\\";

		this.outputFolder = this.rootFolder + "data\\";
		this.floodFolder = this.rootFolder + "flood\\";
		this.rainfallFolder = this.rootFolder + "rainfall\\";

		createFolder();
		createMap();
	}

	private void createMap() {
		this.durationMap = new TreeMap<>();
		for (String duration : this.durations) {
			this.durationMap.put(Double.parseDouble(duration), duration);
		}

		this.accumulationMap = new TreeMap<>();
		for (String acuumulation : this.accumulations) {
			this.accumulationMap.put(Double.parseDouble(acuumulation), acuumulation);
		}

		this.intensiveMap = new TreeMap<>();
		for (String intensive : this.intensives) {
			this.intensiveMap.put(Double.parseDouble(intensive), intensive);
		}

	}

	private void createFolder() {

		// clear data folder
		try {
			FileFunction.delete(this.outputFolder);
		} catch (Exception e) {
		}
		FileFunction.newFolder(this.outputFolder);

		// duration
		for (String duration : this.durations) {

			String durationFolder = this.outputFolder + duration + "\\";
			FileFunction.newFolder(durationFolder);

			// rainfall
			for (String accumulation : this.accumulations) {

				String accumulationFolder = durationFolder + accumulation + "\\";
				FileFunction.newFolder(accumulationFolder);

				// intensive
				for (String intensive : this.intensives) {

					String intensiveFolder = accumulationFolder + intensive + "\\";
					FileFunction.newFolder(intensiveFolder);

					// pattern
					for (String pattern : this.patterns) {

						String patternFolder = intensiveFolder + pattern + "\\";
						FileFunction.newFolder(patternFolder);
					}
				}
			}
		}
	}

	private List<EventDatameta> getEventDatameta(String eventConfigFileAdd) throws IOException {
		String content[][] = new AtFileReader(eventConfigFileAdd).getCsv(1, 0);

		List<EventDatameta> outList = new ArrayList<>();
		for (String[] eventMetadata : content) {
			outList.add(new EventDatameta(eventMetadata));
		}
		return outList;
	}

	public void deploy(String eventConfigFileAdd) throws IOException {

		this.getEventDatameta(eventConfigFileAdd).forEach(eventMetadata -> {
			System.out.println(eventMetadata.getEventID());

			// get deploy folder
			StringBuilder outputFolder = new StringBuilder();
			outputFolder.append(this.outputFolder);
			outputFolder.append(eventMetadata.getDuration(this.durationMap) + "\\");
			outputFolder.append(eventMetadata.getAccumulation(this.accumulationMap) + "\\");
			outputFolder.append(eventMetadata.getIntensive(this.intensiveMap) + "\\");
			outputFolder.append(eventMetadata.getPattern(this.patterns) + "\\");

			// get index
			int outputIndex = new File(outputFolder.toString()).list().length;
			outputFolder.append(outputIndex + "\\");
			FileFunction.newFolder(outputFolder.toString());

			// output rainfall pngFile
			String rainfallNcAdd = this.rainfallFolder + "Event_" + eventMetadata.getEventID() + "_Rainfall.nc";
			try {
				Rainfall2PNG rainfallPNG = new Rainfall2PNG(rainfallNcAdd);
				rainfallPNG.saveAsPNG(outputFolder.toString(), "rainfall");
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}

			// output flood pngFile
			String floodNcAdd = this.floodFolder + "Event_" + eventMetadata.getEventID() + "_Flood.nc";
			try {
				Flood2PNG floodPNG = new Flood2PNG(floodNcAdd);
				floodPNG.saveAsPNG(outputFolder.toString(), "flood");
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		});



	}

}

class EventDatameta {
	private String eventID;
	private String duration;
	private String accumulation;
	private String intensive;
	private int patterns;

	public EventDatameta(String[] eventMetedata) {
		this.eventID = eventMetedata[0].split("_")[1];
		this.duration = eventMetedata[1];
		this.accumulation = eventMetedata[5];
		this.intensive = eventMetedata[2];
		this.patterns = Integer.parseInt(eventMetedata[6]);
	}

	public String getEventID() {
		return this.eventID;
	}

	public String getDuration(TreeMap<Double, String> duarionMap) {
		Double durationKey = Optional.ofNullable(duarionMap.lowerKey(Double.parseDouble(this.duration)))
				.orElse(duarionMap.firstKey());
		return duarionMap.get(durationKey);
	}

	public String getAccumulation(TreeMap<Double, String> accumulationMap) {
		Double accumulationKey = Optional.ofNullable(accumulationMap.lowerKey(Double.parseDouble(this.accumulation)))
				.orElse(accumulationMap.firstKey());
		return accumulationMap.get(accumulationKey);
	}

	public String getIntensive(TreeMap<Double, String> intensiveMap) {
		Double intensiveKey = Optional.ofNullable(intensiveMap.lowerKey(Double.parseDouble(this.intensive)))
				.orElse(intensiveMap.firstKey());
		return intensiveMap.get(intensiveKey);
	}

	public String getPattern(String[] patternsName) {
		return Optional.ofNullable(patternsName[patterns - 1]).orElse(patternsName[0]);
	}
}
