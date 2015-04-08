package org.tellstick.device;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.tellstick.JNA;
import org.tellstick.device.iface.Device;
import org.tellstick.enums.DataType;
import org.tellstick.enums.DeviceType;

import com.sun.jna.Memory;

public class TellstickSensor implements Device {
	
	private int sensorId;
	private String data;
	private DataType method;
	private String protocol;
	private Date timeStamp;
	
	public TellstickSensor(int sensorId, DataType method, String protocol,
			String model) {
		super();
		this.sensorId = sensorId;
		this.method = method;
		this.protocol = protocol;
		this.model = model;
	}
	private String model;

	public static List<TellstickSensor> getAllSensors() {
		List<TellstickSensor> resultSensors = new ArrayList<TellstickSensor>();
		int result = JNA.CLibrary.TELLSTICK_SUCCESS;
		while (result == JNA.CLibrary.TELLSTICK_SUCCESS) {
			int varSize = 255;
			Memory protocol = new Memory(varSize);
			Memory model = new Memory(varSize);
			Memory value = new Memory(varSize);
			int id[] = new int[1];
			int dataTypes[] = new int[1];
			int timeStamp[] = new int[1];
			result = JNA.CLibrary.INSTANCE.tdSensor(protocol, varSize, model, varSize, id, dataTypes);
			DataType[] dTypes = DataType.getDataTypesById(dataTypes[0]);
			for (DataType type : dTypes) {
				TellstickSensor sensor = new TellstickSensor(id[0], type, protocol.getString(0), model.getString(0));
				JNA.CLibrary.INSTANCE.tdSensorValue(sensor.getProtocol(), sensor.getModel(), sensor.getId(), 
						sensor.getMethod().getTellstickId(), value, varSize,timeStamp);
				sensor.setData(value.getString(0));
				long timeInMilli = timeStamp[0]*1000L;
				sensor.setTimeStamp(new Date(timeInMilli));
				resultSensors.add(sensor);
			}
		}
		return resultSensors;
	}
	public int getId() {
		return sensorId;
	}
	public String getData() {
		return data;
	}
	public DataType getMethod() {
		return method;
	}
	public String getProtocol() {
		return protocol;
	}
	public String getModel() {
		return model;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public void setData(String data) {
		this.data = data;
	}
	public DeviceType getDeviceType(){
		return DeviceType.SENSOR;
	}
	@Override
	public String toString() {
		return "TellstickSensor [sensorId=" + sensorId + ", "
				+ (protocol != null ? "protocol=" + protocol + ", " : "")
				+ (model != null ? "model=" + model + ", " : "")
				+ (method != null ? "method=" + method + ", " : "")
				+ (timeStamp != null ? "timeStamp=" + timeStamp + ", " : "")
				+ (data != null ? "data=" + data : "") + "]";
	}
}
