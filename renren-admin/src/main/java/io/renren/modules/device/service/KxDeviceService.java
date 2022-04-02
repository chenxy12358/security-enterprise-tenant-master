package io.renren.modules.device.service;

import cn.hutool.json.JSONObject;
import io.renren.common.service.CrudService;
import io.renren.common.utils.Result;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.device.entity.KxDeviceEntity;

import java.util.List;

/**
 * 设备信息
 *
 * @author cxy
 * @since 3.0 2022-02-16
 */
public interface KxDeviceService extends CrudService<KxDeviceEntity, KxDeviceDTO> {
    void delete(Long id);

    @Override
    KxDeviceDTO get(Long id);

    KxDeviceDTO getBySerialNo(String serialNo);

    /**
     * 根据站点id，查询设备信息
     *
     * @param sid 站点ID
     */
    List<KxDeviceDTO> getListByStationId(Long sid);


    /**
     * 新增
     *
     * @param vo
     * @return
     */
    Result add(KxDeviceDTO vo);


    /**
     * 修改
     *
     * @param vo
     * @return
     */
    Result modify(KxDeviceDTO vo);

    /**
     * 图片分析
     * @param json
     * @param deviceId
     */
     void analysisImg(JSONObject json, Long deviceId);

}