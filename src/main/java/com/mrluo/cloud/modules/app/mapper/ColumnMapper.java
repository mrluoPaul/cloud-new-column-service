package com.mrluo.cloud.modules.app.mapper;

import com.mrluo.cloud.modules.app.model.entity.Column;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mrluo.cloud.modules.app.model.vo.TreeVO;

import java.util.List;

/**
 * 新闻栏目表(Column)表数据库访问层
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */
public interface ColumnMapper extends BaseMapper<Column> {
    List<TreeVO> tree();
}