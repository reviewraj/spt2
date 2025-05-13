package com.tekpyramid.sp.service;

import org.springframework.stereotype.Service;

import com.tekpyramid.sp.entity.SupportingFile;
import com.tekpyramid.sp.repository.SupportFileRepository;
import com.tekpyramid.sp.responseDto.ResponseDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SupportFileServiceImpl implements SupportFileService {
	
	private final SupportFileRepository supportFileRepository;

	@Override
	public ResponseDto saveFile(SupportingFile supportingFile) {
		SupportingFile save = supportFileRepository.save(supportingFile);
		return new ResponseDto(false, "file saved successfully", save);
	}

}
