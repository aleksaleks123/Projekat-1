package isamrs.tim1.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "RentACars")
public class RentACar extends Service implements Serializable {

	@OneToMany(mappedBy = "rentACar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<BranchOffice> branchOffices;

	@OneToMany(mappedBy = "rentACar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Vehicle> vehicles;

	@OneToMany(mappedBy = "rentACar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<QuickCarReservation> quickReservations;

	@OneToMany(mappedBy = "rentACar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<RentACarAdmin> admins;

	private static final long serialVersionUID = 8960219458856435612L;

	public Set<BranchOffice> getBranchOffices() {
		return branchOffices;
	}

	public void setBranchOffices(Set<BranchOffice> branchOffices) {
		this.branchOffices = branchOffices;
	}

	public Set<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(Set<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public RentACar() {
		super();
		branchOffices = new HashSet<BranchOffice>();
		admins = new HashSet<RentACarAdmin>();
		vehicles = new HashSet<Vehicle>();
		quickReservations = new HashSet<QuickCarReservation>();
	}

	public Set<QuickCarReservation> getQuickReservations() {
		return quickReservations;
	}

	public void setQuickReservations(Set<QuickCarReservation> quickReservations) {
		this.quickReservations = quickReservations;
	}

	public Set<RentACarAdmin> getAdmins() {
		return admins;
	}

	public void setAdmins(Set<RentACarAdmin> admins) {
		this.admins = admins;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
