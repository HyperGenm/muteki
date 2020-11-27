package com.weiziplus.muteki.core.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.base.BaseWhere;
import com.weiziplus.muteki.common.base.BaseWhereModel;
import com.weiziplus.muteki.common.models.SysDept;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.DateUtils;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.pc.common.token.PcJwtExpand;
import com.weiziplus.muteki.core.pc.common.token.PcTokenUtils;
import com.weiziplus.muteki.core.pc.system.dto.SysDeptDto;
import com.weiziplus.muteki.core.pc.system.mapper.SysDeptMapper;
import com.weiziplus.muteki.core.pc.system.vo.SysDeptVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/11/26 11/32
 */
@Service
public class SysDeptService extends BaseService {

    @Autowired
    SysDeptMapper mapper;

    /**
     * 获取部门树
     *
     * @return
     */
    public ResultBean<List<SysDeptVo>> getTree() {
        List<SysDeptVo> resultList = new ArrayList<>();
        Integer minParentId = mapper.getMinParentId();
        if (null == minParentId) {
            return ResultBean.success(resultList);
        }
        List<SysDeptVo> list = mapper.getList();
        for (SysDeptVo sysDeptVo : list) {
            if (!minParentId.equals(sysDeptVo.getParentId())) {
                continue;
            }
            List<SysDeptVo> childrenList = getChildrenList(list, sysDeptVo.getId());
            sysDeptVo.setChildren(childrenList);
            resultList.add(sysDeptVo);
        }
        return ResultBean.success(resultList);
    }

    /**
     * 获取子级列表
     *
     * @param list
     * @param parentId
     * @return
     */
    private List<SysDeptVo> getChildrenList(List<SysDeptVo> list, Integer parentId) {
        List<SysDeptVo> resultList = new ArrayList<>();
        for (SysDeptVo sysDeptVo : list) {
            if (!parentId.equals(sysDeptVo.getParentId())) {
                continue;
            }
            List<SysDeptVo> childrenList = getChildrenList(list, sysDeptVo.getId());
            sysDeptVo.setChildren(childrenList);
            resultList.add(sysDeptVo);
        }
        return resultList;
    }

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
     * @param parentId
     * @return
     */
    public ResultBean<PageUtils<SysDept>> getPageList(Integer pageNum, Integer pageSize, Integer parentId) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysDept> listByParentId = mapper.getListByParentId(parentId);
        PageUtils<SysDept> sysDeptVoPageUtils = PageUtils.pageInfo(listByParentId);
        return ResultBean.success(sysDeptVoPageUtils);
    }

    /**
     * 新增
     *
     * @param dto
     * @return
     */
    public ResultBean add(SysDeptDto dto) {
        BaseWhere<SysDept> where = new BaseWhere<>(SysDept.class)
                .where(BaseWhereModel.eq(SysDept.COLUMN_NAME, dto.getName()),
                        BaseWhereModel.eq(SysDept.COLUMN_PARENT_ID, dto.getParentId()),
                        BaseWhereModel.eq(SysDept.COLUMN_DELETE_TIME, 0));
        SysDept sysDept = baseFindOneData(where);
        if (null != sysDept) {
            return ResultBean.error("当前部门已存在");
        }
        sysDept = new SysDept();
        BeanUtils.copyProperties(dto, sysDept);
        PcJwtExpand expand = PcTokenUtils.getExpand();
        sysDept.setEditUsername(expand.getUsername())
                .setEditRealName(expand.getRealName())
                .setUpdateTime(DateUtils.getNowDateTime())
                .setCreateTime(DateUtils.getNowDateTime());
        baseInsert(sysDept);
        return ResultBean.success();
    }

    /**
     * 编辑
     *
     * @param dto
     * @return
     */
    public ResultBean update(SysDeptDto dto) {
        if (null == dto.getId() || 0 >= dto.getId()) {
            return ResultBean.error("id错误");
        }
        BaseWhere<SysDept> where = new BaseWhere<>(SysDept.class)
                .where(BaseWhereModel.eq(SysDept.COLUMN_NAME, dto.getName()),
                        BaseWhereModel.eq(SysDept.COLUMN_PARENT_ID, dto.getParentId()),
                        BaseWhereModel.eq(SysDept.COLUMN_DELETE_TIME, 0),
                        BaseWhereModel.notEq(SysDept.COLUMN_ID, dto.getId()));
        SysDept sysDept = baseFindOneData(where);
        if (null != sysDept) {
            return ResultBean.error("当前部门已存在");
        }
        sysDept = new SysDept();
        BeanUtils.copyProperties(dto, sysDept);
        PcJwtExpand expand = PcTokenUtils.getExpand();
        sysDept.setEditUsername(expand.getUsername())
                .setEditRealName(expand.getRealName())
                .setUpdateTime(DateUtils.getNowDateTime());
        baseUpdate(sysDept);
        return ResultBean.success();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public ResultBean delete(Integer id) {
        if (null == id || 0 >= id) {
            return ResultBean.error("id错误");
        }
        SysDept sysDept = baseFindOneDataByClassAndColumnAndValue(
                SysDept.class, SysDept.COLUMN_PARENT_ID, id);
        if (null != sysDept) {
            return ResultBean.error("该部门下存在子级，无法删除");
        }
        PcJwtExpand expand = PcTokenUtils.getExpand();
        SysDept dept = new SysDept()
                .setId(id)
                .setEditUsername(expand.getUsername())
                .setEditRealName(expand.getRealName())
                .setDeleteTime(DateUtils.getNowDateTimeNum());
        baseUpdate(dept);
        return ResultBean.success();
    }
}
