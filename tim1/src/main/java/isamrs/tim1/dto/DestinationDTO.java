package isamrs.tim1.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import isamrs.tim1.model.Destination;

public class DestinationDTO implements Serializable {
	
	@NotBlank
	private String nameOfDest;
	
	@Min(-90)
	@Max(90)
	private Double latitude;
	
	@Min(-180)
	@Max(180)
	private Double longitude;

	private String airlineName;
	private String oldName;
	
	public DestinationDTO() {
		super();
	}
	
	public DestinationDTO(Destination d) {
		this.nameOfDest = d.getName();
		this.airlineName = d.getAirline().getName();
		this.longitude = d.getLocation().getLongitude();
		this.latitude = d.getLocation().getLatitude();
	}

	public String getNameOfDest() {
		return nameOfDest;
	}

	public void setNameOfDest(String nameOfDest) {
		this.nameOfDest = nameOfDest;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getAirlineName() {
		return airlineName;
	}

	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}


	private static final long serialVersionUID = 231712408553877022L;
}
