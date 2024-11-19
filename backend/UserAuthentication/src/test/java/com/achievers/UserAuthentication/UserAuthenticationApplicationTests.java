package com.achievers.UserAuthentication;

import com.achievers.UserAuthentication.controller.UserController;
import com.achievers.UserAuthentication.dto.LoginRequestDto;
import com.achievers.UserAuthentication.dto.RegisterRequestDto;
import com.achievers.UserAuthentication.model.Admins;
import com.achievers.UserAuthentication.model.Customers;
import com.achievers.UserAuthentication.model.Users;
import com.achievers.UserAuthentication.repository.AdminRepository;
import com.achievers.UserAuthentication.repository.CustomerRepository;
import com.achievers.UserAuthentication.repository.UserRepository;
import com.achievers.UserAuthentication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private UserController userController;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private Authentication authentication;

	@Mock
	private AdminRepository adminRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;



	@Test //Shivaramakrishna
	void testCustomerExist() {

		String uid = "ertyui-345-34rd-wedcxz";
		Customers customer = new Customers("1", uid, "Name", "Street", "mail@example.com", "9876543210", 30, "Male");

		when(customerRepository.findByUid(uid)).thenReturn(Optional.of(customer));

		ResponseEntity<Customers> response = userService.getCustomerById(uid);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Name", response.getBody().getFullName());

		verify(customerRepository, times(1)).findByUid(uid);
	}

	@Test //Shivaramakrishna
	void testCustomerNotExist() {

		String uid = "qwert4567rdwert";
		when(customerRepository.findByUid(uid)).thenReturn(Optional.empty());

		ResponseEntity<Customers> response = userService.getCustomerById(uid);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());

		verify(customerRepository, times(1)).findByUid(uid);
	}

	@Test //Shivaramakrishna
	void testAdminExist() {

		String uid = "admin123";
		Admins admin = new Admins("1", uid, "User", "Street", "admin@example.com", "1234567890", 40, "Female");

		when(adminRepository.findByUid(uid)).thenReturn(Optional.of(admin));

		ResponseEntity<Admins> response = userService.getAdminById(uid);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Street", response.getBody().getAddress());

		verify(adminRepository, times(1)).findByUid(uid);
	}

	@Test //Shivaramakrishna
	void testAdminNotExist() {

		String uid = "admin123";
		when(adminRepository.findByUid(uid)).thenReturn(Optional.empty());

		ResponseEntity<Admins> response = userService.getAdminById(uid);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());

		verify(adminRepository, times(1)).findByUid(uid);
	}

	@Test //Shivaramakrishna
	void testUpdateCustomerExist() {

		String uid = "9sdf9054-ee7f-4126-9cc8-34rfghytrg";

		RegisterRequestDto updateDto = new RegisterRequestDto();
		updateDto.setFullName("Updated Name");
		updateDto.setEmailId("updated.email@example.com");
		updateDto.setAddress("New Address");
		updateDto.setMobileNumber("1234567890");
		updateDto.setAge(30);
		updateDto.setGender("Male");
		updateDto.setPassword("newPassword");

		Customers customer = new Customers();
		customer.setUid(uid);
		customer.setFullName("Old Name");
		customer.setEmailId("old.email@example.com");
		customer.setAddress("Old Address");
		customer.setMobileNumber("9876543210");
		customer.setAge(25);
		customer.setGender("Female");

		Users user = new Users();
		user.setUid(uid);
		user.setUsername("old.email@example.com");
		user.setHashedPassword("oldHashedPassword");

		when(customerRepository.findByUid(uid)).thenReturn(Optional.of(customer));
		when(userRepository.findById(uid)).thenReturn(Optional.of(user));

		when(passwordEncoder.encode(updateDto.getPassword())).thenReturn("hashedNewPassword");

		ResponseEntity<String> response = userService.updateCustomer(uid, updateDto);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Customer updated successfully", response.getBody());
		verify(customerRepository, times(1)).save(customer);
		verify(userRepository, times(1)).save(user);

	}

	@Test //Shivaramakrishna
	void testUpdateCustomerNotExist() {
		String uid = "91839054-ee7f-4126-9cc8-ajk5j4k4";
		RegisterRequestDto updateDto = new RegisterRequestDto();
		updateDto.setFullName("Updated Name");
		updateDto.setEmailId("updated.email@example.com");
		updateDto.setAddress("New Address");
		updateDto.setMobileNumber("1234567890");
		updateDto.setAge(30);
		updateDto.setGender("Male");

		when(customerRepository.existsByUid(uid)).thenReturn(false);

		ResponseEntity<String> response = userService.updateCustomer(uid, updateDto);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Customer not found", response.getBody());
		verify(customerRepository, never()).save(any(Customers.class));
		verify(userRepository, never()).save(any(Users.class));
	}

	@Test //rajesh reddy junnuthula
	void testDeleteAdminExists() {
		String uid = "91839054-ee7f-4126-9cc8-abf8cdaf08c4";

		when(adminRepository.existsByUid(uid)).thenReturn(true);

		ResponseEntity<String> response = userService.deleteAdmin(uid);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Admin deleted successfully", response.getBody());
		verify(adminRepository, times(1)).deleteByUid(uid);
		verify(userRepository, times(1)).deleteById(uid);
	}

	@Test  //rajesh reddy junnuthula
	void testDeleteAdminNotExist() {
		String uid = "91839054-ee7f-4126-9cc8-abf8cdaf08c4";

		when(adminRepository.existsByUid(uid)).thenReturn(false);

		ResponseEntity<String> response = userService.deleteAdmin(uid);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Admin not found", response.getBody());
		verify(adminRepository, never()).deleteByUid(anyString());
		verify(userRepository, never()).deleteById(anyString());
	}

	@Test  //rajesh reddy junnuthula
	void testDeleteCustomerExists() {
		String uid = "91839054-ee7f-4126-9cc8-abf8cdaf08c4";

		when(customerRepository.existsByUid(uid)).thenReturn(true);

		ResponseEntity<String> response = userService.deleteCustomer(uid);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Customer deleted successfully", response.getBody());
		verify(customerRepository, times(1)).deleteByUid(uid);
		verify(userRepository, times(1)).deleteById(uid);
	}

	@Test  //rajesh reddy junnuthula
	void testDeleteCustomerNotExist() {
		String uid = "91839054-ee7f-4126-9cc8-abf8cdaf08c4";

		when(customerRepository.existsByUid(uid)).thenReturn(false);

		ResponseEntity<String> response = userService.deleteCustomer(uid);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Customer not found", response.getBody());
		verify(customerRepository, never()).deleteByUid(anyString());
		verify(userRepository, never()).deleteById(anyString());
	}

	@Test  //rajesh reddy junnuthula
	void testUpdateAdminNotExist() {
		String uid = "91839054-ee7f-4126-9cc8-abf8cdaf08c4";
		RegisterRequestDto r = new RegisterRequestDto();
		r.setFullName("Updated Name");
		r.setEmailId("updated.email@example.com");
		r.setAddress("New Address");
		r.setMobileNumber("1234567890");
		r.setAge(30);
		r.setGender("Male");

		when(adminRepository.existsByUid(uid)).thenReturn(false);

		ResponseEntity<String> response = userService.updateAdmin(uid, r);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Admin not found", response.getBody());
		verify(adminRepository, never()).save(any(Admins.class));
		verify(userRepository, never()).save(any(Users.class));
	}

	@Test  //rajesh reddy junnuthula
	void testUpdateAdminExist() {
		String uid = "91839054-ee7f-4126-9cc8-abf8cdaf08c4";

		RegisterRequestDto r = new RegisterRequestDto();
		r.setFullName("Updated Name");
		r.setEmailId("updated.email@example.com");
		r.setAddress("New Address");
		r.setMobileNumber("1234567890");
		r.setAge(30);
		r.setGender("Male");
		r.setPassword("newPassword");

		Admins admin = new Admins();
		admin.setUid(uid);
		admin.setFullName("Old Name");
		admin.setEmailId("old.email@example.com");
		admin.setAddress("Old Address");
		admin.setMobileNumber("9876543210");
		admin.setAge(25);
		admin.setGender("Female");

		Users user = new Users();
		user.setUid(uid);
		user.setUsername("old.email@example.com");
		user.setHashedPassword("oldHashedPassword");

		when(adminRepository.findByUid(uid)).thenReturn(Optional.of(admin));
		when(userRepository.findById(uid)).thenReturn(Optional.of(user));

		when(passwordEncoder.encode(r.getPassword())).thenReturn("hashedNewPassword");

		ResponseEntity<String> response = userService.updateAdmin(uid, r);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Admin updated successfully", response.getBody());
		verify(adminRepository, times(1)).save(admin);
		verify(userRepository, times(1)).save(user);
	}
	@Test // basil george
	void testLogin_Success_Customer() {
		LoginRequestDto loginRequestDto = new LoginRequestDto();
		loginRequestDto.setUsername("user@test.com");
		loginRequestDto.setPassword("password");

		when(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken("user@test.com", "password")))
				.thenReturn(authentication);

		ResponseEntity<String> response = userController.login(loginRequestDto);


		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Login successful! Redirecting to: User Dashboard", response.getBody());
	}

	@Test // basil george
	void testLogin_Failure() {
		LoginRequestDto loginRequestDto = new LoginRequestDto();
		loginRequestDto.setUsername("invalid.com");
		loginRequestDto.setPassword("wrongpassword");

		when(authenticationManager.authenticate(any()))
				.thenThrow(new RuntimeException("Invalid credentials"));

		ResponseEntity<String> response;

		response = userController.login(loginRequestDto);

		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		assertEquals("Invalid Credentials", response.getBody());
	}
	@Test //viswajith
	public void testRegisterCustomerSuccess() {
		RegisterRequestDto registerRequestDto=new RegisterRequestDto();
		registerRequestDto = new RegisterRequestDto();
		registerRequestDto.setEmailId("newuser@test.com");
		registerRequestDto.setPassword("password123");
		registerRequestDto.setFullName("New User");
		registerRequestDto.setAddress("123 Test St");
		registerRequestDto.setMobileNumber("1234567890");
		registerRequestDto.setAge(30);
		registerRequestDto.setGender("Male");


		when(userService.registerCustomer(registerRequestDto))
				.thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("Customer registered successfully"));

//		when(passwordEncoder.encode(registerRequestDto.getPassword())).thenReturn("hashedNewPassword");


		ResponseEntity<String> response = userService.registerCustomer(registerRequestDto);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Customer registered successfully",response.getBody());


	}

	@Test  //viswajith
	public void testRegisterAdminSuccess() {
		RegisterRequestDto registerRequestDto=new RegisterRequestDto();
		registerRequestDto = new RegisterRequestDto();
		registerRequestDto.setEmailId("newadmin@test.com");
		registerRequestDto.setPassword("password123");
		registerRequestDto.setFullName("New Admin");
		registerRequestDto.setAddress("123 Test St");
		registerRequestDto.setMobileNumber("1234567890");
		registerRequestDto.setAge(30);
		registerRequestDto.setGender("Female");


		when(userService.registerAdmin(registerRequestDto))
				.thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("Admin registered successfully"));

//		when(passwordEncoder.encode(registerRequestDto.getPassword())).thenReturn("hashedNewPassword");


		ResponseEntity<String> response = userService.registerAdmin(registerRequestDto);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Admin registered successfully",response.getBody());

	}

	@Test //viswajith
	void testRegisterCustomerFail(){
		RegisterRequestDto registerRequestDto=new RegisterRequestDto();

		registerRequestDto = new RegisterRequestDto();
		registerRequestDto.setEmailId("existinguser@test.com");
		registerRequestDto.setPassword("password123");
		registerRequestDto.setFullName("Existing User");
		registerRequestDto.setAddress("123 Test St");
		registerRequestDto.setMobileNumber("1234567890");
		registerRequestDto.setAge(30);
		registerRequestDto.setGender("Male");

		when(customerRepository.existsByEmailId(registerRequestDto.getEmailId())).thenReturn(true);

		ResponseEntity<String> response = userController.registerCustomer(registerRequestDto);

		assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
	}

	@Test  //viswajith
	void testRegisterAdminFail(){
		RegisterRequestDto registerRequestDto=new RegisterRequestDto();

		registerRequestDto = new RegisterRequestDto();
		registerRequestDto.setEmailId("existinguser@test.com");
		registerRequestDto.setPassword("password123");
		registerRequestDto.setFullName("Existing User");
		registerRequestDto.setAddress("123 Test St");
		registerRequestDto.setMobileNumber("1234567890");
		registerRequestDto.setAge(30);
		registerRequestDto.setGender("Male");

		when(adminRepository.existsByEmailId(registerRequestDto.getEmailId())).thenReturn(true);

		ResponseEntity<String> response = userController.registerAdmin(registerRequestDto);

		assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
	}

	@Test // Bavin
	void testGetAdminById_Success() {
		String uid = "admin1";
		Admins mockAdmin = new Admins("1", uid, "Admin One", "Address One", "admin1@example.com", "9876543210", 40, "Male");
		when(adminRepository.findByUid(uid)).thenReturn(Optional.of(mockAdmin));
		ResponseEntity<Admins> response = userService.getAdminById(uid);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Admin One", response.getBody().getFullName());
		verify(adminRepository, times(1)).findByUid(uid);
	}

	@Test // Bavin
	void testGetAdminById_NotFound() {
		String uid = "invalidAdmin";
		when(adminRepository.findByUid(uid)).thenReturn(Optional.empty());
		ResponseEntity<Admins> response = userService.getAdminById(uid);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
		verify(adminRepository, times(1)).findByUid(uid);
	}

	@Test //Bavin
	void testGetCustomerById_Success() {
		String uid = "customer1";
		Customers mockCustomer = new Customers("1", uid, "Customer One", "Address One", "customer1@example.com", "9876543210", 30, "Male");
		when(customerRepository.findByUid(uid)).thenReturn(Optional.of(mockCustomer));
		ResponseEntity<Customers> response = userService.getCustomerById(uid);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Customer One", response.getBody().getFullName());
		verify(customerRepository, times(1)).findByUid(uid);
	}

	@Test // Bavin
	void testGetCustomerById_NotFound() {
		String uid = "invalidCustomer";
		when(customerRepository.findByUid(uid)).thenReturn(Optional.empty());
		ResponseEntity<Customers> response = userService.getCustomerById(uid);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
		verify(customerRepository, times(1)).findByUid(uid);
	}



}




