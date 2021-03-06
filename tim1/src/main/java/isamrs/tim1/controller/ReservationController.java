package isamrs.tim1.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim1.dto.CheckVehicleReservationDTO;
import isamrs.tim1.dto.DetailedReservationDTO;
import isamrs.tim1.dto.FlightHotelReservationDTO;
import isamrs.tim1.dto.FlightHotelVehicleReservationDTO;
import isamrs.tim1.dto.FlightReservationDTO;
import isamrs.tim1.dto.FlightVehicleReservationDTO;
import isamrs.tim1.dto.InvitingReservationDTO;
import isamrs.tim1.dto.MessageDTO;
import isamrs.tim1.dto.QuickFlightReservationDTO;
import isamrs.tim1.dto.QuickHotelReservationDTO;
import isamrs.tim1.dto.QuickVehicleReservationDTO;
import isamrs.tim1.dto.VehicleReservationDTO;
import isamrs.tim1.service.ReservationService;

@RestController
public class ReservationController {

	@Autowired
	ReservationService reservationService;

	@PreAuthorize("hasRole('REGISTEREDUSER')")
	@RequestMapping(value = "/api/reserveFlight/{discountPoints}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> reserveFlight(@PathVariable("discountPoints") int discountPoints,
			@Valid @RequestBody FlightReservationDTO flightRes, HttpServletRequest request) {
		String siteName = "";
		try {
			siteName = new URL(request.getRequestURL().toString()).getAuthority();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return new ResponseEntity<MessageDTO>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<MessageDTO>(reservationService.reserveFlight(discountPoints, flightRes,siteName),
				HttpStatus.OK);
	}

	@PreAuthorize("hasRole('REGISTEREDUSER')")
	@RequestMapping(value = "/api/reserveFlightHotel/{discountPoints}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> reserveFlightHotel(@PathVariable("discountPoints") int discountPoints,
			@Valid @RequestBody FlightHotelReservationDTO flightHotelRes, HttpServletRequest request) {
		String siteName = "";
		try {
			siteName = new URL(request.getRequestURL().toString()).getAuthority();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return new ResponseEntity<MessageDTO>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<MessageDTO>(reservationService.reserveFlightHotel(discountPoints, flightHotelRes,siteName),
				HttpStatus.OK);
	}

	@PreAuthorize("hasRole('REGISTEREDUSER')")
	@RequestMapping(value = "/api/reserveFlightVehicle/{discountPoints}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> reserveFlightVehicle(@PathVariable("discountPoints") int discountPoints,
			@RequestBody FlightVehicleReservationDTO flightVehicleRes, HttpServletRequest request) {
		String siteName = "";
		try {
			siteName = new URL(request.getRequestURL().toString()).getAuthority();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return new ResponseEntity<MessageDTO>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<MessageDTO>(reservationService.reserveFlightVehicle(discountPoints, flightVehicleRes,siteName),
				HttpStatus.OK);
	}

	@PreAuthorize("hasRole('REGISTEREDUSER')")
	@RequestMapping(value = "/api/reserveFlightHotelVehicle/{discountPoints}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> reserveFlightHotelVehicle(@PathVariable("discountPoints") int discountPoints,
			@Valid @RequestBody FlightHotelVehicleReservationDTO flightHotelVehicleRes, HttpServletRequest request) {
		String siteName = "";
		try {
			siteName = new URL(request.getRequestURL().toString()).getAuthority();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return new ResponseEntity<MessageDTO>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<MessageDTO>(
				reservationService.reserveFlightHotelVehicle(discountPoints, flightHotelVehicleRes,siteName), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('REGISTEREDUSER')")
	@RequestMapping(value = "/api/getReservations", method = RequestMethod.GET)
	public ArrayList<FlightReservationDTO> getReservations() {
		return reservationService.getReservations();
	}

	@PreAuthorize("hasRole('REGISTEREDUSER')")
	@RequestMapping(value = "/api/getInvitingReservations", method = RequestMethod.GET)
	public ArrayList<InvitingReservationDTO> getInvitingReservations() {
		return reservationService.getInvitingReservations();
	}

	@PreAuthorize("hasRole('REGISTEREDUSER')")
	@RequestMapping(value = "/api/acceptFlightInvitation", method = RequestMethod.POST)
	public ResponseEntity<MessageDTO> acceptFlightInvitation(@Valid @RequestBody FlightReservationDTO fRes) {
		return reservationService.acceptFlightInvitation(fRes);
	}

	@PreAuthorize("hasRole('REGISTEREDUSER')")
	@RequestMapping(value = "/api/declineFlightInvitation", method = RequestMethod.POST)
	public ResponseEntity<MessageDTO> declineFlightInvitation(@RequestBody String resID) {
		return reservationService.declineFlightInvitation(resID);
	}

	@PreAuthorize("hasRole('REGISTEREDUSER')")
	@RequestMapping(value = "/api/cancelReservation", method = RequestMethod.DELETE)
	public ResponseEntity<MessageDTO> cancelReservation(@RequestBody String resID) {
		return reservationService.cancelReservation(resID);
	}

	@PreAuthorize("hasRole('REGISTEREDUSER')")
	@RequestMapping(value = "/api/cancelHotelReservation", method = RequestMethod.DELETE)
	public ResponseEntity<MessageDTO> cancelHotelReservation(@RequestBody String resID) {
		return reservationService.cancelHotelReservation(resID);
	}

	@PreAuthorize("hasRole('REGISTEREDUSER')")
	@RequestMapping(value = "/api/cancelCarReservation", method = RequestMethod.DELETE)
	public ResponseEntity<MessageDTO> cancelCarReservation(@RequestBody String resID) {
		return reservationService.cancelCarReservation(resID);
	}

	@PreAuthorize("hasRole('HOTELADMIN')")
	@RequestMapping(value = "/api/createQuickHotelReservation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> createQuickHotelReservation(
			@Valid @RequestBody QuickHotelReservationDTO hotelRes) {
		return new ResponseEntity<MessageDTO>(reservationService.createQuickHotelReservation(hotelRes), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('RENTADMIN')")
	@RequestMapping(value = "/api/createQuickVehicleReservation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> createQuickVehicleReservation(
			@Valid @RequestBody QuickVehicleReservationDTO quickReservation) {
		return new ResponseEntity<MessageDTO>(reservationService.createQuickVehicleReservation(quickReservation),
				HttpStatus.OK);
	}

	@PreAuthorize("hasRole('RENTADMIN')")
	@RequestMapping(value = "/api/getQuickVehicleReservations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<QuickVehicleReservationDTO>> getQuickVehicleReservations() {
		return new ResponseEntity<ArrayList<QuickVehicleReservationDTO>>(
				reservationService.getQuickVehicleReservations(), HttpStatus.OK);
	}

	// everyone
	@RequestMapping(value = "/api/getQuickReservationsForVehicle/{vehicleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<VehicleReservationDTO>> getQuickVehicleReservationsForVehicle(
			@PathVariable("vehicleId") int vehicleId) {
		return new ResponseEntity<ArrayList<VehicleReservationDTO>>(
				reservationService.getQuickVehicleReservationsForVehicle(vehicleId), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/createQuickFlightReservation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> createQuickFlightReservation(
			@Valid @RequestBody QuickFlightReservationDTO quickDTO) {
		return new ResponseEntity<MessageDTO>(reservationService.createQuickFlightReservation(quickDTO), HttpStatus.OK);
	}

	// everyone
	@RequestMapping(value = "/api/getQuickFlightReservations", method = RequestMethod.GET)
	public ArrayList<QuickFlightReservationDTO> getQuickFlightReservations() {
		return reservationService.getQuickFlightReservations();
	}

	@RequestMapping(value = "/api/checkVehicleForPeriod", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> checkVehicleForPeriod(@Valid @RequestBody CheckVehicleReservationDTO vehicle) {
		return new ResponseEntity<Boolean>(
				reservationService.checkVehicleForPeriod(vehicle.getVehicleID(), vehicle.getStart(), vehicle.getEnd()),
				HttpStatus.OK);
	}

	@PreAuthorize("hasRole('REGISTEREDUSER')")
	@RequestMapping(value = "/api/reserveQuickFlightReservation/{discountPoints}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> reserveQuickFlightReservation(@PathVariable("discountPoints") int discountPoints,
			@Valid @RequestBody FlightReservationDTO flightRes) {
		return new ResponseEntity<MessageDTO>(
				reservationService.reserveQuickFlightReservation(discountPoints, flightRes), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/getDetailedReservation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DetailedReservationDTO> getDetailedReservation(@RequestParam String resID) {
		return new ResponseEntity<DetailedReservationDTO>(reservationService.getDetailedReservation(resID),
				HttpStatus.OK);
	}
}
