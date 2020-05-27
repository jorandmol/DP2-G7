package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.service.BannerService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BannerValidatorTests {

	@Autowired
	private BannerService bannerService;

	@ParameterizedTest
	@CsvSource({
		"thisweb,The pictore url is wrong,http://thisisok.com,Organization Name,2020/04/15",
		"'',The pictore url is empty,http://thisisok.com,Organization Name,2020/04/16",
		"http://thisisok.com,'',http://thisisok.com,Organization Name,2020/04/17",
		"http://thisisok.com,The target url is wrong,thisweb,Organization Name,2020/04/18",
		"http://thisisok.com,The target url is empty,'',Organization Name,2020/04/19",
		"http://thisisok.com,The organization name is empty,http://thisisok.com,'',2020/04/20",
		"http://thisisok.com,Date is empty,http://thisisok.com,'',",
	})
	void shouldNotSaveBanner(String picture, String slogan, String targetUrl, String organizationName, String endColabDate) {
		Banner banner = new Banner();
		banner.setPicture(picture);
		banner.setSlogan(slogan);
		banner.setTargetUrl(targetUrl);
		banner.setOrganizationName(organizationName);
		if (endColabDate != null) {
			assertThrows(ConstraintViolationException.class, () -> {
				banner.setEndColabDate(LocalDate.parse(endColabDate, DateTimeFormatter.ofPattern("yyyy/MM/dd")));
				this.bannerService.saveBanner(banner);
			});
		} else {
			banner.setEndColabDate(null);
			assertThrows(ConstraintViolationException.class, () -> {
				this.bannerService.saveBanner(banner);
			});
		}
	}
	
}
