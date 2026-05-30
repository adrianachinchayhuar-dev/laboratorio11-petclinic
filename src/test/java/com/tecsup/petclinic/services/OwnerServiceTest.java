package com.tecsup.petclinic.services;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;

@SpringBootTest
public class OwnerServiceTest {

	@Autowired
	private OwnerService ownerService;

	@Test
	public void testFindOwnerById() {

		Integer ID = 99;
		OwnerDTO owner = null;

		try {
			owner = ownerService.findById(ID);
		} catch (OwnerNotFoundException e) {
			assertThat(e.getMessage(), false);
		}

		assertThat(owner, notNullValue());
	}

	@Test
	public void testFindOwnerByLastName() {

		String LAST_NAME = "Franklin";
		int SIZE_EXPECTED = 1;

		List<OwnerDTO> owners = ownerService.findByLastName(LAST_NAME);

		assertThat(owners.size(), is(SIZE_EXPECTED));
	}

	@Test
	public void testFindOwnerByFirstName() {

		String FIRST_NAME = "George";
		int SIZE_EXPECTED = 1;

		List<OwnerDTO> owners = ownerService.findByFirstName(FIRST_NAME);

		assertThat(owners.size(), is(SIZE_EXPECTED));
	}

	@Test
	public void testCreateOwner() {

		String FIRST_NAME = "Adriana";
		String LAST_NAME = "Test";
		String ADDRESS = "Av. Lima 123";
		String CITY = "Lima";
		String TELEPHONE = "999999999";

		OwnerDTO owner = new OwnerDTO(null, FIRST_NAME, LAST_NAME, ADDRESS, CITY, TELEPHONE);

		OwnerDTO ownerCreated = ownerService.create(owner);

		assertThat(ownerCreated.getId(), notNullValue());
		assertThat(ownerCreated.getFirstName(), is(FIRST_NAME));
		assertThat(ownerCreated.getLastName(), is(LAST_NAME));
		assertThat(ownerCreated.getAddress(), is(ADDRESS));
		assertThat(ownerCreated.getCity(), is(CITY));
		assertThat(ownerCreated.getTelephone(), is(TELEPHONE));
	}

	@Test
	public void testUpdateOwner() {

		String FIRST_NAME = "Maria";
		String LAST_NAME = "Prueba";
		String ADDRESS = "Calle 1";
		String CITY = "Lima";
		String TELEPHONE = "987654321";

		String UP_FIRST_NAME = "MariaActualizada";
		String UP_LAST_NAME = "PruebaActualizada";
		String UP_ADDRESS = "Calle 2";
		String UP_CITY = "Cusco";
		String UP_TELEPHONE = "900000000";

		OwnerDTO owner = new OwnerDTO(null, FIRST_NAME, LAST_NAME, ADDRESS, CITY, TELEPHONE);

		OwnerDTO ownerCreated = ownerService.create(owner);

		Integer createId = ownerCreated.getId();

		ownerCreated.setFirstName(UP_FIRST_NAME);
		ownerCreated.setLastName(UP_LAST_NAME);
		ownerCreated.setAddress(UP_ADDRESS);
		ownerCreated.setCity(UP_CITY);
		ownerCreated.setTelephone(UP_TELEPHONE);

		OwnerDTO ownerUpdated = ownerService.update(ownerCreated);

		assertThat(ownerUpdated.getId(), is(createId));
		assertThat(ownerUpdated.getFirstName(), is(UP_FIRST_NAME));
		assertThat(ownerUpdated.getLastName(), is(UP_LAST_NAME));
		assertThat(ownerUpdated.getAddress(), is(UP_ADDRESS));
		assertThat(ownerUpdated.getCity(), is(UP_CITY));
		assertThat(ownerUpdated.getTelephone(), is(UP_TELEPHONE));
	}

	@Test
	public void testDeleteOwner() {

		String FIRST_NAME = "Delete";
		String LAST_NAME = "Test";
		String ADDRESS = "Av. Test";
		String CITY = "Lima";
		String TELEPHONE = "911111111";

		OwnerDTO owner = new OwnerDTO(null, FIRST_NAME, LAST_NAME, ADDRESS, CITY, TELEPHONE);

		owner = ownerService.create(owner);

		try {
			ownerService.delete(owner.getId());
		} catch (OwnerNotFoundException e) {
			assertThat(e.getMessage(), false);
		}

		try {
			ownerService.findById(owner.getId());
			assertThat(true, is(false));
		} catch (OwnerNotFoundException e) {
			assertThat(true, is(true));
		}
	}
}