package framework.validation.constraints.impl;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import framework.validation.constraints.AbleAllowFileSize;

/**
 * AbleAllowFileSize Validator
 *
 * @author ByeongDon
 */
public class AbleAllowFileSizeForListOfMultipartFile extends AbleConstraintValidator<AbleAllowFileSize, List<MultipartFile>> {

	protected long allowSize = 0;
	//protected String fancyAllowSize = "";

	@Override
	public void initialize(AbleAllowFileSize constraintAnnotation) {
		this.allowSize = constraintAnnotation.value();
		//this.fancyAllowSize = AbleStringUtil.readableFileSize(this.allowSize);
	}

	@Override
	public boolean isValid(List<MultipartFile> listValue, ConstraintValidatorContext context) {
		if(listValue == null || listValue.isEmpty()) {
			return true;
		}
		boolean allowed = false;
		for(MultipartFile value : listValue){
			if(value == null || value.isEmpty()) {
				return true;
			}
			long filesize = value.getSize();

			allowed = (filesize <= allowSize);
			if (!allowed){
				return false;
			}
			if(logger.isTraceEnabled()) {
				logger.trace("isValid({}) allowSize:{} => allowed: {}", filesize, allowSize, allowed);
			}
		}
		return true;
	}
}
