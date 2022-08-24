package io.renren.modules.netWorkData.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.renren.common.context.TenantContext;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.StringUtil;
import io.renren.modules.device.dto.KxDeviceDTO;
import io.renren.modules.netWorkData.dao.KxNetWorkStateDataDao;
import io.renren.modules.netWorkData.dto.KxNetWorkStateDataDTO;
import io.renren.modules.netWorkData.entity.KxNetWorkStateDataEntity;
import io.renren.modules.netWorkData.service.KxNetWorkStateDataService;
import io.renren.modules.security.user.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 网络状态信息 
 *
 * @author cxy 
 * @since 3.0 2022-02-19
 */
@Service
@Slf4j
public class KxNetWorkStateDataServiceImpl extends CrudServiceImpl<KxNetWorkStateDataDao, KxNetWorkStateDataEntity, KxNetWorkStateDataDTO> implements KxNetWorkStateDataService {

    @Override
    public QueryWrapper<KxNetWorkStateDataEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<KxNetWorkStateDataEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_code", TenantContext.getTenantCode(SecurityUser.getUser()));
        return wrapper;
    }
    @Override
    public PageData<KxNetWorkStateDataDTO> page(Map<String, Object> params) {
        //转换成like
        paramsToLike(params, "deviceName");
        paramsToLike(params, "sName");
        //分页
        IPage<KxNetWorkStateDataEntity> page = getPage(params, "null", false);

        //查询
        List<KxNetWorkStateDataDTO> list = baseDao.getList(params);
        return  new PageData<>(list, page.getTotal());
    }
    /**
     * 保存网络状态数据
     *
     * @param deviceDTO
     * @param senderInfo
     * @param msgInfo
     */
    @Override
    public void saveNetWorkData(KxDeviceDTO deviceDTO, JSONObject senderInfo, JSONObject msgInfo) {
        try {
            KxNetWorkStateDataDTO dto = new KxNetWorkStateDataDTO();
            dto.setDeviceid(deviceDTO.getId());
            dto.setStationid(deviceDTO.getStationId());
            dto.setContent(String.valueOf(msgInfo));
            dto.setOperatorName(String.valueOf(msgInfo.get("OperatorName")));
            dto.setAccessTech(String.valueOf(msgInfo.get("AccessTech")));
            dto.setNicName(String.valueOf(msgInfo.get("NicName")));
            String quality=String.valueOf(msgInfo.get("SignalQuality"));
            if(StringUtil.isNotEmpty(quality)){
                double signalQuality=new Double(quality);
                DecimalFormat df = new DecimalFormat("0.00%");
                dto.setSignalQuality(df.format(signalQuality));
            }
            this.save(dto);
        } catch (Exception e) {
            log.error("saveNetWorkData", e);
            e.printStackTrace();
        }

    }



}