package framework.validation.constraints.impl;

import java.util.ArrayList;

import javax.validation.ConstraintValidatorContext;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import framework.validation.constraints.AbleAllowExtensions;

/**
 * AbleAllowExtensions Validator
 * 
 * @author ByeongDon
 */
public class AbleAllowExtensionsForMultipartFile extends AbleConstraintValidator<AbleAllowExtensions, MultipartFile> {

	protected String allowExtensionsString = "";
	protected ArrayList<String> allowExtensions = new ArrayList<String>();
	
	@Override
	public void initialize(AbleAllowExtensions constraintAnnotation) {
		this.allowExtensionsString = constraintAnnotation.value();
		String[] extentions = StringUtils.tokenizeToStringArray(this.allowExtensionsString, ",");
		for (String extention : extentions) {
			allowExtensions.add(extention.toLowerCase());
		}
	}

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		if(value == null || value.isEmpty()) {
			return true;
		}
		String filename = value.getOriginalFilename();
		String extension = FilenameUtils.getExtension(filename).toLowerCase();
		boolean allowed = false;
		for (String allowExtension : this.allowExtensions) {
			if(allowExtension.equalsIgnoreCase(extension)) {
				allowed = true;
				break;
			}
		}
		if(logger.isTraceEnabled()) {
			logger.trace("isValid({}) allowextension:{} => allowed: {}", filename, allowExtensionsString, allowed);
		}
		return allowed;
	}

}
