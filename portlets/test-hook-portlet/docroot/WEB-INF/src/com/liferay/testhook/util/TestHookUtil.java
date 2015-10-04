/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.testhook.util;

import com.liferay.portal.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.util.PwdGenerator;

import java.io.File;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author Brian Wing Shun Chan
 */
public class TestHookUtil {

	public static File getStartupActionFile() {
		return _instance._getStartupActionFile();
	}

	public static boolean testWrappedModel()
		throws PortalException, SystemException {

		try {
			boolean autoPassword = true;
			boolean autoScreenName = false;
			String screenName = PwdGenerator.getPassword();;
			String password1 = StringPool.BLANK;
			String password2 = StringPool.BLANK;
			String emailAddress = "TestHookUtil" + "@liferay.com";
			long facebookId = 0;
			String openId = StringPool.BLANK;
			Locale locale = LocaleUtil.getDefault();
			String firstName = "ServiceTestSuite";
			String middleName = StringPool.BLANK;
			String lastName = "ServiceTestSuite";
			int prefixId = 0;
			int suffixId = 0;
			boolean male = true;
			int birthdayMonth = Calendar.JANUARY;
			int birthdayDay = 1;
			int birthdayYear = 1970;
			String jobTitle = StringPool.BLANK;
			long[] groupIds = new long[] {_getGroupId()};
			long[] organizationIds = null;
			long[] roleIds = null;
			long[] userGroupIds = null;
			boolean sendMail = false;

			User user = UserLocalServiceUtil.addUser(
				_getUser().getUserId(), _getCompanyId(), autoPassword,
				password1, password2, autoScreenName, screenName, emailAddress,
				facebookId, openId, locale, firstName, middleName, lastName,
				prefixId, suffixId, male, birthdayMonth, birthdayDay,
				birthdayYear, jobTitle, groupIds, organizationIds, roleIds,
				userGroupIds, sendMail, _getServiceContext());

			user = UserLocalServiceUtil.deleteUser(user);

		} catch (ModelListenerException mle) {
			mle.printStackTrace();

			return false;
		}

		return true;
	}

	private static long _getCompanyId()
		throws PortalException, SystemException {

		if (_companyId > 0) {
			return _companyId;
		}

		Company company = CompanyLocalServiceUtil.getCompanyByWebId(
			PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));

		_companyId = company.getCompanyId();

		return _companyId;
	}

	private static long _getGroupId() throws PortalException, SystemException {

		if (_groupId > 0) {
			return _groupId;
		}

		Group group = GroupLocalServiceUtil.getGroup(
			_getCompanyId(), GroupConstants.GUEST);

		_groupId = group.getGroupId();

		return _groupId;
	}

	private static ServiceContext _getServiceContext()
		throws PortalException, SystemException {

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setCompanyId(_getCompanyId());
		serviceContext.setScopeGroupId(_getGroupId());
		serviceContext.setUserId(_getUser().getUserId());

		return serviceContext;
	}

	private static User _getUser() throws PortalException, SystemException {

		if (_user == null) {
			Role role = RoleLocalServiceUtil.getRole(
				_getCompanyId(), RoleConstants.ADMINISTRATOR);

			List<User> users = UserLocalServiceUtil.getRoleUsers(
				role.getRoleId(), 0, 2);

			_user = users.get(0);

		}

		return _user;
	}

	private TestHookUtil() {
		String tmpDir = SystemProperties.get(SystemProperties.TMP_DIR);

		_startupActionFileName =
			tmpDir + "/liferay/testhook/" + Time.getTimestamp();
	}

	private File _getStartupActionFile() {
		return new File(_startupActionFileName);
	}

	private static TestHookUtil _instance = new TestHookUtil();

	private static long _companyId;

	private static long _groupId;

	private static User _user;

	private String _startupActionFileName;

}