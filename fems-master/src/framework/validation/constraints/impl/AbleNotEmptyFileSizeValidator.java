package framework.validation.constraints.impl;

import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import framework.validation.constraints.AbleNotEmptyFileSize;

/**
 * 파일의 크기가 0이면 안됨.
 * @author kim
 */
public class AbleNotEmptyFileSizeValidator extends AbleConstraintValidator<AbleNotEmptyFileSize, MultipartFile> {

	@Override
	public void initialize(AbleNotEmptyFileSize constraintAnnotation) {
		/**/
	}

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		long fileSize = value.getSize();
		if(fileSize == 0){
			return false;
		}
		return true;
	}

}
