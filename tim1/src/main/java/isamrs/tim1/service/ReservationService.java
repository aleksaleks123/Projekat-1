package isamrs.tim1.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import isamrs.tim1.dto.FlightHotelReservationDTO;
import isamrs.tim1.dto.FlightReservationDTO;
import isamrs.tim1.dto.HotelReservationDTO;
import isamrs.tim1.dto.MessageDTO;
import isamrs.tim1.dto.MessageDTO.ToasterType;
import isamrs.tim1.dto.PassengerDTO;
import isamrs.tim1.model.Flight;
import isamrs.tim1.model.FlightReservation;
import isamrs.tim1.model.HotelReservation;
import isamrs.tim1.model.PassengerSeat;
import isamrs.tim1.model.PlaneSegment;
import isamrs.tim1.model.PlaneSegmentClass;
import isamrs.tim1.model.RegisteredUser;
import isamrs.tim1.model.Seat;
import isamrs.tim1.model.UserReservation;
import isamrs.tim1.repository.FlightRepository;
import isamrs.tim1.repository.FlightReservationRepository;
import isamrs.tim1.repository.ServiceRepository;
import isamrs.tim1.repository.UserRepository;
import isamrs.tim1.repository.UserReservationRepository;

@Service
public class ReservationService {

	@Autowired
	FlightReservationRepository flightReservationRepository;

	@Autowired
	FlightRepository flightRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserReservationRepository userReservationRepository;

	@Autowired
	ServiceRepository serviceRepository;
	
	@Autowired
	EmailService mailService;

	public MessageDTO reserveFlight(FlightReservationDTO flightRes) {
		UserReservation ur = new UserReservation();
		FlightReservation fr = new FlightReservation();
		RegisteredUser ru = (RegisteredUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MessageDTO retval = reserveFlightNoSave(flightRes, ur, fr, ru);
		if (retval.getToastType().toString().equals(ToasterType.ERROR.toString()))
			return retval;
		userReservationRepository.save(ur);
		flightReservationRepository.save(fr);
		userRepository.save(ru);
		mailService.sendFlightReservationMail(ru, fr);
		return new MessageDTO("Reservation successfully made.", ToasterType.SUCCESS.toString());
	}

	public ArrayList<FlightReservationDTO> getReservations() {
		RegisteredUser ru = (RegisteredUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Set<UserReservation> ur = userReservationRepository.getByUser(ru.getId());
		ArrayList<FlightReservationDTO> fr = new ArrayList<FlightReservationDTO>();
		for (UserReservation u : ur) {
			if (u.getReservation() instanceof FlightReservation) {
				FlightReservation flightRes = (FlightReservation) u.getReservation();
				String res = flightRes.getFlight().getStartDestination().getName() + "-"
						+ flightRes.getFlight().getEndDestination().getName();
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				String date = sdf.format(flightRes.getFlight().getDepartureTime());
				fr.add(new FlightReservationDTO(res, date, flightRes.getPrice(), u.getGrade()));
			}
		}
		return fr;
	}

	public MessageDTO reserveFlightHotel(FlightHotelReservationDTO flightHotelRes) {
		FlightReservationDTO flightRes = flightHotelRes.getFlightReservation();
		HotelReservationDTO hotelRes = flightHotelRes.getHotelReservation();
		UserReservation ur = new UserReservation();
		FlightReservation fr = new FlightReservation();
		RegisteredUser ru = (RegisteredUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		MessageDTO retval = reserveFlightNoSave(flightRes, ur, fr, ru);
		if (retval.getToastType().toString().equals(ToasterType.ERROR.toString()))
			return retval;
		
		HotelReservation hr = new HotelReservation();
		retval = reserveHotelNoSave(flightHotelRes, fr, hr);
		if (retval.getToastType().toString().equals(ToasterType.ERROR.toString()))
			return retval;
		
		
		
		userReservationRepository.save(ur); // proveri jel radi samo sa jednim save-om, trebalo bi
		flightReservationRepository.save(fr);
		userRepository.save(ru);
		return new MessageDTO("Reservation successfully made.", ToasterType.SUCCESS.toString());
	}

	private MessageDTO reserveHotelNoSave(FlightHotelReservationDTO flightHotelRes, FlightReservation fr,
			HotelReservation hr) {
		// TODO Auto-generated method stub
		return null;
	}

	private MessageDTO reserveFlightNoSave(FlightReservationDTO flightRes, UserReservation ur, FlightReservation fr,
			RegisteredUser ru) {
		Flight f = flightRepository.findOneByFlightCode(flightRes.getFlightCode());
		if (f == null) {
			return new MessageDTO("Flight does not exist.", ToasterType.ERROR.toString());
		}
		fr.setFlight(f);
		fr.setDone(false);
		fr.setDateOfReservation(new Date());
		int counter = 0;
		double price = 0.0;
		String userPassport = flightRes.getPassengers()[0].getPassport();
		int numOfBags = flightRes.getPassengers()[0].getNumberOfBags();
		ArrayList<PassengerDTO> passengerList = new ArrayList<PassengerDTO>(
				Arrays.asList(Arrays.copyOfRange(flightRes.getPassengers(), 1, flightRes.getPassengers().length)));
		passengerList.add(0, new PassengerDTO(ru.getFirstName(), ru.getLastName(), userPassport, numOfBags));
		for (PassengerDTO p : passengerList) {
			String[] idx = flightRes.getSeats()[counter].split("_");
			price += p.getNumberOfBags() * f.getPricePerBag();
			int row = Integer.parseInt(idx[0]);
			int column = Integer.parseInt(idx[1]);
			if (checkIfSeatIsReserved(f, row, column)) {
				return new MessageDTO("One of the seats is already reserved.", ToasterType.ERROR.toString());
			}
			Seat st = new Seat();
			st.setRow(row);
			st.setColumn(column);
			if (idx[2].equalsIgnoreCase(PlaneSegmentClass.FIRST.toString().substring(0, 1))) {
				st.setPlaneSegment(new PlaneSegment(PlaneSegmentClass.FIRST));
				price += f.getFirstClassPrice();
			} else if (idx[2].equalsIgnoreCase(PlaneSegmentClass.BUSINESS.toString().substring(0, 1))) {
				st.setPlaneSegment(new PlaneSegment(PlaneSegmentClass.BUSINESS));
				price += f.getBusinessClassPrice();
			} else if (idx[2].equalsIgnoreCase(PlaneSegmentClass.ECONOMY.toString().substring(0, 1))) {
				st.setPlaneSegment(new PlaneSegment(PlaneSegmentClass.ECONOMY));
				price += f.getEconomyClassPrice();
			} else {
				return null;
			}
			PassengerSeat ps = new PassengerSeat(p, st);
			ps.setReservation(fr);
			fr.getPassengerSeats().add(ps);
			counter++;
		}
		for (String email : flightRes.getInvitedFriends()) {
			inviteFriendToFlight(email, flightRes, f, counter, ru.getEmail());
			counter++;
		}
		fr.setPrice(price);
		ur.setGrade(0);
		ur.setReservation(fr);
		ur.setUser(ru);
		ru.getUserReservations().add(ur);
		fr.setUser(ur);
		f.getAirline().getReservations().add(fr);
		return new MessageDTO("", ToasterType.SUCCESS.toString());
	}
	
	private boolean checkIfSeatIsReserved(Flight flight, int row, int column) {
		for (FlightReservation r : flight.getAirline().getReservations()) {
			if (r.getFlight().getFlightCode().equals(flight.getFlightCode())) {
				for (PassengerSeat ps : r.getPassengerSeats()) {
					if (ps.getSeat() != null && (ps.getSeat().getRow() == row && ps.getSeat().getColumn() == column)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void inviteFriendToFlight(String email, FlightReservationDTO flightRes, Flight f, int counter, String inviter) {
		RegisteredUser friend = (RegisteredUser) userRepository.findOneByEmail(email);
		FlightReservation fRes = new FlightReservation();
		fRes.setFlight(f);
		fRes.setDone(false);
		fRes.setDateOfReservation(new Date());
		double price = 0.0;
		String[] idx = flightRes.getSeats()[counter].split("_");
		Seat st = new Seat();
		st.setRow(Integer.parseInt(idx[0]));
		st.setColumn(Integer.parseInt(idx[1]));
		if (idx[2].equalsIgnoreCase(PlaneSegmentClass.FIRST.toString().substring(0, 1))) {
			st.setPlaneSegment(new PlaneSegment(PlaneSegmentClass.FIRST));
			price += f.getFirstClassPrice();
		} else if (idx[2].equalsIgnoreCase(PlaneSegmentClass.BUSINESS.toString().substring(0, 1))) {
			st.setPlaneSegment(new PlaneSegment(PlaneSegmentClass.BUSINESS));
			price += f.getBusinessClassPrice();
		} else if (idx[2].equalsIgnoreCase(PlaneSegmentClass.ECONOMY.toString().substring(0, 1))) {
			st.setPlaneSegment(new PlaneSegment(PlaneSegmentClass.ECONOMY));
			price += f.getEconomyClassPrice();
		}
		PassengerSeat ps = new PassengerSeat(new PassengerDTO(friend.getFirstName(), friend.getLastName(), "", 0), st);
		ps.setReservation(fRes);
		fRes.setPrice(price);
		fRes.getPassengerSeats().add(ps);
		f.getAirline().getReservations().add(fRes);
		fRes = flightReservationRepository.save(fRes);
		mailService.sendMailToFriend(friend, fRes.getId(), inviter);
	}
}
