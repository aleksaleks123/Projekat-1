package isamrs.tim1.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim1.dto.AirlineDTO;
import isamrs.tim1.dto.DetailedServiceDTO;
import isamrs.tim1.dto.FlightDTO;
import isamrs.tim1.dto.MessageDTO;
import isamrs.tim1.dto.ServiceDTO;
import isamrs.tim1.dto.ServiceViewDTO;
import isamrs.tim1.model.Airline;
import isamrs.tim1.model.AirlineAdmin;
import isamrs.tim1.service.AirlineService;

@RestController
public class AirlineController {

	@Autowired
	private AirlineService airlineService;

	@PreAuthorize("hasRole('SYSADMIN')")
	@RequestMapping(value = "/api/addAirline", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> addAirline(@Valid @RequestBody ServiceDTO airline) {
		return new ResponseEntity<MessageDTO>(airlineService.addAirline(new Airline(airline)), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('AIRADMIN')")
	@RequestMapping(value = "/api/editAirline", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> editAirline(@Valid @RequestBody ServiceDTO airline) throws Exception {
		return new ResponseEntity<MessageDTO>(airlineService.editAirline(airline), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/getAirlineOfAdmin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AirlineDTO> getAirlineOfAdmin() {
		return new ResponseEntity<AirlineDTO>(
				airlineService.getAirlineOfAdmin(
						(AirlineAdmin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()),
				HttpStatus.OK);
	}

	// everyone
	@RequestMapping(value = "/api/getAirlines", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<ServiceViewDTO>> getAirlines() {
		return new ResponseEntity<ArrayList<ServiceViewDTO>>(airlineService.getAirlines(), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('SYSADMIN')")
	@RequestMapping(value = "/api/getAirline", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DetailedServiceDTO> getAirline(@RequestParam String name) {
		return new ResponseEntity<DetailedServiceDTO>(airlineService.getAirline(name), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/api/getDetailedAirline", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AirlineDTO> getDetailedAirline(@RequestParam String name) {
		return new ResponseEntity<AirlineDTO>(airlineService.getDetailedAirline(name), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/api/getIncomeOfAirline", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Double> getIncomeOfAirline(@RequestParam Date fromDate, @RequestParam Date toDate) {
		return airlineService.getIncomeOfAirline(fromDate, toDate);
	}
	
	@RequestMapping(value = "/api/getAirlineDailyGraphData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Long>> getAirlineDailyGraphData() {
		return new ResponseEntity<Map<String, Long>>(airlineService.getDailyGraphData(), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/getAirlineWeeklyGraphData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Long>> getAirlineWeeklyGraphData() {
		return new ResponseEntity<Map<String, Long>>(airlineService.getWeeklyGraphData(), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/getAirlineMonthlyGraphData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Long>> getAirlineMonthlyGraphData() {
		return new ResponseEntity<Map<String, Long>>(airlineService.getMonthlyGraphData(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "api/getFlights", method = RequestMethod.GET)
	public ArrayList<FlightDTO> getFlights() {
		return airlineService.getFlights();
	}
}
