package com.tekpyramid.sp.service;

import com.tekpyramid.sp.entity.SupportingFile;
import com.tekpyramid.sp.responseDto.ResponseDto;

public interface SupportFileService {
	ResponseDto saveFile(SupportingFile supportingFile);
}
