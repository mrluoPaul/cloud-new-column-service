package com.mrluo.cloud.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mrluo.cloud.common.exception.BusinessException;
import com.mrluo.cloud.common.exception.FailCodeEnum;
import com.mrluo.cloud.common.utils.localmap.NewsSecurityUtil;
import com.mrluo.cloud.modules.app.model.entity.Column;
import com.mrluo.cloud.modules.app.mapper.ColumnMapper;
import com.mrluo.cloud.modules.app.model.vo.ColumnVO;
import com.mrluo.cloud.modules.app.service.IColumnService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * 新闻栏目表(Column)表服务实现类
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */
@Transactional(rollbackFor = {Exception.class})
@Service("columnService")
public class ColumnServiceImpl extends ServiceImpl<ColumnMapper, Column> implements IColumnService {
    @Override
    public Boolean add(ColumnVO vo) {
        String username = NewsSecurityUtil.getCurrentUser().get().getUsername();
        Column column = new Column();
        String columnName = vo.getColumnName();
        LambdaQueryWrapper<Column> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Column::getColumnName, columnName);
        Column one = this.getOne(wrapper);
        if (!Objects.isNull(one)) {
            throw new BusinessException(FailCodeEnum.COLUMN_NAME_REPEAT, FailCodeEnum.COLUMN_NAME_REPEAT.getMsg());
        }
        Long parentId = vo.getParentId();
        if (Objects.isNull(parentId)) {
            column.setType(1);
            column.setParentId(0L);
        }
        column.setCreatedUser(username);
        column.setUpdatedUser(username);
        column.setColumnName(columnName);
        return this.save(column);
    }
}