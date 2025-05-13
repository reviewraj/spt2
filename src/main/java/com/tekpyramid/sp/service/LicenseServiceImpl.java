package com.tekpyramid.sp.service;

import org.springframework.stereotype.Service;

import com.tekpyramid.sp.entity.LicenseName;
import com.tekpyramid.sp.repository.LicenseRepository;
import com.tekpyramid.sp.responseDto.ResponseDto;

import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class LicenseServiceImpl implements LicenseService {
	
   private final LicenseRepository licenseRepository;
	@Override
	public ResponseDto saveLicense(String IssueName) {
		LicenseName licenseName=new LicenseName();	
		licenseName.setLicenseName(IssueName);
		LicenseName save = licenseRepository.save(licenseName);
		return new ResponseDto(false,"issue saved successfully", save);
	}

}
