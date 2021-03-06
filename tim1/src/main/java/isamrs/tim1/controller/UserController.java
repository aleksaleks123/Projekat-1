package isamrs.tim1.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim1.dto.MessageDTO;
import isamrs.tim1.dto.UserDTO;
import isamrs.tim1.model.User;
import isamrs.tim1.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/api/editUser", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> editUserProfile(@RequestBody User user) throws Exception {
		return new ResponseEntity<MessageDTO>(userService.editProfile(user), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/getUserInfo", method = RequestMethod.GET)
	public ResponseEntity<UserDTO> getUserData() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return new ResponseEntity<UserDTO>(
				new UserDTO(u),
				HttpStatus.OK);
	}
}
