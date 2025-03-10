// LicenseService.java
package com.optimagrowth.license.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.LicenseRepository;

@Service
public class LicenseService {

	@Autowired
	LicenseRepository licenseRepository;

	@Autowired
	ServiceConfig config;

	public License getLicense(String licenseId, String organizationId, String clientType){
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		if (null == license) {
			throw new IllegalArgumentException(String.format("Unable to find license with id %s and organization id %s", licenseId, organizationId));
		}

		Organization organization = retrieveOrgInfo(organizationId);

		if (null != organization) {
			license.setOrganizationName(organization.getName());
			license.setContactName(organization.getContactName());
			license.setContactEmail(organization.getContactEmail());
			license.setContactPhone(organization.getContactPhone());
		}

		license.setComment(config.getProperty());

		return license.withComment(config.getProperty());
	}

	public License createLicense(License license){
		license.setLicenseId(new Random().nextInt(1000)+"");
		licenseRepository.save(license);
		return license.withComment(config.getProperty());
	}

	public License updateLicense(License license){
		licenseRepository.save(license);
		return license.withComment(config.getProperty());
	}

	public String deleteLicense(String licenseId){
		License license = new License();
		license.setLicenseId(licenseId);
		licenseRepository.delete(license);
		return String.format("License with id %s deleted", licenseId);
	}

	public List<License> getLicensesByOrganization(String organizationId) {
		return licenseRepository.findByOrganizationId(organizationId);
	}

	private Organization retrieveOrgInfo(String organizationId) {
		Organization organization = new Organization();
		organization.setId(organizationId);
		organization.setName("Test Org");
		organization.setContactName("Test Contact");
		organization.setContactEmail("test@example.com");
		organization.setContactPhone("123-456-7890");
		return organization;
	}
}