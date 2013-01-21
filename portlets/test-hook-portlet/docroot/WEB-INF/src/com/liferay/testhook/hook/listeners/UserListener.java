package com.liferay.testhook.hook.listeners;

import com.liferay.portal.ModelListenerException;
import com.liferay.portal.model.BaseModelListener;
import com.liferay.portal.model.ModelWrapper;
import com.liferay.portal.model.User;
import com.liferay.testhook.ModelNotUnwrappedException;

public class UserListener extends BaseModelListener<User>{

	public void onBeforeCreate(User model) throws ModelListenerException {
		if (model instanceof ModelWrapper){
			throw new ModelListenerException(new ModelNotUnwrappedException());
		}
	}

	public void onBeforeUpdate(User model) throws ModelListenerException {
		if (model instanceof ModelWrapper){
			throw new ModelListenerException(new ModelNotUnwrappedException());
		}
	}

}
