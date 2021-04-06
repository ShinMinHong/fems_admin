package framework.validation.constraints.impl;

import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import framework.validation.constraints.AbleAllowFileSize;

/**
 * AbleAllowFileSize Validator
 *
 * @author ByeongDon
 */
public class AbleAllowFileSizeForMultipartFile extends AbleConstraintValidator<AbleAllowFileSize, MultipartFile> {

	protected long allowSize = 0;
	protected String allowSizeText;
	//protected String fancyAllowSize = "";

	@Override
	public void initialize(AbleAllowFileSize constraintAnnotation) {
		this.allowSizeText = constraintAnnotation.allowSizeText();
		this.allowSize = constraintAnnotation.value();
		//this.fancyAllowSize = AbleStringUtil.readableFileSize(this.allowSize);
	}

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		if(value == null || value.isEmpty()) {
			return true;
		}
		long filesize = value.getSize();
		boolean allowed = (filesize <= allowSize);
		if(logger.isTraceEnabled()) {
			logger.trace("isValid({}) allowSize:{} => allowed: {}", filesize, allowSize, allowed);
		}
		return allowed;
	}
}
