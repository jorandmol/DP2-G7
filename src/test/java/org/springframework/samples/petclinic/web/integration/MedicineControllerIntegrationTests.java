package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.samples.petclinic.web.MedicineController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MedicineControllerIntegrationTests {

	@Autowired
	private MedicineController medicineController;

	@Autowired
	private MedicineService medicineService;

	private ModelMap modelMap = new ModelMap();

	private BindingResult result = new MapBindingResult(Collections.emptyMap(), "");

	private static final String VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM = "medicines/createOrUpdateMedicineForm";

	@Test
	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	void testList() throws Exception {
		String view = medicineController.listMedicines(modelMap);
		Assert.assertEquals(view, "medicines/medicinesList");
		assertNotNull(modelMap.get("medicines"));

	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitCreationForm() throws Exception {
		String view = medicineController.initCreationForm(modelMap);
		Assert.assertEquals(view, VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM);
		assertNotNull(modelMap.get("medicine"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		Medicine medicine = new Medicine();
		medicine.setCode("AAA-111");
		medicine.setDescription("medicine description");
		medicine.setExpirationDate(LocalDate.of(2022, 1, 1));
		medicine.setName("Codeina");

		String view = medicineController.processCreationForm(medicine, result, modelMap);

		Assert.assertEquals(view, "redirect:/medicines");
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@ParameterizedTest
	@CsvSource({
    	"Virbaninte,123-123,2022/03/12,desparasitante",
    	"Virbaninte,ATN-674,2022/03/12,desparasitante",
    	"Virbaninte,GTN-999,,desparasitante",
    	"Virbaninte,HUB-232,2022/03/12,''"
    })
	void testProcessCreationFormHasErrors(String name, String code, String expirationDate, String description) throws Exception {
		Medicine medicine = new Medicine();
		medicine.setCode(code);
		medicine.setDescription(description);
		medicine.setName("Codeina");
		if (expirationDate != null) {
			medicine.setExpirationDate(LocalDate.parse(expirationDate, DateTimeFormatter.ofPattern("yyyy/MM/dd")));
		} else {
			medicine.setExpirationDate(null);
		}
		result.reject("code", "Bad pattern!");
		String view = medicineController.processCreationForm(medicine, result, modelMap);

		Assert.assertEquals(view, VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM);
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testShow() throws Exception {
		String view = medicineController.showMedicine(1).getViewName();
		Assert.assertEquals(view, "medicines/medicineDetails");
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitEditMedicineForm() throws Exception {
		String view = medicineController.initEditForm(1, modelMap);
		Assert.assertEquals(view, VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM);
		assertNotNull(modelMap.get("medicine"));
		Medicine med1 = (Medicine) modelMap.get("medicine");
		Assert.assertEquals(med1.getCode(), "PEN-2356");
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessEditMedicineFormSuccess() throws Exception {

		Medicine medicine = medicineService.findMedicineById(1);
		Medicine medicineCopy = new Medicine();
		BeanUtils.copyProperties(medicine, medicineCopy);
		medicineCopy.setName("Paracetamol 500gr");
		String view = medicineController.processEditForm(medicineCopy, result, 1, modelMap);
		Assert.assertEquals(view, "redirect:/medicines");
		Assert.assertEquals(medicineService.findMedicineById(1).getName(), "Paracetamol 500gr");

	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@ParameterizedTest
	@CsvSource({
		"1,'',TET-111,2022/03/12,desparasitante",
    	"1,Virbaninte,123-123,2022/03/12,desparasitante",
    	"1,Virbaninte,GTN-999,,desparasitante",
    	"1,Virbaninte,HUB-232,2022/03/12,''"
	})
	void testProcessEditMedicineFormHasErrors(int medicineId, String name, String code, String expirationDate, String description) throws Exception {
		Medicine medicine = medicineService.findMedicineById(medicineId);
		Medicine medicineCopy = new Medicine();
		BeanUtils.copyProperties(medicine, medicineCopy);
		medicineCopy.setName(name);
		medicineCopy.setCode(code);
		medicineCopy.setDescription(description);
		if (expirationDate != null) {
			medicineCopy.setExpirationDate(LocalDate.parse(expirationDate, DateTimeFormatter.ofPattern("yyyy/MM/dd")));
		} else {
			medicineCopy.setExpirationDate(null);
		}
		result.reject("name", "Cant be empty");
		String view = medicineController.processEditForm(medicineCopy, result, 1, modelMap);
		Assert.assertEquals(view, VIEWS_MEDICINE_CREATE_OR_UPDATE_FORM);
	
	}
}
