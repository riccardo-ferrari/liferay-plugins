package com.liferay.testhook;

import com.liferay.portal.kernel.exception.PortalException;

public class ModelNotUnwrappedException extends PortalException {

	public ModelNotUnwrappedException() {
		super();
	}
	
	public ModelNotUnwrappedException(String msg){
		super(msg);
	}

	public ModelNotUnwrappedException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ModelNotUnwrappedException(Throwable cause) {
		super(cause);
	}
	
}
