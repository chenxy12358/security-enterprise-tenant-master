/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.tenant.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.renren.common.constant.Constant;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.modules.security.password.PasswordUtils;
import io.renren.modules.sys.dao.SysUserDao;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.enums.DeleteEnum;
import io.renren.modules.sys.enums.SuperAdminEnum;
import io.renren.modules.sys.enums.SuperTenantEnum;
import io.renren.modules.sys.enums.SysTenantEnum;
import io.renren.modules.sys.service.SysRoleUserService;
import io.renren.modules.tenant.dao.SysTenantDao;
import io.renren.modules.tenant.dto.SysTenantDTO;
import io.renren.modules.tenant.entity.SysTenantEntity;
import io.renren.modules.tenant.service.SysTenantService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * 租户管理
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class SysTenantServiceImpl extends BaseServiceImpl<SysTenantDao, SysTenantEntity> implements SysTenantService {
	@Autowired
	private SysRoleUserService sysRoleUserService;
	@Autowired
	private SysUserDao sysUserDao;

	@Override
	public PageData<SysTenantDTO> page(Map<String, Object> params) {
		//转换成like
		paramsToLike(params, "tenantName");

		//分页
		IPage<SysTenantEntity> page = getPage(params, "t1.create_date", false);

		//查询
		List<SysTenantEntity> list = baseDao.getList(params);

		return getPageData(list, page.getTotal(), SysTenantDTO.class);
	}

	@Override
	public SysTenantDTO get(Long id) {
		SysTenantEntity entity = baseDao.getById(id);

		return ConvertUtils.sourceToTarget(entity, SysTenantDTO.class);
	}

	@Override
	public SysTenantDTO getTenantCode(Long tenantCode) {
		SysTenantEntity entity = baseDao.getTenantCode(tenantCode);

		return ConvertUtils.sourceToTarget(entity, SysTenantDTO.class);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysTenantDTO dto) {
		SysTenantEntity entity = ConvertUtils.sourceToTarget(dto, SysTenantEntity.class);

		//账号已存在
		SysUserEntity userEntity = sysUserDao.getByUsername(dto.getUsername());
		if(userEntity != null){
			throw new RenException(ErrorCode.ACCOUNT_EXIST);
		}

		//保存用户
		userEntity = ConvertUtils.sourceToTarget(dto, SysUserEntity.class);
		userEntity.setPassword(PasswordUtils.encode(dto.getPassword()));
		userEntity.setSuperTenant(SuperTenantEnum.YES.value());
		userEntity.setSuperAdmin(SuperAdminEnum.NO.value());
		userEntity.setGender(2);
		sysUserDao.insert(userEntity);

		//保存角色用户关系
		sysRoleUserService.saveOrUpdate(userEntity.getId(), dto.getRoleIdList());

		//保存租户
		entity.setUserId(userEntity.getId());
		entity.setDelFlag(DeleteEnum.NO.value());
		entity.setSysTenant(SysTenantEnum.NO.value());
		insert(entity);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysTenantDTO dto) {
		//更新租户
		SysTenantEntity entity = ConvertUtils.sourceToTarget(dto, SysTenantEntity.class);
		updateById(entity);

		//查询租户用户ID
		Long userId = baseDao.selectById(entity.getId()).getUserId();

		//更新用户
		SysUserEntity userEntity = ConvertUtils.sourceToTarget(dto, SysUserEntity.class);
		userEntity.setId(userId);
		//密码加密
		if(StringUtils.isBlank(dto.getPassword())){
			userEntity.setPassword(null);
		}else{
			String password = PasswordUtils.encode(dto.getPassword());
			userEntity.setPassword(password);
		}
		sysUserDao.updateById(userEntity);

		//更新角色用户关系
		sysRoleUserService.saveOrUpdate(userEntity.getId(), dto.getRoleIdList());
	}

	@Override
	public void delete(Long[] ids) {
		baseDao.deleteBatch(ids);
	}
}
