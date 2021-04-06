package framework.validation.constraints.impl;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import framework.validation.constraints.AbleAllowExtensions;

/**
 * AbleAllowExtensions Validator
 *
 * @author kurzgood
 */
public class AbleAllowExtensionsForListOfMultipartFile extends AbleConstraintValidator<AbleAllowExtensions, List<MultipartFile>> {

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
	public boolean isValid(List<MultipartFile> listValue, ConstraintValidatorContext context) {
		if(listValue == null || listValue.isEmpty()) {
			return true;
		}

		boolean allowedAll = true; //false인 파일이 한개도 없어야 함
		for(MultipartFile value : listValue){
			boolean allowedTemp = false; //개별파일의 허용여부
			String filename = value.getOriginalFilename();
			if(!StringUtils.isEmpty(filename)){
				String extension = FilenameUtils.getExtension(filename).toLowerCase();
				for (String allowExtension : this.allowExtensions) {
					if(allowExtension.equalsIgnoreCase(extension)) {
						allowedTemp = true;
						break;
					}
				}
			}else{
				allowedTemp = true;
			}
			allowedAll = allowedAll && allowedTemp;
			if(logger.isTraceEnabled()) {
				logger.trace("isValid({}) allowextension:{} => allowed: {}, allowedAll: {}", filename, allowExtensionsString, allowedTemp, allowedAll);
			}
		}
		return allowedAll;
	}

}
