package io.renren.modules.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.context.TenantContext;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.common.utils.Result;
import io.renren.modules.device.dao.KxDeviceDao;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.device.entity.KxDeviceEntity;
import io.renren.modules.device.service.KxDeviceService;
import io.renren.modules.security.user.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 设备信息
 *
 * @author cxy
 * @since 3.0 2022-02-16
 */
@Service
public class KxDeviceServiceImpl extends CrudServiceImpl<KxDeviceDao, KxDeviceEntity, KxDeviceDTO> implements KxDeviceService {

    @Override
    public QueryWrapper<KxDeviceEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<KxDeviceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        return wrapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        deleteById(id);
    }

    @Override
    public List<KxDeviceDTO> getListByStationId(Long sid) {

        List<KxDeviceEntity> deviceList = baseDao.getListByStationId(sid);

        return ConvertUtils.sourceToTarget(deviceList, KxDeviceDTO.class);
    }

    @Override
    public KxDeviceDTO get(Long id) {
        KxDeviceEntity entity = baseDao.getById(id);

        return ConvertUtils.sourceToTarget(entity, KxDeviceDTO.class);
    }


    /**
     * 接收服务器 通过设备序号获取对应设备
     *
     * @param serialNo
     * @return
     */
    @Override
    public KxDeviceDTO getBySerialNo(String serialNo) {
        KxDeviceEntity entity = baseDao.getBySerialNo(serialNo);
        return ConvertUtils.sourceToTarget(entity, KxDeviceDTO.class);
    }

    @Override
    public Result add(KxDeviceDTO vo) {
        String msg = this.checkData(vo);
        if (StringUtils.isNotBlank(msg)) {
            return new Result().error(msg);
        }
        this.save(vo);
        return new Result();
    }

    @Override
    public Result modify(KxDeviceDTO vo) {
        String msg = this.checkData(vo);
        if (StringUtils.isNotBlank(msg)) {
            return new Result().error(msg);
        }
        this.update(vo);
        return new Result();
    }

    /**
     * 效验数据
     *
     * @param vo
     * @return
     */
    private String checkData(KxDeviceDTO vo) {
        String msg = "";
        if (this.isExist(vo)) {
            msg = vo.getStationName() + "站点已存在相同的设备名称";
        }
        if (this.isExistNo(vo)) {
            msg = "已存在相同的设备编号";
        }
        return msg;

    }

    /**
     * @param vo
     * @return
     */
    private boolean isExist(KxDeviceDTO vo) {
        boolean flag = false;
        QueryWrapper<KxDeviceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("name", vo.getName());
        wrapper.eq("station_id", vo.getStationId());
        if (null != vo.getId()) {
            wrapper.ne("id", vo.getId());
        }
        List<KxDeviceEntity> list = baseDao.selectList(wrapper);
        if (list.size() > 0) {
            flag = true;
        }
        return flag;

    }

    /**
     * @param vo
     * @return
     */
    private boolean isExistNo(KxDeviceDTO vo) {
        boolean flag = false;
        QueryWrapper<KxDeviceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("serial_no", vo.getSerialNo());
        if (null != vo.getId()) {
            wrapper.ne("id", vo.getId());
        }
        List<KxDeviceEntity> list = baseDao.selectList(wrapper);
        if (list.size() > 0) {
            flag = true;
        }
        return flag;

    }

}